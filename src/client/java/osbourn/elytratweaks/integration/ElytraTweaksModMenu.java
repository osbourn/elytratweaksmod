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
            ElytraTweaksConfig config = ElytraTweaksMod.getConfig();

            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Text.literal("Elytra Tweaks Config"));
            ConfigCategory general = builder.getOrCreateCategory(Text.literal("General"));
            ConfigEntryBuilder entryBuilder = builder.entryBuilder();
            general.addEntry(entryBuilder.startFloatField(Text.literal("Fly into wall damage multiplier"), config.flyIntoWallDamageMultiplier)
                    .setSaveConsumer(newVal -> config.flyIntoWallDamageMultiplier = newVal)
                    .build());
            builder.setSavingRunnable(config::save);
            return builder.build();
        };
    }
}
