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
    boolean hasTerritory = false;
    Location territoryCenter;
    Set<Location> territoryArea;
    DisplayInformation di_bear = new DisplayInformation(Color.green, "bear");

    public Bear() {
        health = 200;
        maxEnergy = 200;
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

        // Creates territory for the bear
        makeTerritory(world);

        // Bear moves around randomly in the territory
        move(world);

        // If there is a Creature nearby, the bear will attack the Creature
        attack(world);

        // If there is a berry bush nearby, the bear will eat the berries in the bush.
        eat(world);
    }

    public void makeTerritory(World world) {
        if(!hasTerritory) {
            hasTerritory = true;
            territoryCenter = world.getLocation(this);
            territoryArea = world.getSurroundingTiles(territoryCenter, 3);
        }
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
        Set<Location> neighbourTiles = world.getSurroundingTiles();
        List<Location> locations = new ArrayList<>(neighbourTiles);

        for (Location location : locations) {
            if (world.getTile(location) instanceof Bush) {
                Object objectBush = world.getTile(location);
                Bush bush = (Bush) objectBush;
                bush.isRipe(false);
                energize();
            }
        }
    }

    @Override
    public void attack(World world) {
        Set<Location> neighbourTiles = world.getSurroundingTiles();
        List<Location> neighbourLocations = new ArrayList<>(neighbourTiles);
        List<Location> targetLocations = new ArrayList<>();

        for (Location location : neighbourLocations) {
            Object tile = world.getTile(location);
            if (tile instanceof Creature) {
                targetLocations.add(location);
            }
        }

        if (!targetLocations.isEmpty()) {
            int randomIndex = r.nextInt(targetLocations.size());
            Location chosenLocation = targetLocations.get(randomIndex);
            Object targetEnemy = world.getTile(chosenLocation);

            if (targetEnemy instanceof Creature creatureTargetEnemy) {
                creatureTargetEnemy.takeDamage(50);
            }
        }
    }

    public Set<Location> getTerritoryArea() {
        return territoryArea;
    }

    public Location getTerritoryCenter() {
        return territoryCenter;
    }

}
