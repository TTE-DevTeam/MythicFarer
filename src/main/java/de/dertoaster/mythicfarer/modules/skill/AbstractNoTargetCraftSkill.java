package de.dertoaster.mythicfarer.modules.skill;

import io.lumine.mythic.api.config.MythicLineConfig;

public abstract class AbstractNoTargetCraftSkill extends AbstractCraftAwareSkill implements INoTargetCraftSkill {

    public AbstractNoTargetCraftSkill(MythicLineConfig mlc) {
        super(mlc);
    }
}
