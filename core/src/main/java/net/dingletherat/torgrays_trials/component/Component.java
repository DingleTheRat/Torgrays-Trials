// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.component;

import java.util.List;

import org.json.JSONObject;

/**
 * An interface implemented by all Components.
 * <p>
 * A component is added to an entity to give it special behaviour.
 * For instance, a {@link HealthComponent} add health (and maybe death) to an entity.
 * <p>
 * To create an entity, make a JSON file under {@code values/template} including a name and an array of components.
 * All array arguments must be a {@link JSONObject} that contains the component class filepath and an array of its constructor arguments.
 **/
public interface Component {
    enum ComponentType {
        SINGLE,
        MULTI
    }
    record Entry(Class<? extends Component> componentClass, List<Object> args, int entryIndex) {}

    public ComponentType getType();
}

