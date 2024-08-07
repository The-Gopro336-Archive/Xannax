package me.zoom.xannax.module.modules.client;

import me.zoom.xannax.module.Module;
import me.zoom.xannax.setting.Setting;

public class Rainbow extends Module {
    public Rainbow() {
        super("Rainbow", "Rainbow", Category.Client);
    }

    Setting.Integer speed;

    @Override
    public void setup() {
        speed = registerInteger("Speed", "speed", 2, 1, 10);
        super.setup();
    }

    static int RainbowOffset = 0;

    @Override
    public void onRender() {
        RainbowOffset += speed.getValue();

        super.onRender();
    }

    public static int getRainbowOffset() {
        return RainbowOffset;
    }
}
