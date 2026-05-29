package de.dertoaster.mythicfarer.modules.skill;

import de.dertoaster.mythicfarer.util.CraftUtil;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import net.countercraft.movecraft.MovecraftLocation;
import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.craft.CraftManager;
import org.bukkit.entity.Entity;

import javax.annotation.Nullable;
import java.util.UUID;

public interface ICraftAwareSkill {

    // Return wether or not to check our own craft
    boolean checkOwnedCraft();
    // Return wether or not to check a craft of which we are a passenger
    boolean checkPassengerCraft();

    @Nullable
    public default Craft getCraftByMythicMob(final AbstractEntity abstractEntity) {
        Entity bukkitEntity = abstractEntity.getBukkitEntity();;
        if (bukkitEntity != null) {
            Craft craft = null;
            if (this.checkOwnedCraft()) {
                craft = CraftManager.getInstance().getCraftByEntity(bukkitEntity);
            }
            if (craft == null && this.checkPassengerCraft()) {
                final MovecraftLocation location = new MovecraftLocation(abstractEntity.getLocation().getBlockX(), abstractEntity.getLocation().getBlockY(), abstractEntity.getLocation().getBlockZ());
                final UUID worldUUID = abstractEntity.getWorld().getUniqueId();
                craft = CraftUtil.fastFindCraftAt(location, worldUUID, true);
            }
            return craft;
        }
        return null;
    }

}
