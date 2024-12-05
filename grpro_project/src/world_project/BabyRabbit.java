package world_project;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;


public class BabyRabbit extends Rabbit implements DynamicDisplayInformationProvider {
    DisplayInformation di_baby_rabbit = new DisplayInformation(Color.magenta, "rabbit-small");
    DisplayInformation di_baby_rabbit_sleeping = new DisplayInformation(Color.magenta, "rabbit-small-sleeping");
    DisplayInformation currentDisplayInformation = di_baby_rabbit;

    public BabyRabbit() {
        super();
        health = 10;
        maxHealth = health;
        animal = "Baby Rabbit";
    }



    @Override
    public DisplayInformation getInformation() {
        return currentDisplayInformation;
    }

    public void act(World world) {

        //opdaterer displayInformation når dyret skal sove
        changeCurrentDisplay(world);

        //tjekker om baby rabbit er død af hunger, age eller damage
        hungerDeath(world, animal);

        deathByDamage(world, animal);

        while (alive) {
           if (age == 15) {
               grow(world);
               return;
           }

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


//PRIVATE METHODS
    private void changeCurrentDisplay (World world) {
        if (world.isDay()) {
            currentDisplayInformation = di_baby_rabbit;
        } else if (world.isNight() && !hiding && !hasBuiltRabbitHole()) {
            currentDisplayInformation = di_baby_rabbit_sleeping;
        }
    }
}
