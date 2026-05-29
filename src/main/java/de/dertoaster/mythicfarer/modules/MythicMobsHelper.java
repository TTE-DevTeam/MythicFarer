package de.dertoaster.mythicfarer.modules;

import io.lumine.mythic.bukkit.MythicBukkit;

public class MythicMobsHelper {

    // TODO: Subscribe to MythicTargeterLoadEvent and register the targeters there!

    private static MythicBukkit mythicBukkit;

    private static boolean loadMythicMobs() {
        if (mythicBukkit != null) {
            return true;
        }
        try {
            final MythicBukkit mythicBukkitTmp = MythicBukkit.inst();
            if (mythicBukkitTmp != null) {
                mythicBukkit = mythicBukkitTmp;
                return true;
            }
        } catch(NoClassDefFoundError ncdfe) {
            throw new IllegalStateException("MythicMobs class not found! This should NEVER happen!");
        }
        return false;
    }

    public static MythicBukkit getMythicMobs() {
        if (mythicBukkit == null) {
            if (!loadMythicMobs()) {
                throw new RuntimeException("MythicMobs not found!");
            }
        }
        return mythicBukkit;
    }

}
