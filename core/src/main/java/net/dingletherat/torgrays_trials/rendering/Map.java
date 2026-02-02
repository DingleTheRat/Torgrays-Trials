// Copyright (c) 2025 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.rendering;

import java.util.HashMap;

import net.dingletherat.torgrays_trials.system.TileSystem;

public record Map (HashMap<TileSystem.Pair, Integer> ground, HashMap<TileSystem.Pair, Integer> foreground,
                   int x, int y) {}
