// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.system;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.component.HealthComponent;
import net.dingletherat.torgrays_trials.component.signals.*;
import net.dingletherat.torgrays_trials.main.EntityHandler;

public class HealthSystem implements System {
    public void draw() { }
    public void update(float deltaTime) {
        for (Integer entity : EntityHandler.queryAll(HealthComponent.class, DamageSignal.class)) {
            // Declare all necessary components
            HealthComponent healthComponent = EntityHandler.getComponent(entity, HealthComponent.class).get();
            DamageSignal damageSignal = EntityHandler.getComponent(entity, DamageSignal.class).get();

            // Remove the damage amount from the health in the HealthComponent
            healthComponent.health -= damageSignal.amount;

            // If the health is below 0, set it back to 0 (we don't want negitives here)
            if (healthComponent.health < 0) healthComponent.health = 0;

            // Remove the damage signal
            Main.world.removeComponent(entity, damageSignal);
        }
        for (Integer entity : EntityHandler.queryAll(HealthComponent.class, HealSignal.class)) {
            // Declare all necessary components
            HealthComponent healthComponent = EntityHandler.getComponent(entity, HealthComponent.class).get();
            HealSignal healSignal = EntityHandler.getComponent(entity, HealSignal.class).get();

            // Add the heal amount to the health in the HealthComponent
            healthComponent.health += healSignal.amount;

            // If the health is over the maxHealth, set it to the max health (cuz it reached the max)
            if (healthComponent.health > healthComponent.maxHealth) healthComponent.health = healthComponent.maxHealth;

            // Remove the HealSignal
            Main.world.removeComponent(entity, healSignal);
        }
    }
}
