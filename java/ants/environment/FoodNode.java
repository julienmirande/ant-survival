package ants.environment;

import ants.actors.CellLocatedNode;
import io.jbotsim.core.Node;

import java.util.Random;

public class FoodNode extends CellLocatedNode {

    public static final int MAX_QUANTITY = 10;
    public static final int MIN_QUANTITY = 10;
    public static final int MAX_TTL = 2500;
    public static final int MIN_TTL = 2500;
    private int quantity;
    private int timeToLive;

    public FoodNode(){
        super();
        setIcon("/images/ant-worm.png");
        setWirelessStatus(false);

        setDirection(new Random().nextDouble()*2*Math.PI);
        quantity = new Random().nextInt(MAX_QUANTITY) + MIN_QUANTITY;

        timeToLive = new Random().nextInt(MAX_TTL) + MIN_TTL;

        setIconSize((int)(getIconSize()* quantity /10*0.9));
    }

    @Override
    public void onClock() {
        super.onClock();

        if(timeToLive == 0) {
            this.die();
        }
        else
            timeToLive--;
    }

    @Override
    public void onPostClock() {
        super.onPostClock();
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
        if (quantity <= 0) {
            die();
        }
    }

    public int getQuantity(){
        return quantity;
    }

}
