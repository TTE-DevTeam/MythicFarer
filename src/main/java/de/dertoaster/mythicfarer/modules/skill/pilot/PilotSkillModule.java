package de.dertoaster.mythicfarer.modules.skill.pilot;

import de.dertoaster.modulecore.AbstractModularPlugin;
import de.dertoaster.mythicfarer.modules.skill.AbstractSkillModule;
import de.dertoaster.mythicfarer.modules.skill.pilot.mechanic.PilotMechanic;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ISkillMechanic;

import java.util.Map;
import java.util.function.Function;

public class PilotSkillModule extends AbstractSkillModule {
    @Override
    protected Map<String, Function<MythicLineConfig, ISkillMechanic>> getProvidedSkills() {
        return Map.of("pilotCraft", PilotMechanic::new);
    }

    @Override
    public String getModuleName() {
        return "PilotSkill";
    }

    @Override
    public void onInit(AbstractModularPlugin<?> modularPlugin) {

    }

    @Override
    public void onLoad(AbstractModularPlugin<?> modularPlugin) {

    }

    @Override
    public void onEnable(AbstractModularPlugin<?> modularPlugin) {

    }

    @Override
    public void onDisable(AbstractModularPlugin<?> modularPlugin) {

    }
}
