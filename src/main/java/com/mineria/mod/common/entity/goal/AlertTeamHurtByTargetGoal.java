package com.mineria.mod.common.entity.goal;

import com.google.common.collect.Lists;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.ArrayList;
import java.util.List;

public class AlertTeamHurtByTargetGoal extends HurtByTargetGoal
{
    protected Class<? extends MobEntity>[] entitiesToAlert;

    public AlertTeamHurtByTargetGoal(CreatureEntity entity, Class<?>... toIgnoreDamage)
    {
        super(entity, toIgnoreDamage);
    }

    @SafeVarargs
    public final AlertTeamHurtByTargetGoal setAlertEntities(Class<? extends MobEntity>... entities)
    {
        this.setAlertOthers();
        this.entitiesToAlert = entities;
        return this;
    }

    @Override
    protected void alertOthers()
    {
        double followDistance = this.getFollowDistance();
        AxisAlignedBB aabb = AxisAlignedBB.unitCubeFromLowerCorner(this.mob.position()).inflate(followDistance, 10.0D, followDistance);
        List<MobEntity> entities = Lists.newArrayList(this.mob.level.getLoadedEntitiesOfClass(this.mob.getClass(), aabb));

        for(Class<? extends MobEntity> clazz : entitiesToAlert)
        {
            entities.addAll(this.mob.level.getLoadedEntitiesOfClass(clazz, aabb));
        }

        for(MobEntity entity : entities)
            if(this.mob != entity && entity.getTarget() == null && this.mob.getLastHurtByMob() != null && (!(this.mob instanceof TameableEntity) || ((TameableEntity)this.mob).getOwner() == ((TameableEntity)entity).getOwner()) && !entity.isAlliedTo(this.mob.getLastHurtByMob()))
                this.alertOther(entity, this.mob.getLastHurtByMob());
    }
}
