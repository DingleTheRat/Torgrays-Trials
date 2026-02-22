// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.component;

import net.dingletherat.torgrays_trials.main.States.MovementStates;
import net.dingletherat.torgrays_trials.system.MovementSystem;

/**
 * When added, allows the entity to move around via a state machine.
 * <p>
 * This component allows for any kind of movement. Including walking and idling (all states are in the {@link MovementStates} class).
 * Moving is essentially changing the {@link PositionComponent} X and Y values (Oh yeah, you need that for it to function).
 * It's implemented in the {@link MovementSystem}.
 **/
public class MovementComponent implements Component {
    /// Determine how the entity moves (if at all) (Ex: Idle = Does nothing, Walking moves at speed)
    public MovementStates state;
    /// The direction the entity is facing/moving in. Also changes the sprite sheet if the entity has one
    public String direction;
    /// How much the entity moves each frame
    public int speed;

    /**
     * @param startState The entity's starting state. States determine how the entity moves (if at all)
     * @param startDirection The direction the entity will face/move in at first
     * @param speed How much the entity moves each frame (if at all)
     **/
    public MovementComponent(String startState, Integer speed, String startDirection) {
        state = switch (startState) {
            case "IDLE" -> MovementStates.IDLE;
            case "WALKING" -> MovementStates.WALKING;
            default -> MovementStates.IDLE;
        };
        direction = startDirection;
        this.speed = speed;
    }
    /**
     * Automatically sets state to IDLE (the default)
     * <p>
     * @param speed How much the entity moves each frame
     * @param startDirection The direction the entity will face/move in at first
     **/
    public MovementComponent(Integer speed, String startDirection) {
        this("Idle", speed, startDirection);
    }
    /**
     * Automatically sets the direction to down (the default)
     * <p>
     * @param speed How much the entity moves each frame
     * @param startState The entity's starting state. States determine how the entity moves (if at all)
     **/
    public MovementComponent(String startState, Integer speed) {
        this(startState, speed, "down");
    }
    /**
     * Automatically sets state to IDLE and direction to down (the defaults)
     * <p>
     * @param speed How much the entity moves each frame
     **/
    public MovementComponent(Integer speed) {
        this("Idle", speed, "down");
    }
}
