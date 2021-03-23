package eu.iteije.kitpvp.memory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;

@AllArgsConstructor
@Getter
public class SpawnLocation {

    private final Location location;
    private final Material material;

}
