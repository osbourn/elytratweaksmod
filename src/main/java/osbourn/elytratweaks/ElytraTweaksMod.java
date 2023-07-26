package osbourn.elytratweaks;

import net.fabricmc.api.ModInitializer;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElytraTweaksMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("elytratweaks");
	public static final RegistryKey<DamageType> FLY_ON_GROUND_DAMAGE =
			RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier("elytratweaks", "fly_on_ground"));

	/**
	 * Damage sources are now per-world (because they are data-driven)
	 */
	public static DamageSource getFlyOnGroundDamageSource(World world) {
		return world.getDamageSources().create(FLY_ON_GROUND_DAMAGE);
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");
	}
}