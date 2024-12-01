package world_project;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;
import java.util.*;

public class WolfHole extends Hole implements DynamicDisplayInformationProvider {
    Random r = new Random();
    protected Location wolfHoleLocation;
    DisplayInformation di_wolf_hole = new DisplayInformation(Color.darkGray, "Hole");

    public WolfHole() {
    }

    @Override
    public DisplayInformation getInformation() {
        return di_wolf_hole;
    }
}