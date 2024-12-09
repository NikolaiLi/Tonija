package world_project;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;
import java.util.*;

/**
 * RabbitHole is a subclass extending from hole, extending from Terrain.
 * RabbitHole has no functionality by itself, but is interacted with by the Rabbit-class.
 */
public class RabbitHole extends Hole implements DynamicDisplayInformationProvider {
    protected Location rabbitHoleLocation;
    protected DisplayInformation di_rabbit_hole = new DisplayInformation(Color.green, "hole-small");
    /**
     * initializes a rabbithole Without a rabbitHoleLocation
     * @param world world to access the world library.
     */
    public RabbitHole(World world){
    }

    /**
     * initializes a rabbithole With a rabbitHoleLocation
     * @param world world to access the world library.
     * @param location Location of the placement of the RabbitHole
     */
    public RabbitHole(World world, Location location){
        rabbitHoleLocation = location;
    }

    /**
     * Method used to return the display used for rabbithole.
     * @return the display variable used to store the rabbithole's display.
     */
    @Override
    public DisplayInformation getInformation() {
        return di_rabbit_hole;
    }

    /**
     * Updates the field storing the location of the rabbithole
     * @param world world to access the world library.
     */
    public void setLocation(World world){
        rabbitHoleLocation = world.getLocation(this);
    }

    /**
     * Method used to return the Location for rabbithole.
     * @return the display variable used to store the rabbithole's display.
     */
    public Location getLocation() {
        return rabbitHoleLocation;
    }
}