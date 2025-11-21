// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dinglezz.torgrays_trials.rendering;

import java.util.HashMap;

public record Map (HashMap<TileManager.Pair, Integer> ground, HashMap<TileManager.Pair, Integer> foreground,
                   int x, int y) {}
