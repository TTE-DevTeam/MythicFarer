package de.dertoaster.mythicfarer.modules.skill.release.mechanic;

import de.dertoaster.mythicfarer.modules.skill.AbstractTargetedCraftSkill;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.craft.CraftManager;
import net.countercraft.movecraft.craft.SinkingCraft;
import net.countercraft.movecraft.events.CraftReleaseEvent;
import org.jetbrains.annotations.Nullable;

public class ReleaseMechanic extends AbstractTargetedCraftSkill {

    protected final boolean forceRelease;

    public ReleaseMechanic(MythicLineConfig mlc) {
        super(mlc);
        this.forceRelease = mlc.getBoolean("forceRelease", false);
    }

    @Override
    protected SkillResult cast(SkillMetadata skillMetadata, AbstractLocation targetLocation, @Nullable AbstractEntity targetEntity, Craft craftAtTarget) {
        if (craftAtTarget instanceof SinkingCraft) {
            return SkillResult.INVALID_TARGET;
        }

        if (CraftManager.getInstance().tryRelease(craftAtTarget, CraftReleaseEvent.Reason.PLAYER, this.forceRelease)) {
            return SkillResult.SUCCESS;
        } else {
            return SkillResult.CONDITION_FAILED;
        }
    }
}
