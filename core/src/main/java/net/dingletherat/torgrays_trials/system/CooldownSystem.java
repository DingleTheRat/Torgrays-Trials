package net.dingletherat.torgrays_trials.system;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.component.CooldownComponent;
import net.dingletherat.torgrays_trials.component.InvincibilityComponent;
import net.dingletherat.torgrays_trials.main.EntityHandler;

public class CooldownSystem implements System {
    public void draw() { }
    public void update(float deltaTime) {
        for (Integer entity : EntityHandler.queryAll(InvincibilityComponent.class)) {
            InvincibilityComponent invincibilityComponent = EntityHandler.getComponent(entity, InvincibilityComponent.class).get();
            invincibilityComponent.amount -= deltaTime;

            if (invincibilityComponent.amount <= 0) Main.world.removeComponent(entity, invincibilityComponent);
        }
        for (Integer entity : EntityHandler.queryAll(CooldownComponent.class)) {
            CooldownComponent cooldownComponent = EntityHandler.getComponent(entity, CooldownComponent.class).get();
            cooldownComponent.amount -= deltaTime;

            if (cooldownComponent.amount <= 0) Main.world.removeComponent(entity, cooldownComponent);
        }
    }
}

