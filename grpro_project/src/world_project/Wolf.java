package world_project;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.World;
import itumulator.world.Location;

import java.util.*;
import java.awt.*;
import java.util.List;

public class Wolf extends Creature implements DynamicDisplayInformationProvider {
    public DisplayInformation di_wolf = new DisplayInformation(Color.gray, "wolf");
    Random r = new Random();

    protected ArrayList<Wolf> wolfpack = new ArrayList<>();
    protected boolean isLeader;
    protected boolean hiding;
    protected Location wolfHoleLocation;
    protected int wolfPackID;
    private static int nextPackID = 0;

    public static Map<Integer, Location> wolfHoleLocations = new HashMap<>();


    //1st Constructor: Filereader Constructor used when new wolf is created from files in filereader
    //This wolf automatically creates a Wolfpack, and becomes the leader, then calls 2nd Constructor to create the rest of the pack.
    Wolf(int number, World world, Location initialSpawn) {
        super();
        animal = "WolfLeader";
        health = 80;
        maxHealth = health;
        energy = 125;
        maxEnergy = 125;
        ageOfDeath = 80;

        wolfpack.add(this);
        this.isLeader = true;
        wolfHoleLocation = null;
        wolfPackID = nextPackID;

        System.out.println("Created Packleader");

        //calls 2nd constructor calling the constructor used while in simulation
        for (int i = 0; i < number - 1; i++) {
            Wolf wolf = new Wolf(this, world, initialSpawn);
        }
    }

    //2nd Constructor: used when a new wolf is born by another wolf in the wolfhole while running the simulation.
    // Or when the wolfLeader calls it before simulation starts running to complete the rest of the wolfpack from filereader.
    Wolf(Wolf wolfmother, World world, Location initialSpawn) {
        super();
        animal = "Wolf";
        health = 80;
        maxHealth = health;
        energy = 125;
        maxEnergy = 125;
        ageOfDeath = 80;

        wolfpack = wolfmother.wolfpack;
        wolfpack.add(this);
        isLeader = false;
        wolfHoleLocation = wolfmother.getWolfHoleLocation();
        wolfPackID = wolfmother.wolfPackID;

        System.out.println("Created wolf in wolfpack");

        //if wolf has been born during the simulation, parent gave birth while hiding in wolfhole, and the new wolf will start out in hiding as well
        if (wolfmother.hiding) {
            hiding = true;
        }

        //if wolf was created straight from filereader, the wolf will be placed next to its wolfLeader
        //This method is only used when a wolf is created by calling this constructor while using the 1st constructor.
        else {
            world.setCurrentLocation(initialSpawn);
            ArrayList<Location> neighbours = new ArrayList<>(world.getEmptySurroundingTiles());
            if (!neighbours.isEmpty()) {
                Location l = neighbours.get(r.nextInt(neighbours.size()));
                world.setTile(l, this);
            }
        }
    }


//ABSTRACT METHODS

    @Override
    public void act(World world) {

        //tjekker om wolf er død af hunger, age, eller damage
        hungerDeath(world, animal);

        deathByDamage(world, animal);

        dyingOfAge(world, ageOfDeath, animal);

        while (alive) {

            //only happens during the day
            if (world.isDay()) {

                //comes out of hiding
                unhide(world);

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

            }

            aging();

            eat(world);

            return;
        }
    }



    @Override
    public DisplayInformation getInformation() {
        return di_wolf;
    }

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
                }
            }
        }
    }


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


    @Override
    public void deathByDamage(World world, String animal) {
        if (alive && health <= 0) {
            alive = false;
            if (isLeader) {
                wolfpack.remove(this);
                wolfpack.getFirst().isLeader = true;
            }
            world.delete(this);
            System.out.println(animal + " has bled out and died");
        }
    }

    @Override
    public void hungerDeath(World world, String animal) {
        if (energy <= 0 && alive) {
            alive = false;
            if (isLeader) {
                wolfpack.remove(this);
                wolfpack.getFirst().isLeader = true;
            }
            world.delete(this);
            System.out.println(animal + " has died of hunger");
        }
    }

    @Override
    public void dyingOfAge(World world, int ageOfDeath, String animal) {
        if (isAlive()) {
            int chanceOfDying = r.nextInt(10);
            if (age > ageOfDeath && chanceOfDying == 1) {
                if (isLeader) {
                    wolfpack.remove(this);
                    wolfpack.getFirst().isLeader = true;
                }
                world.delete(this);
                alive = false;
                System.out.println(animal + " has died of age");
            }
        }
    }


//Movement methods

    //method has 2 paths, depending on if wolf is leader or not.
    //Path 1: if Leader, hunts for rabbits in 2 tile radius, else moves randomly
    //Path 2: if !Leader, follows the wolf in wolfpack array index ahead of itself (i.e. wolf in index 1 follows index 0, the wolfLeader)
    public void hunt(World world) {
        //path 1
        if (isLeader && !hiding) {
            Location currentLocation = world.getLocation(this);
            Queue<Location> toVisit = new LinkedList<>(world.getSurroundingTiles(2));
            Set<Location> visited = new HashSet<>();
            toVisit.add(currentLocation);

            while (!toVisit.isEmpty()) {
                Location current = toVisit.poll();
                visited.add(current);

                if (world.getTile(current) instanceof Rabbit) {
                    System.out.println("wolfLeader follows rabbit at: " + current);
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
            if (world.isOnTile(target)) {
                Location l = world.getLocation(target);
                moveTowards(world, l);
                return;
            }
            move(world);
        }
    }



    //Wolf seeks the WolfHole registered to the wolfpack.
    //if no wolfhole is currently registered to wolfpack, method does nothing.
    public void seekWolfHole(World world) {
        if (!hiding && wolfHoleLocations.containsKey(wolfPackID)) {
            moveTowards(world, wolfHoleLocations.get(wolfPackID));
        } else if (!hiding) {
            move(world);
        }
    }


//OTHER BEHAVIOUR Methods

    //attacks a random neighbouring enemy creature, as long as they are not part of the wolf's wolfpack
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





    //digs a WolfHole. Only 1 WolfHole is possible per wolfpack.
    //if wolfTerritories already contains a wolfhole for the wolf's pack, returns method
    public void digWolfHole(World world) {
        if (isLeader && !hiding && (!wolfHoleLocations.containsKey(wolfPackID))) {
            if ((r.nextInt(100)) < 10) {
                Location current = world.getLocation(this);
                if (!world.containsNonBlocking(current)) {
                    WolfHole wolfhole = new WolfHole();
                    world.setTile(current, wolfhole);
                    wolfHoleLocations.put(wolfPackID, current);
                    System.out.println("WolfLeader has dug a hole at " + current);
                }
            }
        }
    }



    //Wolf hides, if not currently
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

    @Override
    public void moveTowards(World world, Location target) {
        super.moveTowards(world, target);
    }

// GET-Methods
    public Location getWolfHoleLocation() {
        return wolfHoleLocation;
    }
}