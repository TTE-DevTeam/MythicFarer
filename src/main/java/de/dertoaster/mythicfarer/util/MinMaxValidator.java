package de.dertoaster.mythicfarer.util;

import net.countercraft.movecraft.craft.type.PropertyKey;
import net.countercraft.movecraft.craft.type.TypeSafeCraftType;

import java.util.function.Predicate;

public record MinMaxValidator<T extends Number>(PropertyKey<T> minKey,
                                                PropertyKey<T> maxKey) implements Predicate<TypeSafeCraftType> {

    @Override
    public boolean test(TypeSafeCraftType typeSafeCraftType) {
        if (!(typeSafeCraftType.hasInSelfOrAnyParent(minKey()) && typeSafeCraftType.hasInSelfOrAnyParent(maxKey()))) {
            return true;
        }
        T min = typeSafeCraftType.get(minKey());
        T max = typeSafeCraftType.get(maxKey());
        return min.doubleValue() <= max.doubleValue();
    }
}
