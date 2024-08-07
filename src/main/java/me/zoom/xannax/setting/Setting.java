package me.zoom.xannax.setting;


import me.zoom.xannax.module.Module;
import me.zoom.xannax.util.Color;
import me.zoom.xannax.util.EnumConverter;

import java.util.function.Predicate;

public class Setting<T> {

    public String name;
    public String[] alias;
    public String desc;
    public Module module;
    public Predicate<T> showIf;

    public T value;

    public void setValue(T value) {
        this.value = value;
    }

    public T min;
    public T max;

    public Setting(String name, String[] alias, String desc)
    {
        this.name = name;
        this.alias = alias;
        this.desc = desc;
    }

    public Setting(String name, String[] alias, String desc, T value)
    {
        this(name, alias, desc);
        this.value = value;
    }

    public Setting(String name, String[] alias, String desc, T value, T min, T max)
    {
        this(name, alias, desc, value);
        this.min = min;
        this.max = max;

    }

    public Setting(String name, String desc)
    {
        this.name = name;
        this.alias = new String[]{};
        this.desc = desc;
    }

    public Setting(String name, String desc, T value)
    {
        this(name, new String[]{}, desc);
        this.value = value;
    }

    public Setting(String name, String desc, T value, T min, T max)
    {
        this(name, new String[]{}, desc, value);
        this.min = min;
        this.max = max;
    }

    public Setting(String name)
    {
        this.name = name;
        this.alias = new String[]{};
        this.desc = "";
    }

    public Setting(String name, T value)
    {
        this(name, new String[]{}, "");
        this.value = value;
    }

    public Setting(String name, T value, T min, T max)
    {
        this(name, new String[]{}, "", value);
        this.min = min;
        this.max = max;
    }

    public Setting(String name, T value, T min, T max, Predicate<T> showIf)
    {
        this(name, new String[]{}, "", value);
        this.min = min;
        this.max = max;
        this.showIf = showIf;
    }

    public Setting(String name, T value, Predicate<T> showIf){
        this(name, new String[]{}, "", value);
        this.showIf = showIf;
    }

    public <T> T clamp(T value, T min, T max)
    {
        return ((Comparable) value).compareTo(min) < 0 ? min : (((Comparable) value).compareTo(max) > 0 ? max : value);
    }

    public T getValue()
    {
        return this.value;
    }

    public String getMeta(){
        return value == null ? "NULL" : value.toString();
    }

    public boolean isVisible() {
        if (this.showIf == null) {
            return true;
        }
        return this.showIf.test(this.getValue());
    }

    public Object parse(String string){
        if (value instanceof Number && !(value instanceof Enum))
        {
            if (value instanceof Integer)
                return Integer.parseInt(string);
            else if (value instanceof Float)
                return Float.parseFloat(string);
            else if (value instanceof Double)
                return Double.parseDouble(string);
            else if(value instanceof Long)
                return Long.parseLong(string);
        }
        else if (value instanceof Boolean)
        {
            return Boolean.parseBoolean(string);
        }
        else if (value instanceof Enum)
        {
            EnumConverter converter = new EnumConverter(((Enum) value).getClass());
            return converter.doBackward(string);
        }
        else if (value instanceof String)
            return value;
        else if(value instanceof Color){
            String[] col = string.split(",");
            Color color = new Color(-1);
            for(String colorString:col){
                String[] split = colorString.split(":");
                switch (split[0]){
                    case "red":
                        color.red = Float.parseFloat(split[1]);
                        break;
                    case "green":
                        color.green = Float.parseFloat(split[1]);
                        break;
                    case "blue":
                        color.blue = Float.parseFloat(split[1]);
                        break;
                    case "alpha":
                        color.alpha = Float.parseFloat(split[1]);
                        break;
                    case "rainbow":
                        color.rainbow = Boolean.parseBoolean(split[1]);
                        break;
                    case "rainbowSpeed":
                        color.rainbowSpeed = Float.parseFloat(split[1]);
                        break;
                    case "rainbowBrightness":
                        color.rainbowBrightness = Float.parseFloat(split[1]);
                        break;
                    case "rainbowSaturation":
                        color.rainbowSaturation = Float.parseFloat(split[1]);
                        break;
                }
            }
            return color;
        }
        return null;
    }
}