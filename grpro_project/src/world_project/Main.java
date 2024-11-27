package world_project;

import java.io.*;
import java.util.Random;

import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;

public class Main {

    public static void main(String[] args) {
        int size = 0;
        int delay = 150;
        int display_size = 800;
        int amountOfGrass = 0;
        int amountOfRabbits = 0;
        int amountOfRabbitHoles = 0;
        int amountOfBears = 0;
        int bearXCoordinate;
        int bearYCoordinate;
        int amountOfWolves = 0;
        int amountOfBBushes = 0;
        Random r = new Random();



        //------------------------FILE MANIPULATION------------------------\\
        String filePath = "data/week-2/test.txt";
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
                if(parts.length > 2){
                    String coordinates = parts[2];
                    coordinates = coordinates.replaceAll("[()]","");
                    String[] xAndy = coordinates.split(",");

                    bearXCoordinate = Integer.parseInt(xAndy[0]);
                    bearYCoordinate = Integer.parseInt(xAndy[1]);
                    System.out.println("Coordinates: x = " + bearXCoordinate + ", y = " + bearYCoordinate);
                }


                int count;
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

                    case "burrow":
                        amountOfRabbitHoles = count;
                        System.out.println("Added " + count + " RabbitHole objects");
                        break;

                    /*
                    case "wolf":
                        amountOfWolves = count;
                        System.out.println("Added " + count + " Wolf objects");
                        break;

                    case "bear":
                        amountOfBears = count;
                        System.out.println("Added " + count + " Bear objects");
                        break;*/

                    case "berry":
                        amountOfBBushes = count;
                        System.out.println("Added " + count + " Bush objects");
                        break;
                }

            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Program p = new Program(size, display_size, delay);
        World world = p.getWorld();

        Bear bear = new Bear();
        bear.makeTerritory(world);
        Location territoryCenter = bear.getTerritoryCenter();
        world.setTile(territoryCenter, bear);


        //------------------------PLACE GRASS------------------------\\
        for (int i = 0; i < amountOfGrass; i++) {
            int x = r.nextInt(size);
            int y = r.nextInt(size);
            Location l = new Location(x, y);

            while (!world.isTileEmpty(l) || world.containsNonBlocking(l)) {
                x = r.nextInt(size);
                y = r.nextInt(size);
                l = new Location(x, y);
            }

            world.setTile(l, new Grass());
        }

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


        //------------------------PLACE RABBIT HOLE------------------------\\
        for (int i = 0; i < amountOfRabbitHoles; i++) {
            int x = r.nextInt(size);
            int y = r.nextInt(size);
            Location l = new Location(x, y);

            while (!world.isTileEmpty(l) || world.containsNonBlocking(l)) {
                x = r.nextInt(size);
                y = r.nextInt(size);
                l = new Location(x, y);
            }

            world.setTile(l, new RabbitHole(world));
        }

        //------------------------PLACE BERRY BUSH------------------------\\
        for (int i = 0; i < amountOfBBushes; i++) {
            int x = r.nextInt(size);
            int y = r.nextInt(size);
            Location l = new Location(x, y);

            while (!world.isTileEmpty(l) || world.containsNonBlocking(l)) {
                x = r.nextInt(size);
                y = r.nextInt(size);
                l = new Location(x, y);
            }

            world.setTile(l, new Bush());
        }

        p.show();
        p.simulate();
    }
}