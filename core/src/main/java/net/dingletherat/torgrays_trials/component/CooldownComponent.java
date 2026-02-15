// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.component;

import java.math.BigDecimal;

public class CooldownComponent implements Component{
    public float amount;

    public CooldownComponent(BigDecimal amount) {
        this.amount = amount.floatValue();
    }
}
