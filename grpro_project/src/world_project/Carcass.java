package world_project;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

import java.awt.*;
import java.util.*;

public class Carcass implements Actor, DynamicDisplayInformationProvider {
    boolean infected;
    int duration;
    int maxDuration;
    boolean isBig;
    Random r;
    DisplayInformation displayBig;
    DisplayInformation displaySmall;

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

    @Override
    public DisplayInformation getInformation() {
        if (isBig) {
            return displayBig;
        } else {
            return displaySmall;
        }
    }

    @Override
    public void act(World world) {
        decompose(world);

        chanceOfInfection();
    }

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

    public boolean getIsInfected() {
        return infected;
    }

    public void getInfected() {
        infected = true;
    }
    
    public int getDuration() {return duration;}

    public void gettingEaten(int damage) {
        duration -= damage;
    }

    public void chanceOfInfection() {
        if (!infected) {
            r = new Random();
            int halfDuration = maxDuration / 2;
            int oneFourthDuration = maxDuration / 4;

            if (duration > halfDuration) {
                if (r.nextInt(100) > 1) {
                    infected = true;
                }
            } else if (duration < halfDuration && duration > oneFourthDuration) {
                if (r.nextInt(100) > 3) {
                    infected = true;
                }
            } else if (duration < oneFourthDuration) {
                if (r.nextInt(100) > 5) {
                    infected = true;
                }
            }
        }
    }
}
