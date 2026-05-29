package de.dertoaster.mythicfarer.modules.skill.scuttle;

import de.dertoaster.modulecore.AbstractModularPlugin;
import de.dertoaster.mythicfarer.modules.skill.AbstractSkillModule;
import de.dertoaster.mythicfarer.modules.skill.scuttle.mechanic.ScuttleMechanic;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ISkillMechanic;

import java.util.Map;
import java.util.function.Function;

public class ScuttleSkillModule extends AbstractSkillModule {
    @Override
    public String getModuleName() {
        return "ScuttleSkill";
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

    @Override
    protected Map<String, Function<MythicLineConfig, ISkillMechanic>> getProvidedSkills() {
        return Map.of("scuttleCraft", ScuttleMechanic::new);
    }
}
