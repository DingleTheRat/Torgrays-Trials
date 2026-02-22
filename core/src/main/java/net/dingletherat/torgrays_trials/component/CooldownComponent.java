// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.component;

import java.math.BigDecimal;

import net.dingletherat.torgrays_trials.system.CooldownSystem;

/**
 * Upon addition to an entity, it will stop the entity from being querried for a set amount of time.
 * <p>
 * This means the entity cannot be updated, drawn, or anything during that period of time.
 * This component is updated in the {@link CooldownSystem}.
 **/
public class CooldownComponent implements Component{
    public float amount;

    /**
     * @param amount How much the entity is on "cooldown" for.
     **/
    public CooldownComponent(BigDecimal amount) {
        this.amount = amount.floatValue();
    }
}
