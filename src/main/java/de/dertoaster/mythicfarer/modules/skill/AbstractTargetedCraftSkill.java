package de.dertoaster.mythicfarer.modules.skill;

import de.dertoaster.mythicfarer.util.CraftUtil;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.*;
import net.countercraft.movecraft.MovecraftLocation;
import net.countercraft.movecraft.craft.Craft;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;

public abstract class AbstractTargetedCraftSkill extends AbstractCraftAwareSkill implements INoTargetSkill, ITargetedLocationSkill, ITargetedEntitySkill {

    public AbstractTargetedCraftSkill(MythicLineConfig mlc) {
        super(mlc);
    }

    @Override
    public SkillResult cast(SkillMetadata skillMetadata) {
        return cast(skillMetadata, skillMetadata.getCaster().getLocation(), skillMetadata.getCaster().getEntity());
    }

    @Override
    public SkillResult castAtEntity(SkillMetadata skillMetadata, AbstractEntity abstractEntity) {
        return cast(skillMetadata, abstractEntity != null ? abstractEntity.getLocation() : skillMetadata.getCaster().getLocation(), abstractEntity);
    }

    @Override
    public SkillResult castAtLocation(SkillMetadata skillMetadata, AbstractLocation abstractLocation) {
        return cast(skillMetadata, abstractLocation, null);
    }

    protected SkillResult cast(SkillMetadata skillMetadata, AbstractLocation targetLocation, @Nullable AbstractEntity targetEntity) {
        final Craft craft = this.getCraftByMobOrLocation(targetEntity, targetLocation);
        if (craft == null) {
            return SkillResult.INVALID_TARGET;
        }
        return this.cast(skillMetadata, targetLocation, targetEntity, craft);
    }

    protected abstract SkillResult cast(SkillMetadata skillMetadata, AbstractLocation targetLocation, @Nullable AbstractEntity targetEntity, Craft craftAtTarget);

    @Nullable
    protected Craft getCraftByMobOrLocation(final @Nullable AbstractEntity targetEntity, final @NotNull AbstractLocation targetLocation) {
        Craft result = null;
        if (targetEntity != null) {
            result = this.getCraftByMythicMob(targetEntity);
        }
        if (result == null) {
            final MovecraftLocation location = new MovecraftLocation(targetLocation.getBlockX(), targetLocation.getBlockY(), targetLocation.getBlockZ());
            final UUID worldUUID = targetLocation.getWorld().getUniqueId();
            result = CraftUtil.fastFindCraftAt(location, worldUUID, true);
        }
        return result;
    }
}
