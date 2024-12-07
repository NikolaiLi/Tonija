package wold_project;

import java.util.Random;
import java.util.Set;

import itumulator.executable.Program;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import itumulator.world.Location;
import itumulator.world.World;

import world_project.Wolf;

import static org.junit.jupiter.api.Assertions.*;

public class WolfTest {
    World world;
    Program p;
    int size = 15;
    Random r = new Random();

    @BeforeEach
    public void setUp(){
        p = new Program(size,600,150);
        world = p.getWorld();
    }

    @Test
    public void BearMovesDuringDay() {

    }
}