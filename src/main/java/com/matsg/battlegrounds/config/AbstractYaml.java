package com.matsg.battlegrounds.config;

import com.matsg.battlegrounds.api.Battlegrounds;
import com.matsg.battlegrounds.api.config.Yaml;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.List;

public abstract class AbstractYaml implements Yaml {

    protected Battlegrounds plugin;
    protected boolean readOnly;
    protected File file;
    protected FileConfiguration config;
    protected String resource;

    public AbstractYaml(Battlegrounds plugin, String resource, boolean readOnly) throws IOException {
        this.file = getNewFile(String.valueOf(plugin.getDataFolder()), resource);
        this.plugin = plugin;
        this.resource = resource;
        this.readOnly = readOnly;

        createFile(plugin.getDataFolder().getPath(), resource);

        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public AbstractYaml(Battlegrounds plugin, String filepath, String resource, boolean readOnly) throws IOException {
        this(filepath, resource, readOnly);
        this.plugin = plugin;
    }

    public AbstractYaml(String filepath, String resource, boolean readOnly) throws IOException {
        this.file = getNewFile(filepath, resource);
        this.resource = resource;
        this.readOnly = readOnly;

        createFile(filepath, resource);

        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public String getResourceName() {
        return resource;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public boolean contains(String path) {
        return config.contains(path);
    }

    public void createSection(String path) {
        config.createSection(path);
    }

    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    public ConfigurationSection getConfigurationSection(String path) {
        return config.getConfigurationSection(path);
    }

    public double getDouble(String path) {
        return config.getDouble(path);
    }

    public String getFilePath() {
        return file.getPath();
    }

    public int getInt(String path) {
        return config.getInt(path);
    }

    public List<?> getList(String path) {
        return config.getList(path);
    }

    public Object getObject(String path) {
        return config.get(path);
    }

    public InputStream getResource() {
        return plugin.getResource(resource);
    }

    public String getString(String path) {
        return config.getString(path);
    }

    public List<String> getStringList(String path) {
        return config.getStringList(path);
    }

    public void load() {
        config = YamlConfiguration.loadConfiguration(file);
    }

    public void removeKey(String path) {
        config.set(path, null);
    }

    public void save() {
        try {
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void set(String path, Object value) {
        config.set(path, value);
    }

    // Copies existing data in the resource as well as comments
    private void copyResource(InputStream resource, File file) throws IOException {
        if (resource == null) {
            return;
        }

        OutputStream out = new FileOutputStream(file);

        int length;
        byte[] buf = new byte[1024];

        while ((length = resource.read(buf)) > 0) {
            out.write(buf, 0, length);
        }

        out.close();
        resource.close();
    }

    // Creates a new yaml from the resources in the given directory
    public void createFile(String filepath, String resource) throws IOException {
        File file = getNewFile(filepath, resource);

        if (file == null) {
            return;
        }

        prepareFile(file, resource);
    }

    private File getNewFile(String filepath, String filename) {
        if (filename.length() == 0 || filename == null) {
            return null;
        }
        if (!filename.endsWith(".yml")) {
            filename.replace(filename.substring(filename.length() - 4, filename.length()), ".yml");
        }

        return new File(filepath, filename);
    }

    // Creates the yaml file in the directory if it does not exist yet
    private void prepareFile(File file, String resource) throws IOException {
        if (file.exists()) {
            return;
        }

        file.getParentFile().mkdirs();
        file.createNewFile();

        if (resource.length() > 0 && resource != null) {
            copyResource(plugin.getResource(resource), file);
        }
    }

    public boolean removeFile() {
        return file.delete();
    }
}