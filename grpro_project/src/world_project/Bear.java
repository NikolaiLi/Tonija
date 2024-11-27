package world_project;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.World;
import itumulator.world.Location;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Bear extends Creature implements DynamicDisplayInformationProvider {
    Random r = new Random();
    Location territoryCenter;
    Set<Location> territoryArea;
    DisplayInformation di_bear = new DisplayInformation(Color.green, "bear");

    Bear() {
        health = 200;
    }

    @Override
    public DisplayInformation getInformation() {
        return di_bear;
    }


    @Override
    public void act(World world) {
        // Checks if the bear is alive, if not, return nothing.
        if (!isAlive()) {
            return;
        }

        // Bear moves around randomly in the territory
        move(world);


    }

    @Override
    public void move(World world) {
        Set<Location> neighbourTiles = world.getEmptySurroundingTiles();
        List<Location> possibleLocations = new ArrayList<>();

        for (Location location : neighbourTiles) {
            if (territoryArea.contains(location)) {
                possibleLocations.add(location);
            }
        }

        if (!possibleLocations.isEmpty()) {
            int randomIndex = r.nextInt(possibleLocations.size());
            Location chosenLocation = possibleLocations.get(randomIndex);
            world.move(this, chosenLocation);
        }
    }


    @Override
    public void eat(World world) {

    }

    @Override
    public void attack(World world) {
        Set<Location> neighbourTiles = world.getSurroundingTiles();
        List<Location> neighbourLocations = new ArrayList<>(neighbourTiles);
        List<Location> targetLocations = new ArrayList<>();

        for (Location location : neighbourLocations) {
            if (world.getTile(location) instanceof Bear || world.getTile(location) instanceof Wolf) {
                targetLocations.add(location);
            }
        }

        if (!targetLocations.isEmpty()) {
            int randomIndex = r.nextInt(targetLocations.size());
            Location chosenLocation = targetLocations.get(randomIndex);
            Object targetEnemy = world.getTile(chosenLocation);
        }

    }

    public void makeTerritory(World world) {
        int worldSize = world.getSize();

        int x = r.nextInt(worldSize);
        int y = r.nextInt(worldSize);
        territoryCenter = new Location(x, y);
        territoryArea = world.getSurroundingTiles(territoryCenter, 3);
    }

    public Set<Location> getTerritoryArea() {
        return territoryArea;
    }

    public Location getTerritoryCenter() {
        return territoryCenter;
    }

}
