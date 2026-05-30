package de.dertoaster.mythicfarer.modules.skill.movement.rudder;

import de.dertoaster.mythicfarer.modules.skill.AbstractSkillModule;
import org.bukkit.Bukkit;

public abstract class AbstractTTEAdditionsDependentModule extends AbstractSkillModule {

    @Override
    public boolean shouldLoad() {
        if (super.shouldLoad()) {
            return Bukkit.getPluginManager().getPlugin("Movecraft-TTE-Additions") != null;
        } else {
            return false;
        }
    }
}
