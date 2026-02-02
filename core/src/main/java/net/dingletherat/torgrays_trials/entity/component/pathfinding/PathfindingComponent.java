// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.entity.component.pathfinding;

import net.dingletherat.torgrays_trials.entity.component.Component;

public class PathfindingComponent implements Component{
    public String activeComponent;
    public boolean avoidCollision;

    public PathfindingComponent(String activeComponent, Boolean avoidCollision) {
        this.activeComponent = activeComponent;
        this.avoidCollision = avoidCollision;
    }
}
