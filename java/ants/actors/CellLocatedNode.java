package ants.actors;

import ants.environment.Cell;
import io.jbotsim.core.Node;
import io.jbotsim.core.Topology;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CellLocatedNode extends Node {

    protected Cell currentCell = null;

    public Cell getCurrentCell() {
        return currentCell;
    }

    public void setCurrentCell(Cell currentCell) {
        this.currentCell = currentCell;
    }

    public List<Node> getSensedNodes(Topology topo, double sensingRange) {
        ArrayList<Node> senseNodes = new ArrayList();
        Iterator var2 = topo.getNodes().iterator();
       // System.out.println("Nodes : " + topo.getNodes().size());

        while(var2.hasNext()) {
            Node n = (Node)var2.next();
            if (this.distance(n) < sensingRange) {
                senseNodes.add(n);
            }
        }
        //System.out.println("sensedNodes : " + senseNodes.toString());
        return senseNodes;
    }

}

