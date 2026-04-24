//Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.component.signal;

import net.dingletherat.torgrays_trials.component.Component;
import net.dingletherat.torgrays_trials.component.HealthComponent;
import net.dingletherat.torgrays_trials.component.InvincibilityComponent;
import net.dingletherat.torgrays_trials.system.HealthSystem;

/**
 * Damages the entity while following the rules of health.
 * <p>
 * This signal damages the entity (and also plays the damage sound :D), while making sure that it's within the bound of health (0 - maxHealth).
 * If the entity being damaged is the player, it will also update the hearts on the UI to reflect the player's new health.
 * It also checks if the entity is dead, in case we need to destroy the entity upon death.
 * Lastly, it also gives the entity an invincibility period (via the {@link InvincibilityComponent}) for a set amount of time determined in the {@link HealthComponent}.
 * If the entity is invincible and a DamageSignal is added to the entity, it will be ignored and removed.
 * <p>
 * - Required components: {@link HealthComponent}
 * - Implemented by {@link HealthSystem}
 **/
public class DamageSignal implements Component {
    public int amount;

    /**
     * @param amount How damage should the entity take
     **/
    public DamageSignal(Integer amount) {
        this.amount = amount;
    }
}
