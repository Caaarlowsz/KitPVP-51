package eu.iteije.kitpvp.commands;

import eu.iteije.kitpvp.KitPvP;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.List;

public class KitPvPCmd implements CommandExecutor {

    // Instance of main class
    private KitPvP instance;

    // Commands and explanation shown in the help list
    private List<String> commands = Arrays.asList(
            "/kitpvp editkits",
            "/kitpvp createmap",
            "/kitpvp deletemap",
            "/kitpvp maps",
            "/kitpvp setspawn",
            "/kitpvp spawn",
            "/kitpvp leave",
            "/kitpvp setleavedelay",
            "/kitpvp modifyhunger",
            "/worldtp",
            "/leaderboard"
    );
    private List<String> explanation = Arrays.asList(
            "Modify/create kits",
            "Create a new map",
            "Delete a map",
            "List of all maps",
            "Change the lobby spawn",
            "Teleport to the spawn",
            "Head back to the spawn while ingame",
            "Modify leave delay",
            "Toggle hunger ingame",
            "Teleport to a specific world",
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
                    DeleteMapSubCmd deleteMapSubCmd = new DeleteMapSubCmd(instance);
                    deleteMapSubCmd.send(sender, args);
                    break;
                case "maps":
                    MapsSubCmd mapsSubCmd = new MapsSubCmd(instance);
                    mapsSubCmd.send(sender);
                    break;
                case "setspawn":
                    SetSpawnSubCmd setSpawnSubCmd = new SetSpawnSubCmd(instance);
                    setSpawnSubCmd.send(sender);
                    break;
                case "spawn":
                    SpawnSubCmd spawnSubCmd = new SpawnSubCmd(instance);
                    spawnSubCmd.send(sender);
                    break;
                case "leave":
                    LeaveSubCmd leaveSubCmd = new LeaveSubCmd(instance);
                    leaveSubCmd.send(sender);
                    break;
                default:
                    help.send(sender);
                    break;
            }
        }
        return true;
    }
}
