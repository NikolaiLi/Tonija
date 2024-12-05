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
        animal = "Bear";
        maxEnergy = 200;
        energy = maxEnergy;
        health = 200;
        maxHealth = health;
        alive = true;
        ageOfDeath = 150;
    }

    @Override
    public DisplayInformation getInformation() {
        return di_bear;
    }


    @Override
    public void act(World world) {

        //tjekker om bear er død af hunger, age eller damage

        hungerDeath(world, animal);

        deathByDamage(world, animal);

        dyingOfAge(world, ageOfDeath, animal);

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

            // Bear using energy
            starve();

            //aldrer bear
            aging();

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
        Location bear_location = world.getLocation(this);

        for (Location location : locations) {
            if(location.equals(bear_location)) {
                continue;
            }

            if (world.getTile(location) instanceof Bush) {
                Object objectBush = world.getTile(location);
                Bush bush = (Bush) objectBush;
                bush.isRipe(false);
                energize(50);
            }
        }
    }

    @Override
    public void moveTowards(World world, Location target) {super.moveTowards(world, target);}

    public void hunt(World world) {
        for (Location location : territoryArea) {
            Object tile = world.getTile(location);
            if (tile instanceof Creature && tile != this) {
                moveTowards(world, location);
                System.out.println("Hunting prey down!");
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

            if (targetEnemy instanceof Creature creatureTargetEnemy && creatureTargetEnemy != this) {
                creatureTargetEnemy.takeDamage(50);
                if (creatureTargetEnemy.getHealth() <= 0) {
                    energize(75);
                }
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
