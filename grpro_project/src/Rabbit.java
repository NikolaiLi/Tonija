import itumulator.simulator.Actor;
import itumulator.world.World;
import itumulator.world.Location;
import java.util.*;

public class Rabbit implements Actor {
    int hunger;

    public Rabbit() {
        hunger = 100;
    }

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

    public void starve() {
        hunger = hunger - 10;
    }

    public int getHunger() {
        return hunger;
    }




}

