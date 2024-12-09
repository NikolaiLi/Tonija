package world_project;

import itumulator.world.World;
import itumulator.world.Location;
import java.util.*;

public abstract class Rabbit extends Creature {
    protected Random r = new Random();
    protected boolean AlreadyBuiltRabbitHole = false;
    protected RabbitHole currentHidingPlace;
    protected boolean hiding;
    protected boolean seeking;

    public Rabbit() {
        super();
        animal = "Rabbit";
        energy = 100;
        maxEnergy = 100;
        hiding = false;
        currentHidingPlace = null;
        seeking = false;
    }

    @Override
    public void act(World world)
    {
        // Tjekker hvis world_project.Rabbit er død, hvis den er, skal det return ingenting.
        hungerDeath(world, animal);

        deathByDamage(world, animal);

        //act runs in a loop that works while creature is alive
        while (alive) {

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

            // Bliver ældre
            aging();

            return;
        }
    }


    /**
     * A method that lets a rabbit eat the tile of grass on which it stands upon to energize.
     * The method checks for grass underneath the rabbit before committing.
     *
     * @param world to access the world library.
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
     * @param world to access the world library.
     */
    @Override
    public void eat(World world) {
        if (!hiding) {
            Location location = world.getLocation(this);
            Object terrain = world.getNonBlocking(location);
            if (terrain instanceof Grass) {
                world.delete(terrain);
                this.energize(10);
            }
        }
    }

    /**
     * A method that lets a rabbit hide in a rabbithole, if it is currently standing on top of one.
     * The method checks for rabbithole underneath the rabbit before committing.
     *
     * @param world to access the world library.
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
     * A method that lets a rabbit come out of the rabbithole connected to its current hidingPlace.
     *
     * @param world to access the world library.
     */
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
    public void energize(int addEnergy) {
        super.energize(addEnergy);
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

    @Override
    public void aging(){super.aging();}

    /**
     * getCurrentHidingPlace() returns the rabbithole that this rabbit is currently hiding inside.
     */
    public RabbitHole getCurrentHidingPlace() {
        return currentHidingPlace;
    }

    @Override
    public void hungerDeath(World world, String death){super.hungerDeath(world, animal);}

    @Override
    public void deathByDamage(World world, String animal) {super.deathByDamage(world, animal);}

    @Override
    public void moveTowards(World world, Location target) {super.moveTowards(world, target);}

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
                    seeking = true;
                    moveTowards(world, current);
                    Location currentL = world.getLocation(this);
                    Object obj = world.getNonBlocking(currentL);
                    if (obj instanceof RabbitHole) {
                        hide(world);
                        seeking = false;
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