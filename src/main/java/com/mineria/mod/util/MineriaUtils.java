package com.mineria.mod.util;

import java.util.function.Consumer;

public class MineriaUtils
{
    public static <T> T make(T obj, Consumer<T> consumer)
    {
        consumer.accept(obj);
        return obj;
    }
}
