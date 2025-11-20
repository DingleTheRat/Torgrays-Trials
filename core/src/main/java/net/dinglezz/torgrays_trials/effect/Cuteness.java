package net.dinglezz.torgrays_trials.effect;

import net.dinglezz.torgrays_trials.entity.Mob;
import net.dinglezz.torgrays_trials.entity.monster.Monster;
import net.dinglezz.torgrays_trials.main.Main;

import java.util.HashMap;

public class Cuteness extends Effect {
    HashMap<Monster, Integer> deductions;

    public Cuteness(int duration, Mob host) {
        super("Cuteness", duration, host);
        registerEffectImage("entity/item/soup/coiner's_soup");
    }

    @Override
    public void onApply() {
        deductions = new HashMap<>();
        host.speed += (int) (host.speed * 0.3); // Make the host 30% faster

        // Give all monsters some debuffs
        for (Monster monster : Main.game.monsters.get(Main.game.currentMap)) {
            if (monster != host && monster != null) {
                // Store the original attack value (for later)
                int originalAttack = monster.attack;

                // Make 'em slower and do less damage
                if (monster.speed > 1) monster.speed--;
                monster.attack = (int) (monster.attack / 1.5);

                // Store the deduction in the hashmap
                deductions.put(monster, originalAttack - monster.attack);
            }
        }
    }

    @Override
    public void during() {}

    @Override
    public void onEnd() {
        host.speed = host.defaultSpeed;

        // Undebuff all monsters
        for (Monster monster : Main.game.monsters.get(Main.game.currentMap)) {
            if (monster != host && monster != null) {
                // Make 'em faster and restore their attack depeneding on the hashmap value
                if (monster.speed > 1) monster.speed++;
                monster.attack += deductions.get(monster);
            }
        }

        deductions.clear();
    }
}
