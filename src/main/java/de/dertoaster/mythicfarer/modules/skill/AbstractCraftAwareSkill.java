package de.dertoaster.mythicfarer.modules.skill;

import io.lumine.mythic.api.config.MythicLineConfig;

public abstract class AbstractCraftAwareSkill implements ICraftAwareSkill {

    private final boolean checkPassengerCraft;
    private final boolean checkOwnedCraft;

    public AbstractCraftAwareSkill(MythicLineConfig mlc) {
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
