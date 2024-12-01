package world_project;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;


public class BabyRabbit extends Rabbit implements DynamicDisplayInformationProvider {

    public BabyRabbit() {
        super();
        health = 10;
    }

    DisplayInformation di_baby_rabbit = new DisplayInformation(Color.magenta, "rabbit-small");

    @Override
    public DisplayInformation getInformation() {
        return di_baby_rabbit;
    }

    public void act(World world) {
        if (health <= 0) {
            alive = false;
            world.delete(this);
        }

        while (alive) {
           if (age == 15) {
               grow(world);
               return;
           }

           // Henter superklassens act (Rabbit's act)
           super.act(world);
           return;
       }
    }

    public void grow(World world) {
        if (hiding) {
            Location rabbitHoleLocation = currentHidingPlace.getLocation();
            if (world.isTileEmpty(rabbitHoleLocation)) {
                world.setTile(rabbitHoleLocation, this);
                world.delete(this);
                System.out.println("Baby Rabbit has grown up");
                world.setTile(rabbitHoleLocation, new AdultRabbit(getEnergy()));
            } else {
                Object o = world.getTile(rabbitHoleLocation);
                world.remove(o);
                Rabbit adultRabbit = new AdultRabbit(getEnergy());
                world.setTile(rabbitHoleLocation, adultRabbit);
                adultRabbit.hide(world);
                world.setTile(rabbitHoleLocation, o);
            }
        } else {
            System.out.println("BabyRabbit has grown up");
            Location l = world.getLocation(this);
            world.delete(this);
            world.setTile(l, new AdultRabbit(getEnergy()));
        }
    }
}
