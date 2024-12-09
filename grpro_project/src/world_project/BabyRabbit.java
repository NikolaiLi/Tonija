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

    /**
     * Baby rabbit grow up and becomes an adult. The method checks for two instances of the rabbit, one where it is
     * hiding and one where it isn't. If the baby rabbit is hiding, the object is deleted and replaced by an adult
     * rabbit with the same amount of energy as the baby rabbit.
     * In the case where location of the baby rabbits hiding place is occupied it temporarily removes the
     * object occupying the hiding place, allows the baby rabbit to grow into an adult
     * and hide, and then restores the original object to maintain the world's state.
     * @param world to access the world library
     */
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

    /**
     * Dynamically changes the display from awake to sleeping depending on whether it is day or night, and if it isn't
     * hiding in a rabbit hole or seeking a rabbit hole
     * @param world to access the world library
     */
    private void changeCurrentDisplay (World world) {
        if (world.isDay()) {
            currentDisplayInformation = di_baby_rabbit;
        } else if (world.isNight() && !hiding && !seeking) {
            currentDisplayInformation = di_baby_rabbit_sleeping;
        }
    }
}
