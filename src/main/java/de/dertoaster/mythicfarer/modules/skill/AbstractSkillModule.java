package de.dertoaster.mythicfarer.modules.skill;

import de.dertoaster.mythicfarer.modules.AbstractMythicMobsIntegration;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ISkillMechanic;
import io.lumine.mythic.bukkit.events.MythicMechanicLoadEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Map;
import java.util.function.Function;

public abstract class AbstractSkillModule extends AbstractMythicMobsIntegration implements Listener {

    // TODO: Migrate into AbstractMythicMobsIntegration
    @EventHandler(ignoreCancelled = true)
    public void onTargeterLoad(final MythicMechanicLoadEvent event) {
        final String mechanicName = event.getMechanicName();
        // Targeter that targets surface blocks of the craft
        // TODO: Perhaps replace with annotations?
        final Function<MythicLineConfig, ISkillMechanic> constructor = this.getProvidedSkills().getOrDefault(mechanicName, null);
        if (constructor != null) {
            event.register(constructor.apply(event.getConfig()));
        }
    }

    protected abstract Map<String, Function<MythicLineConfig, ISkillMechanic>> getProvidedSkills();

}
