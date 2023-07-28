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

    public boolean shouldStopFlyingOnDamage = true;
    public float flyIntoWallDamageMultiplier = 3.0F;
    public boolean lowAirAtHighAltitudesEnabled = true;
    public boolean lowAirOnlyAffectsWhenElytraFlying = true;
    public float normalAirLevel = 150.0F;
    public float neutralAirLevel = 200.0F;
    public float thinnestAirLevel = 300.0F;
    public boolean performFrictionLanding = true;
    public double slowDownRate = 0.03;
    public boolean doFrictionDamage = true;
    public float frictionDamageScale = 20.0F;
    public float lowestFrictionDamagePerTick = 0.1F;

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
