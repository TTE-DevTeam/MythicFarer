package de.dertoaster.mythicfarer.modules.skill.movement.rudder.trim.setangle;

import de.dertoaster.movecrafttteadditions.features.movementrework.DataTags;
import de.dertoaster.movecrafttteadditions.features.movementrework.MovementReworkFeature;
import de.dertoaster.mythicfarer.modules.skill.AbstractTargetedCraftSkill;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.craft.SinkingCraft;
import org.jetbrains.annotations.Nullable;

public class SetTrimRudderAngleMechanic extends AbstractTargetedCraftSkill {

    protected final double angleInDegree;

    public SetTrimRudderAngleMechanic(MythicLineConfig mlc) {
        super(mlc);
        this.angleInDegree = Math.toRadians(mlc.getDouble("angle", 0.0D));
    }

    @Override
    protected SkillResult cast(SkillMetadata skillMetadata, AbstractLocation targetLocation, @Nullable AbstractEntity targetEntity, Craft craftAtTarget) {
        if (craftAtTarget instanceof SinkingCraft)
            return SkillResult.CONDITION_FAILED;

        final double min = craftAtTarget.getCraftProperties().get(MovementReworkFeature.ELEVATOR_MIN_ANGLE);
        final double max = craftAtTarget.getCraftProperties().get(MovementReworkFeature.ELEVATOR_MAX_ANGLE);

        final double value = Math.clamp(this.angleInDegree, min, max);
        craftAtTarget.setDataTag(DataTags.ELEVATOR_RUDDER_ANGLE, value);

        return SkillResult.SUCCESS;
    }
}
