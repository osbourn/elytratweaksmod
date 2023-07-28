package osbourn.elytratweaks;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class ElytraTweaksMod implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("elytratweaks");
	public static final RegistryKey<DamageType> FLY_ON_GROUND_DAMAGE =
			RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier("elytratweaks", "fly_on_ground"));

	private static ElytraTweaksConfig config = null;

	/**
	 * Damage sources are now per-world (because they are data-driven)
	 */
	public static DamageSource getFlyOnGroundDamageSource(World world) {
		return world.getDamageSources().create(FLY_ON_GROUND_DAMAGE);
	}

	public static ElytraTweaksConfig getConfig() {
		return Objects.requireNonNull(config);
	}

	@Override
	public void onInitialize() {
		config = ElytraTweaksConfig.load();
	}
}