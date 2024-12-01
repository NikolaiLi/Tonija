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
    DisplayInformation di_bear = new DisplayInformation(Color.red, "bear");

    public Bear() {
        health = 200;
        maxEnergy = 200;
        alive = true;
    }

    @Override
    public DisplayInformation getInformation() {
        return di_bear;
    }


    @Override
    public void act(World world) {

        if (health <= 0 && alive) {
            alive = false;
            world.delete(this);
        }

        while (alive) {
            // Creates territory for the bear
            makeTerritory(world);

            // Bear moves around randomly in the territory
            move(world);

            // If there's a prey in Bears territory, it will hunt.
            hunt(world);

            // If there is a Creature nearby, the bear will attack the Creature
            attack(world);

            // If there is a berry bush nearby, the bear will eat the berries in the bush.
            eat(world);

            return;
        }
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

            Object object = world.getTile(location);
            if(object instanceof Rabbit){
                System.out.println("Bear ate rabbit! Nom! Nom! Nom!");
                world.delete(object);
                energize();
            }
        }
    }

    private void moveTowards(World world, Location target) {
        Location current = world.getLocation(this);
        int diffX = target.getX() - current.getX();
        int diffY = target.getY() - current.getY();

        int stepX = Integer.compare(diffX, 0);
        int stepY = Integer.compare(diffY, 0);

        Location nextStep = new Location(current.getX() + stepX, current.getY() + stepY);

        if (world.isTileEmpty(nextStep)) {
            world.move(this, nextStep);
        } else {
            System.out.println("Path blocked!");
        }
    }

    public void hunt(World world) {
        for (Location location : territoryArea) {
            Object tile = world.getTile(location);
            if (tile instanceof Creature && tile != this) {
                System.out.println("Bear spotted a creature at " + location);
                moveTowards(world, location);
            }
        }
        System.out.println("No creatures found in bear's territory.");
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

            if (targetEnemy instanceof Creature creatureTargetEnemy && creatureTargetEnemy != this) {
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
