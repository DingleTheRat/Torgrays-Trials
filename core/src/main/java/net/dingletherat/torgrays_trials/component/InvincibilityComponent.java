// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.component;

import java.math.BigDecimal;

public class InvincibilityComponent implements Component {
    public float amount;

    public InvincibilityComponent(BigDecimal amount) {
        this.amount = amount.floatValue();
    }
}
