// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.component.MovementComponent;
import net.dingletherat.torgrays_trials.component.PositionComponent;
import net.dingletherat.torgrays_trials.main.States.MovementStates;
import net.dingletherat.torgrays_trials.rendering.Map;
import net.dingletherat.torgrays_trials.system.TileSystem;

public class PathfindingHandler {
    public static class Node {
        public final int x;
        public final int y;

        public int gCost;
        public int hCost;
        public int fCost;

        public Node parent;

        public Node(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void calculateFCost() {
           this.fCost = this.gCost + this.hCost;
        }
    }

    public static HashMap<TileSystem.Pair, Node> generateNodes() {
        // Create the HashMap that we'll return
        HashMap<TileSystem.Pair, Node> returnMap = new HashMap<>();

        // Get the current map
        Map map = TileSystem.maps.get(Main.world.getMap());

        /* Loop through all the pairs in the map (pairs are just records with X and Y values) and create a node for each
           Each pair represents a tile, so looping through all pairs is basically looping through all tiles */
        for (TileSystem.Pair pair : map.foreground().keySet())
            returnMap.put(pair, new Node(pair.x(), pair.y()));

        return returnMap;
    }
    public static List<TileSystem.Pair> getNeighbors(TileSystem.Pair pair) {
        return List.of(
            new TileSystem.Pair(pair.x() + 1, pair.y()),
            new TileSystem.Pair(pair.x() - 1, pair.y()),
            new TileSystem.Pair(pair.x(), pair.y() + 1),
            new TileSystem.Pair(pair.x(), pair.y() - 1)
        );
    }
    public static int calculateHeuristic(Node nodeA, Node nodeB) {
        return Math.abs(nodeA.x - nodeB.x) + Math.abs(nodeA.y - nodeB.y);
    }

    public static List<TileSystem.Pair> generatePath(Node endNode) {
        // Create our regular return list
        List<TileSystem.Pair> finalPath = new ArrayList<>();

        // Declare the current node as the end node, but it'll eventually change to that node's parent and so on
        Node current = endNode;

        // Go through the chain of parent nodes until we reach the final startNode, the one with no parent
        while (current != null) {
            // Add the node, or actually its position (in pairs) to our favourite return list
            finalPath.add(new TileSystem.Pair(current.x, current.y));

            // Then, move onto our next target :3
            current = current.parent;
        }

        // Now, the path we made here actually sucks cuz it's reversed, so fix that
        Collections.reverse(finalPath);

        return finalPath;
    }
    public static List<TileSystem.Pair> findPath(TileSystem.Pair start, TileSystem.Pair end) {
        // Get all the nodes in the map and get the start and end nodes from that
        HashMap<TileSystem.Pair, Node> nodes = generateNodes();
        Node startNode = nodes.get(start);
        Node endNode = nodes.get(end);

        // Check if the start and end nodes exist. If not, error and return
        if (startNode == null || endNode == null){
            Main.LOGGER.error("Failed to find path: Start or end nodes not found!");
            return new ArrayList<>();
        }

        /* Create a queue and a set for the open and closed nodes. I could have used lists, but this is generally more efficient.
          The opened queue contains all the nodes that have not yet been checked, the closed one contains all the ones that have been checked */
        PriorityQueue<Node> opened = new PriorityQueue<>(Comparator.comparingInt(node -> node.fCost)); // The priority is the lower cost ones
        Set<Node> closed = new HashSet<>();

        // Initialize the starting node costs
        startNode.gCost = 0;
        startNode.hCost = calculateHeuristic(startNode, endNode);
        startNode.calculateFCost();

        // Add in our start node into the opened queue to set off a chain reaction of checking
        opened.add(startNode);

        // Now begin our chain reaction and don't stop it until all nodes are checked
        while (!opened.isEmpty()) {
            // Get the node with the lowest cost (the current one)
            Node current = opened.poll();

            // Close the node
            closed.add(current);

            // If our target node has been reached, we're done :D, so just generate the path and return it
            if (current == endNode) return generatePath(endNode);

            // Otherwise, get its pairs
            TileSystem.Pair currentPair = new TileSystem.Pair(current.x, current.y);

            // Loop through all the currentPair's neighbors
            for (TileSystem.Pair neighborPair : getNeighbors(currentPair)) {
                // Get the node for this neighboring tile, making sure that's its not a null peice of junk
                Node neighbor = nodes.get(neighborPair);
                if (neighbor == null) continue;

                // If the neighbor is collidable, rule it out, it's useless
                if (TileSystem.getTileCollision(neighborPair)) continue;

                // Skip it if we checked it already
                if (closed.contains(neighbor)) continue;

                // Calculate the cost to move from the start to this neighbor through the current node
                int nextGCost = current.gCost + 1;

                // Check whether or not the neighbor is already in the open set
                boolean inOpenSet = opened.contains(neighbor);

                // If the neighbor is not in the open set, or this path to it is cheaper, update it
                if (nextGCost < neighbor.gCost || !inOpenSet) {
                    // Set the parent so the path can be re-traced
                    neighbor.parent = current;

                    // Update cost values for the neighbor
                    neighbor.gCost = nextGCost;
                    neighbor.hCost = calculateHeuristic(neighbor, endNode);
                    neighbor.calculateFCost();

                    // If the neighbor is not already in opened, open it up
                    if (!inOpenSet) opened.add(neighbor);
                }
            }
        }

        // If the target was never reached, then there propably isn't any path
        return new ArrayList<>();
    }

    public static boolean moveToTarget(int entity, TileSystem.Pair target) {
        // Get necessary components and check if they're there. If not, return
        PositionComponent positionComponent = EntityHandler.getComponent(entity, PositionComponent.class).orElse(null);
        MovementComponent movementComponent = EntityHandler.getComponent(entity, MovementComponent.class).orElse(null);
        if (positionComponent == null || movementComponent == null) return false;

        // Get the X and Y distances between the target place and the entity in world units
        float targetX = target.x() * Main.tileSize;
        float targetY = target.y() * Main.tileSize;
        float distanceX = targetX - positionComponent.x;
        float distanceY = targetY - positionComponent.y;

        // Declare the epsilon (it's a small tolerance to avoid float jitter)
        float epsilon = movementComponent.speed + 0.5f;

        // If we reached the target, snap to it exactly, stop movement and return true
        if (Math.abs(distanceX) < epsilon && Math.abs(distanceY) < epsilon) {
            positionComponent.x = targetX;
            positionComponent.y = targetY;
            movementComponent.direction = "";
            movementComponent.state = MovementStates.IDLE;
            return true;
        }

        // Use those distances to determine in which direction the entity should go
        if (distanceX > 0 && distanceY > 0) movementComponent.direction = "down right";
        else if (distanceX > 0 && distanceY < 0) movementComponent.direction = "up right";
        else if (distanceX < 0 && distanceY > 0) movementComponent.direction = "down left";
        else if (distanceX < 0 && distanceY < 0) movementComponent.direction = "up left";
        else if (distanceX > 0) movementComponent.direction = "right";
        else if (distanceX < 0) movementComponent.direction = "left";
        else if (distanceY > 0) movementComponent.direction = "down";
        else if (distanceY < 0) movementComponent.direction = "up";

        // Set the state to walking so the movement system processes this entity
        movementComponent.state = MovementStates.WALKING;
        return false;
    }
}
