// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.component;

import java.math.BigDecimal;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.component.area.AreaComponent;

/**
 * Gives the entity a position in a world, which would otheriwise be 0, 0.
 * <p>
 * It's recomended you add it in the map file, rather than in a template, as you might want entity instances to have their positions varied.
 * This component is usually used for drawing, to know where to draw the sprite. It's also used by all instances of {@link AreaComponent} as an achor.
 * It's highly recomended this component is added to and entity, or else it would be kinda useless.
 **/
public class PositionComponent implements Component {
    public float x;
    public float y;

    public PositionComponent(Integer col, Integer row) {
        this.x = col * Main.tileSize;
        this.y = row * Main.tileSize;
    }

    public PositionComponent(BigDecimal x, BigDecimal y) {
        this.x = x.floatValue();
        this.y = y.floatValue();
    }
}
