package de.dertoaster.mythicfarer.modules.skill.scuttle.mechanic;

import de.dertoaster.mythicfarer.modules.skill.AbstractTargetedCraftSkill;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.craft.CraftManager;
import net.countercraft.movecraft.craft.SinkingCraft;
import net.countercraft.movecraft.events.CraftSinkEvent;
import org.jetbrains.annotations.Nullable;

public class ScuttleMechanic extends AbstractTargetedCraftSkill {
    public ScuttleMechanic(MythicLineConfig mlc) {
        super(mlc);
    }

    @Override
    protected SkillResult cast(SkillMetadata skillMetadata, AbstractLocation targetLocation, @Nullable AbstractEntity targetEntity, Craft craftAtTarget) {
        if (craftAtTarget instanceof SinkingCraft) {
            return SkillResult.INVALID_TARGET;
        }

        CraftManager.getInstance().sink(craftAtTarget, CraftSinkEvent.SIMPLE_SINK_REASONS.SCUTTLE);
        // Event was cancelled, craft was not scuttled
        if (CraftManager.getInstance().getCrafts().contains(craftAtTarget)) {
            return SkillResult.CONDITION_FAILED;
        } else {
            return SkillResult.SUCCESS;
        }
    }
}
