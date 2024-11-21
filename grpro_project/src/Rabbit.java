import itumulator.simulator.Actor;
import itumulator.world.World;
import itumulator.world.Location;
import java.util.*;

public class Rabbit extends Creatures implements Actor {

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

        // Tjekker om kaninen er ved at dø af sult
        if (getHunger() <= 0) {
            super.alive = false;
            world.delete(this);
            System.out.println("A rabbit has died");
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

        // Tjekker om der er kaniner i nabopositionerne, hvis der er fødes der en kanin
        Random random = new Random();
        int chanceOfBirth = random.nextInt(100);
        for (Location location : list) {
            if (world.getTile(location) instanceof Rabbit && chanceOfBirth >= 95) {
                breed(world);
                System.out.println("A rabbit has been born");
                return;
            }
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

    public void breed(World world) {
        Random random = new Random();

        Set<Location> neighbours = world.getEmptySurroundingTiles();
        List<Location> list = new ArrayList<>(neighbours);

        if (!neighbours.isEmpty()) {
            int randomNumber = random.nextInt(list.size());
            Location location = list.get(randomNumber);
            world.setTile(location, new Rabbit());
        }
    }
}

