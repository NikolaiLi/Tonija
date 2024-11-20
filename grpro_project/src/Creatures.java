public class Creatures {
    int hunger;
    boolean alive;

    public Creatures() {
        hunger = 100;
        alive = true;
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
}
