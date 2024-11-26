package world_project;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;
import java.util.*;

public class RabbitHole extends Hole implements DynamicDisplayInformationProvider {
    Random r = new Random();
    protected Map<RabbitHole, ArrayList<RabbitHole>> tunnels = new HashMap<>();
    protected Location rabbitHoleLocation;

    public RabbitHole(World world){
        if (!tunnels.containsKey(this)) {
            tunnels.put(this, new ArrayList<>());
            tunnels.get(this).add(this);
            System.out.println("RabbitHole has been dug");
        }
    }

    DisplayInformation di_rabbit_hole = new DisplayInformation(Color.green, "hole-small");

    @Override
    public DisplayInformation getInformation() {
        return di_rabbit_hole;
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