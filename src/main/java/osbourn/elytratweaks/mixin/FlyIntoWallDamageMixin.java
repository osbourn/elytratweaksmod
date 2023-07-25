package osbourn.elytratweaks.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public class FlyIntoWallDamageMixin {
    @Redirect(method = "travel",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    public boolean modifyFlyIntoWallDamage(LivingEntity instance, DamageSource source, float amount) {
        if (!source.equals(instance.getDamageSources().flyIntoWall())) return instance.damage(source, amount);

        return instance.damage(source, amount * 3);
    }
}
