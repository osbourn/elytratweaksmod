package osbourn.elytratweaks.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import osbourn.elytratweaks.ElytraTweaksMod;

@Mixin(LivingEntity.class)
public abstract class StopFlyingOnDamageMixin extends Entity {
    private static final boolean ACTIVE = true;

    public StopFlyingOnDamageMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "damage", at = @At("TAIL"))
    public void disengageOnDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        LivingEntity self = (LivingEntity) (Object) this;
        if (cir.getReturnValueZ() && !sourceIgnored(source)) {
            if (!self.getWorld().isClient) {
                if (ACTIVE && self.isFallFlying()) {
                    // 7 means "fall flying"
                    this.setFlag(7, false);
                }
            }
        }
    }

    private static boolean sourceIgnored(DamageSource source) {
        return source.isOf(ElytraTweaksMod.FLY_ON_GROUND_DAMAGE) || source.isOf(DamageTypes.FALL);
    }
}
