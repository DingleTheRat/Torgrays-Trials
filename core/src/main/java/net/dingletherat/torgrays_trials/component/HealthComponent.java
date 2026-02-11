// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.component;

public class HealthComponent implements Component {
    public int maxHealth;
    public int health;
    public boolean removeOnDeath = true;

    public HealthComponent(Integer maxHealth, Boolean removeOnDeath) {
        this.maxHealth = maxHealth;
        health = maxHealth;
        this.removeOnDeath = removeOnDeath;
    }
    public HealthComponent(Integer maxHealth, Integer health, Boolean removeOnDeath) {
        this.maxHealth = maxHealth;
        this.health = health;
        this.removeOnDeath = removeOnDeath;
    }
}
