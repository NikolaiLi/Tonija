package world_project;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;
import itumulator.simulator.Actor;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * The Grass class is a non-blocking object that has the ability to spread.
 * Grass implements interfaces actor and DynamicDisplayInformationProvider, to enact its methods
 * and ensure correct display of Grass at all times during the world simulation.
 */
public class Grass extends Terrain implements Actor, DynamicDisplayInformationProvider {
    Random random = new Random();
    DisplayInformation di_grass = new DisplayInformation(Color.green, "grass");

    /**
     * Provides the visual display of grass.
     * @return the relevant image for displaying grass.
     */
    @Override
    public DisplayInformation getInformation() {
        return di_grass;
    }


    /**
     * The act method for Grass makes it able to check its empty surrounding tiles
     * that don't contain non-blocking objects so it can spread across the world.
     * @param world providing details of the position on which the actor is currently located and much more.
     */
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
     * @param world to access the world library.
     * @param location a random location from the current Grass objects' surrounding tiles.
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
