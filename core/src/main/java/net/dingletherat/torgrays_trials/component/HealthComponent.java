// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.component;

import java.math.BigDecimal;

import net.dingletherat.torgrays_trials.component.signals.DamageSignal;
import net.dingletherat.torgrays_trials.component.signals.HealSignal;
import net.dingletherat.torgrays_trials.system.HealthSystem;

/**
 * Gives an entity, well, health.
 * <p>
 * The component has a current health variable ("health") and a maxHealth variable.
 * It's not recomended to manually modify the health variable manually, but instead give the entity a {@link DamageSignal} or {@link HealSignal}.
 * Both of these are components that modify the entity's health while making sure it doesn't go above the limits of 0 and the maxHealth.
 * Updated by the {@link HealthSystem}.
 **/
public class HealthComponent implements Component {
    public int maxHealth;
    public int health;
    /// The {@link DamageSignal} gives the entity an invinvibilty period, stopping the entity from taking damage for a set time. This is the set time.
    public float invincibilityLength;
    /// If the entity dies (gets a health of 0), you can also choose to remove it from the world by setting this to true (true by default).
    public boolean removeOnDeath = true;

    /**
     * @param maxHealth The maximum amount of health the enity can have.
     * @param health How much health the entity start off with.
     * @param invincibilityLength The {@link DamageSignal} gives the entity an invinvibilty period, stopping the entity from taking damage for a set time. This is the set time.
     * @param removeOnDeath If the entity dies (gets a health of 0), you can also choose to remove it from the world by setting this to true (true by default).
     **/
    public HealthComponent(Integer maxHealth, Integer health, BigDecimal invincibilityLength, Boolean removeOnDeath) {
        this.maxHealth = maxHealth;
        this.health = health;
        this.invincibilityLength = invincibilityLength.floatValue();
        this.removeOnDeath = removeOnDeath;
    }
    /**
     * Alternate contructor without {@code removeOnDeath}, which will be set to true.
     * @param maxHealth The maximum amount of health the enity can have.
     * @param health How much health the entity start off with.
     * @param invincibilityLength The {@link DamageSignal} gives the entity an invinvibilty period, stopping the entity from taking damage for a set time. This is the set time.
     **/
    public HealthComponent(Integer maxHealth, Integer health, BigDecimal invincibilityLength) {
        this(maxHealth, maxHealth, invincibilityLength, true);
    }
    /**
     * Alternate contructor without {@code health}, which will be set to the maxHealth.
     * @param maxHealth The maximum amount of health the enity can have.
     * @param health How much health the entity start off with.
     * @param invincibilityLength The {@link DamageSignal} gives the entity an invinvibilty period, stopping the entity from taking damage for a set time. This is the set time.
     **/
    public HealthComponent(Integer maxHealth, Boolean removeOnDeath, BigDecimal invincibilityLength) {
        this(maxHealth, maxHealth, invincibilityLength, removeOnDeath);
    }
    /**
     * Alternate contructor without {@code health} and {@code removeOnDeath}, which will be set to the maxHealth and true.
     * @param maxHealth The maximum amount of health the enity can have.
     * @param health How much health the entity start off with.
     * @param invincibilityLength The {@link DamageSignal} gives the entity an invinvibilty period, stopping the entity from taking damage for a set time. This is the set time.
     **/
    public HealthComponent(Integer maxHealth, BigDecimal invincibilityLength) {
        this(maxHealth, maxHealth, invincibilityLength, true);
    }
}
