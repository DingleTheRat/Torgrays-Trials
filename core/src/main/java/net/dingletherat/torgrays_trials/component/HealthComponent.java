// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.component;

import java.math.BigDecimal;

public class HealthComponent implements Component {
    public int maxHealth;
    public int health;
    public float invincibilityLength;
    public boolean removeOnDeath = true;

    public HealthComponent(Integer maxHealth, Integer health, BigDecimal invincibilityLength, Boolean removeOnDeath) {
        this.maxHealth = maxHealth;
        this.health = health;
        this.invincibilityLength = invincibilityLength.floatValue();
        this.removeOnDeath = removeOnDeath;
    }
    public HealthComponent(Integer maxHealth, Boolean removeOnDeath, BigDecimal invincibilityLength) {
        this(maxHealth, maxHealth, invincibilityLength, removeOnDeath);
    }
}
