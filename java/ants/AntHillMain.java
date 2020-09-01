package ants;

import ants.actors.AntNode;
import ants.actors.QueenNode;
import ants.environment.*;
import ants.ui.EnvironmentBackgroundPainter;
import io.jbotsim.core.Topology;
import io.jbotsim.ui.JViewer;

public class AntHillMain {

    private Topology tp;
    private QueenNode queen;

    public static Environment environment;

    public static void main(String[] args) {
        new AntHillMain();
    }

    public AntHillMain() {
        tp = new Topology(1000,800);

        tp.setNodeModel("ant", AntNode.class);
        tp.setNodeModel("queen", QueenNode.class);
        tp.setNodeModel("food", FoodNode.class);
        tp.setNodeModel("obstacle", ObstacleNode.class);

        //tp.setTimeUnit(20);
        JViewer jv = new JViewer(tp);

        environment = new Environment(tp, 30, 25);

        initializeObstacle(25);
        initializeQueen();
        initializeFood(30);

        EnvironmentBackgroundPainter painter = new EnvironmentBackgroundPainter(tp, environment);
        jv.getJTopology().setDefaultBackgroundPainter(painter);
        tp.start();
    }

    private void initializeFood(int nb) {
        FoodSpawner foodSpawner = new FoodSpawner(tp, environment);
        for(int i = 0; i<nb;i++)
            foodSpawner.spawnRandomFood();
    }

    private void initializeObstacle(int nb) {
        ObstacleSpawner obstacleSpawner = new ObstacleSpawner(tp, environment);
        for(int i = 0; i<nb;i++)
            obstacleSpawner.spawnRandomObstacle();
    }

    public void initializeQueen() {

        queen = new QueenNode();

        Cell queenCell = environment.getRandomLocation();
        while (queenCell.HasObstacle()) {
            queenCell = environment.getRandomLocation();
        }
        queen.setCurrentCell(queenCell);
        queen.setLocation(queenCell);
        queenCell.setDug(true);
        queenCell.setCost(Cell.MIN_COST_VALUE);
        if (!queenCell.getBottomNeighbor().HasObstacle()) {
            queenCell.getBottomNeighbor().setCost(Cell.MIN_COST_VALUE);
            queenCell.getBottomNeighbor().setDug(true);
        }
        if (!queenCell.getRightNeighbor().HasObstacle()) {
            queenCell.getRightNeighbor().setCost(Cell.MIN_COST_VALUE);
            queenCell.getRightNeighbor().setDug(true);
        }
        if (!queenCell.getLeftNeighbor().HasObstacle()) {
            queenCell.getLeftNeighbor().setCost(Cell.MIN_COST_VALUE);
            queenCell.getLeftNeighbor().setDug(true);
        }
        if (!queenCell.getTopNeighbor().HasObstacle()) {
            queenCell.getTopNeighbor().setCost(Cell.MIN_COST_VALUE);
            queenCell.getTopNeighbor().setDug(true);
        }

        tp.addNode(queen);

    }

}
