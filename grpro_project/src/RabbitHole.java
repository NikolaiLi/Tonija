import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.NonBlocking;
import itumulator.world.World;

import java.util.*;

public class RabbitHole implements NonBlocking {
    Random r = new Random();
    protected HashMap<Location, ArrayList<Location>> tunnels;
    protected Location home;
    protected Location exit;

    public RabbitHole(Location home, Location exit){
        this.home = home;
        this.exit = exit;
        System.out.println("RabbitHole has been dug");

        if (!tunnels.containsKey(home)) {
            tunnels.put(home, new ArrayList<>());
            tunnels.get(home).add(exit);
        }
        else {
            tunnels.get(home).add(exit);
        }
    }

    public HashMap<Location, ArrayList<Location>> getTunnels() {
        return tunnels;
    }
}