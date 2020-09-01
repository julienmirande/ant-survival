package ants.actors;

import io.jbotsim.core.Node;
import io.jbotsim.core.Point;

import java.util.LinkedList;
import java.util.Queue;

/**
 * This type of Node can move over a sequence of destinations,
 * specified through the addDestination() method.
 */
public class WaypointNode extends CellLocatedNode {
    Queue<Point> destinations = new LinkedList<Point>();
    double speed = 1;

    @Override
    public void onClock() {
        //System.out.println("onClock WPN");
        if ( ! destinations.isEmpty() ){
            Point dest = destinations.peek();
            if (distance(dest) > speed){
                setDirection(dest);
                move(speed);
            }else{
                setLocation(dest);
                destinations.poll();
                onArrival();
            }
        }
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void addDestination(Point destination){
        destinations.add(destination);
    }

    public void addDestination(double x, double y){
        addDestination(new Point(x, y));
    }

    public void onArrival(){
    }
}
