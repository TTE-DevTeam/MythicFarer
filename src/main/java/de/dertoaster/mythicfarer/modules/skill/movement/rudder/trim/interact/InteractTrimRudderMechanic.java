package de.dertoaster.mythicfarer.modules.skill.movement.rudder.trim.interact;

import de.dertoaster.movecrafttteadditions.features.movementrework.sign.TrimSign;
import de.dertoaster.mythicfarer.modules.skill.AbstractTargetedCraftSkill;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.craft.SinkingCraft;
import org.bukkit.event.block.Action;
import org.jetbrains.annotations.Nullable;

public class InteractTrimRudderMechanic extends AbstractTargetedCraftSkill {

    protected final Action clickType;

    public InteractTrimRudderMechanic(MythicLineConfig mlc) {
        super(mlc);

        this.clickType = mlc.getEnum(new String[]{"clickType"}, Action.class, Action.RIGHT_CLICK_BLOCK);
    }

    @Override
    protected SkillResult cast(SkillMetadata skillMetadata, AbstractLocation targetLocation, @Nullable AbstractEntity targetEntity, Craft craftAtTarget) {
        if (craftAtTarget instanceof SinkingCraft)
            return SkillResult.CONDITION_FAILED;

        if (!TrimSign.process(this.clickType, craftAtTarget)) {
            // Returns false if nothign was changed, how do we react? Do we care at all?
            return SkillResult.CONDITION_FAILED;
        }

        return SkillResult.SUCCESS;
    }
}
