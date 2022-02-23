package com.mineria.mod.common.effects;

// TODO: Remove and replace with EffectRenderer call
public interface IMineriaEffectInstance
{
    // Temporary method due to changes made by forge on effect instances
    default boolean shouldRender()
    {
        return true;
    }
}
