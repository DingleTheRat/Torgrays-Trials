// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.component;

import net.dingletherat.torgrays_trials.system.SpriteSystem;

/**
 * This component allows you to give the entity a draw order.
 * <p>
 * It only has one field, the Z value. Entities with a higher Z value will be drawn above ones with a lower.
 * This component also overrides the normal draw order, which is depending on the entity's Y.
 * If you want the entity to be drawn below all entities without it, set it to a negative. If you want it above, set it to a positive.
 * Implemented in {@link SpriteSystem}.
 **/
public class ZComponent implements Component {
    public int z;

    public ZComponent(Integer z) {
        this.z = z;
    }
}
