package de.dertoaster.mythicfarer.modules.targeter.craft.basic.task;

import com.google.common.collect.Sets;
import net.countercraft.movecraft.MovecraftLocation;
import net.countercraft.movecraft.TrackedLocation;
import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.processing.effects.Effect;
import org.bukkit.NamespacedKey;

import java.util.Set;

public record ApplySurfaceEffect(
        Craft craft,
        NamespacedKey storageKey,
        Set<MovecraftLocation> surface
) implements Effect {

    @Override
    public void run() {
        final Set<TrackedLocation> trackedLocationSet = Sets.newConcurrentHashSet();

        for (MovecraftLocation ml : this.surface) {
            trackedLocationSet.add(new TrackedLocation(this.craft, ml));
        }

        this.craft.getTrackedLocations().put(this.storageKey(), trackedLocationSet);
    }

}
