package io.github.mineria_mc.mineria.common.entity.goal;

import com.google.common.collect.Lists;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.List;

public class AlertTeamHurtByTargetGoal extends HurtByTargetGoal {
    @Nullable
    protected Class<? extends Mob>[] entitiesToAlert;

    public AlertTeamHurtByTargetGoal(PathfinderMob entity, Class<?>... toIgnoreDamage) {
        super(entity, toIgnoreDamage);
    }

    @SafeVarargs
    public final AlertTeamHurtByTargetGoal setAlertEntities(Class<? extends Mob>... entities) {
        this.setAlertOthers();
        this.entitiesToAlert = entities;
        return this;
    }

    @Override
    protected void alertOthers() {
        double followDistance = this.getFollowDistance();
        AABB aabb = AABB.unitCubeFromLowerCorner(this.mob.position()).inflate(followDistance, 10.0D, followDistance);
        List<Mob> entities = Lists.newArrayList(this.mob.level().getEntitiesOfClass(this.mob.getClass(), aabb));

        if(entitiesToAlert != null) {
            for (Class<? extends Mob> clazz : entitiesToAlert) {
                entities.addAll(this.mob.level().getEntitiesOfClass(clazz, aabb));
            }
        }

        for (Mob entity : entities) {
            if (this.mob != entity && entity.getTarget() == null && this.mob.getLastHurtByMob() != null && (!(this.mob instanceof TamableAnimal) || ((TamableAnimal) this.mob).getOwner() == ((TamableAnimal) entity).getOwner()) && !entity.isAlliedTo(this.mob.getLastHurtByMob())) {
                this.alertOther(entity, this.mob.getLastHurtByMob());
            }
        }
    }
}
