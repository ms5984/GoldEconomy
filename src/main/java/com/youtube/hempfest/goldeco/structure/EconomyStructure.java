package com.youtube.hempfest.goldeco.structure;

import com.youtube.hempfest.goldeco.util.Utility;

public interface EconomyStructure {

    public abstract void set(double amount);

    public abstract void add(double amount);

    public abstract void add(double amount, String worldName);

    public abstract void remove(double amount);

    public abstract void remove(double amount, String worldName);

    public abstract void create();

    public abstract boolean has(Utility type);

    public abstract boolean has(Utility type, String worldName);

    public abstract String get(Utility type);

    public abstract double get(Utility type, String item);

    public abstract void buy(String item, int amount);

    public abstract void sell(String item, int amount);

}
