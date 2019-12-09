package org.underlords;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Lord {
    String name;
    ArrayList abils;
    private Map<String, Synergy> synergies = new HashMap();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList getAbils() {
        return abils;
    }

    public void setAbils(ArrayList abils) {
        this.abils = abils;
    }

    public Map<String, Synergy> getSynergies() {
        return synergies;
    }

    public void setSynergies(Map<String, Synergy> synergies) {
        this.synergies = synergies;
    }

}
