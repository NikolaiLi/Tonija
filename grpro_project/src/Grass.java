import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.*;

public class Grass {
    Random random = new Random();

    public void grow(World world){
        Set<Location> neighbours = world.getSurroundingTiles();
        List<Location> list = new ArrayList<>(neighbours);

        if(!list.isEmpty()){
            Location location = list.get(random.nextInt(list.size()));

            if(world.isTileEmpty(location)){
                world.setTile(location, this);
                spread(world, location);
            }
        } else {
            System.out.println("Nothing to grow");
        }
    }

    /**
     * A method that randomly spreads grass objects around the given parameter.
     * The method has a 25% chance of spreading grass after each step
     * @param world
     * @param location
     */
    public void spread(World world, Location location){
        Set<Location> surroundingTiles = world.getSurroundingTiles(location);
        List<Location> list = new ArrayList<>(surroundingTiles);

        if(!list.isEmpty() /*&& Math.random() < 0.25*/){
            Location new_location = list.get(random.nextInt(list.size()));
            if(world.isTileEmpty(new_location)){
                world.add(new Grass());
            }
        }
    }
}
