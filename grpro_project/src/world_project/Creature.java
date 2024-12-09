package world_project;

import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.*;

abstract public class Creature implements Actor {
    protected String animal;
    protected int energy;
    protected int maxEnergy;
    protected boolean alive;
    protected int age;
    protected int ageOfDeath;
    protected int health;
    protected int maxHealth;
    protected boolean isInfected;
    protected Random r = new Random();
    protected boolean sleeping;

    public Creature() {
        maxEnergy = energy;
        alive = true;
        age = 0;
        isInfected = false;
    }

    public abstract void move(World world);

    public abstract void act(World world);

    public abstract void eat(World world);

    public boolean isAlive() {
        return alive;
    }

    public int getEnergy() {
        return energy;
    }

    public int getAge() {
        return age;
    }

    public int getHealth() {
        return health;
    }

    public void starve() {
        energy -= 5;
    }

    public void energize(int addEnergy){
        energy += addEnergy;
        if (energy > maxEnergy) {
            energy = maxEnergy;
        }
    }

    public void increaseMaxEnergy(int n) {
        maxEnergy += n;
    }

    public void aging() {
        age++;
    }

    public void attack(World world) {}

    public void takeDamage(int amountOfDamage) {
        health -= amountOfDamage;
    }

    /**
     * Handles the death of an animal when its health reaches zero.
     * Replaces the animal with a carcass.
     * @param world the world where the animal is
     * @param animal the type of the animal
     */
    public void deathByDamage(World world, String animal) {
        if (alive && health <= 0) {
            alive = false;
            dropCarcass(world);
            System.out.println(animal + " has bled out and died");
        }
    }

    /**
     * Handles the death of an animal by starvation.
     * Replaces the animal with a carcass.
     * @param world the world where the animal is
     * @param animal the type of the animal
     */
    public void hungerDeath(World world, String animal) {
        if (energy <= 0 && alive) {
            alive = false;
            dropCarcass(world);
            System.out.println(animal + " has died of hunger");
        }
    }

    /**
     * Handles the death of an animal by old age.
     * Replaces the animal with a carcass if it surpasses the age of death
     * and have a random chance condition, that makes the animal die.
     * @param world the world where the animal is
     * @param ageOfDeath the age after which the animal may die of old age
     * @param animal the type of the animal
     */
    public void dyingOfAge(World world, int ageOfDeath, String animal) {
        if (isAlive()) {
            int chanceOfDying = r.nextInt(10);
            if (age > ageOfDeath && chanceOfDying == 1) {
                alive = false;
                dropCarcass(world);
                System.out.println(animal + " has died of age");
            }
        }
    }

    /**
     * Replaces the object with a carcass at its current location in the world.
     * The carcass inherits the object's health and infection status.
     * @param world the world where the object and carcass exist
     */
    public void dropCarcass(World world) {
        if (world.isOnTile(this)) {
            Location l = world.getLocation(this);
            world.delete(this);
            world.setTile(l, new Carcass(maxHealth, isInfected));
        }
    }

    /**
     * Moves the object one step towards the target location by calculating
     * the difference in x and y coordinates between the current and target locations.
     * The movement is one step at a time, either horizontally, vertically,
     * or diagonally, depending on the calculated difference.
     * @param world getting the object position in world
     * @param target the target position that the object are heading to
     */
    public void moveTowards(World world, Location target) {
        Location current = world.getLocation(this);
        int diffX = target.getX() - current.getX();
        int diffY = target.getY() - current.getY();

        int stepX = Integer.compare(diffX, 0);
        int stepY = Integer.compare(diffY, 0);

        Location nextStep = new Location(current.getX() + stepX, current.getY() + stepY);

        if (world.isTileEmpty(nextStep)) {
            world.move(this, nextStep);
        }
    }
}
