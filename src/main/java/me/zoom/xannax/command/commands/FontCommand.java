package me.zoom.xannax.command.commands;

import me.zoom.xannax.Xannax;
import me.zoom.xannax.command.Command;
import me.zoom.xannax.util.font.CFontRenderer;

import java.awt.*;

public class FontCommand extends Command {
    @Override
    public String[] getAlias() {
        return new String[]{
                "font", "setfont"
        };
    }

    @Override
    public String getSyntax() {
        return "font <Name> <Size>";
    }

    @Override
    public void onCommand(String command, String[] args) throws Exception {
        String font = args[0].replace("_", " ");
        int size = Integer.parseInt(args[1]);
        Xannax.fontRenderer = new CFontRenderer(new Font(font, Font.PLAIN, size), true, false);
        Xannax.fontRenderer.setFontName(font);
        Xannax.fontRenderer.setFontSize(size);
    }
}
