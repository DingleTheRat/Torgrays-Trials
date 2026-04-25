// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.component;

import java.math.BigDecimal;

import net.dingletherat.torgrays_trials.system.DarknessSystem;

/**
 * Add a circle of light around the entity.
 * <p>
 * Creates a kind of "hole" inside the darkness effect on the screen wherever the entity is.
 * This component is implemented in the {@link DarknessSystem}.
 **/
public class LightComponent implements Component {
    /// How large the circle of light is.
    public int lightRadius;
    /// How bright the light is. Mostly effects the edges of the circle of light.
    public float lightIntensity;
    /// If true, the entity will randomly briefly turn on and off every once an a while.
    public boolean flicker;

    /**
     * @param lightRadius How large the circle of light is.
     * @param lightIntensity How bright the light is. Mostly effects the edges of the circle of light.
     * @param flicker If true, the entity will randomly briefly turn on and off every once an a while.
     **/
    public LightComponent(Integer lightRadius, BigDecimal lightIntensity, Boolean flicker) {
        this.lightRadius = lightRadius;
        this.lightIntensity = lightIntensity.floatValue();
        this.flicker = flicker;
    }
    /**
     * A constructor whithout the {@code flicker} argument, which is set to true.
     * @param lightRadius How large the circle of light is.
     * @param lightIntensity How bright the light is. Mostly effects the edges of the circle of light.
     **/
    public LightComponent(Integer lightRadius, BigDecimal lightIntensity) {
        this(lightRadius, lightIntensity, true);
    }

    @Override
    public ComponentType getType() {
        return ComponentType.MULTI;
    }
}
