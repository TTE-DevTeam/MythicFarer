package de.dertoaster.mythicfarer.modules.skill.movement.rudder.helm.setbearing;

import de.dertoaster.movecrafttteadditions.features.movementrework.Constants;
import de.dertoaster.movecrafttteadditions.features.movementrework.DataTags;
import de.dertoaster.mythicfarer.modules.skill.AbstractTargetedCraftSkill;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.craft.SinkingCraft;
import org.jetbrains.annotations.Nullable;

public class SetBearingMechanic extends AbstractTargetedCraftSkill {

    protected final double angle;

    public SetBearingMechanic(MythicLineConfig mlc) {
        super(mlc);
        this.angle = Math.toRadians(mlc.getDouble("angle", 0.0D));
    }

    @Override
    protected SkillResult cast(SkillMetadata skillMetadata, AbstractLocation targetLocation, @Nullable AbstractEntity targetEntity, Craft craftAtTarget) {
        if (craftAtTarget instanceof SinkingCraft)
            return SkillResult.CONDITION_FAILED;

        final double min = -Constants.HALF_RIGHT_ANGLE_RAD;
        final double max = Constants.HALF_RIGHT_ANGLE_RAD;

        final double value = Math.clamp(this.angle, min, max);
        craftAtTarget.getDataTag(DataTags.BEARING_ANGLE).set(value);

        return SkillResult.SUCCESS;
    }
}