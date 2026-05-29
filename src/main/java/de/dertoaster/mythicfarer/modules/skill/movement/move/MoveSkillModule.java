package de.dertoaster.mythicfarer.modules.skill.movement.move;

import de.dertoaster.modulecore.AbstractModularPlugin;
import de.dertoaster.mythicfarer.modules.skill.AbstractSkillModule;
import de.dertoaster.mythicfarer.modules.skill.movement.move.mechanic.MoveAwayFromTargetMechanic;
import de.dertoaster.mythicfarer.modules.skill.movement.move.mechanic.MoveMechanic;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ISkillMechanic;

import java.util.Map;
import java.util.function.Function;

public class MoveSkillModule extends AbstractSkillModule {
    @Override
    protected Map<String, Function<MythicLineConfig, ISkillMechanic>> getProvidedSkills() {
        return Map.of(
                "moveCraft", MoveMechanic::new,
                "moveCraftAwayFromTarget", MoveAwayFromTargetMechanic::new
        );
    }

    @Override
    public String getModuleName() {
        return "MoveSkill";
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
