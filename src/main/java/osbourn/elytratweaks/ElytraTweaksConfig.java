package osbourn.elytratweaks;

import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;

public class ElytraTweaksConfig {
    private static final File CONFIG_FILE =
            new File(FabricLoader.getInstance().getConfigDir().toFile(), "elytratweaks.json");

    private boolean shouldStopFlyingOnDamage = true;
    public float flyIntoWallDamageMultiplier = 3.0F;
    private boolean lowAirAtHighAltitudesEnabled = true;
    private boolean lowAirOnlyAffectsWhenElytraFlying = true;
    private float normalAirLevel = 150.0F;
    private float neutralAirLevel = 200.0F;
    private float thinnestAirLevel = 300.0F;
    private boolean performFrictionLanding = true;
    private double slowDownRate = 0.03;
    private boolean doFrictionDamage = true;
    private float frictionDamageScale = 20.0F;
    private float lowestFrictionDamagePerTick = 0.1F;

    public boolean shouldStopFlyingOnDamage() {
        return shouldStopFlyingOnDamage;
    }

    public float flyIntoWallDamageMultiplier() {
        return flyIntoWallDamageMultiplier;
    }

    public boolean lowAirAtHighAltitudesEnabled() {
        return lowAirAtHighAltitudesEnabled;
    }

    public boolean lowAirOnlyAffectsWhenElytraFlying() {
        return lowAirOnlyAffectsWhenElytraFlying;
    }

    public float normalAirLevel() {
        return normalAirLevel;
    }

    public float neutralAirLevel() {
        return neutralAirLevel;
    }

    public float thinnestAirLevel() {
        return thinnestAirLevel;
    }

    public boolean performFrictionLanding() {
        return performFrictionLanding;
    }

    public double slowDownRate() {
        return slowDownRate;
    }

    public boolean doFrictionDamage() {
        return doFrictionDamage;
    }

    public float frictionDamageScale() {
        return frictionDamageScale;
    }

    public float lowestFrictionDamagePerTick() {
        return lowestFrictionDamagePerTick;
    }

    public void save() {
        try (Writer writer = Files.newBufferedWriter(CONFIG_FILE.toPath())) {
            new GsonBuilder().setPrettyPrinting().create().toJson(this, writer);
        } catch (IOException e) {
            ElytraTweaksMod.LOGGER.error("IO error while trying to save config file", e);
        }
    }

    public static ElytraTweaksConfig load() {
        if (!CONFIG_FILE.exists()) {
            ElytraTweaksConfig newConfig = new ElytraTweaksConfig();
            newConfig.save();
            return newConfig;
        }

        try (Reader reader = Files.newBufferedReader(CONFIG_FILE.toPath())) {
            return new GsonBuilder().setPrettyPrinting().create().fromJson(reader, ElytraTweaksConfig.class);
        } catch (IOException e) {
            ElytraTweaksMod.LOGGER.error("Unable to read config file, using default config", e);
            return new ElytraTweaksConfig();
        }
    }
}
