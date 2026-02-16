// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.system;

import java.util.List;
import java.util.Map;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.component.Component;
import net.dingletherat.torgrays_trials.component.NameComponent;
import net.dingletherat.torgrays_trials.component.PositionComponent;
import net.dingletherat.torgrays_trials.component.area.TouchComponent;
import net.dingletherat.torgrays_trials.main.AreaChecker;
import net.dingletherat.torgrays_trials.main.EntityHandler;

public class TouchSystem implements System {
    public void draw() { }
    public void update(float deltaTime) {
        for (Integer entity : EntityHandler.queryAll(TouchComponent.class, PositionComponent.class)) {
            List<TouchComponent> touchComponents = EntityHandler.getComponents(entity, TouchComponent.class);

            // Go through all the TouchComponents and see if any of them intersected with another entity's
            for (TouchComponent touchComponent : touchComponents) {
                for (Integer other : EntityHandler.queryAll(TouchComponent.class, PositionComponent.class))
                    if (other != entity)
                        if (AreaChecker.check2EntityIntersecting(entity, other, TouchComponent.class, false)) {
                            // Modify the components of both entities, starting with the first one
                            NameComponent nameComponent = EntityHandler.getComponent(entity, NameComponent.class).get();
                            Map<Class<? extends Component>, List<Object>> components = EntityHandler.modifyComponentClasses(Main.world.entityTemplates.get(entity), touchComponent.entity1Action, nameComponent.name + "'s (" + entity + ") TouchComponent declaration");
                            Main.world.updateEntity(entity, components);

                            // Now, the second
                            NameComponent otherName = EntityHandler.getComponent(other, NameComponent.class).get();
                            Map<Class<? extends Component>, List<Object>> otherComponents = EntityHandler.modifyComponentClasses(Main.world.entityTemplates.get(other), touchComponent.entity2Action, otherName.name + "'s (" + entity + ") TouchComponent declaration");
                            Main.world.updateEntity(other, otherComponents);
                        }
            }
        }
    }
}
