package world_project;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.World;

import java.awt.*;

/**
 * The Bush class is a blocking object that can bear fruits which the Bear class can eat.
 * Bush implements interfaces actor and DynamicDisplayInformationProvider, to enact its methods
 * and ensure correct display of bush at all times during the world simulation.
 */
public class Bush implements Actor, DynamicDisplayInformationProvider {
    protected boolean hasFruits = true;
    int steps;
    DisplayInformation di_berry           = new DisplayInformation(Color.green, "bush-berries");
    DisplayInformation di_berry_no_fruits = new DisplayInformation(Color.green, "bush");

    /**
     * Provides the visual display of the bush, whether if bears fruits or not.
     * @return DisplayInformation type variable that changes the display of the Bush object if it bears fruits or not.
     */
    @Override
    public DisplayInformation getInformation() {
        if (hasFruits) {
            return di_berry;
        } else {
            return di_berry_no_fruits;
        }
    }

    /**
     * The act method is used for the correct displaying of bush whether if it has fruits or not. A step counter
     * counts each step up and if that number is divisible and the bush bears no fruits, it changes the display.
     * @param world providing details of the position on which the actor is currently located and much more.
     */
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
