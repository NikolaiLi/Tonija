package world_project;

import itumulator.world.World;
import itumulator.world.Location;
import java.util.*;

public class Bear extends Creature {
    Random r = new Random();
    Location territoryCenter;

    @Override
    public void act(World world) {
        if (!isAlive()) {
            return;
        }

    }

    @Override
    public void move(World world) {

    }

    @Override
    public void eat(World world) {

    }

    public void territory(World world) {
        int worldSize = world.getSize();

        int x = r.nextInt(worldSize);
        int y = r.nextInt(worldSize);
        Location territoryCenter = new Location(x, y);

    }
}
