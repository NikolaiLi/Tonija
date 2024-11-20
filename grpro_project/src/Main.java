import java.awt.Color;
import java.util.Random;
import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;

public class Main {

    public static void main(String[] args) {
        int size = 15;
        int delay = 150;
        int display_size = 800;

        Program p = new Program(size, display_size, delay);
        World world = p.getWorld();

        DisplayInformation di = new DisplayInformation(Color.yellow, "rabbit-small");
        p.setDisplayInformation(Rabbit.class, di);

        // Spawner Rabbits forskellige steder (Krav 1)
        int amountOfRabbits = 3;
        Random r = new Random();

        for(int i = 0; i < amountOfRabbits; i++){
            int x = r.nextInt(size);
            int y = r.nextInt(size);
            Location l = new Location(x,y);

            while(!world.isTileEmpty(l)) {
                x = r.nextInt(size);
                y = r.nextInt(size);
                l = new Location(x,y);
            }

            world.setTile(l, new Rabbit());
        }

        DisplayInformation di_grass = new DisplayInformation(Color.green, "grass");
        p.setDisplayInformation(Grass.class, di_grass);

        // Spawner Rabbits forskellige steder (Krav 1)

        int amountOfGrass = 1;
        for(int i = 0; i < amountOfGrass; i++){
            int x = r.nextInt(size);
            int y = r.nextInt(size);
            Location l = new Location(x,y);

            while(!world.isTileEmpty(l)) {
                x = r.nextInt(size);
                y = r.nextInt(size);
                l = new Location(x,y);
            }

            world.setTile(l, new Grass());
        }

        p.show();
        p.simulate();
    }
}