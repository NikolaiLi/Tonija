package wold_project;

import java.util.Random;

import itumulator.executable.Program;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import itumulator.world.Location;
import itumulator.world.World;

import world_project.Fungi;
import world_project.Carcass;

import static org.junit.jupiter.api.Assertions.*;

public class FungiTest {
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
    public void FungiDies(){
        Fungi fungi = new Fungi(false);
        Location l = new Location(0,0);
        world.setCurrentLocation(l);
        world.setTile(l, fungi);

        assertTrue(world.contains(fungi));

        for(int i = 0; i < 10; i++){
            p.simulate();
        }

        assertFalse(world.contains(fungi));
    }

    @Test
    public void FungiSpreadsSpores(){
        Fungi fungi = new Fungi(false);
        Carcass carcass = new Carcass(100, false);
        Location l1 = new Location(0,0);
        Location l2 = new Location(0,1);
        world.setCurrentLocation(l1);
        world.setTile(l1, fungi);
        world.setTile(l2, carcass);

        p.simulate();

        assertTrue(carcass.getIsInfected());
    }

    @Test
    public void FungiSpreadRadius(){
        Fungi fungi = new Fungi(false);
        Carcass carcass1 = new Carcass(100, false);
        Carcass carcass2 = new Carcass(100, false);
        Carcass carcass3 = new Carcass(100, false);
        Location l1 = new Location(0,0);
        Location l2 = new Location(0,1);
        Location l3 = new Location(0,2);
        Location l4 = new Location(0,3);
        world.setCurrentLocation(l1);
        world.setTile(l1, fungi);
        world.setTile(l2, carcass1);
        world.setTile(l3, carcass2);
        world.setTile(l4, carcass3);

        p.simulate();

        assertTrue(carcass1.getIsInfected());
        assertTrue(carcass2.getIsInfected());
        assertFalse(carcass3.getIsInfected());
    }
}