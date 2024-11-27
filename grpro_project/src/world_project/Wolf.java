package world_project;

import itumulator.executable.DisplayInformation;
import itumulator.executable.DynamicDisplayInformationProvider;
import itumulator.world.World;
import itumulator.world.Location;

import java.util.*;
import java.awt.*;

public class Wolf extends Creature implements DynamicDisplayInformationProvider {
    public DisplayInformation di_wolf = new DisplayInformation(Color.gray, "wolf");
    private ArrayList<Wolf> wolfpack;
    private Set<Location> territory;
    private boolean isAlpha;


    //Constructor used when new wolf is created from files in filereader
    Wolf (int number, World world) {
        super();
        health = 80;
        this.wolfpack = new ArrayList<>();
        wolfpack.add(this);
        this.isAlpha = true;
        System.out.println("Created Packleader");
        //creates the rest of the pack as children of the alphaWolf by calling the constructor used while in simulation
        for (int i = 0; i > number-1; i++) {
            Wolf wolf = new Wolf(this,world);
        }
    }

    //Constructor used when new wolf is created from a wolf in the simulation.
    Wolf (Wolf wolfmother, World world) {
        super();
        health = 80;
        this.wolfpack = wolfmother.getWolfpack();
        wolfpack.add(this);
        this.isAlpha = false;

        //placing wolf in the wolfHole where breeding took place. If hole is blocked, temporarily removes object to spawn wolf in hole.
        if (world.isOnTile(wolfmother)) {
            Location l = world.getLocation(wolfmother);
            world.setCurrentLocation(l);
            if (world.isTileEmpty(l)) {
                world.setTile(l,this);
            } else {
                Object o = world.getTile(l);
                world.remove(o);
                world.setTile(l, this);
                this.hide(world);
                world.setTile(l, o);
            }
        }
    }

    @Override
    public DisplayInformation getInformation() {
        return di_wolf;
    }

    public void move(World world) {

    }

    public void act(World world) {

    }

    public void eat(World world) {

    }

    public void hide(World world) {

    }

    public ArrayList<Wolf> getWolfpack() {
        return wolfpack;
    }
}
