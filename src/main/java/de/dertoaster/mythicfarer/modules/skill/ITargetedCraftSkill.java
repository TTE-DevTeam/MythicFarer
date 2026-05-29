package de.dertoaster.mythicfarer.modules.skill;

import de.dertoaster.mythicfarer.util.CraftUtil;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.skills.INoTargetSkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import net.countercraft.movecraft.MovecraftLocation;
import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.craft.CraftManager;
import org.bukkit.entity.Entity;

import java.util.UUID;

public interface ITargetedCraftSkill extends INoTargetSkill {

    @Override
    default SkillResult cast(SkillMetadata skillMetadata) {
        final AbstractEntity abstractEntity = skillMetadata.getCaster().getEntity();
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
            if (craft != null) {
                return castAtCraft(skillMetadata, abstractEntity, craft);
            }
        }
        return SkillResult.INVALID_TARGET;
    }

    // Return wether or not to check our own craft
    boolean checkOwnedCraft();
    // Return wether or not to check a craft of which we are a passenger
    boolean checkPassengerCraft();

    SkillResult castAtCraft(SkillMetadata skillMetadata, AbstractEntity entityTarget, Craft craftTarget);
}
