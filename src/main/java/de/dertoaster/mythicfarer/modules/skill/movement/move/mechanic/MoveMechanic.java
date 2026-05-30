package de.dertoaster.mythicfarer.modules.skill.movement.move.mechanic;

import de.dertoaster.mythicfarer.modules.skill.AbstractTargetedCraftSkill;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import net.countercraft.movecraft.MovecraftLocation;
import net.countercraft.movecraft.craft.Craft;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Nullable;

public class MoveMechanic extends AbstractTargetedCraftSkill {

    protected final double vx;
    protected final double vy;
    protected final double vz;

    public MoveMechanic(MythicLineConfig mlc) {
        super(mlc);

        this.vx = mlc.getDouble("vX", 1);
        this.vy = mlc.getDouble("vY", 1);
        this.vz = mlc.getDouble("vZ", 1);
    }

    @Override
    protected SkillResult cast(SkillMetadata skillMetadata, AbstractLocation targetLocation, @Nullable AbstractEntity targetEntity, Craft craftAtTarget) {
        // TODO: Check and store the interaction times => InteractListener#storeInteraction() and InteractListener#isCraftReadyForInteraction()
        final Vector cruiseVector = new Vector(vx, vy, vz);
        Vector discreteTranslation = craftAtTarget.translate(craftAtTarget.getWorld(), cruiseVector);
        craftAtTarget.setLastTranslation(new MovecraftLocation((int) discreteTranslation.getX(), (int) discreteTranslation.getY(), (int) discreteTranslation.getZ()));
        craftAtTarget.setLastCruiseUpdate(System.currentTimeMillis());

        return SkillResult.SUCCESS;
    }

    @Override
    public boolean checkOwnedCraft() {
        return false;
    }

    @Override
    public boolean checkPassengerCraft() {
        return false;
    }

}
