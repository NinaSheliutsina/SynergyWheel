package org.underlords;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class Mediator {
    private ArrayList<Hero> heroes = new ArrayList<>();
    private Map<String, Synergy> synergies = new HashMap<>();
    private Map<String, ArrayList<Integer>> allianceBonus = new HashMap<>();
    private static Logger log = Logger.getLogger(Mediator.class.getName());

    //попросить саню преедлать csv
    public void readHeroesFile() throws IOException {
        Reader in = new FileReader("./src/main/resources/org/underlords/heroes.csv");
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
        for (CSVRecord record : records) {
            Hero hero = new Hero();
            hero.setName(record.get(0));
            hero.addSynergy(record.get(1));
            hero.addSynergy(record.get(2));
            if(!record.get(3).equals(""))
            hero.addSynergy(record.get(3));
            hero.setLevel(record.get(4));
            heroes.add(hero);
        }
    }

    public void createAllianceBonus() throws IOException {
        //формирую список синергий из героев
        for (Hero hero : heroes) {
            ArrayList<String> synergies = hero.getSynergies();
            for (String syn : synergies) {
                if (!allianceBonus.containsKey(syn))
                    allianceBonus.put(syn, null);
            }
        }

        //запиываю уровни бонусов за синергии из таблицы
        Reader in = new FileReader("./src/main/resources/org/underlords/allianceBonus.csv");
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(';').parse(in);
        for (CSVRecord record : records) {
            if (allianceBonus.containsKey(record.get(0))) {
                ArrayList<Integer> list = new ArrayList<>();
                list.add(Integer.parseInt(record.get(1)));
                if (record.get(2) != null  && !record.get(2).equals(""))
                    list.add(Integer.parseInt(record.get(2)));
                if (record.get(3) != null && !record.get(3).equals(""))
                    list.add(Integer.parseInt(record.get(3)));
                allianceBonus.put(record.get(0),list);
            }
        }
    }

    public void createSynergyList(){
        for (String alliance : allianceBonus.keySet()) {
            for (Hero hero : heroes) {
                if (hero.isSynergy(alliance)) {
                    Synergy synergy;
                    if (synergies.containsKey(alliance)) {
                        synergy = synergies.get(alliance);
                    } else {
                        synergy = new Synergy(alliance);
                    }
                    synergy.addHero(hero);
                    synergies.put(alliance,synergy);
                }
            }
        }
        for (Synergy syn: synergies.values()) {
            syn.print();
        }

    }

    public void calculator(ArrayList<Hero> heroes, String synergy){
        //рекурсивная функция перестановок
        System.out.println("Start calculator... ");
        ArrayList<ArrayList<Hero>> per = new ArrayList<ArrayList<Hero>>();
        perestanovki(new ArrayList<Hero>(),0, synergies.get(synergy).getHeroes(), per,6);
        for (ArrayList<Hero> alliance : per){
//            printHeroes(alliance);
            ArrayList<String> fullSynergies = new ArrayList<>();
            fullSynergies.add(synergy);
            calculator_(alliance, fullSynergies);
        }

    }

    private void perestanovki(ArrayList<Hero> allianse, int heroIndex, ArrayList<Hero> heroes, ArrayList<ArrayList<Hero>> per, int allianseSize) {
        if (allianse.size() == allianseSize) {
            per.add(allianse);
            return;
        }
        if (heroIndex >= heroes.size()) {
            return;
        }
        Hero hero = heroes.get(heroIndex);

        //проверка для того, чтобы не было дубликатов
        if (!allianse.contains(hero)) {
            //не добавляю
            perestanovki(allianse, heroIndex + 1, heroes, per, allianseSize);
            ArrayList<Hero> allianse2 = new ArrayList<Hero>(allianse);
            allianse2.add(hero);
            // добавляю итого героя
            perestanovki(allianse2, heroIndex + 1, heroes, per, allianseSize);
        } else {
            perestanovki(allianse, heroIndex + 1, heroes, per, allianseSize);
        }
    }

    private final int fullSize = 8;
    private void calculator_(ArrayList<Hero> heroes, ArrayList<String> fullSynergies) {
        if (heroes.size() == fullSize) {
            log.info(getHeroesString(heroes));
            printHeroes(heroes);
        }

        HashMap<String, Integer> alliances = calcAlliances(heroes, fullSynergies);
        for (String syn : alliances.keySet()) {
            int num = alliances.get(syn);
            ArrayList<Integer> bonus = allianceBonus.get(syn);
            boolean isFull = true;
            boolean isAddFullSyn = false;
            for (Integer i : bonus) {
                if (i > num) {
                    isFull = false;
                    if (num == bonus.get(bonus.size() - 1))
                        isAddFullSyn = true;
                    num = i - num;
                    break;
                }
                //num - число героев, которых надо добавить до синергии
            }
            if(isFull) continue;
            ArrayList<ArrayList<Hero>> per = new ArrayList<ArrayList<Hero>>();
            perestanovki(heroes, 0, synergies.get(syn).getHeroes(), per, heroes.size() + num);
            for (ArrayList<Hero> alliance : per) {
                if (heroes.size() > fullSize) continue;
                if(isAddFullSyn)
                fullSynergies.add(syn);
                calculator_(alliance, fullSynergies);
            }
        }
    }

    private HashMap<String, Integer> calcAlliances(ArrayList<Hero> heroes, ArrayList<String> fullSynergies) {
        Map<String, Integer> alliances = new HashMap<>();
        Map<String, Integer> alliancesMax = new HashMap<>();
        int maxNum = 0;
        for (Hero hero : heroes) {
            ArrayList<String> syn = hero.getSynergies();
            for (String s : syn) {
                if (!fullSynergies.contains(s)) {
                    int num = 1;
                    if (!alliances.containsKey(s)) {
                        alliances.put(s, num);
                    } else {
                        num += alliances.get(s);
                        alliances.put(s,  num);
                    }
                    if (num >= maxNum) {
                        if(num > maxNum)
                        alliancesMax = new HashMap<>();
                        alliancesMax.put(s, num);
                        maxNum = num;
                    }

                }
            }
        }
        return (HashMap<String, Integer>) alliancesMax;
    }

    private void printHeroes(ArrayList<Hero> heroes){
        System.out.println("NEW HERO ALLIANCE");
        for (Hero hero: heroes) {
            hero.print();
        }
    }

    private String getHeroesString(ArrayList<Hero> heroes){
        StringBuffer sb = new StringBuffer("NEW HERO ALLIANCE");
        for (Hero hero: heroes) {
            sb.append(hero.toString());
        }
        sb.append("\n");
        return sb.toString() ;
    }


    public ArrayList<Hero> getHeroes() {
        return heroes;
    }


}
