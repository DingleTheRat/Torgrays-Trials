// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.component;

import net.dingletherat.torgrays_trials.main.World;
import net.dingletherat.torgrays_trials.system.PlayerSystem;

/**
 * This component that allows you to control an entity and have the camera centered on it
 * <p>
 * It basically gives you god powers :b
 * Oh yeah, only one entity in a world can have it, as it's stored inside the {@code player} variable in {@link World}.
 * If you ever decide to add a second one with it, this component will be automatically removed from the entity with a warning.
 * See all the goofy stuff it does in {@link PlayerSystem}
 **/
public class PlayerComponent implements Component { }
