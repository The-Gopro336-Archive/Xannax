package me.zoom.xannax.command.commands;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.zoom.xannax.command.Command;
import me.zoom.xannax.module.Module;
import me.zoom.xannax.module.ModuleManager;

public class ToggleCommand extends Command {
    boolean found;
    @Override
    public String[] getAlias() {
        return new String[]{"toggle", "t"};
    }

    @Override
    public String getSyntax() {
        return "toggle <Module>";
    }

    @Override
    public void onCommand(String command, String[] args) throws Exception {
        found = false;
        ModuleManager.getModules().forEach(m -> {
            if(m.getName().equalsIgnoreCase(args[0])){
                if(m.isEnabled()){
                    m.disable();
                    found = true;
                    Command.sendClientMessage(ChatFormatting.RED + m.getName() + " disabled!");
                } else if(!m.isEnabled()){
                    m.enable();
                    found = true;
                    Command.sendClientMessage(ChatFormatting.GREEN + m.getName() + " enabled!");
                }
            }
        });
        if(!found && args.length == 1) Command.sendClientMessage(ChatFormatting.GRAY + "Module not found!");
    }
}

