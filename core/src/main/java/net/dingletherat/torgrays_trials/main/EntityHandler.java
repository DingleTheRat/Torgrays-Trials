// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.json.JSONArray;
import org.json.JSONObject;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.component.*;

public class EntityHandler {
    public static final Map<String, Map<Class<? extends Component>, List<Object>>> TEMPLATES = new HashMap<>();

    /**
     * Loops through the given componentData and finds the corresponding classes returning them in a map with the args.
     * @param componentData A list of JSONObjects that contains all the componentData.
     * Each JSONObject needs to contain the filepath to the component it wants to add ("component") and the arguments for its constructor ("args")
     * @param location Whenever an error is thrown, which JSON file should the error blame?
     * @return Returns a map with the corresponding component class as a key and a list of arguments as the value.
     **/
    public static Map<Class<? extends Component>, List<Object>> getComponentClasses(JSONArray componentData, String location) {
        // Convert the componentData JSONArray to a list of JSONObject so it's easier to manage
        List<JSONObject> components = IntStream.range(0, componentData.length())
            .mapToObj(componentData::getJSONObject)
            .toList();

        // Create the list in which we will put our component classes in
        Map<Class<? extends Component>, List<Object>> componentClasses = new HashMap<>();

        // Loop through all the component strings, get its corresponding class via the forName method, and add it to the componentClasses List
        for (JSONObject component : components) {
            // Make sure the necessary stuff is inside the JSONObject. If not, warn and continue
            if (!component.has("component") || !(component.get("component") instanceof String)) {
                Main.LOGGER.error("[Location: {}] A 'component' field is missing or is not a String.", location);
                continue;
            }

            String componentPath = component.getString("component");
            if (!component.has("args") || !(component.get("args") instanceof JSONArray)) {
                Main.LOGGER.error("[Location: {}] 'args' field in '{}' component is missing or is not a JSONArray.", location, componentPath);
                continue;
            }

            // Get the component class. If it does not exist, it will throw a class not found exception. In that case, we continue and warn
            Class<? extends Component> componentClass;
            try {
                componentClass = Class.forName(componentPath).asSubclass(Component.class);
            } catch (ClassNotFoundException exception) {
                Main.LOGGER.error("[Location: {}] Component path '{}' is not a path to a component or does not implement the 'Component' interface!", location, componentPath);
                continue;
            }

            // Convert the "args" JSONArray into a list and add it into the componentClasses Map.
            JSONArray argsArray = component.getJSONArray("args");
            List<Object> args = new ArrayList<>(IntStream.range(0, argsArray.length())
                            .mapToObj(argsArray::get)
                            .toList());
            componentClasses.put(componentClass, args);
        }
        return componentClasses;
    }

    /**
     * Loops through the given componentData and finds the corresponding classes from the original map, removing them from the original map.
     * @param original The map from which the components will be removed
     * @param componentData A list of JSONObjects that contains all the componentData.
     * Each JSONObject must contain a string filepath of the component its wanting to remove ("component")
     * @param location Whenever an error is thrown, which JSON file should the error blame?
     * @return The map provided, just missing the stuff you asked the method to remove.
     **/
    public static Map<Class<? extends Component>, List<Object>> removeComponentClasses(Map<Class<? extends Component>, List<Object>> original, JSONArray componentData, String location) {
        // Convert the componentData JSONArray to a list of JSONObject so it's easier to manage
        List<JSONObject> components = IntStream.range(0, componentData.length())
            .mapToObj(componentData::getJSONObject)
            .toList();

        // Create a copy map of the original that will be returned once we finish tinkering with it.
        Map<Class<? extends Component>, List<Object>> returnMap = original;

        // Loop through all the component strings, getting the class to remove and either remove all instances or remove one with a specified ID
        for (JSONObject component : components) {
            // Make sure the necessary stuff is inside the JSONObject. If not, warn and continue
            if (!component.has("component") || !(component.get("component") instanceof String)) {
                Main.LOGGER.error("[Location: {}] A 'component' field is missing or is not a String.", location);
                continue;
            }

            // Get the component class. If it does not exist, it will throw a class not found exception. In that case, we continue and warn
            String componentPath = component.getString("component");
            Class<? extends Component> componentClass;
            try {
                componentClass = Class.forName(componentPath).asSubclass(Component.class);
            } catch (ClassNotFoundException exception) {
                Main.LOGGER.error("[Location: {}] Component path '{}' is not a path to a class or does not extend the 'Component' interface!", location, componentPath);
                continue;
            }

            // Now, get rid of the class
            returnMap.remove(componentClass);
        }
        return returnMap;
    }

    /**
     * Goes through the componentData seeing wether a JSONObject wants to remove a componentClass from the original map or add one.
     * @param original The map that will be modified
     * @param componentData A list of JSONObjects that contains all the componentData.
     * Each JSONObject needs to have a filepath string to the component ("component") and an action boolean ("action").
     * If the field is true, it's set to add a component. If false, it's set to remove.
     * A JSONObject that's adding a component must also provide an JSONArray of arguments ("args").
     * @param location Whenever an error is thrown, which JSON file should the error blame?
     * @return The map provided, just modified via the componentData provided.
     **/
    public static Map<Class<? extends Component>, List<Object>> modifyComponentClasses(Map<Class<? extends Component>, List<Object>> original, JSONArray componentData, String location) {
        // Convert the componentData JSONArray to a list of JSONObject so it's easier to manage
        List<JSONObject> components = IntStream.range(0, componentData.length())
            .mapToObj(componentData::getJSONObject)
            .toList();

        // Declare two JSONArrays, with one holding all the components that are set to remove, the other holding all the ones to add
        JSONArray additions = new JSONArray();
        JSONArray removals = new JSONArray();

        // Loop through all the component strings, checking its "action" to see where to add it
        for (JSONObject component : components) {
            // Make sure the necessary stuff is inside the JSONObject. If not, warn and continue
            if (!component.has("action") || !(component.get("action") instanceof Boolean)) {
                Main.LOGGER.error("[Location: {}] An 'action' field is missing or is not a boolean.", location);
                continue;
            }

            // Now add the component to either additions or removals
            if (component.getBoolean("action")) additions.put(component);
            else removals.put(component);
        }

        // Now declare the return map and modify it via the removal and additon methods
        Map<Class<? extends Component>, List<Object>> returnMap = original;
        returnMap.putAll(getComponentClasses(additions, location));
        returnMap = removeComponentClasses(returnMap, removals, location);

        return returnMap;
    }

    public static void generateTemplates() {
        TEMPLATES.clear();
        String filepath = "values/templates/";

        // Get the fileNames of everything inside the directory from the filepath
        List<String> fileNames = UtilityTool.getDecendantFileNames(filepath, ".json");

        // Append the filepath onto the fileNames
        fileNames.replaceAll(name -> filepath + name);

        // Create a new jsons list that contains a list of all the fileNames in JSONObject form
        List<JSONObject> jsons = new ArrayList<>(fileNames.stream()
            .map(UtilityTool::getJsonObject)
            .toList());

        // Get rid of the json objects that returned null
        jsons.removeIf(Objects::isNull);

        // Loop through the rest of the stuff
        for (JSONObject json : jsons) {
            // Check if the json has the necessary stuff. If not, warn and continue
            if (!json.has("name") || !(json.get("name") instanceof String)) {
                Main.LOGGER.warn("Invalid entity template '{}': 'name' field is missing or is not a String.", fileNames.get(jsons.indexOf(json)));
                continue;
            }
            if (!json.has("components") || !(json.get("components") instanceof JSONArray)) {
                Main.LOGGER.warn("Invalid entity template '{}': 'components' field is missing or is not a JSONArray.", fileNames.get(jsons.indexOf(json)));
                continue;
            }

            // Get the components
            JSONArray components = json.getJSONArray("components");

            Map<Class<? extends Component>, List<Object>> componentClasses = getComponentClasses(components, fileNames.get(jsons.indexOf(json)));

            // Add in the name component
            componentClasses.put(NameComponent.class, List.of(json.getString("name")));

            // Last but not least, add our list of componentClasses to the TEMPLATES HashMap
            TEMPLATES.put(json.getString("name"), componentClasses);
        }

        Main.LOGGER.info("Loaded {} templates!", TEMPLATES.size());
    }

    public static List<Integer> queryAll(Class<? extends Component>... components) {
        List<Integer> result = new ArrayList<>();
        boolean wantsCooldown = Arrays.asList(components).contains(CooldownComponent.class);

        for (int entity : Main.world.getEntities().keySet()) {
            if (getComponent(entity, CooldownComponent.class).isPresent() && !wantsCooldown) continue;

            boolean match = true;

            for (Class<? extends Component> componentClass : components) {
                boolean found = false;

                for (Component component : Main.world.getEntities().get(entity)) {

                    if (componentClass.isInstance(component)) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    match = false;
                    break;
                }
            }

            if (match) result.add(entity);
        }

        return result;
    }
    public static List<Integer> queryAny(Class<? extends Component>... components) {
        List<Integer> result = new ArrayList<>();
        boolean wantsCooldown = Arrays.asList(components).contains(CooldownComponent.class);

        for (int entity : Main.world.getEntities().keySet()) {
            if (getComponent(entity, CooldownComponent.class).isPresent() && !wantsCooldown) continue;
            boolean match = false;

            for (Class<? extends Component> componentClass : components) {
                for (Component component : Main.world.getEntities().get(entity)) {
                    if (componentClass.isInstance(component)) {
                        match = true;
                        break;
                    }
                }
            }

            if (match) result.add(entity);
        }

        return result;
    }
    public static <T extends Component> Optional<T> getComponent(int identifier, Class<T> type) {
        if (!Main.world.getEntities().containsKey(identifier)) return Optional.empty();

        List<Component> entity = Main.world.getEntities().get(identifier);

        for (Component element : entity) {
            if (type.isInstance(element)) {
                return Optional.of((T) element);
            }
        }
        return Optional.empty();
    }
    public static <T extends Component> List<T> getComponents(int identifier, Class<T> type) {
        List<T> result = new ArrayList<>();
        List<Component> entity = Main.world.getEntities().get(identifier);

        for (Component component : entity) {
            if (type.isInstance(component)) {
                result.add(type.cast(component));
            }
        }
        return result;
    }
    public static int getClosestEntity(int entity, String name, Class<? extends Component>... components) {
        // Declare return variable
        float closestDistanceSquared = Float.MAX_VALUE;
        int returnEntity = -1;

        // Get necessary component from main entity and make sure it exists
        PositionComponent positionComponent = getComponent(entity, PositionComponent.class).orElse(null);

        // Add in a positionComponent as one of the components needed to query
        Class<? extends Component>[] queryComponents;
        if (components == null || components.length == 0) queryComponents = new Class[]{PositionComponent.class};
        else queryComponents = Stream.concat(Stream.of(PositionComponent.class), Arrays.stream(components)).toArray(Class[]::new);

        for (int targetEntity : queryAll(queryComponents)) {
            // Get necessary components
            PositionComponent targetPosition = getComponent(targetEntity, PositionComponent.class).get();
            NameComponent targetName = getComponent(targetEntity, NameComponent.class).get();

            // Make sure the name matches the target one. However, if the name property is empty, let it pass anyway
            if (!targetName.name.equals(name) && !name.isEmpty()) continue;

            // Get the distance between the target entity and the entity
            float distanceX = targetPosition.x - positionComponent.x;
            float distanceY = targetPosition.y - positionComponent.y;

            // Square it to compare it! (Best rhyme ever)
            float distanceSquared = distanceX * distanceX + distanceY * distanceY;

            // Now do the comparing part I mentioned. If it's larger than the current closest distance, set it as the returnEntity for now.
            if (distanceSquared < closestDistanceSquared) {
                closestDistanceSquared = distanceSquared;
                returnEntity = targetEntity;
            }
        }

        return returnEntity;
    }
}
