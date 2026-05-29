package de.dertoaster.mythicfarer.modules.targeter.craft.basic;

import io.lumine.mythic.bukkit.MythicBukkit;
import net.countercraft.movecraft.craft.type.PropertyKey;
import net.countercraft.movecraft.craft.type.PropertyKeyTypes;
import org.bukkit.NamespacedKey;

public class CraftProperties {

    public static final PropertyKey<Integer> CRAFT_SURFACE_LIFETIME = PropertyKeyTypes.intPropertyKey(new NamespacedKey(MythicBukkit.inst().namespace(), "surface_cache_lifetime"), 30000);

}
