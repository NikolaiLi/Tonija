import java.util.Random;

import itumulator.executable.Program;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import itumulator.world.Location;
import itumulator.world.World;
import static org.junit.jupiter.api.Assertions.*;

public class WorldTest {
    World world;
    int size = 15;
    int delay = 150;
    int display_size = 800;
    Random r = new Random();

    @BeforeEach
    public void setUp(){
        world = new World(15);
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
    }

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
    public void ChanceOfRabbitDiggingHole(){
        int amount = 200;
        int counter = 0;
        Program p = new Program(size, display_size, delay);

        //add and locate people
        for(int i = 0; i < amount; i++){
            AdultRabbit rabbit = new AdultRabbit(100);
            int x = r.nextInt(size);
            int y = r.nextInt(size);
            Location l = new Location(x,y);

            while(!world.isTileEmpty(l)){
                x = r.nextInt(size);
                y = r.nextInt(size);
                l = new Location(x,y);
            }

            world.setCurrentLocation(l);
            world.setTile(l, rabbit);

            rabbit.act(world);

            if(rabbit.hasBuiltRabbitHole()){
                counter++;
            }
        }
        assertTrue((1.0* counter/amount) *100 > 6.0 && (1.0* counter/amount) *100 < 13.0,
                "Chance of digging hole is not 10% but was: " + (1.0* counter/amount) *100 + "%");
    }
}
