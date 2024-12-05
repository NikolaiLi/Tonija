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
    int duration;
    boolean isBig;

    public Fungi(boolean isBig) {
        this.isBig = isBig;
        if (isBig) {
            displayBig = new DisplayInformation(Color.yellow, "fungi");
            duration = 100;
        } else {
            displaySmall = new DisplayInformation(Color.yellow, "fungi-small");
            duration = 50;
        }
    }

    @Override
    public void act(World world) {
        dying();
    }

    @Override
    public DisplayInformation getInformation() {
        if (isBig) {
            return displayBig;
        } else {
            return displaySmall;
        }
    }

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

    public void dying() {
        duration -= 5;
    }
}
