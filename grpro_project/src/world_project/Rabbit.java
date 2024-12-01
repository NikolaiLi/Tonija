package world_project;

import itumulator.world.World;
import itumulator.world.Location;
import java.util.*;

public abstract class Rabbit extends Creature {
    protected Random r = new Random();
    protected boolean AlreadyBuiltRabbitHole = false;
    protected RabbitHole currentHidingPlace;
    protected boolean hiding;

    public Rabbit() {
        super();
        energy = 100;
        maxEnergy = 100;
        hiding = false;
        currentHidingPlace = null;
    }

    @Override
    public void act(World world)
    {
        // Tjekker hvis world_project.Rabbit er død, hvis den er, skal det return ingenting.
        if (alive && health <= 0) {
            death(world);
        }

        //act runs in a loop that works while creature is alive
        while (alive) {

             //Tjekker om kaninen er ved at dø af sult
            if (getEnergy() <= 0) {
                hungerDeath(world);
                return;
            }

            // Bliver ældre
            aging();

            if (world.isDay()) {
                // unhider
                unhide(world);

                // Får kaninen til at spise, hvis der er græs
                eat(world);

                // Flytter kaninen over til en tilfældig nabo-tile
                move(world);

                // Sulter kaninen
                starve();
            }

            if (world.isNight()) {
                seek(world);
            }
            return;
        }
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
        if (!hiding) {
            Location location = world.getLocation(this);
            Object terrain = world.getNonBlocking(location);
            if (terrain instanceof Grass) {
                world.delete(terrain);
                this.energize();
            }
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
            world.remove(this);
            hiding = true;
        }
    }

    /**
     * A method that lets a rabbit come out of a random rabbithole connected to its current hidingspot (rabbithole).
     * Checks @tunnels to gather all exits currently not being blocked, and chooses one randomly for rabbit to exit from.
     *
     * @param world
     */
/*    public void unhideTest(World world) {
        if (hiding) {
            if (!world.isOnTile(this)) {
                world_project.RabbitHole hidingSpot = currentHidingPlace;
                ArrayList<world_project.RabbitHole> allExits = hidingSpot.getTunnels().get(hidingSpot);
                ArrayList<world_project.RabbitHole> unblockedExits = new ArrayList<>();

                for (world_project.RabbitHole exit : allExits) {
                    if (world.isTileEmpty(exit.getLocation())) {
                        unblockedExits.add(exit);
                    }
                }
                if(!unblockedExits.isEmpty()) {
                    int number = r.nextInt(unblockedExits.size());
                    world_project.RabbitHole exit = unblockedExits.get(number);
                    Location rabbithole = exit.getLocation();
                    world.setTile(rabbithole,this);
                } else {
                    System.out.println("No exits available yet. world_project.Rabbit remains in hiding at " + this.getCurrentHidingPlace().getLocation());
                }
            }
        }
    }*/

    public void unhide(World world) {
        if (!world.isOnTile(this) && hiding) {
            RabbitHole hidingSpot = currentHidingPlace;
            world.setCurrentLocation(currentHidingPlace.getLocation());
            ArrayList<Location> list = new ArrayList<>(world.getEmptySurroundingTiles());

            if (!list.isEmpty()) {
                int size = r.nextInt(list.size());
                world.setTile(list.get(size),this);
                hiding = false;
            }
        }
    }

    /**
     * Energize() er en metode, som giver energi til objektet, altså world_project.Rabbit.
     */
    @Override
    public void energize() {
        super.energize();
    }

    /**
     * Starve() er en metode som sørger for at sulte objektet, altså world_project.Rabbit.
     */
    @Override
    public void starve() {
        super.starve();
    }

    /**
     * getHunger() er en metode som returnere det antal hunger som objektet har.
     */

    @Override
    public int getEnergy() {
        return super.getEnergy();
    }

    /**
     * isAlive() er en metode som returnerer en sandhedsværdi om world_project.Rabbit er død eller ej.
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

    public void hungerDeath(World world) {
        alive = false;
        world.delete(this);
        System.out.println("A rabbit has died of hunger");
    }

    public void death(World world) {
        alive = false;
        world.delete(this);
    }

    private void moveTowards(World world, Location target) {
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

    public void seek(World world) {
        if (!hiding) {
            Location currentLocation = world.getLocation(this);
            Queue<Location> toVisit = new LinkedList<>();
            Set<Location> visited = new HashSet<>();
            toVisit.add(currentLocation);

            while (!toVisit.isEmpty()) {
                Location current = toVisit.poll();
                visited.add(current);

                if (world.getTile(current) instanceof RabbitHole) {
                    moveTowards(world, current);
                    Location currentL = world.getLocation(this);
                    Object obj = world.getNonBlocking(currentL);
                    if (obj instanceof RabbitHole) {
                        hide(world);
                    }
                    return;
                }

                for (Location neighbor : world.getSurroundingTiles(current)) {
                    if (!visited.contains(neighbor) && !toVisit.contains(neighbor)) {
                        toVisit.add(neighbor);
                    }
                }
            }
        }
    }

    public boolean hasBuiltRabbitHole(){ return AlreadyBuiltRabbitHole; }
}