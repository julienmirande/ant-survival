package ants.actors;

import java.util.Random;

public class QueenNode extends CellLocatedNode {

    public int getFoodStock() {
        return foodStock;
    }

    public void setFoodStock(int foodStock) {
        this.foodStock = foodStock;
    }

    private int foodStock;
    private int nbAnts;

    public QueenNode(){
        super();
        foodStock = 10;
        nbAnts = 0;
        setIcon("/images/ant-queen.png");
        setIconSize(getIconSize()*2);
    }

    @Override
    public void onClock() {

       if (shouldProduceOffspring())
            produceOffspring();
    }

    @Override
    public void onPostClock() {
        super.onPostClock();
    }

    private boolean shouldProduceOffspring() {
        return new Random().nextDouble() < 0.01;
    }

    public void produceOffspring(){
        if(foodStock <= 0){
            die();
        }

        foodStock--;

        AntNode babyAnt = new AntNode(this);
        babyAnt.setCurrentCell(getCurrentCell());
        getTopology().addNode(babyAnt);
    }
}
