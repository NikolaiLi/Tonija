package world_project;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;
import itumulator.simulator.Actor;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Grass extends Terrain implements Actor, DynamicDisplayInformationProvider {
    Random random = new Random();
    DisplayInformation di_grass = new DisplayInformation(Color.green, "grass");

    @Override
    public DisplayInformation getInformation() {
        return di_grass;
    }


    @Override
    public void act(World world) {
        Set<Location> neighbours = world.getSurroundingTiles();
        List<Location> list = new ArrayList<>(neighbours);

        if (!list.isEmpty()) {
            Location location = list.get(random.nextInt(list.size()));

            if (world.isTileEmpty(location) && !world.containsNonBlocking(location)) {
                spread(world, location);
            }
        }

        if(!world.contains(this)){
            Location location = list.get(random.nextInt(list.size()));
            spread(world, location);
        }
    }

    /**
     * A method that randomly spreads grass objects around the given parameter.
     * The method has a 25% chance of spreading grass after each step
     *
     * @param world
     * @param location
     */
    public void spread(World world, Location location) {
        Set<Location> surroundingTiles = world.getSurroundingTiles(location);
        List<Location> list = new ArrayList<>(surroundingTiles);

        if (!list.isEmpty() && Math.random() < 0.5) {
            Location new_location = list.get(random.nextInt(list.size()));
            if (world.isTileEmpty(new_location) && !world.containsNonBlocking(new_location)) {
                world.setTile(new_location, new Grass());
            }
        }
    }
}
