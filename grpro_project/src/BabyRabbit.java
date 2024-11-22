import itumulator.world.Location;
import itumulator.world.World;
import java.util.*;


public class BabyRabbit extends Rabbit {

    public BabyRabbit() {
        super();
    }

    public void act(World world) {
        // Henter superklassens act (Rabbit's act)
        super.act(world);
        if (age == 15) {
            grow(world);
            return;
        }
    }

    public void grow(World world) {
        System.out.println("Baby Rabbit has grown up");
        Location l = world.getLocation(this);
        world.delete(this);
        world.setTile(l, new AdultRabbit(getHunger()));
    }
}
