package de.dertoaster.mythicfarer.modules.skill.movement.rudder.trim;

import de.dertoaster.modulecore.AbstractModularPlugin;
import de.dertoaster.mythicfarer.modules.skill.movement.rudder.AbstractTTEAdditionsDependentModule;
import de.dertoaster.mythicfarer.modules.skill.movement.rudder.trim.interact.InteractTrimRudderMechanic;
import de.dertoaster.mythicfarer.modules.skill.movement.rudder.trim.setangle.SetTrimRudderAngleMechanic;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ISkillMechanic;

import java.util.Map;
import java.util.function.Function;

public class TrimSkillsModule extends AbstractTTEAdditionsDependentModule {
    @Override
    protected Map<String, Function<MythicLineConfig, ISkillMechanic>> getProvidedSkills() {
        return Map.of(
                "interactTrimRudder", InteractTrimRudderMechanic::new,
                "setTrimAngle", SetTrimRudderAngleMechanic::new);
    }

    @Override
    public String getModuleName() {
        return "TrimSkills";
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
