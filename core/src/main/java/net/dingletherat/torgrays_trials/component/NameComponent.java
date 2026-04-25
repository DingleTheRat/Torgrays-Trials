// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.component;

import net.dingletherat.torgrays_trials.main.EntityHandler;

/**
 * Automatically added to an entity via the "name" field in the template. In other words, don't bother adding it
 * <p>
 * Its main use is to keep track of templates in a HashMap inside of the {@link EntityHandler}.
 * However, it's also used for error messages and other misc stuff.
 * Implemented/Automatically added in inside of {@link EntityHandler}
 **/
public class NameComponent implements Component {
    public String name;

    public NameComponent(String name) {
        this.name = name;
    }

    @Override
    public ComponentType getType() {
        return ComponentType.SINGLE;
    }
}
