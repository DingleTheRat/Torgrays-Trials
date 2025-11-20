package net.dinglezz.torgrays_trials.event;

import net.dinglezz.torgrays_trials.main.Main;
import net.dinglezz.torgrays_trials.main.States;
import net.dinglezz.torgrays_trials.tile.TilePoint;
import org.json.JSONObject;

import java.util.Map;

public abstract class Event {
    private final Map<String, Object> parameters;
    public final TilePoint tilePoint;
    public boolean wasInEvent;

    public Event(TilePoint tilePoint, JSONObject parameters) {
        // Change the parameters to a map and add the event to the events to a list
        this.parameters = parameters.toMap();
        EventHandler.events.add(this);
        this.tilePoint = tilePoint;

        // Add a rectangle to detect when the player is in the event
        EventHandler.eventRectangles.put(tilePoint, new EventRectangle());
        EventHandler.eventRectangles.get(tilePoint).x = 23;
        EventHandler.eventRectangles.get(tilePoint).y = 23;
        EventHandler.eventRectangles.get(tilePoint).width = 2;
        EventHandler.eventRectangles.get(tilePoint).height = 2;
        EventHandler.eventRectangles.get(tilePoint).eventRectangleDefaultX = EventHandler.eventRectangles.get(tilePoint).x;
        EventHandler.eventRectangles.get(tilePoint).eventRectangleDefaultY = EventHandler.eventRectangles.get(tilePoint).y;
    }

    // Methods that are called when the player hits the event
    public abstract void onHit();
    public abstract void whileHit();
    public abstract void onLeave();

    // Parameter getters
    public <T> T getParameter(String key, Class<T> type) {
        // If the event doesn't have the required parameter, throw an exception
        if (!parameters.containsKey(key)) {
            Main.game.exceptionState = States.ExceptionStates.ONLY_QUITABLE;
            throw new IllegalArgumentException("Missing required parameter '" + key + "' for event at " + tilePoint);
        }
        Object value = parameters.get(key);
        if (!type.isInstance(value)) {
            throw new IllegalStateException("Invalid type for parameter '" + key + "' in event at " + tilePoint + ". Expected " + type.getSimpleName() + " but got " + value.getClass().getSimpleName());
        }
        return type.cast(value);
    }
    public boolean hasParameter(String key) {
        return parameters.containsKey(key);
    }
}
