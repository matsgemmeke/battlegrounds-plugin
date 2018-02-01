package com.matsg.battlegrounds.api.config;

import org.bukkit.configuration.ConfigurationSection;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface Yaml {

    boolean contains(String path);

    void createFile(String filepath, String resource) throws IOException;

    void createSection(String path);

    boolean getBoolean(String path);

    ConfigurationSection getConfigurationSection(String path);

    double getDouble(String path);

    String getFilePath();

    int getInt(String path);

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