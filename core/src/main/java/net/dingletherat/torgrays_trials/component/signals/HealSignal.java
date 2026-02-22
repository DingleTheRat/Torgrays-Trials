//Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.component.signals;

import net.dingletherat.torgrays_trials.component.Component;

/**
 * Heals the entity while following the rules of health.
 * <p>
 * This signal heals the entity (and also plays the heal sound :D), while making sure that it's within the bounds of health (0 - maxHealth).
 * If the entity being healed is the player, it will also update the hearts on the UI to reflect the player's new health.
 * <p>
 * - Required components: {@link HealthComponent}
 * - Implemented by {@link HealthSystem}
 **/
public class HealSignal implements Component {
    public int amount;

    public HealSignal(Integer amount) {
        this.amount = amount;
    }
}
