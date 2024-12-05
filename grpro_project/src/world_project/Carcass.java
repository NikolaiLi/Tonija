package world_project;

import itumulator.simulator.Actor;
import itumulator.world.Location;
import itumulator.world.World;

import java.util.*;

public class Carcass implements Actor {
    boolean infected;
    int duration;
    int maxDuration;
    boolean isBig;
    Random r;

    Carcass(int maxHealth, boolean isInfected) {
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
    public void act(World world) {
        decompose(world);
    }

    public void decompose(World world) {
        if (infected) {
            duration -= 10;
        } else {
            duration -= 5;
        }

        if (duration <= 0) {
            Location l = world.getLocation(this);
            world.delete(this);
            world.setTile(l, new Fungi(isBig));
        }
    }

    public boolean getIsInfected() {
        return infected;
    }

    public void getInfected() {
        infected = true;
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

    public double test() {
        int duration = 45;
        int maxHealth = 200;
        double durationPercentage = Math.round(((double) duration / maxHealth) * 100);
        return durationPercentage;
    }
}
