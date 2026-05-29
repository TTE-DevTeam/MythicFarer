package de.dertoaster.mythicfarer.modules.skill.pilot.mechanic;

import de.dertoaster.mythicfarer.util.CraftUtil;
import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.INoTargetSkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import net.countercraft.movecraft.MovecraftLocation;
import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.craft.CraftManager;
import net.countercraft.movecraft.craft.PilotedCraftImpl;
import net.countercraft.movecraft.craft.PlayerCraftImpl;
import net.countercraft.movecraft.craft.type.TypeSafeCraftType;
import net.countercraft.movecraft.localisation.I18nSupport;
import net.countercraft.movecraft.processing.functions.Result;
import net.countercraft.movecraft.util.Pair;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class PilotMechanic implements INoTargetSkill {

    protected final String craftTypeName;

    public PilotMechanic(MythicLineConfig mlc) {
        this.craftTypeName = mlc.getString("craftType", "");
    }

    @Override
    public SkillResult cast(SkillMetadata skillMetadata) {
        final AbstractEntity abstractEntity = skillMetadata.getCaster().getEntity();
        Entity bukkitEntity = abstractEntity.getBukkitEntity();
        if (bukkitEntity == null) {
            return SkillResult.CONDITION_FAILED;
        }
        Craft craftForEntity = CraftUtil.getCraftForEntity(bukkitEntity);

        // If we already have a craft, success or not?
        // TODO: Setting to release owned craft in before
        if (craftForEntity != null) {
            return SkillResult.SUCCESS;
        } else {
            final TypeSafeCraftType craftType = CraftManager.getInstance().getCraftTypeByName(this.craftTypeName);
            if (craftType == null) {
                return SkillResult.INVALID_CONFIG;
            }

            final World world = bukkitEntity.getWorld();
            final MovecraftLocation startPoint = new MovecraftLocation(bukkitEntity.getLocation());

            CraftManager.getInstance().detect(startPoint, craftType, (type, w, p, parents) -> {
                assert p != null;

                if (parents.size() > 0) {
                    return new Pair(Result.failWithMessage(I18nSupport.getInternationalisedString("Detection - Failed - Already commanding a craft")), (Object)null);
                } else if (p instanceof Player) {
                    Player player = (Player)p;
                    return new Pair(Result.succeed(), new PlayerCraftImpl(type, w, player));
                } else {
                    return new Pair(Result.succeed(), new PilotedCraftImpl(type, w, p));
                }
            }, world, bukkitEntity, Bukkit.getConsoleSender(), (craft) -> () -> {
                // TODO: Should we support this?
//                if (executor instanceof Player player) {
//                    Craft oldCraft = CraftManager.getInstance().getCraftByPlayer(player);
//                    if (oldCraft != null) {
//                        CraftManager.getInstance().release(oldCraft, CraftReleaseEvent.Reason.PLAYER, false);
//                    }
//                }
            });
        }
        return SkillResult.SUCCESS;
    }

}
