package de.dertoaster.mythicfarer.util.task;

import com.google.common.collect.Sets;
import net.countercraft.movecraft.MovecraftLocation;
import net.countercraft.movecraft.craft.Craft;
import net.countercraft.movecraft.processing.effects.Effect;
import net.countercraft.movecraft.util.hitboxes.HitBoxSlicer;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinTask;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class CollectBlocksTask implements Supplier<Effect> {

    protected final Craft craft;

    public CollectBlocksTask(Craft craft) {
        this.craft = craft;
    }

    protected abstract boolean checkBlock(final MovecraftLocation location);

    protected Set<MovecraftLocation> getLocations() {
        ArrayList<ForkJoinTask<WorkerData>> workers = new ArrayList<>();
        new HitBoxSlicer(craft.getHitBox()).forEach(slice -> workers.add(ForkJoinTask.adapt(new Worker(slice, this::checkBlock, Sets.newConcurrentHashSet()))));

        Optional<WorkerData> workResult = ForkJoinTask
                .invokeAll(workers)
                .stream()
                .map(ForkJoinTask::join)
                .reduce(WorkerData::add);

        if(workResult.isEmpty()){
            return null;
        } else {
            return workResult.get().locations();
        }
    }

    private record WorkerData(
            Set<MovecraftLocation> locations
    ){

        public WorkerData add(WorkerData other) {
            final Set<MovecraftLocation> newSet = Sets.newConcurrentHashSet(other.locations());
            newSet.addAll(this.locations());
            return new WorkerData(
                    newSet
            );
        }
    }

    // Goes over a single slice and checks for air
    private record Worker(
            @NotNull Iterable<MovecraftLocation> slice,
            @NotNull Predicate<MovecraftLocation> validationRule,
            @NotNull Set<MovecraftLocation> locations
    ) implements Callable<CollectBlocksTask.WorkerData> {

        @Override
        public CollectBlocksTask.WorkerData call() {
            Set<MovecraftLocation> locations = Sets.newConcurrentHashSet();

            for (MovecraftLocation l : slice) {
                if (validationRule.test(l)) {
                    locations.add(l);
                }
            }

            return new WorkerData(locations);
        }
    }

}
