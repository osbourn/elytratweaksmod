package osbourn.elytratweaks.integration;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;
import osbourn.elytratweaks.ElytraTweaksConfig;
import osbourn.elytratweaks.ElytraTweaksMod;

public class ElytraTweaksModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.literal("Elytra Tweaks Config"));
            addConfigOptions(builder);
            return builder.build();
        };
    }

    private void addConfigOptions(ConfigBuilder builder) {
        ElytraTweaksConfig config = ElytraTweaksMod.getConfig();

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory general = builder.getOrCreateCategory(Text.literal("General"));
        general.addEntry(entryBuilder.startBooleanToggle(Text.literal("Should stop flying on damage"), config.shouldStopFlyingOnDamage)
                .setSaveConsumer(newVal -> config.shouldStopFlyingOnDamage = newVal)
                .build());
        general.addEntry(entryBuilder.startFloatField(Text.literal("Fly into wall damage multiplier"), config.flyIntoWallDamageMultiplier)
                .setSaveConsumer(newVal -> config.flyIntoWallDamageMultiplier = newVal)
                .build());

        ConfigCategory lowAir = builder.getOrCreateCategory(Text.literal("Low Air"));
        lowAir.addEntry(entryBuilder.startBooleanToggle(Text.literal("Low air at high altitudes"), config.lowAirAtHighAltitudesEnabled)
                .setSaveConsumer(newVal -> config.lowAirAtHighAltitudesEnabled = newVal)
                .build());
        lowAir.addEntry(entryBuilder.startBooleanToggle(Text.literal("Low air only affects when elytra flying"), config.lowAirOnlyAffectsWhenElytraFlying)
                .setSaveConsumer(newVal -> config.lowAirOnlyAffectsWhenElytraFlying = newVal)
                .build());
        lowAir.addEntry(entryBuilder.startFloatField(Text.literal("Normal air level"), config.normalAirLevel)
                .setSaveConsumer(newVal -> config.normalAirLevel = newVal)
                .build());
        lowAir.addEntry(entryBuilder.startFloatField(Text.literal("Neutral air level"), config.neutralAirLevel)
                .setSaveConsumer(newVal -> config.neutralAirLevel = newVal)
                .build());
        lowAir.addEntry(entryBuilder.startFloatField(Text.literal("Thinnest air level"), config.thinnestAirLevel)
                .setSaveConsumer(newVal -> config.thinnestAirLevel = newVal)
                .build());

        ConfigCategory frictionLanding = builder.getOrCreateCategory(Text.literal("Friction Landing"));
        frictionLanding.addEntry(entryBuilder.startBooleanToggle(Text.literal("Friction landings"), config.performFrictionLanding)
                .setSaveConsumer(newVal -> config.performFrictionLanding = newVal)
                .build());
        frictionLanding.addEntry(entryBuilder.startDoubleField(Text.literal("Slow down rate"), config.slowDownRate)
                .setSaveConsumer(newVal -> config.slowDownRate = newVal)
                .build());
        frictionLanding.addEntry(entryBuilder.startBooleanToggle(Text.literal("Do friction damage"), config.doFrictionDamage)
                .setSaveConsumer(newVal -> config.doFrictionDamage = newVal)
                .build());
        frictionLanding.addEntry(entryBuilder.startFloatField(Text.literal("Friction damage scale"), config.frictionDamageScale)
                .setSaveConsumer(newVal -> config.frictionDamageScale = newVal)
                .build());
        frictionLanding.addEntry(entryBuilder.startFloatField(Text.literal("Lowest friction damage per tick"), config.lowestFrictionDamagePerTick)
                .setSaveConsumer(newVal -> config.lowestFrictionDamagePerTick = newVal)
                .build());
        frictionLanding.addEntry(entryBuilder.startBooleanToggle(Text.literal("Make damage sound every tick"), config.makeDamageSoundEveryTick)
                .setSaveConsumer(newVal -> config.makeDamageSoundEveryTick = newVal)
                .build());

        builder.setSavingRunnable(config::save);
    }
}
