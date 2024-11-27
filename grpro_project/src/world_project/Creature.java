package world_project;

import itumulator.simulator.Actor;
import itumulator.world.World;
abstract public class Creature implements Actor {
    protected int energy;
    protected int maxEnergy;
    protected boolean alive;
    protected int age;
    protected int health;

    public Creature() {
        maxEnergy = energy;
        alive = true;
        age = 0;
    }

    public void starve() {
        energy -= 5;
    }

    public void energize(){
        energy += 30;
        if (energy > maxEnergy) {
            energy = maxEnergy;
        }
    }

    public void increaseMaxEnergy(int n) {
        maxEnergy += n;
    }

    public int getEnergy() {
        return energy;
    }

    public abstract void move(World world);

    public abstract void act(World world);

    public abstract void eat(World world);

    public boolean isAlive() {
        return alive;
    }

    public int getAge() {
        return age;
    }

    public void aging() {
        age++;
    }

    public void attack(World world) {}

    public int getHealth(World world) {
        return health;
    }

}
