import itumulator.world.Location;
import itumulator.world.World;
import java.util.*;

public class AdultRabbit extends Rabbit{
Random r = new Random();
    
    public AdultRabbit(int hunger) {
        super();
        age = 15;
        this.hunger = hunger;
        increaseMaxHunger(50);
    }

    @Override
    public void act(World world) {
        // Henter super-klassens act (Rabbit's act)
        super.act(world);

        int chanceOfDying = r.nextInt(10);
        if (age > 60 && chanceOfDying == 1) {
            world.delete(this);
            System.out.println("An adult rabbit has died of age");
        }

        // Finder ledige nabopositioner
        Set<Location> neighbours = world.getSurroundingTiles();
        List<Location> list = new ArrayList<>(neighbours);

        // Tjekker om der er voksne kaniner i nabopositionerne, og hvis der er, er der 50% chance for at føde en babykanin på en nærliggende empty tile
        int chanceOfBirth = r.nextInt(100);
        for (Location location : list) {
            if (world.getTile(location) instanceof AdultRabbit && chanceOfBirth >= 50) {
                breed(world);
                System.out.println("A baby rabbit has been born");
                return;
            }
        }

        //10% chance to dig rabbithole, if unblocking element is empty and rabbit has not dug a hole already
        if (r.nextInt(100) < 10 && this.isAlive()) {
            digRabbitHole(world);
        }
    }


    public void breed(World world) {
        Random random = new Random();

        Set<Location> neighbours = world.getEmptySurroundingTiles();
        List<Location> list = new ArrayList<>(neighbours);

        if (!neighbours.isEmpty()) {
            int randomNumber = random.nextInt(list.size());
            Location location = list.get(randomNumber);
            world.setTile(location, new BabyRabbit());
        } else {
            System.out.println("No space available for a baby rabbit to be born");
        }
    }

    public void digRabbitHole(World world) {
        if (!AlreadyBuiltRabbitHole) {
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
