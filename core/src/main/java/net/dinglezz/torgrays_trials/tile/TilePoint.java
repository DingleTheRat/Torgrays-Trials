package net.dinglezz.torgrays_trials.tile;

import java.io.Serializable;

public record TilePoint(String map, int col, int row) implements Serializable {}
