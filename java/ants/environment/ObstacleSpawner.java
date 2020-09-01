package ants.environment;

import io.jbotsim.core.Topology;

import java.util.Random;

public class ObstacleSpawner{

    private final Random random;
    private Topology tp;
    private Environment environment;

    public ObstacleSpawner(Topology topology, Environment environment) {
        tp = topology;
        this.environment = environment;
        random = new Random();
    }

    public void spawnRandomObstacle() {
        ObstacleNode n = new ObstacleNode();
        Cell location = environment.getRandomLocation();
        location.setHasObstacle(true);
        n.setLocation(location);
        n.setCurrentCell(location);
        tp.addNode(n);
    }

    private boolean shouldSpawn() {
        return random.nextDouble() < 0.01;
    }
}
