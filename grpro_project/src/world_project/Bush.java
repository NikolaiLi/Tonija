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
     * When this method is called, the hasFruits variable can be changed.
     * @param x a boolean value that can be set to the hasFruits variable
     */
    public void isRipe(boolean x){
        hasFruits = x;
    }

    /**
     * A method to call for the boolean variable of hasFruits
     * @return the boolean value of hasFruits
     */
    public boolean isHasFruits() {
        return hasFruits;
    }
}
