package world_project;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.World;
import itumulator.world.Location;

import java.util.*;
import java.awt.*;
import java.util.List;

/**
 * The Wolf class is a carnivorous subclass inherited from the abstract creature class and an object in the world.
 * Wolf implements interfaces actor and DynamicDisplayInformationProvider, to enact its methods
 * and ensure correct display of wolf at all times during the world simulation.
 * Wolves are organized into packs at birth, either organizing their own pack as its leader,
 * or being assigned the pack of its parent.
 * a Wolf Leader will lead its pack, and will hunt for prey/enemies in the vicinity.
 * a Wolf Leader will try to provide shelter by digging a hole, which the wolf pack will search for
 * When a wolf Leader dies, then next eldest wolf in the pack will become the new Leader.
 */
public class Wolf extends Creature implements DynamicDisplayInformationProvider {
    public DisplayInformation di_wolf = new DisplayInformation(Color.gray, "wolf");
    public DisplayInformation di_wolf_sleeping = new DisplayInformation(Color.gray, "wolf-sleeping");
    public DisplayInformation currentDisplayInformation = di_wolf;

    Random r = new Random();

    protected ArrayList<Wolf> wolfpack = new ArrayList<>();
    protected boolean isLeader;
    protected boolean hiding;
    protected Location wolfHoleLocation;
    protected int wolfPackID;
    private static int nextPackID = 0;

    public static Map<Integer, Location> wolfHoleLocations = new HashMap<>();

    /**
     * 1st Constructor: FileReader Constructor used when new wolf is initialized from input files used in the fileReader.
     * Initializing a wolf with this constructor will make it create its own Wolfpack, become leader,
     * then call the 2nd Constructor to create the rest of the pack.
     * Every wolfpack is identified with an int ID, which gets incremented after usage marking a wolfpack.
     * WolfHoleLocation is initialized as null, as a WolfHole will only become assigned to a wolfpack once the WolfLeader digs it.
     *
     * @param number of wolves the fileReader expects to create for the specific pack.
     * @param world to access the World library.
     * @param initialSpawn location onto which the wolf will be spawned. Relevant for the 2nd constructor which this
     * constructor calls, when fileReader expects more than 1 wolf in the pack, and that wolf needs to stand next to its leader.
     */
    public Wolf(int number, World world, Location initialSpawn) {
        super();
        animal = "WolfLeader";
        health = 80;
        maxHealth = health;
        energy = 125;
        maxEnergy = 125;
        ageOfDeath = 80;

        wolfPackID = nextPackID++;
        wolfpack.add(this);
        this.isLeader = true;
        wolfHoleLocation = null;
        wolfPackID = nextPackID;


        //calls 2nd constructor calling the constructor used while in simulation
        for (int i = 0; i < number - 1; i++) {
            Wolf wolf = new Wolf(this, world, initialSpawn);
        }
    }

    /**
     * 2nd Constructor: RealTime-Constructor used when new wolf is initialized by a WolfLeader as a member of the pack, either while
     * constructing the WolfLeader with 1st constructor, or during the simulation when
     * breeding with another wolf in the wolfpack's assigned wolfhole.
     * Wolves instantiated from this constructor serve as followers of their WolfLeader.
     * Followers will adopt WolfLeader's wolfpackID and WolfHoleLocation, and thus join the wolfpack.
     * Depending on whether follower is initialized during simulationstep or not, follower will either
     * be placed next to its leader, or simply be hiding in the wolfhole until morning.
     *
     * @param wolfLeader reference Wolf that the initialized wolf uses to join the wolfpack.
     * @param world to access the World library.
     * @param initialSpawn location onto which the wolf-Leader will be spawned. The wolf Follower will spawn
     * onto an empty neighbouring tile.
     */

    Wolf(Wolf wolfLeader, World world, Location initialSpawn) {
        super();
        animal = "Wolf";
        health = 80;
        maxHealth = health;
        energy = 125;
        maxEnergy = 125;
        ageOfDeath = 80;

        wolfpack = wolfLeader.wolfpack;
        wolfpack.add(this);
        isLeader = false;
        wolfHoleLocation = wolfLeader.getWolfHoleLocation();
        wolfPackID = wolfLeader.wolfPackID;


        //if wolf has been born during the simulation, parent gave birth while hiding in wolfhole, and the new wolf will start out in hiding as well
        if (wolfLeader.hiding) {
            world.add(this);
            hiding = true;
        }

        //if wolf was created straight from filereader, and wolfleader is on the map, the wolf will be placed next to its wolfLeader
        //This method is only called when a wolf is created by calling this constructor during usage of the 1st constructor.
        else if (world.contains(wolfLeader) && world.isOnTile(wolfLeader)){
            world.setCurrentLocation(initialSpawn);
            ArrayList<Location> neighbours = new ArrayList<>(world.getEmptySurroundingTiles());
            if (!neighbours.isEmpty()) {
                Location l = neighbours.get(r.nextInt(neighbours.size()));
                world.setTile(l, this);
            }
        }
    }


//ABSTRACT METHODS
    /**
     * Provides the order of when individual methods should be executed inside the simulation.
     * @param world providing details of the position on which the actor is currently located and much more.
     */
    @Override
    public void act(World world) {

        //opdaterer displayInformation når dyret skal sove
        changeCurrentDisplay(world);

        //tjekker om wolf er død af hunger, age, eller damage
        hungerDeath(world, animal);

        deathByDamage(world, animal);

        dyingOfAge(world, ageOfDeath, animal);

        while (alive) {

            //only happens during the day
            if (world.isDay()) {

                //comes out of hiding
                unhide(world);

                //eats a surrounding piece of meat
                eat(world);

                //wolfLeader hunts for food, while pack follows
                hunt(world);

                //wolf attacks neighbouring creature that is not a friendly wolf
                attack(world);
            }


            if (world.isNight()) {

                //WolfLeader will try to dig wolfHole, if none has been created yet
                digWolfHole(world);

                //seeks wolfhole, if one has been created
                seekWolfHole(world);

                //wolf will try to hide, if currently standing on top of its wolfHole
                hide(world);

                //if WolfLeader is currently hiding with another wolf, they will try to breed another wolf
                breed(world);

            }

            //starves wolf
            starve();

            //ages wolf
            aging();

            return;
        }
    }


    /**
     * Provides the visual display of the wolf, whether if its sleeping or not.
     * @return DisplayInformation for the simulation to display.
     */
    @Override
    public DisplayInformation getInformation() {
        return currentDisplayInformation;
    }

    /**
     * The Wolf eats other animals by checking its surrounding tiles for instances of other objects.
     * In the case of an instance of a nearby carcass to eat, it will then energize with a given amount of
     * energy.
     * @param world to access the World library.
     */
    @Override
    public void eat(World world) {
        if (!hiding) {
            Set<Location> neighbourTiles = world.getSurroundingTiles();
            List<Location> locations = new ArrayList<>(neighbourTiles);
            Location wolf_location = world.getLocation(this);

            for (Location location : locations) {
                if(location.equals(wolf_location)) {
                    continue;
                }

                if (world.getTile(location) instanceof Carcass) {
                    Object objectCarcass = world.getTile(location);
                    Carcass carcass = (Carcass) objectCarcass;
                    carcass.gettingEaten(35);
                    energize(35);
                    return;
                }
            }
        }
    }

    /**
     * The wolf moves by checking surrounding tiles for a free position to move towards.
     * @param world to access the World library.
     */
    //moves wolf to random free tile
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
     * Handles the death of an animal when its health reaches zero.
     * Replaces the animal with a carcass.
     * @param world the world where the animal is
     * @param animal the type of the animal
     */
    @Override
    public void deathByDamage(World world, String animal) {
        if (alive && health <= 0) {
            alive = false;
            if (isLeader) {
                wolfpack.remove(this);
                if (!wolfpack.isEmpty()) {
                    wolfpack.getFirst().isLeader = true;
                }
            }

            dropCarcass(world);
        }
    }

    /**
     * Handles the death of an animal by starvation.
     * Replaces the animal with a carcass.
     * @param world the world where the animal is
     * @param animal the type of the animal
     */
    @Override
    public void hungerDeath(World world, String animal) {
        if (energy <= 0 && alive) {
            alive = false;
            if (isLeader) {
                wolfpack.remove(this);
                if (!wolfpack.isEmpty()) {
                    wolfpack.getFirst().isLeader = true;
                }
            }

            dropCarcass(world);
        }
    }

    /**
     * Handles the death of an animal by old age.
     * Replaces the animal with a carcass if it surpasses the age of death
     * and have a random chance condition, that makes the animal die.
     * @param world the world where the animal is
     * @param ageOfDeath the age after which the animal may die of old age
     * @param animal the type of the animal
     */
    @Override
    public void dyingOfAge(World world, int ageOfDeath, String animal) {
        if (isAlive()) {
            int chanceOfDying = r.nextInt(10);
            if (age > ageOfDeath && chanceOfDying == 1) {
                if (isLeader) {
                    wolfpack.remove(this);
                    if (!wolfpack.isEmpty()) {
                        wolfpack.getFirst().isLeader = true;
                    }
                }

                alive = false;
                dropCarcass(world);
            }
        }
    }


    /**
     * Method for moving towards a given location.
     * @param world to access the world library.
     * @param target a Location type object that specifies where to move towards.
     */
    @Override
    public void moveTowards(World world, Location target) {super.moveTowards(world, target);}

    //method has 2 paths, depending on if wolf is leader or not.
    //Path 1: if Leader, hunts for rabbits in 2 tile radius, else moves randomly
    //Path 2: if !Leader, follows the wolf in wolfpack array index ahead of itself (i.e. wolf in index 1 follows index 0, the wolfLeader)
    public void hunt(World world) {
        //path 1
        if (isLeader && !hiding) {
            Location currentLocation = world.getLocation(this);
            world.setCurrentLocation(currentLocation);
            Queue<Location> toVisit = new LinkedList<>(world.getSurroundingTiles(2));
            Set<Location> visited = new HashSet<>();
            toVisit.add(currentLocation);

            while (!toVisit.isEmpty()) {
                Location current = toVisit.poll();
                visited.add(current);

                if (world.getTile(current) instanceof Rabbit) {
                    moveTowards(world, current);
                    return;
                }
            }

            //if no rabbit is found, wolfLeader moves aimlessly
            move(world);
            return;
        }

        //path 2
        if (!isLeader && !hiding) {
            int ownIndex = wolfpack.indexOf(this);
            Wolf target = wolfpack.get(ownIndex-1);
            if (world.contains(target)) {
                if (world.isOnTile(target)) {
                    Location l = world.getLocation(target);
                    moveTowards(world, l);
                    return;
                }
            }
            move(world);
        }
    }



    /**
     * Wolf will seek the wolfhole registrered to it's wolfpack.
     *
     * @param world to access the world library.
     */
    public void seekWolfHole(World world) {
        if (!hiding && wolfHoleLocations.containsKey(wolfPackID)) {
            moveTowards(world, wolfHoleLocations.get(wolfPackID));
        }
    }


    /**
     * attacks a random neighbouring enemy creature, as long as they are not part of the wolf's wolfpack
     *
     * @param world to access the world library.
     */
    @Override
    public void attack(World world) {
        Set<Location> neighbourTiles = world.getSurroundingTiles();
        List<Location> neighbourLocations = new ArrayList<>(neighbourTiles);
        List<Location> targetLocations = new ArrayList<>();

        for (Location location : neighbourLocations) {
            Object tile = world.getTile(location);
            if (tile instanceof Creature enemy && !wolfpack.contains(enemy)) {
                targetLocations.add(location);
            }
        }

        if (!targetLocations.isEmpty()) {
            int randomIndex = r.nextInt(targetLocations.size());
            Location chosenLocation = targetLocations.get(randomIndex);
            Object targetEnemy = world.getTile(chosenLocation);

            if (targetEnemy instanceof Creature creatureTargetEnemy && creatureTargetEnemy != this) {
                creatureTargetEnemy.takeDamage(35);
            }
        }
    }


    /**
     * If WolfLeader and another follower is hiding in their wolfHole, wolfleader tries to breed a new Wolf, unless the wolfpack contains 4 or more wolves already.
     *
     * @param world to access the world library.
     */
    public void breed(World world) {
        if (isLeader && hiding && (wolfpack.size() <= 5) && (r.nextInt(100) <= 10)) {

            ArrayList<Wolf> tempWolfPack = new ArrayList<>(wolfpack);
            int wolfcounter = wolfpack.size();

            for (Wolf wolf : tempWolfPack) {
                if (wolfcounter >= 4) {
                    break;
                }
                if (wolf.equals(this)) {
                    continue;
                }
                if (wolf.hiding) {
                    Wolf wolfcub = new Wolf(this, world, this.wolfHoleLocation);
                    wolfcounter ++;
                }
            }
        }
    }




    /**
     * digs a WolfHole. Only 1 WolfHole is attainable per wolfpack.
     *
     * @param world to access the world library.
     */
    public void digWolfHole(World world) {

        if (isLeader && !hiding && (!wolfHoleLocations.containsKey(wolfPackID))) {
            if ((r.nextInt(100)) <= 10) {
                Location current = world.getLocation(this);
                if (!world.containsNonBlocking(current)) {
                    WolfHole wolfhole = new WolfHole();
                    world.setTile(current, wolfhole);
                    wolfHoleLocations.put(wolfPackID, current);
                }
            }
        }
    }



    /**
     * A method that lets a wolf hide in the wolfhole assigned to the wolfpack, if it is currently standing on top of it.
     * The method checks for a wolfhole underneath the wolf, then checks if it's the same
     *  as the one registered for the wolfpack before committing.
     *
     * @param world to access the world library.
     */
    public void hide(World world) {
        if(world.isOnTile(this) && !hiding && wolfHoleLocations.containsKey(wolfPackID)) {
            Object o = world.getNonBlocking(world.getLocation(this));
            Location home = wolfHoleLocations.get(wolfPackID);
            Location currentLocation = world.getLocation(this);

            if (o instanceof WolfHole wolfhole && home.equals(currentLocation)) {
                world.remove(this);
                hiding = true;
            }
        }
    }


    /**
     * A method that lets a wolf come out of hiding if it is currently daytime.
     * The method checks if the wolf is currently hiding, then places it on the map.
     * If the hole is blocked, the wolf will be placed next to the whole on an empty tile.
     *  as the one registered for the wolfpack before committing.
     * The wolf's field hiding will be turned false.
     *
     * @param world to access the world library.
     */
    public void unhide(World world) {
        if (!world.isOnTile(this) && hiding) {
            Location hidingSpot = wolfHoleLocations.get(wolfPackID);
            world.setCurrentLocation(hidingSpot);
            ArrayList<Location> list = new ArrayList<>(world.getEmptySurroundingTiles());

            if (!list.isEmpty()) {
                int size = r.nextInt(list.size());
                world.setTile(list.get(size), this);
                hiding = false;
            }
        }
    }



// GET-Methods

    /**
     * Used for constructing followers in the leaders wolfpack.
     *
     * @return the location of the wolf-instance's wolfHoleLocation field.
     */
    public Location getWolfHoleLocation() {
        return wolfHoleLocation;
    }

    /**
     * Used for unit testing the wolf object.
     *
     * @return the wolfpack of the wolf-Instance's wolfpack field.
     */
    public ArrayList<Wolf> getWolfPack() {
        return wolfpack;
    }



//PRIVATE METHODS
    /**
     * Changes the display according to the state of the day.
     * @param world to access the World library
     */
    private void changeCurrentDisplay (World world) {
        if (world.isDay()) {
            currentDisplayInformation = di_wolf;
        } else if (world.isNight() && !hiding && !wolfHoleLocations.containsKey(wolfPackID)) {
            currentDisplayInformation = di_wolf_sleeping;
        }
    }

}