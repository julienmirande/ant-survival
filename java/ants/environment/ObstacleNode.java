package ants.environment;

import ants.actors.CellLocatedNode;
import io.jbotsim.core.Node;

import java.util.Random;

public class ObstacleNode extends CellLocatedNode {

    public ObstacleNode() {
        super();
        setIconSize(10);
        setIcon("/images/caillou.png");
        setWirelessStatus(false);

        setDirection(new Random().nextDouble() * 2 * Math.PI);
    }

}
