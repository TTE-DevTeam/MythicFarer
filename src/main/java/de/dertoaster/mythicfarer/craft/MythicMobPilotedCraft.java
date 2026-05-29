package de.dertoaster.mythicfarer.craft;

import net.countercraft.movecraft.craft.PilotedCraftImpl;
import net.countercraft.movecraft.craft.type.TypeSafeCraftType;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class MythicMobPilotedCraft extends PilotedCraftImpl {

    public MythicMobPilotedCraft(@NotNull TypeSafeCraftType type, @NotNull World world, @NotNull Entity pilot) {
        super(type, world, pilot);
    }

}
