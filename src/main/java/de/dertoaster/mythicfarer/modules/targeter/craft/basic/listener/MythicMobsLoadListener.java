package de.dertoaster.mythicfarer.modules.targeter.craft.basic.listener;

import de.dertoaster.modulecore.ModuleListener;
import de.dertoaster.mythicfarer.AbstractMythicFarerModule;
import de.dertoaster.mythicfarer.modules.targeter.craft.basic.targeter.BasicCraftTargeter;
import io.lumine.mythic.bukkit.events.MythicTargeterLoadEvent;
import org.bukkit.event.EventHandler;

public class MythicMobsLoadListener extends ModuleListener<AbstractMythicFarerModule> {

    public MythicMobsLoadListener(AbstractMythicFarerModule module) {
        super(module);
    }

    @EventHandler(ignoreCancelled = true)
    public void onTargeterLoad(final MythicTargeterLoadEvent event) {
        final String targeterName = event.getTargeterName();
        // Targeter that targets surface blocks of the craft
        if (targeterName.equalsIgnoreCase("BASIC_CRAFT_TARGETER")) {
            event.register(new BasicCraftTargeter(event.getConfig()));
        }
    }

}
