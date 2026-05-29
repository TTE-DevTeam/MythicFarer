package de.dertoaster.mythicfarer.util;

import net.countercraft.movecraft.MovecraftLocation;
import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.craft.CraftManager;
import net.countercraft.movecraft.craft.type.PropertyKeys;
import net.countercraft.movecraft.craft.type.TypeSafeCraftType;
import net.countercraft.movecraft.processing.MovecraftWorld;
import net.countercraft.movecraft.util.hitboxes.BitmapHitBox;
import net.countercraft.movecraft.util.hitboxes.HitBox;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class CraftUtil {

    // Rewritten version that doesnt access the world
    @Nullable
    public static Craft findClosestCraft(MovecraftLocation location, UUID worldUUID, boolean asyncAccess) {
        long closestDistSq = Long.MAX_VALUE;
        Craft result = null;
        for (Craft craft : CraftManager.getInstance().getCrafts()) {
            MovecraftWorld movecraftWorld = craft.getMovecraftWorld();

            if (!movecraftWorld.getWorldUUID().equals(worldUUID)) {
                continue;
            }

            if (craft.getHitBox() == null) {
                continue;
            }
            final HitBox hitBox = asyncAccess ? new BitmapHitBox(craft.getHitBox()) : craft.getHitBox();
            if (hitBox.isEmpty()) {
                continue;
            }

            int midX = hitBox.getMaxX() + hitBox.getMinX() >> 1;
            int midZ = hitBox.getMaxZ() + hitBox.getMinZ() >> 1;
            long distSquared = (long)(Math.pow((double)(midX - location.getX()), 2.0) + Math.pow((double)(midZ - location.getZ()), 2.0));
            if (distSquared < closestDistSq) {
                closestDistSq = distSquared;
                result = craft;
            }
        }
        return result;
    }

    @Nullable
    public static Craft fastFindCraftAt(MovecraftLocation location, UUID worldUUID, boolean asyncAccess) {
        final Craft closest = findClosestCraft(location, worldUUID, asyncAccess);
        if (closest == null) {
            return null;
        }
        final HitBox hitBox = asyncAccess ? new BitmapHitBox(closest.getHitBox()) : closest.getHitBox();
        if (hitBox.inBounds(location) && hitBox.contains(location)) {
            return closest;
        } else {
            return null;
        }
    }

    public static boolean craftCollidesWith(final NamespacedKey material, final NamespacedKey craftMaterialColliding, final TypeSafeCraftType craftType) {
        Material matTmp = Material.getMaterial(material.toString());
        if (matTmp != null && matTmp.isAir()) {
            return false;
        }

        if (craftType.get(PropertyKeys.PASSTHROUGH_BLOCKS).contains(material)) {
            return false;
        }

        if (craftType.get(PropertyKeys.MOVE_BREAK_BLOCKS).contains(material)) {
            return false;
        }

        if (craftType.get(PropertyKeys.HARVEST_BLOCKS).contains(material)) {
            return !craftType.get(PropertyKeys.HARVESTER_BLADE_BLOCKS).contains(craftMaterialColliding);
        }
        return true;
    }

}
