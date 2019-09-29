package com.matsg.battlegrounds.api.storage;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.List;
import java.util.Set;

public abstract class AbstractYaml implements Yaml {

    protected boolean readOnly;
    protected File file;
    protected FileConfiguration config;
    protected InputStream resource;

    public AbstractYaml(String fileName, String filepath, InputStream resource, boolean readOnly) throws IOException {
        this.resource = resource;
        this.readOnly = readOnly;
        this.file = getNewFile(filepath, fileName);

        createFile(filepath, fileName);

        this.config = YamlConfiguration.loadConfiguration(file);
    }

    public File getFile() {
        return file;
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

    public Set<String> getKeys(boolean deep) {
        return config.getKeys(deep);
    }

    public List<?> getList(String path) {
        return config.getList(path);
    }

    public Object getObject(String path) {
        return config.get(path);
    }

    public InputStream getResource() {
        return resource;
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
        OutputStream out = new FileOutputStream(file);

        int length;
        byte[] buffer = new byte[1024];

        while ((length = resource.read(buffer)) > 0) {
            out.write(buffer, 0, length);
        }

        out.close();
        resource.close();
    }

    // Creates a new yaml from the resources in the given directory
    public void createFile(String filePath, String fileName) throws IOException {
        File file = getNewFile(filePath, fileName);

        prepareFile(file, resource);
    }

    private File getNewFile(String filepath, String filename) {
        if (filename.length() == 0) {
            throw new IllegalArgumentException("Yaml file name must but be atleast one character");
        }

        if (!filename.endsWith(".yml")) {
            // Add the .yml file extension if it is not present
            filename += ".yml";
        }

        return new File(filepath, filename);
    }

    /**
     * Creates the yaml file in the directory if it does not exist yet
     */
    private void prepareFile(File file, InputStream resource) throws IOException {
        if (file.exists()) {
            return;
        }

        file.getParentFile().mkdirs();
        file.createNewFile();

        if (resource != null) {
            copyResource(resource, file);
        }
    }

    public boolean removeFile() {
        return file.delete();
    }
}
