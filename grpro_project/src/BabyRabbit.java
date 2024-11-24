import itumulator.world.Location;
import itumulator.world.World;
import java.util.*;


public class BabyRabbit extends Rabbit {

    public BabyRabbit() {
        super();
    }

    public void act(World world) {
        if (age == 15) {
            grow(world);
            return;
        }

        // Henter superklassens act (Rabbit's act)
        super.act(world);
    }

    public void grow(World world) {
        if (hiding) {
            Location rabbitHoleLocation = currentHidingPlace.getLocation();
            if (world.isTileEmpty(rabbitHoleLocation)) {
                world.setTile(rabbitHoleLocation, this);
                world.delete(this);
                System.out.println("Baby Rabbit has grown up");
                world.setTile(rabbitHoleLocation, new AdultRabbit(getHunger()));
            } else {
                Object o = world.getTile(rabbitHoleLocation);
                world.remove(o);
                Rabbit adultRabbit = new AdultRabbit(getHunger());
                world.setTile(rabbitHoleLocation, adultRabbit);
                adultRabbit.hide(world);
                world.setTile(rabbitHoleLocation, o);
            }
        } else {
            System.out.println("Baby Rabbit has grown up");
            Location l = world.getLocation(this);
            world.delete(this);
            world.setTile(l, new AdultRabbit(getHunger()));
        }
    }
}
