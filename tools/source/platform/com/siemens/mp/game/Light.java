package com.siemens.mp.game;

/** Test-host vendor boundary, never delivered: record calls without physical light control. */
public final class Light {
    public static int onCalls, offCalls;
    public static void setLightOn() { onCalls++; }
    public static void setLightOff() { offCalls++; }
}
