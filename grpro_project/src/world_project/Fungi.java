package world_project;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;
import java.util.*;

public class Fungi implements Actor, DynamicDisplayInformationProvider {
    DisplayInformation displaySmall;
    DisplayInformation displayBig;
    boolean isBig;

    public Fungi(boolean isBig) {
        this.isBig = isBig;
        if (isBig) {
            displayBig = new DisplayInformation(Color.yellow, "fungi");
        } else {
            displaySmall = new DisplayInformation(Color.yellow, "fungi-small");
        }
    }

    @Override
    public void act(World world) {

    }

    @Override
    public DisplayInformation getInformation() {
        if (isBig) {
            return displayBig;
        } else {
            return displaySmall;
        }
    }
}
