package com.example.pochi;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class PetModel {
    private static final String PREFS_NAME = "pet_prefs";
    private static final String KEY_HUNGER = "hunger";
    private static final String KEY_HAPPINESS = "happiness";
    private static final String KEY_ENERGY = "energy";
    private static final String KEY_AGE = "age";
    private static final String KEY_LAST_UPDATE = "last_update";

    private final SharedPreferences prefs;
    private int hunger;
    private int happiness;
    private int energy;
    private long ageMinutes;
    private long lastUpdateMillis;

    public PetModel(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        load();
    }

    private void load() {
        hunger = prefs.getInt(KEY_HUNGER, 50);
        happiness = prefs.getInt(KEY_HAPPINESS, 50);
        energy = prefs.getInt(KEY_ENERGY, 50);
        ageMinutes = prefs.getLong(KEY_AGE, 0);
        lastUpdateMillis = prefs.getLong(KEY_LAST_UPDATE, System.currentTimeMillis());
        applyTimeDecay();
        save();
    }

    private void applyTimeDecay() {
        long now = System.currentTimeMillis();
        long elapsedMinutes = (now - lastUpdateMillis) / 60000;
        if (elapsedMinutes > 0) {
            int decay = (int) Math.min(elapsedMinutes, 10);
            hunger = Math.min(100, hunger + decay * 2);
            happiness = Math.max(0, happiness - decay);
            energy = Math.max(0, energy - decay);
            ageMinutes += elapsedMinutes;
            lastUpdateMillis = now;
        }
    }

    public synchronized void save() {
        prefs.edit()
                .putInt(KEY_HUNGER, hunger)
                .putInt(KEY_HAPPINESS, happiness)
                .putInt(KEY_ENERGY, energy)
                .putLong(KEY_AGE, ageMinutes)
                .putLong(KEY_LAST_UPDATE, System.currentTimeMillis())
                .apply();
    }

    public synchronized void feed() {
        applyTimeDecay();
        hunger = Math.max(0, hunger - 30);
        happiness = Math.min(100, happiness + 10);
        save();
    }

    public synchronized void play() {
        applyTimeDecay();
        happiness = Math.min(100, happiness + 20);
        energy = Math.max(0, energy - 10);
        hunger = Math.min(100, hunger + 5);
        save();
    }

    public synchronized void sleep() {
        applyTimeDecay();
        energy = Math.min(100, energy + 40);
        hunger = Math.min(100, hunger + 10);
        save();
    }

    public synchronized void pet() {
        applyTimeDecay();
        happiness = Math.min(100, happiness + 5);
        save();
    }

    public int getHunger() { return hunger; }
    public int getHappiness() { return happiness; }
    public int getEnergy() { return energy; }
    public long getAgeMinutes() { return ageMinutes; }
    public String getMood() {
        if (energy < 20) return "Sleepy";
        if (hunger > 80) return "Hungry";
        if (happiness > 70 && hunger < 40 && energy > 40) return "Excited";
        if (happiness > 60) return "Happy";
        if (happiness < 30 || hunger > 70) return "Sad";
        return "Neutral";
    }

    public String getAsciiArt() {
        String mood = getMood();
        switch (mood) {
            case "Happy":
                return "      \\  |  /\n" +
                        "       \\ | /\n" +
                        "    ▄▄▄▄▄▄▄▄▄\n" +
                        "   █  ●   ●  █\n" +
                        "   █    △    █\n" +
                        "   █  \\___/  █\n" +
                        "    ▀▀▀▀▀▀▀▀▀\n" +
                        "      ∪   ∪\n" +
                        "    \\  (^o^)  /\n" +
                        "  ★  ~ ♪  ~  ★";
            case "Excited":
                return "    ★  ☆  ★  ☆\n" +
                        "      \\  |  /\n" +
                        "       \\ | /\n" +
                        "    ▄▄▄▄▄▄▄▄▄\n" +
                        "   █  ◕  ◕  █\n" +
                        "   █    ♡    █\n" +
                        "   █  \\___/  █\n" +
                        "    ▀▀▀▀▀▀▀▀▀\n" +
                        "    ∪  ∪  ∪  ∪\n" +
                        "  ♪ (^▽^) ♪  ♫";
            case "Sad":
                return "      \\  |  /\n" +
                        "       \\ | /\n" +
                        "    ▄▄▄▄▄▄▄▄▄\n" +
                        "   █  ●   ●  █\n" +
                        "   █    ▼    █\n" +
                        "   █  /_____\\ █\n" +
                        "    ▀▀▀▀▀▀▀▀▀\n" +
                        "      ∪   ∪\n" +
                        "    \\ (T_T) /\n" +
                        "      ~ 雨 ~";
            case "Sleepy":
                return "      \\  |  /\n" +
                        "       \\ | /\n" +
                        "    ▄▄▄▄▄▄▄▄▄\n" +
                        "   █  -   -  █\n" +
                        "   █    ─    █\n" +
                        "   █  \\___/  █\n" +
                        "    ▀▀▀▀▀▀▀▀▀\n" +
                        "      ∪   ∪\n" +
                        "    \\ (-_-) /\n" +
                        "    z Z z Z z";
            case "Hungry":
                return "      \\  |  /\n" +
                        "       \\ | /\n" +
                        "    ▄▄▄▄▄▄▄▄▄\n" +
                        "   █  @   @  █\n" +
                        "   █  ( ) ( ) █\n" +
                        "   █  \\___/  █\n" +
                        "    ▀▀▀▀▀▀▀▀▀\n" +
                        "      ∪   ∪\n" +
                        "    \\ (>_<) /\n" +
                        "  🍎  🍞  🍖  🍰";
            default: // Neutral
                return "      \\  |  /\n" +
                        "       \\ | /\n" +
                        "    ▄▄▄▄▄▄▄▄▄\n" +
                        "   █  ●   ●  █\n" +
                        "   █    ─    █\n" +
                        "   █  \\___/  █\n" +
                        "    ▀▀▀▀▀▀▀▀▀\n" +
                        "      ∪   ∪\n" +
                        "        (・ω・)";
        }
    }
}