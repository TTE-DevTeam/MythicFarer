package de.dertoaster.mythicfarer.modules.skill.release.mechanic;

import de.dertoaster.mythicfarer.modules.skill.ITargetedCraftSkill;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.craft.CraftManager;
import net.countercraft.movecraft.craft.SinkingCraft;
import net.countercraft.movecraft.events.CraftReleaseEvent;

public class ReleaseMechanic implements ITargetedCraftSkill {

    protected final boolean forceRelease;

    public ReleaseMechanic(MythicLineConfig mlc) {
        this.forceRelease = mlc.getBoolean("forceRelease", false);
    }

    @Override
    public boolean checkOwnedCraft() {
        return true;
    }

    @Override
    public boolean checkPassengerCraft() {
        return false;
    }

    @Override
    public SkillResult castAtCraft(SkillMetadata skillMetadata, AbstractEntity entityTarget, Craft craftTarget) {
        if (craftTarget instanceof SinkingCraft) {
            return SkillResult.INVALID_TARGET;
        }

        if (CraftManager.getInstance().tryRelease(craftTarget, CraftReleaseEvent.Reason.PLAYER, this.forceRelease)) {
            return SkillResult.SUCCESS;
        } else {
            return SkillResult.CONDITION_FAILED;
        }
    }
}
