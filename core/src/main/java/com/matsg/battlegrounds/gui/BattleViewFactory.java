package com.matsg.battlegrounds.gui;

import com.github.stefvanschie.inventoryframework.Gui;
import com.matsg.battlegrounds.FactoryCreationException;
import com.matsg.battlegrounds.InternalsProvider;
import com.matsg.battlegrounds.ItemFinder;
import com.matsg.battlegrounds.TaskRunner;
import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.gui.view.*;
import com.matsg.battlegrounds.mode.GameModeFactory;
import com.matsg.battlegrounds.mode.zombies.gui.SectionSettingsView;
import com.matsg.battlegrounds.mode.zombies.gui.ZombiesSettingsView;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

public class BattleViewFactory implements ViewFactory {

    private Battlegrounds plugin;
    private GameModeFactory gameModeFactory;
    private InternalsProvider internals;
    private ItemFinder itemFinder;
    private TaskRunner taskRunner;

    public BattleViewFactory(Battlegrounds plugin, InternalsProvider internals, ItemFinder itemFinder, TaskRunner taskRunner) {
        this.plugin = plugin;
        this.internals = internals;
        this.itemFinder = itemFinder;
        this.taskRunner = taskRunner;

        this.gameModeFactory = new GameModeFactory(plugin, internals, taskRunner, plugin.getTranslator(), this);
    }

    @SuppressWarnings("unchecked")
    public <T extends View> View make(@NotNull Class<T> viewClass, @NotNull Consumer<T> configure) {
        if (viewClass == ArenaSettingsView.class) {
            T view = (T) new ArenaSettingsView()
                    .setTranslator(plugin.getTranslator());

            configure.accept(view);

            InputStream in = getInputStreamOfFile("arena_settings.xml");

            Gui.load(plugin, view, in);

            return view;
        }
        if (viewClass == EditGameConfigurationView.class) {
            T view = (T) new EditGameConfigurationView()
                    .setViewFactory(this);

            configure.accept(view);

            InputStream in = getInputStreamOfFile("edit_game_configuration.xml");

            Gui.load(plugin, view, in);

            return view;
        }
        if (viewClass == EditLoadoutView.class) {
            T view = (T) new EditLoadoutView()
                    .setItemFinder(itemFinder)
                    .setPlayerStorage(plugin.getPlayerStorage())
                    .setTranslator(plugin.getTranslator())
                    .setViewFactory(this);

            configure.accept(view);

            InputStream in = getInputStreamOfFile("edit_loadout.xml");

            Gui.load(plugin, view, in);

            return view;
        }
        if (viewClass == GameSettingsView.class) {
            T view = (T) new GameSettingsView()
                    .setViewFactory(this);

            configure.accept(view);

            InputStream in = getInputStreamOfFile("game_settings.xml");

            Gui.load(plugin, view, in);

            return view;
        }
        if (viewClass == ItemTransactionView.class) {
            T view = (T) new ItemTransactionView()
                    .setTranslator(plugin.getTranslator());

            configure.accept(view);

            InputStream in = getInputStreamOfFile("item_transaction.xml");

            Gui.load(plugin, view, in);

            return view;
        }
        if (viewClass == LoadoutManagerView.class) {
            T view = (T) new LoadoutManagerView()
                    .setAttachmentFactory(plugin.getAttachmentFactory())
                    .setEquipmentFactory(plugin.getEquipmentFactory())
                    .setFirearmFactory(plugin.getFirearmFactory())
                    .setMeleeWeaponFactory(plugin.getMeleeWeaponFactory())
                    .setPlayerStorage(plugin.getPlayerStorage())
                    .setTranslator(plugin.getTranslator())
                    .setViewFactory(this);

            configure.accept(view);

            InputStream in = getInputStreamOfFile("loadout_manager.xml");

            Gui.load(plugin, view, in);

            return view;
        }
        if (viewClass == PluginSettingsView.class) {
            T view = (T) new PluginSettingsView()
                    .setGameManager(plugin.getGameManager())
                    .setTranslator(plugin.getTranslator())
                    .setViewFactory(this);

            configure.accept(view);

            InputStream in = getInputStreamOfFile("plugin_settings.xml");

            Gui.load(plugin, view, in);

            return view;
        }
        if (viewClass == SectionSettingsView.class) {
            T view = (T) new SectionSettingsView()
                    .setTranslator(plugin.getTranslator());

            configure.accept(view);

            InputStream in = getInputStreamOfFile("section_settings.xml");

            Gui.load(plugin, view, in);

            return view;
        }
        if (viewClass == SelectAttachmentView.class) {
            T view = (T) new SelectAttachmentView()
                    .setAttachmentFactory(plugin.getAttachmentFactory())
                    .setLevelConfig(plugin.getLevelConfig())
                    .setPlayerStorage(plugin.getPlayerStorage())
                    .setTranslator(plugin.getTranslator());

            configure.accept(view);

            InputStream in = getInputStreamOfFile("select_attachment.xml");

            Gui.load(plugin, view, in);

            return view;
        }
        if (viewClass == SelectGameModesView.class) {
            T view = (T) new SelectGameModesView()
                    .setGameModeFactory(gameModeFactory)
                    .setTranslator(plugin.getTranslator());

            configure.accept(view);

            InputStream in = getInputStreamOfFile("select_gamemodes.xml");

            Gui.load(plugin, view, in);

            return view;
        }
        if (viewClass == SelectLoadoutView.class) {
            T view = (T) new SelectLoadoutView()
                    .setAttachmentFactory(plugin.getAttachmentFactory())
                    .setEquipmentFactory(plugin.getEquipmentFactory())
                    .setFirearmFactory(plugin.getFirearmFactory())
                    .setInternals(internals)
                    .setLevelConfig(plugin.getLevelConfig())
                    .setMeleeWeaponFactory(plugin.getMeleeWeaponFactory())
                    .setPlayerStorage(plugin.getPlayerStorage())
                    .setTaskRunner(taskRunner)
                    .setTranslator(plugin.getTranslator());

            configure.accept(view);

            InputStream in = getInputStreamOfFile("select_loadout.xml");

            Gui.load(plugin, view, in);

            return view;
        }
        if (viewClass == SelectWeaponTypeView.class) {
            T view = (T) new SelectWeaponTypeView()
                    .setTranslator(plugin.getTranslator())
                    .setViewFactory(this);

            configure.accept(view);

            InputStream in = getInputStreamOfFile("select_weapon_type.xml");

            Gui.load(plugin, view, in);

            return view;
        }
        if (viewClass == SelectWeaponView.class) {
            T view = (T) new SelectWeaponView()
                    .setLevelConfig(plugin.getLevelConfig())
                    .setPlayerStorage(plugin.getPlayerStorage())
                    .setTranslator(plugin.getTranslator());

            configure.accept(view);

            InputStream in = getInputStreamOfFile("select_weapon.xml");

            Gui.load(plugin, view, in);

            return view;
        }
        if (viewClass == ZombiesSettingsView.class) {
            T view = (T) new ZombiesSettingsView()
                    .setTranslator(plugin.getTranslator())
                    .setViewFactory(this);

            configure.accept(view);

            InputStream in = getInputStreamOfFile("zombies_settings.xml");

            Gui.load(plugin, view, in);

            return view;
        }

        throw new FactoryCreationException("Cannot create a view instance for input class " + viewClass.getSimpleName());
    }

    private InputStream getInputStreamOfFile(String fileName) {
        File layoutFile = new File(plugin.getDataFolder().getPath() + "/layout/" + fileName);
        InputStream input;

        try {
            input = new FileInputStream(layoutFile);
        } catch (IOException e) {
            throw new FactoryCreationException("Cannot load layout from file " + layoutFile.getName());
        }

        return input;
    }
}
