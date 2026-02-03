// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.component;

import java.math.BigDecimal;

public class LightComponent implements Component {
    public int lightRadius;
    public float lightIntensity;
    public boolean flicker;

    public LightComponent(Integer lightRadius, BigDecimal lightIntensity) {
        this.lightRadius = lightRadius;
        this.lightIntensity = lightIntensity.floatValue();
        flicker = true;
    }
    public LightComponent(Integer lightRadius, BigDecimal lightIntensity, Boolean flicker) {
        this.lightRadius = lightRadius;
        this.lightIntensity = lightIntensity.floatValue();
        this.flicker = flicker;
    }
}
