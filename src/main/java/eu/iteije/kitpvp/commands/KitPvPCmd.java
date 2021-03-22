package eu.iteije.kitpvp.commands;

import eu.iteije.kitpvp.KitPvP;
import eu.iteije.kitpvp.commands.objects.Help;
import eu.iteije.kitpvp.commands.subcommands.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class KitPvPCmd implements CommandExecutor {

    // Instance of main class
    private final KitPvP instance;

    // Commands and explanation shown in the help list
    private final List<String> commands = Arrays.asList(
            "/kitpvp createmap <mapname>",
            "/kitpvp deletemap <mapname>",
            "/kitpvp maps",
            "/kitpvp setspawn",
            "/kitpvp spawn",
            "/kitpvp placenpc <mapname>",
            "/kitpvp setcenter <mapname>",
            "/kill",
            "/leaderboard"
    );
    private final List<String> explanation = Arrays.asList(
            "Create a new map",
            "Delete a map",
            "List of all maps",
            "Change the lobby spawn",
            "Teleport to the spawn",
            "Place a join NPC at your current location",
            "Set the center location for a specific map",
            "Kill yourself while not in combat",
            "A list of players with the most kills"
    );

    // Help class instance
    private Help help = new Help(commands, explanation);

    /**
     * @param instance instance of KitPvP (main) class
     */
    public KitPvPCmd(KitPvP instance) {
        this.instance = instance;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        // If CommandSender entered 0 arguments, global help list will show up
        if (args.length == 0) {
            help.send(sender);
        } else {
            // Check first argument and redirect to associated class
            switch (args[0]) {
                case "createmap":
                    CreateMapSubCmd createMapSubCmd = new CreateMapSubCmd(instance);
                    createMapSubCmd.send(sender, args);
                    break;
                case "deletemap":
                    DeleteMapSubCmd deleteMapSubCmd = new DeleteMapSubCmd();
                    deleteMapSubCmd.send(sender, args);
                    break;
                case "maps":
                    MapsSubCmd mapsSubCmd = new MapsSubCmd();
                    mapsSubCmd.send(sender);
                    break;
                case "setspawn":
                    SetSpawnSubCmd setSpawnSubCmd = new SetSpawnSubCmd();
                    setSpawnSubCmd.send(sender);
                    break;
                case "spawn":
                    SpawnSubCmd spawnSubCmd = new SpawnSubCmd();
                    spawnSubCmd.send(sender);
                    break;
                case "placenpc":
                    PlaceNpcSubCmd placeNpcSubCmd = new PlaceNpcSubCmd();
                    placeNpcSubCmd.send(sender, args);
                    break;
                case "setcenter":
                    SetCenterSubCmd setCenterSubCmd = new SetCenterSubCmd();
                    setCenterSubCmd.send(sender, args);
                    break;
                default:
                    help.send(sender);
                    break;
            }
        }
        return true;
    }
}
