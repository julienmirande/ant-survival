package ants.environment;


import io.jbotsim.core.Point;
import io.jbotsim.core.event.ClockListener;

import java.io.IOException;
import java.util.*;

public class Cell extends Point implements ClockListener  {

    public static final int MAX_COST_VALUE = 40;
    public static final int MIN_COST_VALUE = 1;
    public static final int MAX_TTL = 5000;
    public static final int MIN_TTL = 1000;
    public static final int MIN_TTD = 100;
    private int cost;

    private int foodPheromoneTTL;
    private int queenPheromoneTTL;
    private int timeToDig;

    private boolean isDug;
    private boolean hasObstacle;

    private double foodPheromoneIntensity;
    private double queenPheromoneIntensity;
    Map<Integer, Cell> neighBor = new HashMap<>();

    public Cell(Point location){
        super(location);
        cost = new Random().nextInt(MAX_COST_VALUE - MIN_COST_VALUE+1) + MIN_COST_VALUE+1;
        foodPheromoneTTL = MIN_TTL;
        queenPheromoneTTL = MAX_TTL;
        timeToDig = MIN_TTD;

        isDug = false;
        hasObstacle = false;

        foodPheromoneIntensity = 0.0;
        queenPheromoneIntensity = 0.0;

    }

    @Override
    public void onClock() {
        if(foodPheromoneIntensity > 0)
            foodPheromoneTTL--;

        if(foodPheromoneTTL <= 0){
            foodPheromoneTTL = MIN_TTL;
            foodPheromoneIntensity -= 0.1;
        }

        if(queenPheromoneIntensity > 0)
        {
            queenPheromoneTTL--;

        }

        if(queenPheromoneTTL <= 0) {
            queenPheromoneTTL = MAX_TTL;
            queenPheromoneIntensity -= 0.1;
        }
    }

    public Cell getNeighBor(int index) {
        return neighBor.get(index);
    }
    public void setNeighBor(int index, Cell value) {
        neighBor.put(index, value);
    }

    public static final int RIGHT = 0;
    public static final int LEFT = 1;
    public static final int TOP = 2;
    public static final int BOTTOM = 3;
    public static final int TOP_RIGHT = 4;
    public static final int BOTTOM_RIGHT = 5;
    public static final int BOTTOM_LEFT = 6;
    public static final int TOP_LEFT = 7;

    public Cell getRightNeighbor() {
        return getNeighBor(RIGHT);
    }
    public void setRightNeighbor(Cell neighbor) {
        setNeighBor(RIGHT, neighbor);
    }
    public Cell getLeftNeighbor() {
        return getNeighBor(LEFT);
    }
    public void setLefNeighbor(Cell neighbor) {
        setNeighBor(LEFT, neighbor);
    }
    public Cell getTopNeighbor() {
        return getNeighBor(TOP);
    }
    public void setTopNeighbor(Cell neighbor) {
        setNeighBor(TOP, neighbor);
    }
    public Cell getBottomNeighbor() {
        return getNeighBor(BOTTOM);
    }
    public void setBottomNeighbor(Cell neighbor) {
        setNeighBor(BOTTOM, neighbor);
    }

    public Cell getTopRightNeighbor() {
        return getNeighBor(TOP_RIGHT);
    }
    public void setTopRightNeighbor(Cell neighbor) {
        setNeighBor(TOP_RIGHT, neighbor);
    }
    public Cell getTopLeftNeighbor() {
        return getNeighBor(TOP_LEFT);
    }
    public void setTopLefNeighbor(Cell neighbor) {
        setNeighBor(TOP_LEFT, neighbor);
    }
    public Cell getBottomRightNeighbor() {
        return getNeighBor(BOTTOM_RIGHT);
    }
    public void setBottomRightNeighbor(Cell neighbor) {
        setNeighBor(BOTTOM_RIGHT, neighbor);
    }
    public Cell getBottomLeftNeighbor() {
        return getNeighBor(BOTTOM_LEFT);
    }
    public void setBottomLeftNeighbor(Cell neighbor) {
        setNeighBor(BOTTOM_LEFT, neighbor);
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void decrementCost() {
        cost -= 4;
        if(cost < MIN_COST_VALUE)
            cost = MIN_COST_VALUE;
    }

    public ArrayList<Cell> getAllNeighbors(){
        ArrayList<Cell> neighBors = new ArrayList<Cell>();
        if (getLeftNeighbor() != null)
            neighBors.add(getLeftNeighbor());
        if (getTopNeighbor() != null)
            neighBors.add(getTopNeighbor());
        if (getBottomNeighbor() != null)
            neighBors.add((getBottomNeighbor()));
         if (getTopRightNeighbor() != null)
            neighBors.add(getTopRightNeighbor());
        if (getTopLeftNeighbor() != null)
            neighBors.add(getTopLeftNeighbor());
        if (getBottomLeftNeighbor() != null)
            neighBors.add(getBottomLeftNeighbor());
        if (getBottomRightNeighbor() != null)
            neighBors.add(getBottomRightNeighbor());
        if (getRightNeighbor() != null)
            neighBors.add(getRightNeighbor());

        return neighBors;
    }

    public boolean isDug() {
        return isDug;
    }

    public void setDug(boolean dug) {
        isDug = dug;
    }

    public double getFoodPheromoneIntensity() {
        return foodPheromoneIntensity;
    }

    public void setFoodPheromoneIntensity(double foodPheromoneIntensity) {
        if(foodPheromoneIntensity < 1)
            this.foodPheromoneIntensity = foodPheromoneIntensity;
    }

    public double getQueenPheromoneIntensity() {
        return queenPheromoneIntensity;
    }

    public void setQueenPheromoneIntensity(double queenPheromoneIntensity) {
        if(queenPheromoneIntensity < 1)
            this.queenPheromoneIntensity = queenPheromoneIntensity;
    }

    public int getTimeToDig() {
        return timeToDig;
    }

    public void setTimeToDig(int timeToDig) {
        this.timeToDig = timeToDig;
        if (this.timeToDig <= 0) {
            isDug = true;
            setCost(1);
        }
    }

    public boolean HasObstacle() {
        return hasObstacle;
    }

    public void setHasObstacle(boolean hasObstacle) {
        this.hasObstacle = hasObstacle;
    }
}
