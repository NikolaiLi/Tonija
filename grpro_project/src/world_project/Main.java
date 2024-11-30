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
        int count;
        Random r = new Random();
        Program p;
        World world;


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
                String numberInfo = parts[1];
                boolean hasCoordinates = parts.length > 2;
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
                        break;

                    case "rabbit":
                        System.out.println("Added " + count + " Rabbits objects");
                        //------------------------PLACE RABBIT------------------------\\
                        for (int i = 0; i < count; i++) {
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
                        break;

                    case "burrow":
                        System.out.println("Added " + count + " RabbitHole objects");
                        //------------------------PLACE RABBIT HOLE------------------------\\
                        for (int i = 0; i < count; i++) {
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
                        break;

                    case "wolf":
                        System.out.println("Added " + count + " Wolf objects");
                        //------------------------PLACE WOLF------------------------\\
                        for (int i = 0; i < count; i++) {
                            int x = r.nextInt(size);
                            int y = r.nextInt(size);
                            Location l = new Location(x, y);

                            while (!world.isTileEmpty(l) || world.containsNonBlocking(l)) {
                                x = r.nextInt(size);
                                y = r.nextInt(size);
                                l = new Location(x, y);
                            }

                            world.setTile(l, new Wolf(count, world));
                        }
                        break;

                    case "bear":
                        System.out.println("Added " + count + " Bear objects");
                        //------------------------PLACE BEAR------------------------\\
                        for (int i = 0; i < count; i++) {
                            Location l;

                            if (hasCoordinates) {
                                l = new Location(bearXCoordinate, bearYCoordinate);
                            } else {
                                int x = r.nextInt(size);
                                int y = r.nextInt(size);
                                l = new Location(x, y);

                                while (!world.isTileEmpty(l) || world.containsNonBlocking(l)) {
                                    x = r.nextInt(size);
                                    y = r.nextInt(size);
                                    l = new Location(x, y);
                                }
                            }

                            System.out.println("Bear coordinates: " + l.getX() + ", " + l.getY());
                            world.setTile(l, new Bear());
                        }
                        break;

                    case "berry":
                        System.out.println("Added " + count + " Bush objects");
                        //------------------------PLACE BERRY BUSH------------------------\\
                        for (int i = 0; i < count; i++) {
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
}