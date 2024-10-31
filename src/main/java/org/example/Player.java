package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Player {
    private String name;
    private List<Integer> dice;

    public Player(String name) {
        this.name = name;
        this.dice = new ArrayList<>();
        rollDice();
    }

    public void rollDice() {
        Random rand = new Random();
        dice.clear();
        for (int i = 0; i < 5; i++) {
            dice.add(rand.nextInt(6) + 1);
        }
    }

    public String getName() {
        return name;
    }

    public List<Integer> getDice() {
        return dice;
    }
}
