package ants.actors;

import ants.environment.*;
import io.jbotsim.core.Node;

import java.util.*;

public class AntNode extends WaypointNode {

    public static final int MAX_TTL = 2000;
    public static final int MIN_TTL = 2000;

    private int timeToLive;

    private Cell nextCell;
    private Cell lastCell;
    private Cell lastLastCell;

    private boolean isCharged;
    private boolean isOnQueenCell;
    private boolean isDigging;
    private boolean hasJustFoundFood;
    private boolean hasJustDroppedFood;

    private QueenNode queenNode;

    List<Node> listSensedNodes;
    ArrayList<Cell> listNeighbors;
    ArrayList<Cell> listNeighborsSortedByFood;
    ArrayList<Cell> listNeighborsSortedByQueen;

    double maxIntensity;

    public AntNode(QueenNode qn) {
        super();

        setWirelessStatus(false);

        isCharged = false;
        isOnQueenCell = false;
        isDigging = false;
        hasJustFoundFood = false;
        hasJustDroppedFood = false;

        queenNode = qn;

        timeToLive = new Random().nextInt(MAX_TTL) + MIN_TTL;

        nextCell = null;
        lastCell = null;
        lastLastCell = null;

        listSensedNodes = new ArrayList<Node>();
        listNeighbors = new ArrayList<Cell>();
        listNeighborsSortedByFood = new ArrayList<Cell>();
        listNeighborsSortedByQueen = new ArrayList<Cell>();

        maxIntensity = 0.0;

        setIcon("/images/ant.png");
    }

    @Override
    public void onStart() {
        super.onStart();
        setSensingRange(40);
        setLocation(currentCell);
        lastCell = currentCell;

        antAlgorithm();
    }

    @Override
    public void onClock() {
        super.onClock();

        if (timeToLive == 0)
            this.die();
        else
            timeToLive--;

        // si la fourmi est en train de creuser
        if (isDigging) {
            // si la cellule est creusée, la fourmi arrête de creuser et va sur la cellule qu'elle vient de creuser
            if(nextCell.isDug()){
                isDigging = false;
                goToCell(nextCell);
            }
            // si la cellule n'est pas finie de creuser, la fourmi continue à creuser
            else {
                nextCell.setTimeToDig(nextCell.getTimeToDig() - 1);
                if (nextCell.getTimeToDig() % 5 == 0 && nextCell.getCost() > 5) {
                    nextCell.decrementCost();
                }
            }
        }
    }

    @Override
    public void onArrival() {
        antAlgorithm();
    }

    protected void goToCell(Cell cell) {
        nextCell = cell;

        // si la fourmi n'est pas en train d'attendre
        if (!isDigging) {
            // si la cellule est déjà creusée, la fourmi va sur la cellule
            if (cell.isDug()) {
                lastLastCell = lastCell;
                lastCell = currentCell;
                setCurrentCell(cell);
                addDestination(currentCell);
            }
            // si la cellule n'est pas déjà creusée, la fourmi décide si elle veut creuser ou pas avec une chance sur deux
            else {
                Random random = new Random();
                int rand = random.nextInt(2);

                // si la fourmi décide de ne pas creuser, elle choisit une nouvelle direction
                if (rand == 0) {
                    antAlgorithm();
                }

                // si la fourmi décide de creuser, elle commence à attendre
                else {
                    this.setDirection(nextCell);
                    isDigging = true;
                }
            }
        }
    }

    protected void antAlgorithm() {

        listSensedNodes.clear();
        //System.out.println("clear : " + listSensedNodes.toString());
        //listSensedNodes = getSensedNodes(getTopology(), getSensingRange());
        listSensedNodes = getSensedNodes();
        //System.out.println("listSensedNodes : " + listSensedNodes.toString());
        maxIntensity = 0.0;
        //System.out.println("ListSensedNodes : " + listSensedNodes.toString());

        listNeighbors = currentCell.getAllNeighbors();
        listNeighborsSortedByFood = sortListByFoodIntensity(listNeighbors);

        // SI LA FOURMI N'EST PAS DÉJÀ CHARGÉE (elle cherche de la nourriture)
        if (!this.isCharged) {

            for (Node n : listSensedNodes) {

                // la fourmi inspecte sa cellule
                if (distance(n) < speed) {
                    // si la fourmi est sur une cellule avec de la nourriture, elle prend la nourriture puis retourne à la reine
                    if (n instanceof FoodNode) {
                        takeFood((FoodNode) n);
                        goBackToQueen();
                        return;
                    }
                    // sinon, la fourmi dépose une phéromone de reine sur sa cellule courante
                    else {
                        currentCell.setQueenPheromoneIntensity(currentCell.getQueenPheromoneIntensity() + 0.1);
                    }
                }

                // si la fourmi détecte une cellule avec de la nourriture autour d'elle,
                // elle dépose une phéromone de nourriture sur sa cellule actuelle puis va prendre la nourriture
                if (n instanceof FoodNode && distance(n) < 39) {
                    currentCell.setFoodPheromoneIntensity(currentCell.getFoodPheromoneIntensity() + 0.1);
                    goToCell(((FoodNode) n).getCurrentCell());
                    return;
                }
            }
            
            // s'il y a au moins une cellule avec une phéromone de nourriture, la fourmi va sur la plus intense qui n'est ni sa cellule précédente ni sa pré-précédente
            for(int i = 0; i < listNeighborsSortedByFood.size(); i++){
                if(listNeighborsSortedByFood.get(i).getFoodPheromoneIntensity() > 0.0) {
                    // si la fourmi vient juste de lâcher de la nourriture, elle peut retourner sur sa cellule précédente
                    if(hasJustDroppedFood){
                        hasJustDroppedFood = false;
                        goToCell(listNeighborsSortedByQueen.get(i));
                        return;
                    }
                    if (!listNeighborsSortedByFood.get(i).equals(lastCell) && !listNeighborsSortedByFood.get(i).equals(lastLastCell)) {
                        goToCell(listNeighborsSortedByFood.get(i));
                        return;
                    }
                }
            }

            // si la fourmi n'a rien détecté autour d'elle, elle choisit une destination aléatoire
            Cell cell = pickNeighBoringCell();
            goToCell(cell);
            return;
        }

        // SI LA FOURMI EST DÉJÀ CHARGÉE (elle cherche la reine)
        else {

            // si la fourmi est déjà sur la cellule de la reine, elle lâche sa nourriture, puis repars
            if (isOnQueenCell) {
                dropFood(queenNode);
                isOnQueenCell = false;
                antAlgorithm();
                return;
            }

            // sinon, elle continue de retourner vers la reine
            else {
                goBackToQueen();
                return;
            }
        }
    }

    protected void goBackToQueen() {

        maxIntensity = 0.0;
        listNeighbors = currentCell.getAllNeighbors();
        listNeighborsSortedByQueen = sortListByQueenIntensity(listNeighbors);

        // si la fourmi vient juste de prendre de la nourriture, elle retourne sur sa cellule précédente
        if(hasJustFoundFood){
            hasJustFoundFood = false;
            goToCell(lastCell);
            return;
        }
        // la fourmi dépose une phéromone de nourriture sur sa cellule courante si ce n'est pas celle où elle a pris la nourriture
        else
            currentCell.setFoodPheromoneIntensity(currentCell.getFoodPheromoneIntensity() + 0.1);

        // la fourmi inspecte les cellules autour d'elle
        for (Node n : listSensedNodes) {
            // si la fourmi détecte la reine,
            // elle dépose une phéromone de reine sur sa cellule actuelle puis va sur la cellule de la reine
            if (n instanceof QueenNode && distance(n) < 39) {
                if (n.equals(queenNode)) {
                    //System.out.println("Ant " + this.toString() + " : Found Queen");
                    isOnQueenCell = true;
                    currentCell.setQueenPheromoneIntensity(currentCell.getQueenPheromoneIntensity() + 0.1);
                    goToCell(queenNode.getCurrentCell());
                    return;
                }
            }
        }

        // s'il y a au moins une cellule avec une phéromone de reine,
        for(int i = 0; i < listNeighborsSortedByQueen.size(); i++){
            // la fourmi va sur la plus intense qui n'est ni sa cellule précédente ni sa pré-précédente
            if(listNeighborsSortedByQueen.get(i).getQueenPheromoneIntensity() > 0.0
                    && !listNeighborsSortedByQueen.get(i).equals(lastCell)
                    && !listNeighborsSortedByQueen.get(i).equals(lastLastCell)) {
                //System.out.println("Ant " + this.toString() + " : Found QueenPheromone");
                goToCell(listNeighborsSortedByQueen.get(i));
                return;
            }
        }

        // si la fourmi n'a rien détecté autour d'elle, elle choisit une destination aléatoire
        Cell cell = pickNeighBoringCell();
        goToCell(cell);
        return;
    }

    protected ArrayList<Cell> sortListByFoodIntensity(ArrayList<Cell> l) {
        Collections.sort(listNeighbors, new Comparator<Cell>() {
            @Override
            public int compare(Cell c1, Cell c2) {
                if (c1.getFoodPheromoneIntensity() == c2.getFoodPheromoneIntensity())
                    return 0;
                if (c1.getFoodPheromoneIntensity() > c2.getFoodPheromoneIntensity())
                    return -1;
                else
                    return 1;
            }
        });
        return l;
    }

    protected ArrayList<Cell> sortListByQueenIntensity(ArrayList<Cell> l) {
        Collections.sort(listNeighbors, new Comparator<Cell>() {
            @Override
            public int compare(Cell c1, Cell c2) {
                if (c1.getQueenPheromoneIntensity() == c2.getQueenPheromoneIntensity())
                    return 0;
                if (c1.getQueenPheromoneIntensity() > c2.getQueenPheromoneIntensity())
                    return -1;
                else
                    return 1;
            }
        });
        return l;
    }


    protected Cell pickNeighBoringCell() {
        //System.out.println("Ant " + this.toString() + " : pickNeighboringCell");
        Cell NextCell = null;
        Random random = new Random();
        int rand;

        // la fourmi choisit une direction aléatoire qui n'est pas sa cellule précédente et qui n'a pas d'obstacle
        while (NextCell == null || NextCell.equals(lastCell) || NextCell.HasObstacle()) {
            rand = random.nextInt(8);
            switch (rand) {
                case 0:
                    NextCell = getCurrentCell().getRightNeighbor();
                    break;
                case 1:
                    NextCell = getCurrentCell().getLeftNeighbor();
                    break;
                case 2:
                    NextCell = getCurrentCell().getTopNeighbor();
                    break;
                case 3:
                    NextCell = getCurrentCell().getBottomNeighbor();
                    break;
                case 4:
                    NextCell = getCurrentCell().getTopRightNeighbor();
                    break;
                case 5:
                    NextCell = getCurrentCell().getBottomRightNeighbor();
                    break;
                case 6:
                    NextCell = getCurrentCell().getBottomLeftNeighbor();
                    break;
                case 7:
                    NextCell = getCurrentCell().getTopLeftNeighbor();
                    break;
                default:
                    break;
            }
        }
        return NextCell;
    }

    public void takeFood(FoodNode fn) {
        if (!fn.equals(null)) {
            //System.out.println("Ant " + this.toString() + " : takeFood");
            this.setIcon("/images/ant-bean.png");
            this.isCharged = true;
            hasJustFoundFood = true;
            fn.setQuantity(fn.getQuantity() - 1);
        }
    }

    public void dropFood(QueenNode qn) {
        this.setIcon("/images/ant.png");
        this.isCharged = false;
        hasJustDroppedFood = true;
        qn.setFoodStock(qn.getFoodStock() + 1);
    }
}



