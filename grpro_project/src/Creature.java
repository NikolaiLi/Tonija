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

    public int getHunger() {
        return hunger;
    }

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
