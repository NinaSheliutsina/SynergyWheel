package org.underlords;
import java.util.ArrayList;

public class Hero {
    private String name;
    private ArrayList<String> synergies = new ArrayList<>();
    private String level;

    public Hero() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList getSynergies() {
        return synergies;
    }

    public void addSynergy(String syn) {
        this.synergies.add(syn);
    }

    public boolean isSynergy(String syn){
        if (synergies.contains(syn)) return true;
        return false;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    //неформатированный вывод героя с синергияи
    public String toString() {
        StringBuffer sb = new StringBuffer(name + " ");
        for (String syn : synergies) {
            sb.append(syn + " ");
        }
        sb.append(level);
        return sb.toString();
    }

    //форматированный вывод героя, где сначала печататется указанная синерия для наглядности
    public void print(String syn) {
        System.out.printf("%-30s", name);
        if (synergies.contains(syn))
            System.out.printf("%-15s", syn);
       ;
        for (String syng : synergies) {
            if (!syng.equals(syn))
                System.out.printf("%-15s", syng);
        }
        System.out.println();
//        System.out.printf("%-10s%n", level);
    }

    public void print() {
        System.out.printf("%-30s", name);
        for (String syng : synergies) {
                System.out.printf("%-15s", syng);
        }
        System.out.println();
    }




}
