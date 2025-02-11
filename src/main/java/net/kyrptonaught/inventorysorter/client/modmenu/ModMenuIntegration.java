package net.kyrptonaught.inventorysorter.client.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.inventorysorter.InventorySorterMod;
import net.kyrptonaught.inventorysorter.SortCases;
import net.kyrptonaught.inventorysorter.client.InventorySorterModClient;
import net.kyrptonaught.inventorysorter.client.config.ConfigOptions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.TranslatableText;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {


    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (screen) -> {
            ConfigOptions options = InventorySorterModClient.getConfig();
            ConfigBuilder builder = ConfigBuilder.create().setParentScreen(screen).setTitle(new TranslatableText("Inventory Sorting Config"));
            builder.setSavingRunnable(() -> {
                InventorySorterMod.configManager.save();
                InventorySorterModClient.keycode = null;
                if (MinecraftClient.getInstance().player != null)
                    InventorySorterModClient.syncConfig();
            });
            ConfigEntryBuilder entryBuilder = ConfigEntryBuilder.create();

            ConfigCategory displayCat = builder.getOrCreateCategory(new TranslatableText("key.inventorysorter.config.category.display"));
            displayCat.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("key.inventorysorter.config.displaysort"), options.displaySort).setSaveConsumer(val -> options.displaySort = val).setDefaultValue(true).build());
            displayCat.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("key.inventorysorter.config.seperatebtn"), options.seperateBtn).setSaveConsumer(val -> options.seperateBtn = val).setDefaultValue(true).build());
            displayCat.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("key.inventorysorter.config.displaytooltip"), options.displayTooltip).setSaveConsumer(val -> options.displayTooltip = val).setDefaultValue(true).build());

            ConfigCategory logicCat = builder.getOrCreateCategory(new TranslatableText("key.inventorysorter.config.category.logic"));
            logicCat.addEntry(entryBuilder.startEnumSelector(new TranslatableText("key.inventorysorter.config.sorttype"), SortCases.SortType.class, options.sortType).setSaveConsumer(val -> options.sortType = val).build());
            logicCat.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("key.inventorysorter.config.sortplayer"), options.sortPlayer).setSaveConsumer(val -> options.sortPlayer = val).setDefaultValue(false).build());


            ConfigCategory activationCat = builder.getOrCreateCategory(new TranslatableText("key.inventorysorter.config.category.activation"));
            activationCat.addEntry(entryBuilder.startKeyCodeField(new TranslatableText("key.inventorysorter.sort"), InputUtil.fromTranslationKey(options.keybinding)).setSaveConsumer(key -> options.keybinding = key.toString()).build());
            activationCat.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("key.inventorysorter.config.middleclick"), options.middleClick).setSaveConsumer(val -> options.middleClick = val).setDefaultValue(true).build());
            activationCat.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("key.inventorysorter.config.doubleclick"), options.doubleClickSort).setSaveConsumer(val -> options.doubleClickSort = val).setDefaultValue(true).build());
            activationCat.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("key.inventorysorter.config.sortmousehighlighted"), options.sortMouseHighlighted).setSaveConsumer(val -> options.sortMouseHighlighted = val).setDefaultValue(true).build());

            return builder.build();
        };
    }
}
