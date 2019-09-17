package org.underlords;

import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        try {
            Mediator reader = new Mediator();
            reader.readHeroesFile();
            ArrayList<Hero> heroes = reader.getHeroes();
            reader.createAllianceBonus();
            reader.createSynergyList();
            reader.calculator(heroes, "Elusive");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
