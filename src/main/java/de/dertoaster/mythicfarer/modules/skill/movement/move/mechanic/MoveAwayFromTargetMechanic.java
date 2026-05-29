package de.dertoaster.mythicfarer.modules.skill.movement.move.mechanic;

import de.dertoaster.mythicfarer.modules.skill.AbstractCraftAwareSkill;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.ITargetedLocationSkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import net.countercraft.movecraft.MovecraftLocation;
import net.countercraft.movecraft.craft.Craft;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class MoveAwayFromTargetMechanic extends AbstractCraftAwareSkill implements ITargetedEntitySkill, ITargetedLocationSkill {

    protected final double maxX;
    protected final double maxY;
    protected final double maxZ;

    public MoveAwayFromTargetMechanic(MythicLineConfig mlc) {
        super(mlc);

        this.maxX = mlc.getDouble("maxJumpLengthX", 1);
        this.maxY = mlc.getDouble("maxJumpLengthY", 1);
        this.maxZ = mlc.getDouble("maxJumpLengthZ", 1);
    }

    @Override
    public boolean checkOwnedCraft() {
        return false;
    }

    @Override
    public boolean checkPassengerCraft() {
        return false;
    }

    @Override
    public SkillResult castAtEntity(SkillMetadata skillMetadata, AbstractEntity abstractEntity) {
        return cast(skillMetadata, abstractEntity.getLocation());
    }

    @Override
    public SkillResult castAtLocation(SkillMetadata skillMetadata, AbstractLocation abstractLocation) {
        return cast(skillMetadata, abstractLocation);
    }

    protected SkillResult cast(SkillMetadata skillMetadata, AbstractLocation targetLocation) {
        final AbstractEntity abstractEntity = skillMetadata.getCaster().getEntity();
        if (abstractEntity != null) {

            if (!targetLocation.getWorld().getUniqueId().equals(abstractEntity.getWorld().getUniqueId())) {
                return SkillResult.CONDITION_FAILED;
            }

            final Craft craft = this.getCraftByMythicMob(abstractEntity);
            if (craft != null) {
                final AbstractLocation delta = abstractEntity.getLocation().subtract(targetLocation);

                // TODO: Check and store the interaction times => InteractListener#storeInteraction() and InteractListener#isCraftReadyForInteraction()
                final Vector cruiseVector = getVector(delta);
                Vector discreteTranslation = craft.translate(craft.getWorld(), cruiseVector);
                craft.setLastTranslation(new MovecraftLocation((int) discreteTranslation.getX(), (int) discreteTranslation.getY(), (int) discreteTranslation.getZ()));
                craft.setLastCruiseUpdate(System.currentTimeMillis());

                return SkillResult.SUCCESS;
            }
        }
        return SkillResult.CONDITION_FAILED;
    }

    @NotNull
    private Vector getVector(AbstractLocation delta) {
        double x = Math.min(this.maxX, Math.abs(delta.getX()));
        if (delta.getX() < 0) {
            x = -x;
        }
        double y = Math.min(this.maxY, Math.abs(delta.getY()));
        if (delta.getY() < 0) {
            y = -y;
        }
        double z = Math.min(this.maxZ, Math.abs(delta.getZ()));
        if (delta.getZ() < 0) {
            z = -z;
        }

        final Vector cruiseVector = new Vector(x, y, z);
        return cruiseVector;
    }

}
