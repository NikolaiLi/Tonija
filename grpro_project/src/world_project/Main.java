package world_project;

import java.io.*;
import java.util.Random;
import java.util.function.Supplier;

import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;

public class Main {

    public static void main(String[] args) {
        int size = 0;
        int delay = 150;
        int display_size = 800;
        int count;
        boolean hasCoordinates;
        Random r = new Random();
        Program p;
        World world;


        //------------------------FILE MANIPULATION------------------------\\
        String filePath = "data/week-1/tf1-1.txt";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;

            line = reader.readLine();
            System.out.println(line);
            if (line != null) {
                size = Integer.parseInt(line.trim());
                System.out.println("World size set to: " + size);

            }

            p = new Program(size, display_size, delay);
            world = p.getWorld();

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                String[] parts = line.split(" ");

                if (parts.length < 2) {
                    System.out.println("Skipping invalid line: " + line);
                    continue;
                }
                String className = parts[0].toLowerCase();
                String numberInfo;
                boolean hasFungi = parts[1].equals("fungi");
                if(hasFungi){
                    numberInfo = parts[2];
                } else {
                    numberInfo = parts[1];
                }

                hasCoordinates = parts.length > 2 && parts[2].contains("(");

                int bearXCoordinate = -1, bearYCoordinate = -1;

                if (hasCoordinates) {
                    String coordinates = parts[2].replaceAll("[()]", "");
                    String[] xAndy = coordinates.split(",");
                    bearXCoordinate = Integer.parseInt(xAndy[0]);
                    bearYCoordinate = Integer.parseInt(xAndy[1]);
                }

                if (numberInfo.contains("-")) {
                    String[] range = numberInfo.split("-");
                    int min = Integer.parseInt(range[0]);
                    int max = Integer.parseInt(range[1]);
                    count = r.nextInt(max - min + 1) + min;
                } else {
                    count = Integer.parseInt(numberInfo);
                }

                switch (className) {
                    case "grass":
                        System.out.println("Added " + count + " Grass objects");
                        //------------------------PLACE GRASS------------------------\\
                        for (int i = 0; i < count; i++) {
                            world.setTile(addLocation(world, r, size), new Grass());
                        }
                        break;

                    case "rabbit":
                        System.out.println("Added " + count + " Rabbits objects");
                        //------------------------PLACE RABBIT------------------------\\
                        for (int i = 0; i < count; i++) {
                            world.setTile(addLocation(world,r,size), new BabyRabbit());
                        }
                        break;

                    case "burrow":
                        System.out.println("Added " + count + " RabbitHole objects");
                        //------------------------PLACE RABBIT HOLE------------------------\\
                        for (int i = 0; i < count; i++) {
                            world.setTile(addLocation(world,r,size), new RabbitHole(world,addLocation(world,r,size)));
                        }
                        break;

                    case "wolf":
                        System.out.println("Added " + count + " Wolf objects");
                        //------------------------PLACE WOLF------------------------\\
                        for (int i = 0; i < 1; i++) {
                            Location l = addLocation(world,r,size);
                            world.setTile(l, new Wolf(count, world, l));
                        }
                        break;

                    case "berry":
                        System.out.println("Added " + count + " Bush objects");
                        //------------------------PLACE BERRY BUSH------------------------\\
                        for (int i = 0; i < count; i++) {
                            world.setTile(addLocation(world,r,size), new Bush());
                        }
                        break;

                    case "bear":
                        System.out.println("Added " + count + " Bear objects");
                        //------------------------PLACE BEAR------------------------\\
                        Location l;
                        for (int i = 0; i < count; i++) {
                            if (hasCoordinates) {
                                l = new Location(bearXCoordinate, bearYCoordinate);
                            }
                            else {
                                l = addLocation(world,r,size);
                            }
                            System.out.println("Bear coordinates: " + l.getX() + ", " + l.getY());
                            world.setTile(l, new Bear());
                        }
                        break;

                    case "carcass":
                        System.out.println("Added " + count + " Carcass objects");

                        for (int i = 0; i < count; i++) {
                            world.setTile(addLocation(world,r,size), new Carcass(0,hasFungi));
                        }
                        break;
                }

            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        p.show();
        p.simulate();
    }


    /**
     * Reduces potential redundancy with finding a random free tile to place blocking objects
     * @param world to access the world library
     * @param r fetch a randum number for finding a random tile
     * @param size using the size of the program to avoid tileOutOfBounds errors
     * @return location for the object to be placed on
     */
    private static Location addLocation(World world, Random r, int size){
        int x = r.nextInt(size);
        int y = r.nextInt(size);
        Location l = new Location(x, y);

        while (!world.isTileEmpty(l) || world.containsNonBlocking(l)) {
            x = r.nextInt(size);
            y = r.nextInt(size);
            l = new Location(x, y);
        }
        return l;
    }
}