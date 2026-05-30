package de.dertoaster.mythicfarer.modules.skill.movement.rudder.helm;

import de.dertoaster.modulecore.AbstractModularPlugin;
import de.dertoaster.mythicfarer.modules.skill.movement.rudder.AbstractTTEAdditionsDependentModule;
import de.dertoaster.mythicfarer.modules.skill.movement.rudder.helm.interact.InteractHelmRudderMechanic;
import de.dertoaster.mythicfarer.modules.skill.movement.rudder.helm.setangle.SetHelmRudderAngleMechanic;
import de.dertoaster.mythicfarer.modules.skill.movement.rudder.helm.setbearing.SetBearingMechanic;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ISkillMechanic;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class HelmSkillsModule extends AbstractTTEAdditionsDependentModule {
    @Override
    protected Map<String, Function<MythicLineConfig, ISkillMechanic>> getProvidedSkills() {
        Map<String, Function<MythicLineConfig, ISkillMechanic>> result = new HashMap();

        // Non-tte dependent
        result.put("interactHelmRudder", InteractHelmRudderMechanic::new);
        // tte-additiosn dependent
        if (this.tteAdditionsIsLoaded()) {
            result.put("setHelmAngle", SetHelmRudderAngleMechanic::new);
            result.put("setBearing", SetBearingMechanic::new);
        }

        return result;
    }

    @Override
    protected boolean requiresTTEAlways() {
        return false;
    }

    @Override
    public String getModuleName() {
        return "HelmSkills";
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
