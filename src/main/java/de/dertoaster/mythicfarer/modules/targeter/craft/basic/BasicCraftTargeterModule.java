package de.dertoaster.mythicfarer.modules.targeter.craft.basic;

import de.dertoaster.modulecore.AbstractModularPlugin;
import de.dertoaster.mythicfarer.modules.AbstractMythicMobsIntegration;
import de.dertoaster.mythicfarer.modules.targeter.craft.basic.listener.MythicMobsLoadListener;
import net.countercraft.movecraft.craft.type.TypeSafeCraftType;
import net.countercraft.movecraft.craft.type.Validators;
import net.countercraft.movecraft.util.Pair;

public class BasicCraftTargeterModule extends AbstractMythicMobsIntegration {
    @Override
    public String getModuleName() {
        return "BasicCraftTargeter";
    }

    @Override
    public void onInit(AbstractModularPlugin<?> modularPlugin) {

    }

    @Override
    public void onLoad(AbstractModularPlugin<?> modularPlugin) {
        TypeSafeCraftType.PROPERTY_REGISTRY.register(CraftProperties.CRAFT_SURFACE_LIFETIME.key(), CraftProperties.CRAFT_SURFACE_LIFETIME);

        Validators.register(new Pair<>(type -> {
            int value = type.get(CraftProperties.CRAFT_SURFACE_LIFETIME);
            return value >= 0;
        }, "Surface lifetime must be at least 0!"));
    }

    @Override
    public void onEnable(AbstractModularPlugin<?> modularPlugin) {
        DataTags.register();

        modularPlugin.getServer().getPluginManager().registerEvents(new MythicMobsLoadListener(this), modularPlugin);
    }

    @Override
    public void onDisable(AbstractModularPlugin<?> modularPlugin) {

    }
}
