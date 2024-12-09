package world_project;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;
import java.util.*;

/**
 * The Carcass class is a blocking object that can decompose and get infected by other fungi infected Carcass objects.
 * Carcass implements interfaces actor and DynamicDisplayInformationProvider, to enact its methods
 * and ensure correct display of Carcass at all times during the world simulation.
 */
public class Carcass implements Actor, DynamicDisplayInformationProvider {
    boolean infected;
    int duration;
    int maxDuration;
    boolean isBig;
    Random r;
    DisplayInformation displayBig;
    DisplayInformation displaySmall;

    /**
     * Initializes a Carcass object with parameters relevant to the objects size and duration.
     * @param maxHealth determines whether if the Carcass is a normal or small carcass.
     * @param isInfected determines whether if the Carcass is infected by fungi.
     */
    public Carcass(int maxHealth, boolean isInfected) {
        displayBig = new DisplayInformation(Color.blue, "carcass");
        displaySmall = new DisplayInformation(Color.blue, "carcass-small");
        infected = isInfected;
        isBig = maxHealth >= 150;

        if (isBig) {
            duration = 150;
        } else {
            duration = 50;
        }
        maxDuration = duration;
    }

    /**
     * Provides the relevant display information if the Carcass should be displayed as a normal or small Carcass.
     * @return the relevant display image for the object.
     */
    @Override
    public DisplayInformation getInformation() {
        if (isBig) {
            return displayBig;
        } else {
            return displaySmall;
        }
    }

    /**
     * Provides the order of when individual methods should be executed inside the simulation.
     * The Carcass can decompose and get infected by Fungi.
     * @param world providing details of the position on which the actor is currently located and much more.
     */
    @Override
    public void act(World world) {
        decompose(world);

        chanceOfInfection();
    }

    /**
     * Decrements duration with 10 after each step if it is infected by fungi or 5 if it isn't.
     *
     * @param world to access the world library
     */
    public void decompose(World world) {
        if (infected) {
            duration -= 10;
        } else {
            duration -= 5;
        }

        if (duration <= 0 && infected) {
            Location l = world.getLocation(this);
            world.delete(this);
            world.setTile(l, new Fungi(isBig));
        } else if (duration <= 0) {
            world.delete(this);
        }
    }

    /**
     * Provides an indicator of whether it is infected or not.
     * @return true if it is infected by fungi or false if it isn't.
     */
    public boolean getIsInfected() {
        return infected;
    }

    /**
     * Updates the {@link #infected} variable to true in the case where a Carcass is infected by a fungi.
     */
    public void getInfected() {
        infected = true;
    }

    /**
     * Provides information about the {@link #duration} field.
     * Used in unit testing.
     * @return the number that the field represents.
     */
    public int getDuration() {return duration;}

    /**
     * Provides information about the size of the Carcass object.
     * Used in unit testing.
     * @return true if the carcass is big and false if it isn't.
     */
    public boolean getIsBig(){return isBig;}

    /**
     * Method that subtracts the {@link #duration} field with the value of the parameter.
     * @param damage that reduces the value of duration.
     */
    public void gettingEaten(int damage) {
        duration -= damage;
    }

    /**
     * Method that monitors the {@link #duration} after each step.
     * When the duration is greater than half of its maximal value, then there is 1% chance of the Carcass object
     * being infected by fungi.
     * When the duration is less that half the maximal value and greater than a quarter of its maximal value
     * there is 25% chance of infection.
     * When the duration is less than a quarter there is 50% chance of infection.
     */
    public void chanceOfInfection() {
        if (!infected) {
            r = new Random();
            int halfDuration = maxDuration / 2;
            int oneFourthDuration = maxDuration / 4;

            if (duration > halfDuration) {
                if (r.nextInt(100) < 1) {
                    getInfected();
                }
            } else if (duration < halfDuration && duration > oneFourthDuration) {
                if (r.nextInt(100) < 25) {
                    getInfected();
                }
            } else if (duration < oneFourthDuration) {
                if (r.nextInt(100) < 50) {
                    getInfected();
                }
            }
        }
    }
}
