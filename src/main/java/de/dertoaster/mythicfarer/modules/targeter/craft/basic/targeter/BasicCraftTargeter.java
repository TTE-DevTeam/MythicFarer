package de.dertoaster.mythicfarer.modules.targeter.craft.basic.targeter;

import de.dertoaster.mythicfarer.modules.targeter.craft.basic.DataTags;
import de.dertoaster.mythicfarer.modules.MythicMobsHelper;
import io.lumine.mythic.api.adapters.AbstractLocation;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.targeters.IEntityTargeter;
import io.lumine.mythic.api.skills.targeters.ILocationTargeter;
import io.lumine.mythic.bukkit.BukkitAdapter;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.skills.SkillTargeter;
import io.lumine.mythic.core.skills.targeters.ILocationSelector;
import net.countercraft.movecraft.MovecraftLocation;
import net.countercraft.movecraft.TrackedLocation;
import net.countercraft.movecraft.craft.*;
import net.countercraft.movecraft.util.NamespacedIDUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.World;

import java.util.*;
import java.util.stream.Collectors;

public class BasicCraftTargeter extends ILocationSelector implements ILocationTargeter  {

    //ILocationSelector
    protected final Set<NamespacedKey> targetableBlocks;
    protected final double maxDistance;
    protected final Set<String> craftTypeFilter;
    protected final IEntityTargeter pilotEntityVerifier;
    protected final int maxLocationsPerCraft;
    protected final int minLocationsPerCraft;
    protected final int maxTargetedCrafts;
    protected final boolean shuffleCraftLocations;

    public BasicCraftTargeter(MythicLineConfig mlc) {
        super(MythicMobsHelper.getMythicMobs().getSkillManager(), mlc);

        this.targetableBlocks = mlc.getStringList("blocks", "").stream().map(NamespacedKey::fromString).filter(Objects::nonNull).collect(Collectors.toSet());
        this.maxDistance = mlc.getDouble("maxDistance", -1);
        this.craftTypeFilter = mlc.getStringList("craftTypes", "").stream().map(String::toUpperCase).collect(Collectors.toSet());
        int max = mlc.getInteger("maxLocationsPerCraft", -1);
        int min = mlc.getInteger("minLocationsPerCraft", -1);
        this.maxLocationsPerCraft = Math.max(min, max);
        this.minLocationsPerCraft = Math.min(min, max);
        this.maxTargetedCrafts = mlc.getInteger("maxTargetableCrafts", 1);
        this.shuffleCraftLocations = mlc.getBoolean("randomizeCraftPositions", false);
        String pilotTargeterString = mlc.getString("pilotTargeter", "");
        if (pilotTargeterString == null || pilotTargeterString.isEmpty()) {
            this.pilotEntityVerifier = null;
        } else {
            SkillTargeter stTmp = null;
            try {
                stTmp = MythicBukkit.inst().getSkillManager().getTargeter(pilotTargeterString);
            } catch(Exception ex) {
                stTmp = null;
                ex.printStackTrace();
            }
            if (stTmp instanceof IEntityTargeter iEntityTargeter) {
                this.pilotEntityVerifier = iEntityTargeter;
            } else {
                // TODO: Log error
                this.pilotEntityVerifier = null;
            }
        }
    }

    @Override
    public Collection<AbstractLocation> getLocations(SkillMetadata skillMetadata) {
        World world = BukkitAdapter.adapt(skillMetadata.getOrigin().getWorld());
        if (world == null) {
            return List.of();
        }

        List<Craft> craftList = new ArrayList<>(CraftManager.getInstance().getCraftsInWorld(world));
        craftList.removeIf(craft -> !checkCraft(craft, skillMetadata));
        if (craftList.isEmpty()) {
            return List.of();
        }
        // Sort crafts by distance to the target
        craftList.sort(new Comparator<Craft>() {
            @Override
            public int compare(Craft o1, Craft o2) {
                final double d1 = getDistance(o1, skillMetadata);
                final double d2 = getDistance(o2, skillMetadata);
                return Double.compare(d1, d2);
            }
        });

        Set<AbstractLocation> resultSet = new HashSet<>();
        // Iterate over the sorted crafts until we find something, then return that
        // DONE: Implement datatags and/or trackedlocations for the desired block locations
        // DONE: Implement updating those => Via access method
        // DONE: Choose locations and return them
        // DONE: Add max-targeted crafts setting
        // DONE: Rule out the craft piloted by the mythicmob => Checked in checkCraft()

        // Go over all crafts now and search the locations
        int targetedCrafts = 0;
        for (Craft craft : craftList) {
            if (targetedCrafts > this.maxTargetedCrafts && this.maxTargetedCrafts > 0) {
                break;
            }
            List<TrackedLocation> surface = new ArrayList<>(DataTags.getSurface(craft));
            if (this.shuffleCraftLocations) {
                Collections.shuffle(surface);
            }
            Set<AbstractLocation> perCraft = new HashSet<>();
            if (surface.isEmpty())
                continue;

            for (TrackedLocation tl : surface) {
                final MovecraftLocation location = tl.getAbsoluteLocation();
                final AbstractLocation abstractLocation = new AbstractLocation(skillMetadata.getOrigin().getWorld(), location.getX(), location.getY(), location.getZ());

                if (!this.targetableBlocks.isEmpty()) {
                    NamespacedKey block = NamespacedIDUtil.getBlockID(BukkitAdapter.adapt(abstractLocation).getBlock());
                    if (this.targetableBlocks.contains(block)) {
                        perCraft.add(abstractLocation);
                    }
                } else {
                    perCraft.add(abstractLocation);
                }
            }

            if (perCraft.size() > 0) {
                // If we need to fulfill a minLocations goal, we can not target this craft
                if (perCraft.size() > this.minLocationsPerCraft || (this.minLocationsPerCraft < 0)) {

                    for (AbstractLocation abstractLocation : perCraft) {
                        resultSet.add(this.mutate(skillMetadata, abstractLocation));
                        if (maxLocationsPerCraft > 0 && resultSet.size() >= maxLocationsPerCraft) {
                            break;
                        }
                    }

                    targetedCrafts++;
                }
            }
        }

        return resultSet;
    }

    protected static final double getDistance(final Craft craft, final SkillMetadata skillMetadata) {
        final AbstractLocation casterLocation = skillMetadata.getCaster().getLocation();
        final double dist = craft.getHitBox().getMidPoint().distance(new MovecraftLocation(casterLocation.getBlockX(), casterLocation.getBlockY(), casterLocation.getBlockZ()));
        return dist;
    }

    protected boolean checkCraft(final Craft craft, SkillMetadata skillMetadata) {
        if (craft instanceof SinkingCraft) {
            return false;
        }
        if (this.maxDistance > 0) {
            final double dist = this.getDistance(craft, skillMetadata);
            if (dist > this.maxDistance) {
                return false;
            }
        }
        
        if (this.craftTypeFilter != null && !this.craftTypeFilter.contains(craft.getCraftProperties().getName().toUpperCase())) {
            return false;    
        }
            
        if (craft instanceof PilotedCraft pilotedCraft) {
            final UUID pilotUUID = pilotedCraft.getPilotUUID();
            // DO NOT target your own craft
            if (pilotUUID != null && skillMetadata.getCaster() != null && skillMetadata.getCaster().getEntity() != null && pilotUUID.equals(skillMetadata.getCaster().getEntity().getUniqueId())) {
                return false;
            }
            // Otherwise, check if the pilot already matches your entity verifier
            if (this.pilotEntityVerifier != null) {
                if (pilotUUID == null) {
                    return false;
                }
                else {
                    // TODO: This is not pretty... maybe we can make it better?
                    return this.pilotEntityVerifier.getEntities(skillMetadata).stream().map(mmEntity -> BukkitAdapter.adapt(mmEntity).getUniqueId()).anyMatch(uuid -> uuid.equals(pilotUUID));
                }
            }
        }
        // TODO: Subcraft and torpedo craft-like handling!
        return true;
    }

}
