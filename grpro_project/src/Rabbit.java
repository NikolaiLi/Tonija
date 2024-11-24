import itumulator.world.World;
import itumulator.world.Location;
import java.util.*;

public abstract class Rabbit extends Creature {
    protected Random r = new Random();
    protected boolean AlreadyBuiltRabbitHole = false;
    protected RabbitHole currentHidingPlace = null;

    public Rabbit() {
        super();
    }

    @Override
    public void act(World world)
    {
        // Tjekker hvis Rabbit er død, hvis den er, skal det return ingenting.
        if (!alive) {
            return;
        }

        // Sulter kaninen
        starve();

        // Tjekker om kaninen er ved at dø af sult
        if (getHunger() <= 0){
            alive = false;
            world.delete(this);
            System.out.println("A rabbit has died of hunger");
            return;
        }

        //
        aging();

        // Får kaninen til at spise, hvis der er græs
        eat(world);

        // Flytter kaninen over til en tilfældig nabo-tile
        move(world);
    }


    /**
     * A method that lets a rabbit eat the tile of grass on which it stands upon to energize.
     * The method checks for grass underneath the rabbit before committing.
     *
     * @param world
     */
    @Override
    public void move(World world) {
        Set<Location> neighbours = world.getEmptySurroundingTiles();
        List<Location> list = new ArrayList<>(neighbours);

        if (!neighbours.isEmpty()) {
            int randomNumber = r.nextInt(list.size());
            Location l = list.get(randomNumber);
            world.move(this, l);
        }
    }


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
        }
    }

    /**
     * A method that lets a rabbit hide in a rabbithole, if it is currently standing on top of one.
     * The method checks for rabbithole underneath the rabbit before committing.
     *
     * @param world
     */
    public void hide(World world) {
        Object o = world.getNonBlocking(world.getLocation(this));
        if (o instanceof RabbitHole) {
            currentHidingPlace = (RabbitHole) o;
        }
        world.remove(this);
    }

    /**
     * A method that lets a rabbit come out of a random rabbithole connected to its current hidingspot (rabbithole).
     * Checks @tunnels to gather all exits currently not being blocked, and chooses one randomly for rabbit to exit from.
     *
     * @param world
     */
    public void unhide(World world) {
        if (world.isDay()) {
            if (!world.isOnTile(this)) {
                RabbitHole hidingspot = this.getCurrentHidingPlace();
                ArrayList<RabbitHole> allExits = hidingspot.getTunnels().get(hidingspot);
                ArrayList<RabbitHole> unblockedExits = new ArrayList<>();
                for (RabbitHole exit : allExits) {
                    if (world.isTileEmpty(exit.getLocation())) {
                        unblockedExits.add(exit);
                    }
                }
                if(!unblockedExits.isEmpty()) {
                    int number = r.nextInt(unblockedExits.size());
                    RabbitHole exit = unblockedExits.get(number);
                    Location rabbithole = exit.getLocation();
                    world.setTile(rabbithole,this);
                } else {
                        System.out.println("No exits available yet. Rabbit remains in hiding at " + this.getCurrentHidingPlace().getLocation());
                }
            }
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

    /**
     * getCurrentHidingPlace() returns the rabbithole that this rabbit is currently hiding inside.
     */

    public RabbitHole getCurrentHidingPlace() {
        return currentHidingPlace;
    }
}