package wold_project;

import java.util.Random;
import java.util.*;

import itumulator.executable.Program;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import itumulator.world.Location;
import itumulator.world.World;

import world_project.AdultRabbit;
import world_project.BabyRabbit;
import world_project.Bear;
import world_project.Wolf;

import static org.junit.jupiter.api.Assertions.*;

public class WolfTest {
    World world;
    Program p;
    int size = 15;
    Random r = new Random();

    @BeforeEach
    public void setUp(){
        p = new Program(size,600,150);
        world = p.getWorld();
    }

    @Test
    public void wolfAttacksSurroundingEnemies() {
        Location l = new Location(2,2);
        Wolf leader = new Wolf(2, world, l);
        Wolf companion = leader.getWolfPack().get(1);

        //sets a wolfleader on Tile with a companion wolf next to it
        world.setCurrentLocation(l);
        ArrayList<Location> neighbours = new ArrayList<>(world.getEmptySurroundingTiles());
        Location neighbour = neighbours.getFirst();
        world.setTile(l, leader);
        world.setTile(neighbour, companion);

        //set a bear next to the Leader wolf
        world.setCurrentLocation(l);
        neighbours = new ArrayList<>(world.getEmptySurroundingTiles());
        neighbour = neighbours.getFirst();
        Bear bear = new Bear();
        world.setTile(neighbour, bear);

        //runs attack method, attacking bear and disregarding companion wolf
        leader.attack(world);


        //wolf attacks for 35 damage, so bear should be at 165 health, and companion still at full health.
        assertEquals(165, bear.getHealth());
        assertEquals(80, companion.getHealth());
    }

    @Test //note: wolf has a radius of 2 tiles within which it detects prey it will hunt
    public void WolfLeaderHunts() {
        //place wolf in world
        Location wolfLocation = new Location(2,2);
        Wolf leader = new Wolf(2, world, wolfLocation);
        world.setTile(wolfLocation, leader);

        //place rabbit in world 3 tiles away
        BabyRabbit prey = new BabyRabbit();
        Location rabbitLocation = new Location(2,4);
        world.setTile(rabbitLocation, prey);

        leader.hunt(world);//wolf moves to 2,3
        Location wolfMovedOnce = world.getLocation(leader);
        Location expectedLocation = new Location(2,3);
        assertEquals(expectedLocation, wolfMovedOnce);

        leader.hunt(world);//wolf should stay put, as it is now standing right next to the rabbit and has no reason to move
        Location currentWolfLocation = world.getLocation(leader);
        assertEquals(expectedLocation, currentWolfLocation);

        //This also means that wolf's current Location should be one of rabbits neighbouring tiles
        world.setCurrentLocation(rabbitLocation);
        ArrayList<Location> neighbours = new ArrayList<>(world.getSurroundingTiles());

        Location wolfIsStandingNextToRabbit = null;
        for (Location l : neighbours) {
            if (l.equals(currentWolfLocation)) {
                wolfIsStandingNextToRabbit = l;
            }
        }

        assertEquals(currentWolfLocation, wolfIsStandingNextToRabbit);

        //Rabbit moves around for 3 steps, with wolf following it
        prey.move(world);
        leader.hunt(world);
        prey.move(world);
        leader.hunt(world);
        prey.move(world);
        leader.hunt(world);

        //refresh wolf and rabbit current location
        currentWolfLocation = world.getLocation(leader);

        Location currentRabbitLocation = world.getLocation(prey);
        world.setCurrentLocation(currentRabbitLocation);
        neighbours = new ArrayList<>(world.getSurroundingTiles());

        Location WolfIsStillStandingNextToRabbit = null;
        for (Location l : neighbours) {
            if (l.equals(currentWolfLocation)) {
                WolfIsStillStandingNextToRabbit = l;
            }
        }

        System.out.println("currentWolfLocation: " + currentWolfLocation);
        System.out.println("WolfIsStillStandingNextToRabbit: " + WolfIsStillStandingNextToRabbit);

        assertEquals(currentWolfLocation, WolfIsStillStandingNextToRabbit);
    }


    @Test
    public void WolfHoleDiggingRates() {//run 100 simulations and check a counter every time a hole is dug +/-5% accuracy
        for(int i = 0; i <= 100; i++) {

        }
    }
}