// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.component;

import net.dingletherat.torgrays_trials.main.States.MovementStates;

public class MovementComponent implements Component {
    /// Determine how the entity moves (if at all) (Ex: Idle = Does nothing, Walking moves at speed)
    public MovementStates state;
    /// The direction the entity is facing/moving in. Also changes the sprite sheet if the entity has one
    public String direction;
    /// How much the entity moves each frame
    public int speed;

    /**
     * Automatically sets state to IDLE and direction to down (the defaults)
     * <p>
     * @param speed How much the entity moves each frame
     **/
    public MovementComponent(Integer speed) {
        state = MovementStates.IDLE;
        direction = "up";
        this.speed = speed;
    }
    /**
     * @param startState The entity's starting state. States determine how the entity moves (if at all)
     * @param startDirection The direction the entity will face/move in at first
     * @param speed How much the entity moves each frame (if at all)
     **/
    public MovementComponent(MovementStates startState, String startDirection, Integer speed) {
        state = startState;
        direction = startDirection;
        this.speed = speed;
    }
}
