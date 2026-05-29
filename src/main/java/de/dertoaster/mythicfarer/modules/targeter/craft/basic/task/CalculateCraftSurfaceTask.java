package de.dertoaster.mythicfarer.modules.targeter.craft.basic.task;

import de.dertoaster.mythicfarer.util.task.CollectBlocksTask;
import net.countercraft.movecraft.MovecraftLocation;
import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.craft.datatag.CraftDataTagKey;
import net.countercraft.movecraft.processing.MovecraftWorld;
import net.countercraft.movecraft.processing.effects.Effect;
import net.countercraft.movecraft.util.hitboxes.BitmapHitBox;
import net.countercraft.movecraft.util.hitboxes.HitBox;
import org.bukkit.Material;

import java.util.function.Supplier;

public class CalculateCraftSurfaceTask extends CollectBlocksTask implements Supplier<Effect> {

    private final CraftDataTagKey<?> storageKey;
    private final CraftDataTagKey<Long> timeStampKey;
    final MovecraftWorld world;
    final HitBox checkBox;

    public CalculateCraftSurfaceTask(Craft craft, CraftDataTagKey<?> storageKey, CraftDataTagKey<Long> timeStampKey) {
        super(craft);
        this.storageKey = storageKey;
        this.timeStampKey = timeStampKey;
        this.world = craft.getMovecraftWorld();
        this.checkBox = new BitmapHitBox(craft.getHitBox());
    }

    @Override
    public Effect get() {
        if (this.checkBox.isEmpty()) {
            return Effect.NONE;
        }
        // Apply the surface
        return new ApplySurfaceEffect(this.craft, this.storageKey.key(), this.getLocations())
        // Afterwards, reset the datatag so it fetches again
        .andThen(() -> {
            craft.resetDataTag(storageKey);
            craft.setDataTag(timeStampKey, System.currentTimeMillis());
        });
    }

    static MovecraftLocation[] CHECK_DIRECTIONS = new MovecraftLocation[] {
            new MovecraftLocation(1,0,0),
            new MovecraftLocation(0,1,0),
            new MovecraftLocation(0,0,1),
            new MovecraftLocation(-1,0,0),
            new MovecraftLocation(0,-1,0),
            new MovecraftLocation(0,0, -1)
    };

    @Override
    protected boolean checkBlock(MovecraftLocation checkLocation) {
        for (MovecraftLocation direction : CHECK_DIRECTIONS) {
            final MovecraftLocation checkLocAct = checkLocation.add(direction);

            boolean add = !(checkBox.inBounds(checkLocAct) && checkBox.contains(checkLocAct));
            if (!add) {
                Material matAtLoc = world.getMaterial(checkLocAct);
                if (matAtLoc.isAir()) {
                    add = true;
                }
            }

            if (add) {
                return true;
            }
        }
        return false;
    }

}
