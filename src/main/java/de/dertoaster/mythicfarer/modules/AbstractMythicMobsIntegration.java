package de.dertoaster.mythicfarer.modules;

import de.dertoaster.mythicfarer.AbstractMythicFarerModule;
import org.bukkit.Bukkit;

public abstract class AbstractMythicMobsIntegration extends AbstractMythicFarerModule {

    @Override
    public boolean shouldLoad() {
        return Bukkit.getPluginManager().getPlugin("MythicMobs") != null;
    }

}
