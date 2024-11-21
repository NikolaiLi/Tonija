import itumulator.executable.Program;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.*;

public class Grass implements Actor {
    Random random = new Random();

    @Override
    public void act(World world){
        world.getLocation(this);
    }
}
