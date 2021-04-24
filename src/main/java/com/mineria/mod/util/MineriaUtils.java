package com.mineria.mod.util;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class MineriaUtils
{
    public static <T> T make(T obj, Consumer<T> consumer)
    {
        consumer.accept(obj);
        return obj;
    }

    /**
     * Accepts the given {@link Consumer} if the given {@link Predicate} returns true.
     *
     * @param obj the object to test.
     * @param condition the condition required to accept the consumer.
     * @param action the action executed if the condition is true.
     * @param <T> the type of the tested object.
     * @return the result of the condition.
     */
    public static <T> boolean doIf(T obj, Predicate<T> condition, Consumer<T> action)
    {
        boolean test = condition.test(obj);
        if(test)
            action.accept(obj);
        return test;
    }
}
