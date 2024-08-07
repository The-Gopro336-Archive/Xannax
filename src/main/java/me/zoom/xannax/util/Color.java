package me.zoom.xannax.util;

public class Color {

    public float red;
    public float blue;
    public float green;
    public float alpha;
    public boolean rainbow;
    public float rainbowSpeed;
    public float rainbowSaturation;
    public float rainbowBrightness;
    public float hue = 0f;

    public Color(float red, float blue, float green, float alpha, boolean rainbow, float rainbowSpeed, float rainbowSaturation, float rainbowBrightness) {
        this.red = red;
        this.blue = blue;
        this.green = green;
        this.alpha = alpha;
        this.rainbow = rainbow;
        this.rainbowBrightness = rainbowBrightness;
        this.rainbowSpeed = rainbowSpeed;
        this.rainbowSaturation = rainbowSaturation;
    }

    public Color(int colorInt) {
        int[] colArr = ColorUtil.toRGBAArray(colorInt);
        red = colArr[0];
        green = colArr[1];
        blue = colArr[2];
        alpha = colArr[3];
    }

    public int toInt(){
        return ColorUtil.toRGBA(red, green, blue, alpha);
    }

    @Override
    public String toString() {
        return
                "red:" + red +
                ",blue:" + blue +
                ",green:" + green +
                ",alpha:" + alpha +
                ",rainbow:" + rainbow +
                ",rainbowSpeed:" + rainbowSpeed +
                ",rainbowSaturation:" + rainbowSaturation +
                ",rainbowBrightness:" + rainbowBrightness;
    }

    public void nextRainbowColor() {
        this.hue += rainbowSpeed / 20000.0f;
        int rgb = java.awt.Color.HSBtoRGB(hue, rainbowSaturation / 255.0F, rainbowBrightness / 255.0F);
        red = rgb >>> 16 & 0xFF;
        green = rgb >>> 8 & 0xFF;
        blue = rgb & 0xFF;
    }
}