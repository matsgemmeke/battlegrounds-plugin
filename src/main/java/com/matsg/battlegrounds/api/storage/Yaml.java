package com.matsg.battlegrounds.api.storage;

import org.bukkit.configuration.ConfigurationSection;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

public interface Yaml {

    boolean contains(String path);

    void createFile(String filepath, String resource) throws IOException;

    void createSection(String path);

    boolean getBoolean(String path);

    ConfigurationSection getConfigurationSection(String path);

    double getDouble(String path);

    String getFilePath();

    int getInt(String path);

    Set<String> getKeys(boolean deep);

    List<?> getList(String path);

    Object getObject(String path);

    InputStream getResource();

    String getResourceName();

    String getString(String path);

    List<String> getStringList(String path);

    boolean isReadOnly();

    void load();

    boolean removeFile();

    void removeKey(String path);

    void save();

    void set(String path, Object value);
}
