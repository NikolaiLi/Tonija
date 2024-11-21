import itumulator.simulator.Actor;
import itumulator.world.World;
import itumulator.world.Location;
import java.util.*;

abstract public class Rabbit extends Creature implements Actor {

    public Rabbit() {
        super();
    }

    @Override
    public void act(World world) {
        if (!alive) {
            return;
        }

        // Sulter kaninen
        starve();

        //
        aging();

        // Tjekker om kaninen er ved at dø af sult
        if (getHunger() <= 0) {
            super.alive = false;
            world.delete(this);
            System.out.println("A rabbit has died of hunger");
            return;
        }

        // Flytter kaninen over til en tilfældig nabo-tile
        Random r = new Random();

        Set<Location> neighbours = world.getEmptySurroundingTiles();
        List<Location> list = new ArrayList<>(neighbours);

        if (!neighbours.isEmpty()) {
            int randomNumber = r.nextInt(list.size());
            Location l = list.get(randomNumber);
            world.move(this, l);
        }
    }

    /**
     * Starve() er en metode som sørger for at sulte objektet, altså Rabbit.
     */
    @Override
    public void starve() {
        super.starve();
    }

    /**
     * getHunger() er en metode som returnere det antal hunger som objektet har.
     */

    @Override
    public int getHunger() {
        return super.getHunger();
    }

    /**
     * isAlive() er en metode som returnerer en sandhedsværdi om Rabbit er død eller ej.
     */

    @Override
    public boolean isAlive() {
        return super.isAlive();
    }
}

