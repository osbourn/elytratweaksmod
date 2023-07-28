package osbourn.elytratweaks.integration;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;
import osbourn.elytratweaks.ElytraTweaksConfig;
import osbourn.elytratweaks.ElytraTweaksMod;

import java.util.Arrays;

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
                .setTooltip(tooltipOfString("""
                        If true, when an elytra-flying player takes damage of any type, their
                        elytra will disengage. They can activate it immediately afterwards. Note
                        that some forms of damage (fall damage and this mod's friction damage) do
                        not trigger this.
                        """))
                .build());
        general.addEntry(entryBuilder.startFloatField(Text.literal("Fly into wall damage multiplier"), config.flyIntoWallDamageMultiplier)
                .setSaveConsumer(newVal -> config.flyIntoWallDamageMultiplier = newVal)
                .setTooltip(tooltipOfString("""
                        Whenever a player takes damage because they flew into a wall
                        the damage is multiplied by this value.
                        """))
                .build());

        ConfigCategory lowAir = builder.getOrCreateCategory(Text.literal("Low Air"));
        lowAir.addEntry(entryBuilder.startBooleanToggle(Text.literal("Low air at high altitudes"), config.lowAirAtHighAltitudesEnabled)
                .setSaveConsumer(newVal -> config.lowAirAtHighAltitudesEnabled = newVal)
                .setTooltip(tooltipOfString("""
                        Whether the player should lose air if they fly too high with an elytra.
                        If set to false, none of the other settings in this section will have an effect.
                        """))
                .build());
        lowAir.addEntry(entryBuilder.startBooleanToggle(Text.literal("Low air only affects when elytra flying"), config.lowAirOnlyAffectsWhenElytraFlying)
                .setSaveConsumer(newVal -> config.lowAirOnlyAffectsWhenElytraFlying = newVal)
                .setTooltip(tooltipOfString("""
                        If set to true, then the only entities that are affected by the low air are entities which are
                        elytra-flying.
                        If set to false, then all entities will find it difficult to breathe if they travel high enough,
                        including mobs and players that aren't flying.
                        """))
                .build());
        lowAir.addEntry(entryBuilder.startFloatField(Text.literal("Normal air level"), config.normalAirLevel)
                .setSaveConsumer(newVal -> config.normalAirLevel = newVal)
                .setTooltip(tooltipOfString("""
                        This value should be a y-value lower than neutral air level.
                        Players at a y-level between the normal and neutral air levels will not lose air,
                        but will recover air at a reduced rate. Players closer to the neutral air level will recover
                        air slower.
                        Players below the normal air level will recover air as normal.
                        """))
                .build());
        lowAir.addEntry(entryBuilder.startFloatField(Text.literal("Neutral air level"), config.neutralAirLevel)
                .setSaveConsumer(newVal -> config.neutralAirLevel = newVal)
                .setTooltip(tooltipOfString("""
                        Should be a y-value that represents the point at which players start losing air. Should
                        be higher than the normal air level and lower than the thinnest air level.
                        Players at a y-level between the neutral and thinnest air levels will start losing air. The
                        closer to the thinnest level, the faster the player loses air.
                        """))
                .build());
        lowAir.addEntry(entryBuilder.startFloatField(Text.literal("Thinnest air level"), config.thinnestAirLevel)
                .setSaveConsumer(newVal -> config.thinnestAirLevel = newVal)
                .setTooltip(tooltipOfString("""
                        Should be a y-value greater than the neutral air level.
                        Players at or above the thinnest air level will lose air at the fastest rate, at the same rate
                        as if they were drowning.
                        """))
                .build());

        ConfigCategory frictionLanding = builder.getOrCreateCategory(Text.literal("Friction Landing"));
        frictionLanding.addEntry(entryBuilder.startBooleanToggle(Text.literal("Friction landings"), config.performFrictionLanding)
                .setSaveConsumer(newVal -> config.performFrictionLanding = newVal)
                .setTooltip(tooltipOfString("""
                        If true, then players will slow down gradually when landing instead of disengaging the elytra
                        the moment they touch the ground.
                        If false, then none of the other settings in this section will have an effect.
                        """))
                .build());
        frictionLanding.addEntry(entryBuilder.startDoubleField(Text.literal("Slow down rate"), config.slowDownRate)
                .setSaveConsumer(newVal -> config.slowDownRate = newVal)
                .setTooltip(tooltipOfString("""
                        Every tick while a player is flying on the ground, the magnitude of their velocity is decreased
                        by this much.
                        """))
                .build());
        frictionLanding.addEntry(entryBuilder.startBooleanToggle(Text.literal("Do friction damage"), config.doFrictionDamage)
                .setSaveConsumer(newVal -> config.doFrictionDamage = newVal)
                .setTooltip(tooltipOfString("""
                        If players should take damage while slowing down.
                        """))
                .build());
        frictionLanding.addEntry(entryBuilder.startFloatField(Text.literal("Friction damage scale"), config.frictionDamageScale)
                .setSaveConsumer(newVal -> config.frictionDamageScale = newVal)
                .setTooltip(tooltipOfString("""
                        This value is proportional to the amount of damage players take while slowing down.
                        Every tick, the damage is calculated as the product of the amount the player slowed down,
                        the distance the player traveled last tick, and this value.
                        If adjusting this value, you should probably adjust the Lowest friction damage per tick
                        value as well.
                        """))
                .build());
        frictionLanding.addEntry(entryBuilder.startFloatField(Text.literal("Lowest friction damage per tick"), config.lowestFrictionDamagePerTick)
                .setSaveConsumer(newVal -> config.lowestFrictionDamagePerTick = newVal)
                .setTooltip(tooltipOfString("""
                        If a player would take less than this much damage in a tick due to friction, it doesn't deal
                        the damage to them. This is so players which are moving at a slow enough speed don't take
                        damage.
                        Remember that this damage is per tick, so it should be set to a fraction of a heart.
                        """))
                .build());
        frictionLanding.addEntry(entryBuilder.startBooleanToggle(Text.literal("Make damage sound every tick"), config.makeDamageSoundEveryTick)
                .setSaveConsumer(newVal -> config.makeDamageSoundEveryTick = newVal)
                .setTooltip(tooltipOfString("""
                        Damage is dealt every tick due to friction, but this mod alters the "lastDamageTaken" value
                        rather than the invulnerability time to allow this. This means that the damage sound still only
                        sounds every half-second. It is manually played by this mod every tick if this setting is
                        enabled.
                        """))
                .build());

        builder.setSavingRunnable(config::save);
    }

    private Text[] tooltipOfString(String textBlock) {
        return Arrays.stream(textBlock.split("\\n"))
                .filter(s -> !s.equals(""))
                .map(Text::literal)
                .toArray(Text[]::new);
    }
}
