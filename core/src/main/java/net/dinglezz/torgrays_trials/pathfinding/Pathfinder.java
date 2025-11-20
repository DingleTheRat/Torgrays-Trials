package net.dinglezz.torgrays_trials.pathfinding;

import net.dinglezz.torgrays_trials.main.Game;
import net.dinglezz.torgrays_trials.tile.TileManager;
import net.dinglezz.torgrays_trials.tile.TilePoint;

import java.util.ArrayList;
import java.util.HashMap;

public class Pathfinder {
    Game game;
    HashMap<Integer, HashMap<Integer, Node>> node = new HashMap<>();
    ArrayList<Node> openList = new ArrayList<>();
    public ArrayList<Node> pathList = new ArrayList<>();
    Node startNode, goalNode, currentNode;
    boolean goalReached = false;
    int step = 0;

    public Pathfinder(Game game) {
        this.game = game;
        placeNodes();
    }

    public void placeNodes() {
        int col = 0;
        int row = 0;
        while (col < game.maxWorldCol && row < game.maxWorldRow) {
            node.computeIfAbsent(col, k -> new HashMap<>()).put(row, new Node(col, row));

            col++;
            if (col == game.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }
    public void resetNodes() {
        int col = 0;
        int row = 0;
        while (col < game.maxWorldCol && row < game.maxWorldRow) {
            // Reset open, checked and solid states
            node.get(col).get(row).open = false;
            node.get(col).get(row).checked = false;
            node.get(col).get(row).solid = false;

            col++;
            if (col == game.maxWorldCol) {
                col = 0;
                row++;
            }
        }

        // Reset some other stuff
        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }
    public void setNodes(int startCol, int startRow, int goalCol, int goalRow) {
        resetNodes();

        // Set start and goal nodes
        startNode = node.get(startCol).get(startRow);
        currentNode = startNode;
        goalNode = node.get(goalCol).get(goalRow);
        openList.add(currentNode);


        int col = 0;
        int row = 0;
        while (col < game.maxWorldCol && row < game.maxWorldRow) {
            // Set the solid nodes
            int tileNumber = TileManager.mapTileNumbers.get("foreground").get(new TilePoint(game.currentMap, col, row));
            if (TileManager.tile.get(tileNumber).collision) {
                node.get(col).get(row).solid = true;
            }

            // Set the cost
            getCost(node.get(col).get(row));

            col++;
            if (col == game.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }
    public void getCost(Node node) {
        // G cost
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;

        // H cost
        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;

        // F cost
        node.fCost = node.gCost + node.hCost;
    }
    public boolean search() {
        while (!goalReached && step < 500) {
            int col = currentNode.col;
            int row = currentNode.row;

            // Check the current node
            currentNode.checked = true;
            openList.remove(currentNode);

            // Open the node on the top
            if (row - 1 >= 0) {
                openNode(node.get(col).get(row - 1));
            }

            // Open the node on the bottom
            if (row + 1 < game.maxWorldRow) {
                openNode(node.get(col).get(row + 1));
            }

            // Open the node on the left
            if (col - 1 >= 0) {
                openNode(node.get(col - 1).get(row));
            }

            // Open the node on the right
            if (col + 1 < game.maxWorldCol) {
                openNode(node.get(col + 1).get(row));
            }

            // Find the best Node
            int bestNodeIndex = 0;
            int bestNodeFCost = Integer.MAX_VALUE;

            for (int i = 0; i < openList.size(); i++) {
                if (openList.get(i).fCost < bestNodeFCost) {
                    bestNodeIndex = i;
                    bestNodeFCost = openList.get(i).fCost;
                } else if (openList.get(i).fCost == bestNodeFCost) {
                    if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost) {
                        bestNodeIndex = i;
                    }
                }
            }

            if (openList.isEmpty()) {break;}
            currentNode = openList.get(bestNodeIndex);

            if (currentNode == goalNode) {
                goalReached = true;
                trackPath();
            }
            step++;
        }
        return goalReached;
    }
    public void openNode(Node node) {
        if (!node.open && !node.checked && !node.solid) {
            node.open = true;
            node.parent = currentNode;
            openList.add(node);
        }
    }
    public void trackPath() {
        Node current = goalNode;

        while (current != startNode) {
            pathList.addFirst(current);
            current = current.parent;
        }
    }
}
