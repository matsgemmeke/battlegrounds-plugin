package com.matsg.battlegrounds.gui;

public interface ButtonFunction<T, TResult> {

    TResult invoke(T input);
}
