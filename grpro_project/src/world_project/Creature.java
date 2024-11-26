package world_project;

import itumulator.simulator.Actor;
import itumulator.world.World;
abstract public class Creature implements Actor {
    protected int hunger;
    protected int maxHunger;
    protected boolean alive;
    protected int age;

    public Creature() {
        hunger = 100;
        maxHunger = hunger;
        alive = true;
        age = 0;
    }

    public void starve() {
        hunger -= 5;
    }

    public void energize(){
        hunger += 30;
        if (hunger > maxHunger) {
            hunger = maxHunger;
        }
    }

    public void increaseMaxHunger(int n) {
        maxHunger += n;
    }

    public int getHunger() {
        return hunger;
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
}
