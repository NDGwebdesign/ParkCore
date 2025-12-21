package net.ndgwebdesign.parkCore.commands.sub;

import net.ndgwebdesign.parkCore.functions.attractions.CreateAttraction;
import net.ndgwebdesign.parkCore.functions.attractions.DeleteAttraction;
import org.bukkit.command.CommandSender;

public class AttractionCommand {

    private final CreateAttraction createAttraction = new CreateAttraction();
    private final DeleteAttraction deleteAttraction = new DeleteAttraction();

    public boolean handle(CommandSender sender, String[] args) {

        if (args.length < 2) {
            sender.sendMessage("§e/parkcore att create <region> <naam>");
            sender.sendMessage("§e/parkcore att delete <naam>  §7(of: /parkcore att delete <region> <naam>)");
            return true;
        }

        if (args[1].equalsIgnoreCase("create")) {
            return createAttraction.execute(sender, args);
        }

        if (args[1].equalsIgnoreCase("delete")) {
            return deleteAttraction.execute(sender, args);
        }

        sender.sendMessage("§cOnbekend attraction command.");
        return true;
    }
}
