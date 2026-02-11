//Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.component.signals;

import net.dingletherat.torgrays_trials.component.Component;

public class HealSignal implements Component {
    public int amount;

    public HealSignal(Integer amount) {
        this.amount = amount;
    }
}
