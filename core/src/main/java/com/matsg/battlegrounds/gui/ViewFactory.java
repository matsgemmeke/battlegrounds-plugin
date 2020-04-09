package com.matsg.battlegrounds.gui;

import com.matsg.battlegrounds.gui.view.View;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public interface ViewFactory {

    <T extends View> View make(@NotNull Class<T> viewClass, @NotNull Consumer<T> configure);
}
