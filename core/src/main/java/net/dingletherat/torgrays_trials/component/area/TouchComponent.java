// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.component.area;

import java.math.BigDecimal;

import org.json.JSONArray;

import net.dingletherat.torgrays_trials.Main;

public class TouchComponent extends AreaComponent {
    public int height;
    public int width;

    public JSONArray entity1Action;
    public JSONArray entity2Action;

    public TouchComponent(Integer height, Integer width, JSONArray entity1Action, JSONArray entity2Action) {
        super(height, width);
        this.entity1Action = entity1Action;
        this.entity2Action = entity2Action;
    }
    public TouchComponent(BigDecimal height, BigDecimal width, JSONArray entity1Action, JSONArray entity2Action) {
        super(height, width);
        this.entity1Action = entity1Action;
        this.entity2Action = entity2Action;
    }
}
