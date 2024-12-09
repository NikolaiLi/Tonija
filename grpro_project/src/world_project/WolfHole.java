package world_project;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;
import java.util.*;

/**
 * WolfHole is a subclass extending from hole, extending from Terrain.
 * Wolfhole has no functionality by itself, but is interacted with by the wolf-class.
 */
public class WolfHole extends Hole implements DynamicDisplayInformationProvider {

    public WolfHole() {
    }

    DisplayInformation di_wolf_hole = new DisplayInformation(Color.darkGray, "hole");

    /**
     * Method used to return the display used for wolfhole.
     * @return the display variable used to store the wolfhole's display.
     */
    @Override
    public DisplayInformation getInformation() {
        return di_wolf_hole;
    }
}