package org.underlords;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mediator {
    private ArrayList<Hero> heroes = new ArrayList<>();
    private Map<String, Synergy> synergies = new HashMap<>();
    private Map<String, List<Integer>> allianceBonus = new HashMap<>();


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
        perestanovki(new ArrayList<Hero>(),0, synergies.get(synergy).getHeroes(), per);
        for (ArrayList<Hero> alliance : per){
            printHeroes(alliance);
        }
        //вызов функции в цикле

    }

//     =
private void perestanovki(ArrayList<Hero> allianse, int heroIndex, ArrayList<Hero> heroes, ArrayList<ArrayList<Hero>> per) {
    if (allianse.size() == 6) {
        per.add(allianse);
        return;
    }
    if (heroIndex >= heroes.size()) {
        return;
    }

    Hero hero = heroes.get(heroIndex);
    //не добавляю
    perestanovki(allianse, heroIndex + 1, heroes, per);
    ArrayList<Hero> allianse2 = new ArrayList<Hero>(allianse);
    allianse2.add(hero);
    // добавляю итого героя
    perestanovki(allianse2, heroIndex + 1, heroes, per);
}

/*    private ArrayList<Hero> calculator_(ArrayList<Hero> heroes, String synergy, ArrayList<String> fullSynergies){
        if(heroes.size()==10) {
            printHeroes(heroes);
            return heroes;
        }

        ArrayList<String> alliances = calcAlliances(heroes, fullSynergies);
        for()




        return list;
    }*/

    private ArrayList<String> calcAlliances(ArrayList<Hero> heroes, ArrayList<String> fullSynergies){
        ArrayList<String> alliances = new ArrayList<>();
        for(Hero hero: heroes){
            ArrayList<String> syn = hero.getSynergies();
            for(String s: syn){
                if(!alliances.contains(s))
                    alliances.add(s);
            }
        }
        return alliances;
    }

    private void printHeroes(ArrayList<Hero> heroes){
        System.out.println("NEW HERO ALLIANCE");
        for (Hero hero: heroes) {
            hero.print();
        }
    }


    public ArrayList<Hero> getHeroes() {
        return heroes;
    }


}
