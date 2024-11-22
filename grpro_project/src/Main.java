import java.awt.Color;
import java.io.*;
import java.util.Random;

import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;

public class Main {

    public static void main(String[] args) {
        int size = 0; //Updated through txt files in the file manipulation section
        int delay = 150;
        int display_size = 800;
        int amountOfGrass = 0; //Updated through txt files in the file manipulation section
        int amountOfRabbits = 0; //Updated through txt files in the file manipulation section
        int amountOfRabbitHoles = 0;
        Random r = new Random();



        //------------------------FILE MANIPULATION------------------------\\
        String filePath = "data/week-1/t1-2cde.txt";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;

            line = reader.readLine();
            System.out.println(line);
            if (line != null) {
                size = Integer.parseInt(line.trim());
                System.out.println("World size set to: " + size);
            }

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                String[] parts = line.split(" ");

                if(parts.length < 2){
                    System.out.println("Skipping invalid line: " + line);
                    continue;
                }
                String className = parts[0].toLowerCase();
                String numberInfo = parts[1];


                int count = 0;
                if(numberInfo.contains("-")){
                    String[] range = numberInfo.split("-");
                    int min = Integer.parseInt(range[0]);
                    int max = Integer.parseInt(range[1]);
                    count = r.nextInt(max - min + 1) + min;
                } else {
                    count = Integer.parseInt(numberInfo);
                }

                switch (className) {
                    case "grass":
                        amountOfGrass = count;
                        System.out.println("Added " + count + " Grass objects");
                        break;

                    case "rabbit":
                        amountOfRabbits = count;
                        System.out.println("Added " + count + " Rabbits objects");
                        break;
                        /*
                        case "burrow":
                            amountOfHoles = count;
                            System.out.println("Added " + count + " RabbitHole objects");
                            break;*/
                }

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

        //------------------------PLACE RABBIT------------------------\\
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

        //------------------------PLACE GRASS------------------------\\
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
        /*
        //------------------------PLACE RABBITHOLE------------------------\\
        for (int i = 0; i < amountOfRabbitHoles; i++) {
            int x = r.nextInt(size);
            int y = r.nextInt(size);
            Location l = new Location(x, y);

            while (!world.isTileEmpty(l)) {
                x = r.nextInt(size);
                y = r.nextInt(size);
                l = new Location(x, y);
            }

            world.setTile(l, new RabbitHole());
        }*/

        p.show();
        p.simulate();
    }
}