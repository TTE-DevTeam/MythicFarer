package de.dertoaster.mythicfarer;

import de.dertoaster.modulecore.AbstractModularPlugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

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
    public void onEnable() {
        setLogLevel(Level.INFO);
        super.onEnable();
    }

    private void setLogLevel(Level defaultLevel) {
        Level logLevel = defaultLevel;
        if (getConfig().contains("LogLevel")) {
            String configLL = getConfig().getString("LogLevel", defaultLevel.getName());
            try {
                logLevel = Level.parse(configLL);
            } catch(Exception ex) {
                this.getLogger().warning("Invalid loglevel <" + configLL + "> entered!");
                logLevel = defaultLevel;
            }
        }
        this.getLogger().setLevel(logLevel);
    }

    @Override
    protected Class<AbstractMythicFarerModule> getModuleBaseClass() {
        return AbstractMythicFarerModule.class;
    }
}
