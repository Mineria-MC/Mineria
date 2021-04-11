package com.mineria.mod.util;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface TriFunction<T, U, E, R>
{
    R apply(T t, U u, E e);

    default <V> TriFunction<T, U, E, V> andThen(Function<? super R, ? extends V> after)
    {
        Objects.requireNonNull(after);
        return (T t, U u, E e) -> after.apply(apply(t, u, e));
    }
}
