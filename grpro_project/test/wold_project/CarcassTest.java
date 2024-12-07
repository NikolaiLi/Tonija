package wold_project;

import java.util.Random;
import java.util.Set;

import itumulator.executable.Program;
import itumulator.simulator.Simulator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import itumulator.world.Location;
import itumulator.world.World;

import world_project.Bear;
import world_project.Carcass;
import world_project.Fungi;

import static org.junit.jupiter.api.Assertions.*;

public class CarcassTest {
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
    public void CarcassDecomposes(){
        Carcass carcass = new Carcass(100,false);
        int duration = carcass.getDuration();

        carcass.act(world);

        int new_duration = carcass.getDuration();
        assertNotNull(carcass);
        assertNotEquals(duration, new_duration,"Old duration: " + duration + "\nNew Duration: " + new_duration);
    }

    @Test
    public void CarcassGetsInfected(){
        Carcass carcass  = new Carcass(150,false);
        Fungi fungi = new Fungi(carcass.getIsBig());
        Location l1 = new Location(0,0);
        Location l2 = new Location(0,1);
        world.setCurrentLocation(l1);
        world.setTile(l1,carcass);
        world.setTile(l2,fungi);

        for(int i = 0; i < 10; i++){
            p.simulate();
        }

        assertTrue(carcass.getIsInfected(), "the value was: " + carcass.getIsInfected() +"\n"
                + carcass.getDuration());
    }

    @Test
    public void CarcassGetsEaten(){
        Carcass carcass  = new Carcass(50,false);
        Bear bear = new Bear();
        Location l1 = new Location(0,0);
        Location l2 = new Location(0,1);
        world.setCurrentLocation(l1);
        world.setTile(l1,bear);
        world.setTile(l2,carcass);

        assertTrue(world.contains(carcass));

        for(int i = 0; i < 5; i++){
            p.simulate();
        }

        assertFalse(world.contains(carcass));
    }
}