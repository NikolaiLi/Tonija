package world_project;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;
import java.util.*;
import java.util.List;

public class AdultRabbit extends Rabbit implements DynamicDisplayInformationProvider {
    Random r = new Random();
    DisplayInformation di_adult_rabbit_day = new DisplayInformation(Color.green, "rabbit-large");
    DisplayInformation di_adult_rabbit_night = new DisplayInformation(Color.green, "rabbit-large-sleeping");

    public AdultRabbit(int energy) {
        super();
        animal = "Adult Rabbit";
        age = 15;
        ageOfDeath = 60;
        this.energy = energy;
        increaseMaxEnergy(50);
        health = 30;
        maxHealth = health;
    }

    @Override
    public DisplayInformation getInformation() {
            return di_adult_rabbit_day;
    }

    @Override
    public void act(World world) {
        //outside the act loop
        //tjekker om Adult rabbit er død af hunger, age eller damage

        hungerDeath(world, animal);

        deathByDamage(world, animal);

        dyingOfAge(world,ageOfDeath, animal);

        while (alive) {

            super.act(world);

            breed(world);

            //10% chance to dig rabbithole, if unblocking element is empty and rabbit has not dug a hole already
            if (r.nextInt(100) < 10 && this.isAlive()) {
                digRabbitHole(world);
            }
            return;
        }
    }

    @Override
    public void dyingOfAge(World world, int ageOfDeath, String animal) {super.dyingOfAge(world,ageOfDeath, animal);}


    public void breed(World world) {
        if (!hiding) {
            Set<Location> neighbours = world.getSurroundingTiles();
            List<Location> list = new ArrayList<>(neighbours);
            int chanceOfBirth = r.nextInt(100);
            for (Location location : list) {
                if (world.getTile(location) instanceof AdultRabbit && chanceOfBirth >= 95) {
                    Set<Location> emptyNeighbours = world.getEmptySurroundingTiles();
                    List<Location> listOfEmptyTiles = new ArrayList<>(emptyNeighbours);

                    if (!emptyNeighbours.isEmpty()) {
                        int randomNumber = r.nextInt(listOfEmptyTiles.size());
                        Location l = listOfEmptyTiles.get(randomNumber);
                        world.setTile(l, new BabyRabbit());
                    }
                    System.out.println("A baby rabbit has been born");
                    return;
                }
            }
        }
    }

    public void digRabbitHole(World world) {
        if (!AlreadyBuiltRabbitHole && !hiding && isAlive()) {
            Location current = world.getLocation(this);
            if (!world.containsNonBlocking(current)) {
                RabbitHole rabbitHole = new RabbitHole(world);
                world.setTile(current, rabbitHole);
                AlreadyBuiltRabbitHole = true;
                rabbitHole.setLocation(world);
                System.out.println("Rabbit has dug a hole at " + current);
            } else {
                System.out.println("Cannot dig hole. Non-blocking element already present on tile");
            }
        } else {
            System.out.println("Rabbit already has a hole");
        }
    }

    public void digTunnel() {
        RabbitHole rabbitHole = this.getCurrentHidingPlace();
        Map<RabbitHole, ArrayList<RabbitHole>> tunnels = rabbitHole.getTunnels();
        HashSet<RabbitHole> exits = new HashSet<>(tunnels.get(rabbitHole));
        Set<RabbitHole> allHoles = tunnels.keySet();
        if (exits.equals(allHoles)) {
            System.out.println("Cannot dig tunnel. RabbitHole is already connected to all other RabbitHoles.");
            return;
        }
        RabbitHole newExit = null;
        ArrayList<RabbitHole> allHolesList= new ArrayList<>(allHoles);
        while (!tunnels.get(rabbitHole).contains(newExit)) {
            RabbitHole tempExit = allHolesList.get(r.nextInt(allHolesList.size()));
            if (!exits.contains(tempExit)) {
                newExit = tempExit;
                tunnels.get(rabbitHole).add(newExit);
                tunnels.get(newExit).add(rabbitHole);
                System.out.println("New tunnel built between " + rabbitHole + " and " + newExit);
            }
        }

    }


}
