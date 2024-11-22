import itumulator.world.Location;
import itumulator.world.World;
import java.util.*;

public class AdultRabbit extends Rabbit{

    public AdultRabbit() {
        super();
        age = 15;
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
        Set<Location> neighbours = world.getEmptySurroundingTiles();
        List<Location> list = new ArrayList<>(neighbours);

        // Tjekker om der er kaniner i nabopositionerne, hvis der er fødes der en kanin
        int chanceOfBirth = r.nextInt(100);
        for (Location location : list) {
            if (world.getTile(location) instanceof AdultRabbit && chanceOfBirth >= 95) {
                breed(world);
                System.out.println("A baby rabbit has been born");
                return;
            }
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
        }
    }

}
