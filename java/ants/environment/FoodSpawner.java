package ants.environment;

import io.jbotsim.core.Topology;
import io.jbotsim.core.event.ClockListener;

import java.util.Random;

public class FoodSpawner implements ClockListener{

    private final Random random;
    private Topology tp;
    private Environment environment;

    public FoodSpawner(Topology topology, Environment environment) {
        tp = topology;
        this.environment = environment;
        tp.addClockListener(this);
        random = new Random();
    }

    @Override
    public void onClock() {

        if (shouldSpawn())
            spawnRandomFood();

    }

    public void spawnRandomFood() {
        FoodNode n = new FoodNode();

        Cell location = environment.getRandomLocationAdvanced();
        while (location.HasObstacle()) {
            location = environment.getRandomLocationAdvanced();
        }
        n.setLocation(location);
        n.setCurrentCell(location);
        tp.addNode(n);
    }

    private boolean shouldSpawn() {
        return random.nextDouble() < 0.01;
    }
}
