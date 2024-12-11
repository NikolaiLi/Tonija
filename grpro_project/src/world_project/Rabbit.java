package world_project;

import itumulator.world.World;
import itumulator.world.Location;
import java.util.*;

/**
 * The Rabbit class is an abstract class that inherits from the creature class.
 * Rabbits are herbivores that eat grass and seek nearby rabbitholes to hide in during nighttime.
 * All Rabbits share common methods like {@link #eat(World world)}, {@link #hide(World world)},
 * {@link #unhide(World world)} and {@link #seek(World world)}, which are documented here.
 */
public abstract class Rabbit extends Creature {
    protected Random r = new Random();
    protected boolean AlreadyBuiltRabbitHole = false;
    protected Location currentHidingPlace;
    protected boolean hiding;
    protected boolean seeking;

    /**
     * Initializes a rabbit object with several attributes that are of relevance to how the class works and
     * acts in the world simulation and interacts with other objects in the world.
     */
    public Rabbit() {
        super();
        animal = "Rabbit";
        energy = 100;
        maxEnergy = 100;
        hiding = false;
        currentHidingPlace = null;
        seeking = false;
    }

    /**
     * Provides the order of when individual methods should be executed inside the simulation.
     * @param world providing details of the position on which the actor is currently located and much more.
     */
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

                hide(world);
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
        if (alive && !hiding && world.isOnTile(this)) {
            Set<Location> neighbours = world.getEmptySurroundingTiles(world.getLocation(this));
            List<Location> list = new ArrayList<>(neighbours);

            if (!neighbours.isEmpty()) {
                int randomNumber = r.nextInt(list.size());
                Location l = list.get(randomNumber);
                world.move(this, l);
            }
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
        if(!hiding) {
            Object o = world.getNonBlocking(world.getLocation(this));
            if (o instanceof RabbitHole) {
                currentHidingPlace = world.getLocation(o);
                world.remove(this);
                hiding = true;
            }
        }
    }

    /**
     * A method that lets a rabbit come out of the rabbithole connected to its current hidingPlace.
     *
     * @param world to access the world library.
     */
    public void unhide(World world) {
        if (!world.isOnTile(this) && hiding) {
            ArrayList<Location> list = new ArrayList<>(world.getEmptySurroundingTiles(currentHidingPlace));

            if (!list.isEmpty()) {
                int size = r.nextInt(list.size());
                System.out.println("trying to unhide");
                world.setTile(list.get(size),this);
                System.out.println("unhiding");
                hiding = false;
            }
        }
    }

    /**
     * Method energizes the rabbit by amount given in parameter.
     * @param addEnergy amount of energy rabbit regains.
     */
    @Override
    public void energize(int addEnergy) {
        super.energize(addEnergy);
    }

    /**
     * Method starves rabbit, cosuming some of its energy.
     */
    @Override
    public void starve() {
        super.starve();
    }

    /**
     * Method returns the current amount of hunger/energy rabbit has.
     * @return amount of energy the rabbit posses currently.
     */
    @Override
    public int getEnergy() {
        return super.getEnergy();
    }

    /**
     * Method returns a boolean that answers whether rabbit is alive or not.
     * @return truth or false based on what the field alive is set to.
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
    public Location getCurrentHidingPlace() {
        return currentHidingPlace;
    }

    @Override
    public void hungerDeath(World world, String death){super.hungerDeath(world, animal);}

    @Override
    public void deathByDamage(World world, String animal) {super.deathByDamage(world, animal);}

    @Override
    public void moveTowards(World world, Location target) {super.moveTowards(world, target);}

    /**
     * A method used to scan the world tiles for a rabbithole, then moving the rabbit towards it.
     * If the location rabbit tries to move towards is blocked, the rabbit will simply stand still.
     * @param world
     */
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

    /**
     * A method used in the Rabbit Junit tests to return the field AlreadyBuiltRabbitHole.
     * @return returns AlreadyBuiltRabbitHole
     */
    public boolean hasBuiltRabbitHole(){ return AlreadyBuiltRabbitHole; }
}