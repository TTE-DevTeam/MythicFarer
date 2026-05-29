package de.dertoaster.mythicfarer;

import de.dertoaster.modulecore.AbstractModularPlugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class MythicFarer extends AbstractModularPlugin<AbstractMythicFarerModule> {

    // Initialize INSTANCE field
    {
        INSTANCE = this;
    }

    private static JavaPlugin INSTANCE;

    public static JavaPlugin getInstance() {
        return INSTANCE;
    }

    @Override
    protected Class<AbstractMythicFarerModule> getModuleBaseClass() {
        return AbstractMythicFarerModule.class;
    }
}
