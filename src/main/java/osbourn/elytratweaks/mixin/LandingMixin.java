package osbourn.elytratweaks.mixin;

import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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

    @Shadow protected abstract void playHurtSound(DamageSource source);

    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    private LandingMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    // TODO: This should be an @Inject rather than a @Redirect because we don't actually redirect anything
    @Redirect(method = "travel",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V"))
    public void causeFrictionWhenGlidingOnGround(LivingEntity entity, MovementType movementType, Vec3d vec3d) {
        boolean featureEnabled = ElytraTweaksMod.getConfig().performFrictionLanding;
        boolean frictionDamageEnabled = ElytraTweaksMod.getConfig().doFrictionDamage;
        if (featureEnabled && entity.isOnGround() && entity.isFallFlying()) {
            Vec3d oldVelocity = entity.getVelocity();
            entity.setVelocity(getNewVelocity(oldVelocity));
            entity.velocityDirty = true;

            if (frictionDamageEnabled) {
                double changeInHorizontalVelocity = oldVelocity.horizontalLength() - entity.getVelocity().horizontalLength();

                // deceleration in meters per second
                float deceleration = (float) changeInHorizontalVelocity;
                // estimate of how far was traveled this tick
                float distanceTraveled = (float) oldVelocity.length();
                float damageConstant = ElytraTweaksMod.getConfig().frictionDamageScale;
                float damageAmount = deceleration * distanceTraveled * damageConstant;
                boolean isInCreative = entity instanceof PlayerEntity p && p.isCreative();

                if (!isInCreative && damageAmount > ElytraTweaksMod.getConfig().lowestFrictionDamagePerTick) {
                    this.lastDamageTaken = 0.0F;
                    entity.damage(ElytraTweaksMod.getFlyOnGroundDamageSource(entity.getWorld()), damageAmount);
                    if (ElytraTweaksMod.getConfig().makeDamageSoundEveryTick) {
                        this.playHurtSound(ElytraTweaksMod.getFlyOnGroundDamageSource(entity.getWorld()));
                    }
                }
            }
        }
        entity.move(movementType, vec3d);
    }

    //TODO: Cancel tiltScreen in damage method if the source is FLY_ON_GROUND_DAMAGE_SOURCE

    @Redirect(method = "travel",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setFlag(IZ)V"))
    public void preventDisengagingElytraInTravelMethod(LivingEntity entity, int index, boolean value) {
        boolean featureActive = ElytraTweaksMod.getConfig().performFrictionLanding;
        boolean isDisablingElytra = index == 7 && !value;
        boolean movingSlowEnoughToDisableOnGround = entity.getVelocity().length() <= 0.1;
        if (!featureActive || !isDisablingElytra || movingSlowEnoughToDisableOnGround) {
            // "this" should be the same as "entity" at this point in the code
            this.setFlag(index, value);
        }
    }

    @Redirect(method = "tickFallFlying",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setFlag(IZ)V"))
    public void preventDisengagingElytraInTickFallFlyingMethod(LivingEntity entity, int index, boolean value) {
        boolean featureActive = ElytraTweaksMod.getConfig().performFrictionLanding;
        boolean isDisablingElytra = index == 7 && !value;
        boolean isWearingUsableElytra = this.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA)
                && ElytraItem.isUsable(this.getEquippedStack(EquipmentSlot.CHEST));
        boolean movingSlowEnoughToDisableOnGround = entity.getVelocity().length() <= 0.1;
        if (!featureActive || !isDisablingElytra) {
            // "this" should be the same as "entity" at this point in the code
            this.setFlag(index, value);
        }
        if (isDisablingElytra && (entity.isSubmergedIn(FluidTags.WATER) || !isWearingUsableElytra ||
                movingSlowEnoughToDisableOnGround)) {
            this.setFlag(7, false);
        }
    }

    private static Vec3d getNewVelocity(Vec3d oldVec) {
        Vec3d horizontalDirectionVec = new Vec3d(oldVec.x, 0, oldVec.z).normalize();
        Vec3d adjustmentVec = horizontalDirectionVec.multiply(ElytraTweaksMod.getConfig().slowDownRate);
        if (adjustmentVec.horizontalLength() > oldVec.horizontalLength()) {
            return Vec3d.ZERO;
        } else {
            return oldVec.subtract(adjustmentVec);
        }
    }
}
