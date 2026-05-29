package de.dertoaster.mythicfarer.modules.targeter.craft.basic;

import com.google.common.collect.Sets;
import de.dertoaster.mythicfarer.modules.targeter.craft.basic.task.CalculateCraftSurfaceTask;
import io.lumine.mythic.bukkit.MythicBukkit;
import net.countercraft.movecraft.TrackedLocation;
import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.craft.datatag.CraftDataTagKey;
import net.countercraft.movecraft.craft.datatag.CraftDataTagRegistry;
import net.countercraft.movecraft.processing.WorldManager;
import org.bukkit.NamespacedKey;

import java.util.Set;

public class DataTags {

    static final NamespacedKey TRACKED_LOCATION_KEY = new NamespacedKey(MythicBukkit.inst().namespace(), "integration/craft_surface");
    public static final CraftDataTagKey<Set<TrackedLocation>> CRAFT_SURFACE = CraftDataTagRegistry.INSTANCE.registerTagKey(TRACKED_LOCATION_KEY, DataTags::getOrCreateSurface);
    public static final CraftDataTagKey<Long> LAST_SURFACE_CALCULATION = CraftDataTagRegistry.INSTANCE.registerTagKey(new NamespacedKey(MythicBukkit.inst().namespace(), "integration/surface_calculation_time"), c -> System.currentTimeMillis());

    public static void register() {
        // Do nothing
    }

    private static Set<TrackedLocation> getOrCreateSurface(final Craft craft) {
        // Fall back to the list of tracked locations
        Set<TrackedLocation> result = craft.getTrackedLocations().getOrDefault(TRACKED_LOCATION_KEY, null);
        if (result != null) {
            return result;
        }

        craft.setDataTag(LAST_SURFACE_CALCULATION, System.currentTimeMillis());
        result = Sets.newConcurrentHashSet();
        craft.getTrackedLocations().put(TRACKED_LOCATION_KEY, result);

        // Start the async task
        WorldManager.INSTANCE.submit(new CalculateCraftSurfaceTask(craft, CRAFT_SURFACE, LAST_SURFACE_CALCULATION));

        return result;
    }

    // Retrieves the surface of this craft, if necessary, the cache is reset and will be recalculated next time
    public static Set<TrackedLocation> getSurface(final Craft craft) {
        Set<TrackedLocation> result = craft.getDataTag(CRAFT_SURFACE);
        long lastUpdate = craft.getDataTag(LAST_SURFACE_CALCULATION);
        int cacheLifetime = craft.getCraftProperties().get(CraftProperties.CRAFT_SURFACE_LIFETIME);
        if (System.currentTimeMillis() - lastUpdate >= cacheLifetime) {
            invalidateSurfaceDataTag(craft);
        }
        return result;
    }

    private static void invalidateSurfaceDataTag(final Craft craft) {
        // Delete both, it will be recomputed on the next update
        //craft.getTrackedLocations().remove(TRACKED_LOCATION_KEY);
        //craft.resetDataTag(CRAFT_SURFACE);
        // Just start the async task, it will reset what it needs to reset
        WorldManager.INSTANCE.submit(new CalculateCraftSurfaceTask(craft, CRAFT_SURFACE, LAST_SURFACE_CALCULATION));
    }

}
