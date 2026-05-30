package de.dertoaster.mythicfarer.modules.skill.movement.rudder;

import de.dertoaster.mythicfarer.modules.skill.AbstractSkillModule;
import org.bukkit.Bukkit;

public abstract class AbstractTTEAdditionsDependentModule extends AbstractSkillModule {

    @Override
    public boolean shouldLoad() {
        if (super.shouldLoad()) {
            return this.tteAdditionsIsLoaded() || !this.requiresTTEAlways();
        } else {
            return false;
        }
    }

    protected final boolean tteAdditionsIsLoaded() {
        return Bukkit.getPluginManager().getPlugin("Movecraft-TTE-Additions") != null;
    }

    protected boolean requiresTTEAlways() {
        return true;
    }
}
