import java.util.*;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import itumulator.world.Location;
import itumulator.world.World;
import static org.junit.jupiter.api.Assertions.*;

public class WorldTest {
    World world;
    Random r = new Random();

    @BeforeEach
    public void setUp(){
        world = new World(2);
    }

    @Test
    public void RabbitMovesDuringDay(){
        world.setDay();
        BabyRabbit rabbit = new BabyRabbit();
        Location location = new Location(0,0);
        world.setCurrentLocation(location);
        world.setTile(location,rabbit);

        rabbit.act(world);

        Location new_location =  world.getLocation(rabbit);
        assertNotEquals(new_location,location);
    }
/* Klar Efter Pull
    @Test
    public void RabbitSleepsDuringNight(){
        world.setNight();
        BabyRabbit rabbit = new BabyRabbit();
        Location location = new Location(0,0);
        world.setCurrentLocation(location);
        world.setTile(location,rabbit);

        rabbit.act(world);

        Location new_location =  world.getLocation(rabbit);
        assertEquals(location,new_location);
    }*/

    @Test
    public void RabbitEatsGrass(){
        BabyRabbit rabbit = new BabyRabbit();
        Location location = new Location(0,0);
        world.setCurrentLocation(location);
        world.setTile(location,rabbit);

        Grass grass = new Grass();
        Location grass_location = new Location(1,0);
        world.setTile(grass_location,grass);

        world.move(rabbit, grass_location);

        rabbit.act(world);

        Location l = world.getLocation(rabbit);
        assertFalse(world.containsNonBlocking(grass_location));
    }

    @Test
    public void RabbitDigsTunnel(){

    }
}
