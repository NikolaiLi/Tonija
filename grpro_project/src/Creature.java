import itumulator.world.World;
abstract public class Creature {
    protected int hunger;
    protected boolean alive;
    protected int age;

    public Creature() {
        hunger = 100;
        alive = true;
        age = 0;
    }

    public void starve() {
        hunger -= 5;
    }

    public void energize(){
        hunger += 30;
        if (hunger > 100) {
            hunger = 100;
        }
    }

    public int getHunger() {
        return hunger;
    }

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
