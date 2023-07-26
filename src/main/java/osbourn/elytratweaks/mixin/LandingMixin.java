package osbourn.elytratweaks.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import osbourn.elytratweaks.ElytraTweaksMod;

@Mixin(LivingEntity.class)
abstract class LandingMixin extends Entity {
    @Shadow protected float lastDamageTaken;

    private LandingMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Redirect(method = "travel",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V"))
    public void causeFrictionWhenGlidingOnGround(LivingEntity entity, MovementType movementType, Vec3d vec3d) {
        if (entity.isOnGround() && entity.isFallFlying()) {
            Vec3d oldVelocity = entity.getVelocity();
            entity.setVelocity(getNewVelocity(oldVelocity));
            entity.velocityDirty = true;
            double changeInHorizontalVelocity = oldVelocity.horizontalLength() - entity.getVelocity().horizontalLength();

            // deceleration in meters per second
            float deceleration = (float) changeInHorizontalVelocity;
            // estimate of how far was traveled this tick
            float distanceTraveled = (float) oldVelocity.length();
            float damageConstant = 5.0F;
            float damageAmount = deceleration * distanceTraveled * damageConstant;

            if (damageAmount > 0.1F) {
                this.lastDamageTaken = 0.0F;
                entity.damage(ElytraTweaksMod.getFlyOnGroundDamageSource(entity.getWorld()), damageAmount);
            }
        }
        entity.move(movementType, vec3d);
    }

    @Redirect(method = "travel",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setFlag(IZ)V"))
    public void preventDisengagingElytraInTravelMethod(LivingEntity entity, int index, boolean value) {
        boolean isDisablingElytra = index == 7 && !value;
        boolean movingSlowEnoughToDisableOnGround = entity.getVelocity().length() <= 0.1;
        if (!isDisablingElytra || movingSlowEnoughToDisableOnGround) {
            // "this" should be the same as "entity" at this point in the code
            this.setFlag(index, value);
        }
    }

    @Redirect(method = "tickFallFlying",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setFlag(IZ)V"))
    public void preventDisengagingElytraInTickFallFlyingMethod(LivingEntity entity, int index, boolean value) {
        boolean isDisablingElytra = index == 7 && !value;
        boolean movingSlowEnoughToDisableOnGround = entity.getVelocity().length() <= 0.1;
        if (!isDisablingElytra) {
            // "this" should be the same as "entity" at this point in the code
            this.setFlag(index, value);
        }
        if (isDisablingElytra && (entity.isSubmergedIn(FluidTags.WATER) || movingSlowEnoughToDisableOnGround)) {
            this.setFlag(7, false);
        }
    }

    private static Vec3d getNewVelocity(Vec3d oldVec) {
        Vec3d horizontalDirectionVec = new Vec3d(oldVec.x, 0, oldVec.z).normalize();
        Vec3d adjustmentVec = horizontalDirectionVec.multiply(0.03);
        if (adjustmentVec.horizontalLength() > oldVec.horizontalLength()) {
            return Vec3d.ZERO;
        } else {
            return oldVec.subtract(adjustmentVec);
        }
    }
}
