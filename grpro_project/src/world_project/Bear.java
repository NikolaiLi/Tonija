package world_project;

import itumulator.world.World;
import itumulator.world.Location;
import java.util.*;

public class Bear extends Creature {
    Random r = new Random();
    Location territoryCenter;
    Set<Location> territoryArea;


    @Override
    public void act(World world) {
        if (!isAlive()) {
            return;
        }

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
            Random r = new Random();
            int randomIndex = r.nextInt(possibleLocations.size());
            Location chosenLocation = possibleLocations.get(randomIndex);
            world.move(this, chosenLocation);
        }
    }


    @Override
    public void eat(World world) {

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
