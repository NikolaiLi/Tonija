package world_project;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.World;

import java.awt.*;

public class Bush implements Actor, DynamicDisplayInformationProvider {
    protected boolean hasFruits = true;
    int steps;
    DisplayInformation di_berry           = new DisplayInformation(Color.green, "bush-berries");
    DisplayInformation di_berry_no_fruits = new DisplayInformation(Color.green, "bush");

    @Override
    public DisplayInformation getInformation() {
        if (hasFruits) {
            return di_berry;
        } else {
            return di_berry_no_fruits;
        }
    }

    @Override
    public void act(World world){
        if(!hasFruits) {
            steps++;
        }

        if(steps % 5 == 0){
            isRipe(true);
        }
    }

    /**
     * when this method is called, the hasFruits variable can be changed.
     * @param x
     */
    public void isRipe(boolean x){
        hasFruits = x;
    }

    public boolean isHasFruits() {
        return hasFruits;
    }
}
