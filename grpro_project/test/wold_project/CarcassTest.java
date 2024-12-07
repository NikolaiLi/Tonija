package wold_project;

import java.util.Random;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import itumulator.world.Location;
import itumulator.world.World;

import world_project.Carcass;

import static org.junit.jupiter.api.Assertions.*;

public class CarcassTest {
    World world;
    int size = 15;
    Random r = new Random();

    @BeforeEach
    public void setUp(){
        world = new World(15);
    }

    @Test
    public void CarcassDecomposes(){
        Carcass carcass = new Carcass(100,false);
    }
}