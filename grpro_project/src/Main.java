import java.awt.Color;
import java.io.*;
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


        //FILE READING AND MANIPULATION
        String filePath = "data/week-1/t1-1a.txt";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(reader.readLine());
                //Hvordan skal filerne manipuleres?:
                //Første element, size variablen skal opdateres til dette
                    //Skal ikke være i samme while loop som andet og tredje
                //Andet/Tredje element,  type + antal || type + antal(min,max)
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        Program p = new Program(size, display_size, delay);
        World world = p.getWorld();

        DisplayInformation di_BabyRabbit = new DisplayInformation(Color.yellow, "rabbit-small");
        p.setDisplayInformation(BabyRabbit.class, di_BabyRabbit);

        DisplayInformation di_AdultRabbit = new DisplayInformation(Color.orange, "rabbit-large");
        p.setDisplayInformation(AdultRabbit.class, di_AdultRabbit);

        // Spawner Rabbits forskellige steder (Krav 1)
        int amountOfRabbits = 3;
        Random r = new Random();

        for (int i = 0; i < amountOfRabbits; i++) {
            int x = r.nextInt(size);
            int y = r.nextInt(size);
            Location l = new Location(x, y);

            while (!world.isTileEmpty(l)) {
                x = r.nextInt(size);
                y = r.nextInt(size);
                l = new Location(x, y);
            }

            world.setTile(l, new BabyRabbit());
        }

        p.setDisplayInformation(Grass.class, new DisplayInformation(Color.green, "grass"));


        // Spawner Rabbits forskellige steder (Krav 1)

        int amountOfGrass = 1;
        for (int i = 0; i < amountOfGrass; i++) {
            int x = r.nextInt(size);
            int y = r.nextInt(size);
            Location l = new Location(x, y);

            while (!world.isTileEmpty(l)) {
                x = r.nextInt(size);
                y = r.nextInt(size);
                l = new Location(x, y);
            }

            world.setTile(l, new Grass());
        }

        p.show();
        p.simulate();
    }
}