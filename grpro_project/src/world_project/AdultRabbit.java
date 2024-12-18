package world_project;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * The Adult Rabbit class is a herbivorous subclass inherited from the abstract Rabbit class and an object in the world.
 * Adult Rabbit implements interfaces actor and DynamicDisplayInformationProvider, to enact its methods
 * and ensure correct display of Adult Rabbit at all times during the world simulation.
 * Adult Rabbits have the ability to breed with other adult rabbits and dig rabbit holes for sleeping and breeding
 * at night.
 */
public class AdultRabbit extends Rabbit implements DynamicDisplayInformationProvider {
    Random r = new Random();
    DisplayInformation di_adult_rabbit = new DisplayInformation(Color.green, "rabbit-large");
    DisplayInformation di_adult_rabbit_sleeping = new DisplayInformation(Color.green, "rabbit-sleeping");
    DisplayInformation currentDisplayInformation = di_adult_rabbit;

    /**
     * Initializes an adult rabbit with more health and energy than a baby rabbit.
     * The adult rabbit automatically gets more energy with an increase of 50 energy from that of the baby rabbit.
     * @param energy is the baby rabbit's amount of energy when growing into an adult rabbit.
     */
    public AdultRabbit(int energy) {
        super();
        animal = "Adult Rabbit";
        age = 15;
        ageOfDeath = 60;
        this.energy = energy;
        increaseMaxEnergy(50);
        health = 30;
        maxHealth = health;
    }

    @Override
    public DisplayInformation getInformation() {return currentDisplayInformation;}

    /**
     * Provides the order of when individual methods should be executed inside the simulation.
     * @param world providing details of the position on which the actor is currently located and much more.
     */
    @Override
    public void act(World world) {
        //outside the act loop

        //opdaterer displayInformation når dyret skal sove
        changeCurrentDisplay(world);

        //tjekker om Adult rabbit er død af hunger, age eller damage

        hungerDeath(world, animal);

        deathByDamage(world, animal);

        dyingOfAge(world,ageOfDeath, animal);

        while (alive) {

            super.act(world);

            breed(world);

            //10% chance to dig rabbithole, if unblocking element is empty and rabbit has not dug a hole already
            if (r.nextInt(100) < 10 && this.isAlive()) {
                digRabbitHole(world);
            }
            return;
        }
    }

    /**
     * Deletes the rabbit object from the world and drops a Carcass object on the tile.
     * @param world the world where the animal is
     * @param ageOfDeath the age after which the animal may die of old age
     * @param animal the type of the animal
     */
    @Override
    public void dyingOfAge(World world, int ageOfDeath, String animal) {super.dyingOfAge(world,ageOfDeath, animal);}


    /**
     * A method that enables rabbits to breed, checking the rabbits surrounding tiles for other rabbits. If another
     * rabbit is inside the surrounding tiles there will be a 5% chance of them breeding. A baby rabbit will be born on
     * an empty surrounding tile.
     * @param world to access the world library
     */
    public void breed(World world) {
        if (!hiding && world.isOnTile(this)) {
            Set<Location> neighbours = world.getSurroundingTiles(world.getLocation(this));
            List<Location> list = new ArrayList<>(neighbours);
            int chanceOfBirth = r.nextInt(100);
            for (Location location : list) {
                if (world.getTile(location) instanceof AdultRabbit mate && (mate != this) && chanceOfBirth >= 95) {
                    Set<Location> emptyNeighbours = world.getEmptySurroundingTiles();
                    List<Location> listOfEmptyTiles = new ArrayList<>(emptyNeighbours);

                    if (!emptyNeighbours.isEmpty()) {
                        int randomNumber = r.nextInt(listOfEmptyTiles.size());
                        Location l = listOfEmptyTiles.get(randomNumber);
                        world.setTile(l, new BabyRabbit());
                    }
                    return;
                }
            }
        }
    }

    /**
     * A rabbit digs a rabbit hole if its current tile doesn't already contain any non-blocking objects. The rabbit has
     * a boolean that checks whether it has built a hole or not. If it hasn't already, a hole can be dug and the
     * boolean AlreadyBuiltRabbitHole is updated.
     * @param world to access the world library.
     */
    public void digRabbitHole(World world) {
        if (!AlreadyBuiltRabbitHole && !hiding && isAlive()) {
            Location current = world.getLocation(this);
            if (!world.containsNonBlocking(current)) {
                RabbitHole rabbitHole = new RabbitHole(world);
                world.setTile(current, rabbitHole);
                AlreadyBuiltRabbitHole = true;
                rabbitHole.setLocation(world);
            }
        }
    }



// PRIVATE METHODS

    /**
     * Dynamically changes the display from awake to sleeping depending on whether it is day or night, and if it isn't
     * hiding in a rabbit hole
     * @param world to access the world library
     */
    private void changeCurrentDisplay (World world) {
        if (world.isNight() && !hiding && !seeking) {
            currentDisplayInformation = di_adult_rabbit_sleeping;
        } else {
            currentDisplayInformation = di_adult_rabbit;
        }
    }

}
