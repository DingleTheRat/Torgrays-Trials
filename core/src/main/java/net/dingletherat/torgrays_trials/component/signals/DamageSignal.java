//Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.component.signals;

import net.dingletherat.torgrays_trials.component.Component;

public class DamageSignal implements Component {
    public int amount;

    public DamageSignal(Integer amount) {
        this.amount = amount;
    }
}
