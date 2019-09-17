package org.underlords;

import java.util.ArrayList;
import java.util.List;

public class Synergy {
    private String name;
    private String num;
    private ArrayList<Hero> heroes = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Synergy(String name) {
        this.name = name;
    }

    public ArrayList<Hero> getHeroes() {
        return heroes;
    }

    public void addHero (Hero hero) {
        this.heroes.add(hero);
    }

    public void print(){
        System.out.printf("%-10s%n", name);
        for (Hero hero: heroes){
           hero.print(name);
        }
        System.out.println();
    }


}
