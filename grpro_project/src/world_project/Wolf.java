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

    protected ArrayList<Wolf> wolfpack;
    protected boolean isAlpha;
    protected boolean hiding;
    protected boolean AlreadyDugWolfhole = false;
    protected Map<ArrayList<Wolf>, Location> wolfHoleLocations = new HashMap<ArrayList<Wolf>, Location>();
    public Map<ArrayList<Wolf>, Set<Location>> wolfTerritories = new HashMap<>();

    //Constructor used when new wolf is created from files in filereader
    Wolf(int number, World world, Location initialSpawn) {
        super();
        health = 80;
        energy = 125;
        maxEnergy = 125;
        this.wolfpack = new ArrayList<>();
        wolfpack.add(this);
        this.isAlpha = true;
        System.out.println("Created Packleader");
        //creates the rest of the pack as children of the alphaWolf by calling the constructor used while in simulation
        for (int i = 0; i < number - 1; i++) {
            Wolf wolf = new Wolf(this, world, initialSpawn);
        }
    }

    //Constructor used when new wolf is created from a wolf in the simulation.
    Wolf(Wolf wolfmother, World world, Location initialSpawn) {
        super();
        health = 80;
        energy = 125;
        this.wolfpack = wolfmother.getWolfpack();
        wolfpack.add(this);
        this.isAlpha = false;

        System.out.println("Created wolf in wolfpack");

        //placing wolf in the wolfHole where breeding took place. If hole is blocked, temporarily removes object to spawn wolf in hole.
        if (wolfmother.hiding) {
            if (!world.isOnTile(wolfmother)) {
                Location l = world.getLocation(wolfmother);
                world.setCurrentLocation(l);
                if (world.isTileEmpty(l)) {
                    world.setTile(l, this);
                } else {
                    Object o = world.getTile(l);
                    world.remove(o);
                    world.setTile(l, this);
                    this.hide(world);
                    world.setTile(l, o);
                }
            }
        } else {//placing wolf next to alphawolf (only used when creating wolfpack from files in filereader
            world.setCurrentLocation(initialSpawn);
            ArrayList<Location> neighbours = new ArrayList<>(world.getEmptySurroundingTiles());
            if (!neighbours.isEmpty()) {
                Location l = neighbours.get(r.nextInt(neighbours.size()));
                world.setTile(l, this);
            } else {
                System.out.println("No empty tile to spawn wolf");
            }

        }
    }


    public void act(World world) {
        //outside the act loop
        if (!alive) {
            death(world);
        }

        while (alive) {// Tjekker hvis world_project.Wolf er død, hvis den er, skal act stoppes.
            if (!hiding) {
                setAlphaTerritory(world);
            }

            // Tjekker om wolf er ved at dø af sult
            if (getEnergy() <= 0) {
                hungerDeath(world);
                return;
            }

            if (world.isDay()) {
                move(world);

                // unhider
                unhide(world);

                // Flytter wolf mod en rabbit, hvis det er en alpha
                seekRabbit(world);

                //Flytter wolf mod sit territorie, hvis den ikke er alpha
                seekTerritory(world);

                //Attacks enemy in neighbouring Tile
                attack(world);

                // Sulter wolf
                starve();

                //AlfaWolf prøver at grave et hul
                digWolfHole(world);
            }

            if (world.isNight()) {
                seekWolfHole(world);
            }
            return;
        }
    }


    @Override
    public DisplayInformation getInformation() {
        return di_wolf;
    }

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
    public void eat(World world) {
        energize();
        System.out.println("Wolf has eaten its prey");
    }

    public void attack(World world) {
        if (!hiding) {
            System.out.println("wolf current position: " + world.getLocation(this));
            //Attack kører i 2 dele. hvis Wolf er i sit territorie, og der er et fjendtligt creature, angribes dette. Ellers angribes en tilfældig naboUlv eller Rabbit, i rækkefølge efter trussel-niveau.
            //tjekker først, om wolf allerede er i territoriet, og at der er en enenmy i territoriet den kan angribe (vælger random enemy fra listen af territory)
            if (wolfTerritories.get(wolfpack).contains(world.getLocation(this)) && !(isAlpha)) {
                ArrayList<Location> territory = new ArrayList<>(wolfTerritories.get(wolfpack));
                for (Location l : territory) {
                    Object o = world.getTile(l);

                    //Springer over hvis object == null eller object er en del af wolfpack
                    if (!(o instanceof Creature) || (o instanceof Wolf ally && wolfpack.contains(ally))) {
                        continue;
                    }

                    //angriber kun, hvis der er en enemy i territoriet, som ikke er en wolf fra samme wolfpack
                    Creature enemy = (Creature) o;
                    enemy.takeDamage(50);
                    System.out.println("Wolf hits enemy " + enemy + " for 50 damage");
                    if (enemy.health <= 0) {
                        System.out.println("Wolf has slain enemy in territory!");
                        eat(world);
                        return;
                    }
                }
            }

            //Angriber en tilfældig fjendtlig naboulv
            Set<Location> neighbourTiles = world.getSurroundingTiles();
            List<Location> locations = new ArrayList<>(neighbourTiles);
            for (Location l : locations) {
                Object o = world.getTile(l);

                // Skipper null objects
                if (o == null) {
                    continue;
                }

                if (o instanceof Wolf enemy) {
                    if (!wolfpack.contains(enemy)) {
                        enemy.takeDamage(50);
                        System.out.println("Wolf hits enemy wolf for 50 damage");
                        if (enemy.getHealth(world) <= 0) {
                            System.out.println("Wolf has slain Enemy wolf");
                            eat(world);
                            return;
                        }
                    }
                }

                //angriber en tilfældig nabokanin
                if (o instanceof Rabbit) {
                    Rabbit prey = (Rabbit) o;
                    prey.takeDamage(50);
                    if (prey.getHealth(world) <= 0) {
                        System.out.println("Wolf has slain Rabbit Prey");
                        eat(world);
                        return;
                    }
                }
            }
        }
    }


    public ArrayList<Wolf> getWolfpack() {
        return wolfpack;
    }

    public void seekRabbit(World world) {
        if (isAlpha) {
            if (world.isOnTile(this)) {
                Location currentLocation = world.getLocation(this);
                Queue<Location> toVisit = new LinkedList<>(wolfTerritories.get(wolfpack));
                Set<Location> visited = new HashSet<>();
                toVisit.add(currentLocation);

                while (!toVisit.isEmpty()) {
                    Location current = toVisit.poll();
                    visited.add(current);

                    if (world.getTile(current) instanceof Rabbit) {
                        System.out.println("Hunting Rabbit at: " + current);
                        moveTowards(world, current);
                        setAlphaTerritory(world);//opdaterer wolfpacks territorie
                        Location currentL = world.getLocation(this);
                        Object obj = world.getTile(currentL);
                        if (obj instanceof Rabbit) {
                            System.out.println("Wolf at " + currentLocation + "has approached Rabbit at: " + currentL);
                        }
                        return;
                    }
                }

                System.out.println("No Prey found, wolf wanders aimlessly");
                move(world);
            }
        }
    }

    public void seekTerritory(World world) {
        if (!isAlpha) {
            if (!wolfTerritories.containsKey(wolfpack)) {
                System.out.println("No territory found for wolfpack. Registering territory...");
                setAlphaTerritory(world); // register territory for this wolfpack
                wolfTerritories.put(wolfpack, new HashSet<>());
            } else {
                setAlphaTerritory(world);
            }

            if (!wolfTerritories.get(wolfpack).contains(world.getLocation(this))) {//checks if wolfs current tile is part of the territory of its wolfpack before running method.
                Location currentLocation = world.getLocation(this);
                Queue<Location> toVisit = new LinkedList<>();
                Set<Location> visited = new HashSet<>();
                toVisit.add(currentLocation);
                Set<Location> territory = wolfTerritories.get(wolfpack);


                while (!toVisit.isEmpty()) {
                    Location current = toVisit.poll();
                    visited.add(current);


                    if (territory.contains(current)) {
                        System.out.println("Seeking territory near: " + current);
                        moveTowards(world, current);
                        return;
                    }

                    for (Location neighbor : world.getSurroundingTiles(current)) {
                        if (!visited.contains(neighbor) && !toVisit.contains(neighbor)) {
                            toVisit.add(neighbor);
                        }
                    }
                }
                System.out.println("No Territory found");
            }
        }
    }


    public void seekWolfHole(World world) {
        if (!(wolfHoleLocations.get(wolfpack) == null)) {
            Location wolfHoleLocation = wolfHoleLocations.get(wolfpack);
            this.moveTowards(world, wolfHoleLocation);

            Location currentL = world.getLocation(this);
            Object obj = world.getNonBlocking(currentL);
            if (obj instanceof WolfHole) {
                System.out.println("Wolf is trying to hide");
                hide(world);
            }
        }
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
        } else {
            System.out.println("Path blocked!");
        }
    }


    public void digWolfHole(World world) {
        if (isAlpha && !AlreadyDugWolfhole) {
            System.out.println("wolf tries to dig hole");
            if (!(wolfHoleLocations.containsKey(wolfpack)) && !hiding && isAlive() && (r.nextInt(100)) < 10) {
                Location current = world.getLocation(this);
                if (!world.containsNonBlocking(current)) {
                    WolfHole wolfhole = new WolfHole();
                    world.setTile(current, wolfhole);
                    wolfHoleLocations.put(wolfpack, current);
                    AlreadyDugWolfhole = true;
                    System.out.println("WolfLeader has dug a hole at " + current);
                } else {
                    System.out.println("Cannot dig Wolfhole. Non-blocking element already present on tile");
                }
            } else {
                System.out.println("Wolf already has a hole");
            }
        }
    }

    public void hide(World world) {
        Object o = world.getNonBlocking(world.getLocation(this));
        if (o instanceof WolfHole) {
            world.remove(this);
            hiding = true;
            System.out.println("Wolf is hiding");
        } else {
            System.out.println("Didn't find a hiding spot");
        }
    }

    public void unhide(World world) {
        if (!world.isOnTile(this) && hiding) {
            Location hidingSpot = wolfHoleLocations.get(wolfpack);
            world.setCurrentLocation(hidingSpot);
            ArrayList<Location> list = new ArrayList<>(world.getEmptySurroundingTiles());

            if (!list.isEmpty()) {
                int size = r.nextInt(list.size());
                world.setTile(list.get(size), this);
                hiding = false;
            } else {
                System.out.println("No exit available");
            }
        }
    }

    private void setAlphaTerritory(World world) {
        if (!wolfpack.isEmpty() && world.isOnTile(wolfpack.getFirst())) {
            Location l = world.getLocation(wolfpack.getFirst());
            HashSet<Location> territory = new HashSet<>();
            world.setCurrentLocation(l);
            territory = new HashSet<>(world.getSurroundingTiles(2));
            wolfTerritories.put(wolfpack, territory);
        }
    }


    private void hungerDeath(World world) {
        alive = false;
        world.delete(this);
        System.out.println("A wolf has died of hunger");
        wolfpack.remove(this);
        if (!wolfpack.isEmpty()) {
            wolfpack.getFirst().isAlpha = true;
        } else {
            System.out.println("last wolf in wolfpack has died, their legacy will not be forgotten");
        }
    }


    public void death(World world) {
        if (health <= 0) {
            world.delete(this);
            System.out.println("Wolf has no health left and has died");
            wolfpack.remove(this);
            if (!wolfpack.isEmpty()) {
                wolfpack.getFirst().isAlpha = true;
            } else {
                System.out.println("last wolf in wolfpack has died, their legacy will not be forgotten");
            }
        }
    }
}