package de.dertoaster.mythicfarer.modules.skill.scuttle.mechanic;

import de.dertoaster.mythicfarer.modules.skill.AbstractTargetedCraftSkill;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.craft.CraftManager;
import net.countercraft.movecraft.craft.SinkingCraft;
import net.countercraft.movecraft.events.CraftSinkEvent;

public class ScuttleMechanic extends AbstractTargetedCraftSkill {
    public ScuttleMechanic(MythicLineConfig mlc) {
        super(mlc);
    }

    @Override
    public SkillResult castAtCraft(SkillMetadata skillMetadata, AbstractEntity entityTarget, Craft craftTarget) {
        if (craftTarget instanceof SinkingCraft) {
            return SkillResult.INVALID_TARGET;
        }

        CraftManager.getInstance().sink(craftTarget, CraftSinkEvent.SIMPLE_SINK_REASONS.SCUTTLE);
        // Event was cancelled, craft was not scuttled
        if (CraftManager.getInstance().getCrafts().contains(craftTarget)) {
            return SkillResult.CONDITION_FAILED;
        } else {
            return SkillResult.SUCCESS;
        }
    }
}
