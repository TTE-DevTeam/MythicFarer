package de.dertoaster.mythicfarer.modules.skill.movement.rudder.helm.interact;

import de.dertoaster.mythicfarer.modules.skill.AbstractTargetedCraftSkill;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.craft.SinkingCraft;
import net.countercraft.movecraft.craft.controller.AbstractRotationController;
import net.countercraft.movecraft.craft.type.PropertyKeys;
import org.bukkit.entity.Entity;
import org.bukkit.event.block.Action;
import org.jetbrains.annotations.Nullable;

public class InteractHelmRudderMechanic extends AbstractTargetedCraftSkill {

    protected final Action clickType;

    public InteractHelmRudderMechanic(MythicLineConfig mlc) {
        super(mlc);

        this.clickType = mlc.getEnum(new String[]{"clickType"}, Action.class, Action.RIGHT_CLICK_BLOCK);
    }

    @Override
    protected SkillResult cast(SkillMetadata skillMetadata, AbstractLocation targetLocation, @Nullable AbstractEntity targetEntity, Craft craftAtTarget) {
        if (craftAtTarget instanceof SinkingCraft) {
            return SkillResult.INVALID_TARGET;
        }
        Entity bukkitEntity = null;
        if (targetEntity != null) {
            bukkitEntity = targetEntity.getBukkitEntity();
        }
        if (bukkitEntity == null && skillMetadata.getCaster().getEntity() != null) {
            bukkitEntity = skillMetadata.getCaster().getEntity().getBukkitEntity();
        }
        if (bukkitEntity == null) {
            return SkillResult.INVALID_TARGET;
        }

        final AbstractRotationController rotationController = craftAtTarget.getCraftProperties().get(PropertyKeys.ROTATION_CONTROLLER);
        if (rotationController == null) {
            return SkillResult.INVALID_TARGET;
        }

        if (rotationController.onHelmInteraction(craftAtTarget, null, this.clickType, bukkitEntity)) {
            return SkillResult.SUCCESS;
        } else {
            return SkillResult.CONDITION_FAILED;
        }
    }
}
