package world_project;

import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.util.*;

public class RabbitHole extends Hole {
    Random r = new Random();
    protected Map<RabbitHole, ArrayList<RabbitHole>> tunnels = new HashMap<>();
    protected Location rabbitHoleLocation;

    public RabbitHole(World world){
        if (!tunnels.containsKey(this)) {
            tunnels.put(this, new ArrayList<>());
            tunnels.get(this).add(this);
            System.out.println("world_project.RabbitHole has been dug");
        }
    }

    public Map<RabbitHole, ArrayList<RabbitHole>> getTunnels() {
        return tunnels;
    }

    public void setLocation(World world){
        rabbitHoleLocation = world.getLocation(this);
    }

    public Location getLocation() {
        return rabbitHoleLocation;
    }
}