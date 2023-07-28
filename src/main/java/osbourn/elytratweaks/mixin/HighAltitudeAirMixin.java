package osbourn.elytratweaks.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import osbourn.elytratweaks.ElytraTweaksMod;

@Mixin(LivingEntity.class)
public abstract class HighAltitudeAirMixin extends Entity {
    /*
     * If false, then the thinner atmosphere won't affect entities unless they are elytra-flying.
     */
    //private static final boolean ONLY_AFFECT_IF_FALL_FLYING = false;
    /*
     * At or below the normal air level, players recover air at a normal speed.
     */
    //private static final float NORMAL_AIR_LEVEL = 150.0F;
    /*
     * The neutral air level is the y-level where players neither lose nor gain air.
     */
    //private static final float NEUTRAL_AIR_LEVEL = 200.0F;
    /*
     * At or above the thinnest air level, players lose air as if they were underwater.
     */
    //private static final float THINNEST_AIR_LEVEL = 300.0F;

    private HighAltitudeAirMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    private static boolean shouldAffect(LivingEntity entity) {
        if (!ElytraTweaksMod.getConfig().lowAirAtHighAltitudesEnabled()) {
            return false;
        }
        return entity.isPlayer() && (entity.isFallFlying()
                || !ElytraTweaksMod.getConfig().lowAirOnlyAffectsWhenElytraFlying());
    }

    /**
     * If the player is above the neutral air level, then it should act as if they were submerged in water
     * when calculating how much their air should change. The actual amount that their air will change is
     * adjusted by the other methods in this mixin, but Minecraft will only damage the player for air if it
     * believes that they are in water.
     */
    @Redirect(method = "baseTick",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isSubmergedIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
    public boolean makeHighAltitudeCountAsWater(LivingEntity entity, TagKey<Fluid> fluidTag) {
        if (entity.getY() > ElytraTweaksMod.getConfig().neutralAirLevel() && shouldAffect(entity)) {
            return true;
        } else {
            return entity.isSubmergedIn(fluidTag);
        }
    }

    @Inject(method = "getNextAirUnderwater",
        at = @At("HEAD"), cancellable = true)
    public void airLossInHighAltitude(int air, CallbackInfoReturnable<Integer> cir) {
        // If the player is submerged in water, then vanilla mechanics should be used
        if (!this.isSubmergedIn(FluidTags.WATER)) {
            double neutralAirLevel = ElytraTweaksMod.getConfig().neutralAirLevel();
            double thinnestAirLevel = ElytraTweaksMod.getConfig().thinnestAirLevel();
            if (this.getY() > neutralAirLevel && shouldAffect((LivingEntity) (Object) this)) {

                // This would be 0.5 if the player is exactly halfway between the neutral and thinnest air levels,
                // and the higher the number is the higher up the player is
                double percentageToNeutral = (this.getY() - neutralAirLevel) / (thinnestAirLevel - neutralAirLevel);

                // A player that is 70% of the way above the neutral air level should lose air 70% of the time
                if (percentageToNeutral > Math.random()) {
                    cir.setReturnValue(air - 1);
                } else {
                    cir.setReturnValue(air);
                }
            }
        }
    }

    @Inject(method = "getNextAirOnLand",
            at = @At("HEAD"), cancellable = true)
    public void airRecoveryInHighAltitude(int air, CallbackInfoReturnable<Integer> cir) {
        // If the player is submerged in water, then vanilla mechanics should be used
        if (!this.isSubmergedIn(FluidTags.WATER)) {
            double normalAirLevel = ElytraTweaksMod.getConfig().normalAirLevel();
            double neutralAirLevel = ElytraTweaksMod.getConfig().neutralAirLevel();

            if (this.getY() > normalAirLevel && shouldAffect((LivingEntity) (Object) this)) {
                double percentageToNeutral = (this.getY() - normalAirLevel) / (neutralAirLevel - normalAirLevel);
                double recoveryAmount = 4.0 * (1.0 - percentageToNeutral);

                // Since recoveryAmount is a double, we would like to convert it to an int. If it is 2.7,
                // it should have a 70% chance of being rounded to 3 and a 30% chance of being rounded to 2.
                int amountToIncreaseAirBy = (int) recoveryAmount +
                        (recoveryAmount - (int) recoveryAmount) > Math.random() ? 1 : 0;
                cir.setReturnValue(Math.min(air + amountToIncreaseAirBy, this.getMaxAir()));
            }
        }
    }
}
