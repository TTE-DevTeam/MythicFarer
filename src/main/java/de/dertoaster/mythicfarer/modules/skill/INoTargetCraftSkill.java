package de.dertoaster.mythicfarer.modules.skill;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.skills.INoTargetSkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import net.countercraft.movecraft.craft.Craft;

public interface INoTargetCraftSkill extends INoTargetSkill, ICraftAwareSkill {

    @Override
    default SkillResult cast(SkillMetadata skillMetadata) {
        final AbstractEntity abstractEntity = skillMetadata.getCaster().getEntity();
        if (abstractEntity != null) {
            final Craft craft = this.getCraftByMythicMob(abstractEntity);
            if (craft != null) {
                return castAtCraft(skillMetadata, abstractEntity, craft);
            }
        }
        return SkillResult.INVALID_TARGET;
    }

    SkillResult castAtCraft(SkillMetadata skillMetadata, AbstractEntity entityTarget, Craft craftTarget);

}
