import itumulator.simulator.Actor;
import itumulator.world.World;
import itumulator.world.Location;
import java.util.*;

abstract public class Rabbit extends Creature implements Actor {
Random r = new Random();
protected boolean RabbitHole = false;

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
        Set<Location> neighbours = world.getEmptySurroundingTiles();
        List<Location> list = new ArrayList<>(neighbours);

        if (!neighbours.isEmpty()) {
            int randomNumber = r.nextInt(list.size());
            Location l = list.get(randomNumber);
            world.move(this, l);
        }
    }
    //spiser græsset på en tilfældig nabo-tile
    /**
     * A method that lets a rabbit eat the tile of grass on which it stands upon to energize.
     * The method checks for grass underneath the rabbit before committing.
     *
     * @param world
     */
    @Override
    public void eat(World world) {
        Location location = world.getLocation(this);
        Object terrain = world.getNonBlocking(location);
        if (terrain instanceof Grass) {
            world.delete(terrain);
            this.energize();
        } else {
            System.out.println("Nothing to eat");
        }
    }


    /**
     * Energize() er en metode, som giver energi til objektet, altså Rabbit.
     */
    @Override
    public void energize() {
        super.energize();
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

    public boolean HoleAlreadyMade(){
        return RabbitHole;
    }
}

