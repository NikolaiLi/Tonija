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
    public int maxHealth;
    public boolean isInfected;
    protected Random r = new Random();

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

    public void deathByDamage(World world, String animal) {
        if (alive && health <= 0) {
            alive = false;
            dropCarcass(world);
            System.out.println(animal + " has bled out and died");
        }
    }

    public void hungerDeath(World world, String animal) {
        if (energy <= 0 && alive) {
            alive = false;
            dropCarcass(world);
            System.out.println(animal + " has died of hunger");
        }
    }

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

    public void dropCarcass(World world) {
        Location l = world.getLocation(this);
        world.delete(this);
        world.setTile(l, new Carcass(maxHealth, isInfected));
    }

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
