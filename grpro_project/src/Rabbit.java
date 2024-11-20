import itumulator.simulator.Actor;
import itumulator.world.World;
import itumulator.world.Location;
import java.util.*;

public class Rabbit implements Actor {
    @Override
    public void act(World world) {
        Random r = new Random();

        Set<Location> neighbours = world.getEmptySurroundingTiles();
        List<Location> list = new ArrayList<>(neighbours);

        if (!neighbours.isEmpty()) {
            int randomNumber = r.nextInt(list.size());
            Location l = list.get(randomNumber);
            world.move(this, l);
        }

        }
    }
