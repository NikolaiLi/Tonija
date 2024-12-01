package wold_project;

import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import itumulator.world.Location;
import itumulator.world.World;

import world_project.Bear;

import static org.junit.jupiter.api.Assertions.*;

public class BearTest {
    World world;
    int size = 15;
    Random r = new Random();

    @BeforeEach
    public void setUp(){
        world = new World(15);
    }

    @Test
    public void BearMovesDuringDay(){
        world.setDay();
        Bear bear = new Bear();
        Location location = new Location(0,0);
        world.setCurrentLocation(location);
        world.setTile(location, bear);

        bear.act(world);

        Location new_location =  world.getLocation(bear);
        assertNotEquals(new_location,location);
    }

    @Test
    public void BearMovesInTerritory(){
        Bear bear = new Bear();
        Location location = new Location(0,0);
        world.setCurrentLocation(location);
        world.setTile(location, bear);
        bear.makeTerritory(world);
        Set<Location> territory = bear.getTerritoryArea();

        for(int i = 0; i < 10; i++){
            bear.act(world);
        }

        Location l = world.getLocation(bear);
        assertTrue(territory.contains(l));
    }

    @Test
    public void BearEatsCreature(){
        Bear bear = new Bear();
        Location location = new Location(0,0);
        world.setCurrentLocation(location);
        world.setTile(location, bear);
        bear.makeTerritory(world);


    }
}