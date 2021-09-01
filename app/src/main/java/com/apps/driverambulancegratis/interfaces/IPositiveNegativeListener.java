package com.apps.driverambulancegratis.interfaces;

public interface IPositiveNegativeListener {
    void onPositive();

    default void onNegative() {
    }
}
