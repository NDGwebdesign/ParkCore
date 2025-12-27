package net.ndgwebdesign.parkCore.commands;

import net.ndgwebdesign.parkCore.commands.sub.AttractionCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ParkCoreCommand implements CommandExecutor {

    private final AttractionCommand attractionCommand = new AttractionCommand();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            sender.sendMessage("§e/parkcore att create <region> <naam>");
            return true;
        }

        if (args[0].equalsIgnoreCase("att") || args[0].equalsIgnoreCase("attraction")) {
            return attractionCommand.handle(sender, args);
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("genmap")) {
            return new GenerateMapCommand().execute(sender);
        }


        sender.sendMessage("§cOnbekend subcommand.");
        return true;
    }
}
