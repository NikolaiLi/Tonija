package world_project;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.*;

/**
 * The Creature class is an abstract class meant to overlook all creatures in the world.
 * Creatures are free agents that move around and interact with the world and each other.
 * All creatures share some commonalities in both fields and methods, that are documented here.
 * Some methods are deliberately empty, only written here to help structure methods that all creatures posses,
 * but perhaps in different ways.
 */
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

    /**
     * initializes a creature with the most basic common stats shared among all creatures.
     */
    public Creature() {
        maxEnergy = energy;
        alive = true;
        age = 0;
        isInfected = false;
    }

    /**
     * abstract method used across all creatures to move.
     * written here to make sure all creatures implement the method.
     * @param world to access the world library.
     */
    public abstract void move(World world);

    /**
     * abstract method used across all creatures to act.
     * written here to make sure all creatures implement the method.
     * @param world to access the world library.
     */
    public abstract void act(World world);

    /**
     * abstract method used across all creatures to eat.
     * written here to make sure all creatures implement the method.
     * @param world to access the world library.
     */
    public abstract void eat(World world);

    /**
     * Method used to check if a creature is still alive
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Method returns the current amount of Energy a creature has at the given moment
     */
    public int getEnergy() {
        return energy;
    }

    /**
     * Method returns the current amount of health a creature has at the given moment
     */
    public int getHealth() {
        return health;
    }

    /**
     * Method starves a creature
     */
    public void starve() {
        energy -= 5;
    }

    /**
     * Method used to Energizes a creature, refilling its energy based on the parameter
     * @param addEnergy amount of energy given to creature
     */
    public void energize(int addEnergy){
        energy += addEnergy;
        if (energy > maxEnergy) {
            energy = maxEnergy;
        }
    }

    /**
     * Increases a creatures max amount of energy based on the parameter given
     * @param increaseEnergy amount of energy that maxEnergy is increased with
     */
    public void increaseMaxEnergy(int increaseEnergy) {
        maxEnergy += increaseEnergy;
    }

    /**
     * Ages a creature by one incrementation.
     * i.e. called once in every act method, to age a creature once every step
     */
    public void aging() {
        age++;
    }

    /**
     * Method used to attack other creatures.
     * Not an abstract method, as not all creatures are predators, yet still worth mentioning here.
     * as not enough methods were developed to necessitate a predator class.
     * @param world to access the world library.
     */
    public void attack(World world) {}

    /**
     * inflicts damage on an instance of a creature.
     * @param amountOfDamage integer determining the amount of damage taken.
     */
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
