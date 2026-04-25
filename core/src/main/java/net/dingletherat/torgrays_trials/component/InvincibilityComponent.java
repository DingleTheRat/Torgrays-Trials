// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.component;

import java.math.BigDecimal;

import net.dingletherat.torgrays_trials.component.signal.DamageSignal;
import net.dingletherat.torgrays_trials.system.CooldownSystem;
import net.dingletherat.torgrays_trials.system.HealthSystem;

/**
 * Makes the entity who has it not be able to take damage from the {@link DamageSignal} for a set amount of time.
 * <p>
 * Most commonly added by the {@link HealthSystem} whenever a {@link DamageSignal} is recived.
 * This component is implemented in {@link CooldownSystem}/
 **/
public class InvincibilityComponent implements Component {
    public float amount;

    /**
     * @param amount How long is the entity invincible for?
     **/
    public InvincibilityComponent(BigDecimal amount) {
        this.amount = amount.floatValue();
    }

    @Override
    public ComponentType getType() {
        return ComponentType.SINGLE;
    }
}
