package world_project;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;
import java.util.*;

/**
 * The Fungi class is a blocking object that can infect other Carcass objects that are within its reach.
 * Fungi implements interfaces actor and DynamicDisplayInformationProvider, to enact its methods
 * and ensure correct display of Fungi at all times during the world simulation.
 */
public class Fungi implements Actor, DynamicDisplayInformationProvider {
    DisplayInformation displaySmall;
    DisplayInformation displayBig;
    int duration;
    boolean isBig;
    boolean alive;

    /**
     * Initializes a Fungi with a parameter that checks whether if the Fungi is big or small.
     * The size has an effect on the {@link #duration} of the Fungi.
     * @param isBig determines whether if the Fungi is a big or small Fungi.
     */
    public Fungi(boolean isBig) {
        this.isBig = isBig;
        alive = true;
        if (isBig) {
            displayBig = new DisplayInformation(Color.yellow, "fungi");
            duration = 100;
        } else {
            displaySmall = new DisplayInformation(Color.yellow, "fungi-small");
            duration = 50;
        }
    }

    /**
     * Provides the order of when individual methods should be executed inside the simulation.
     * @param world providing details of the position on which the actor is currently located and much more.
     */
    @Override
    public void act(World world) {
        while (alive) {
            spread(world);

            dying(world);

            return;
        }
    }

    /**
     * Provides the relevant display information if the Fungi should be displayed as a big or small Fungi.
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
     * Method that makes the Fungi able to spread spores onto other Carcass objects and infects them.
     * @param world to access the world library.
     */
    public void spread(World world) {
        Location l = world.getLocation(this);
        Set<Location> spreadArea = world.getSurroundingTiles(l, 2);

        for (Location location : spreadArea) {
            Object tile = world.getTile(location);
            if (tile instanceof Carcass spreadCarcass) {
                if (!spreadCarcass.getIsInfected()) {
                    spreadCarcass.getInfected();
                }
            }
        }
    }

    /**
     * Method that deletes the object from the world if its {@link #duration} goes to zero.
     * @param world to access the world library.
     */
    public void dying(World world) {
        duration -= 5;

        if (duration <= 0) {
            alive = false;
            world.delete(this);
        }
    }
}
