import java.awt.Color;

import itumulator.executable.DisplayInformation;
import itumulator.executable.Program;
import itumulator.world.Location;
import itumulator.world.World;

public class Main {

    public static void main(String[] args) {
        int size = 15;
        int delay = 300;
        int display_size = 800;

        Program p = new Program(size, display_size, delay);
        World world = p.getWorld();

        p.show();

        for (int i = 0; i < 200; i++) {
            p.simulate();
        }
        
    }
}