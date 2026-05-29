package de.dertoaster.mythicfarer.modules.skill;

import io.lumine.mythic.api.config.MythicLineConfig;

public abstract class AbstractTargetedCraftSkill implements ITargetedCraftSkill {
    private final boolean checkPassengerCraft;
    private final boolean checkOwnedCraft;

    public AbstractTargetedCraftSkill(MythicLineConfig mlc) {
        this.checkPassengerCraft = mlc.getBoolean("checkPassengerCraft", false);
        this.checkOwnedCraft = mlc.getBoolean("checkOwnedCraft", true);
    }

    @Override
    public boolean checkOwnedCraft() {
        return this.checkOwnedCraft;
    }

    @Override
    public boolean checkPassengerCraft() {
        return this.checkPassengerCraft;
    }

}
