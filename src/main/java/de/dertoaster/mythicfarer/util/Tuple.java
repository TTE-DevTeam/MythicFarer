package de.dertoaster.mythicfarer.util;

public record Tuple<A, B>(
        A a,
        B b
) {

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Tuple<?,?>)) {
            return false;
        }
        Tuple<?, ?> other = (Tuple<?, ?>) obj;
        return other.a().equals(this.a()) && other.b().equals(this.b());
    }
}
