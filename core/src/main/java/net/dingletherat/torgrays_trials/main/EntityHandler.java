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
    // JSON keys for stuff
    public static final String KEY_COMPONENT = "component";
    public static final String KEY_ARGS = "args";
    public static final String KEY_ENTRY_INDEX = "entry index";
    public static final String KEY_ACTION = "action";

    public static final Map<String, List<Component.Entry>> TEMPLATES = new HashMap<>();

    /**
     * Loops through the given componentData and finds the corresponding classes returning them in a list with the args.
     * @param componentData A list of JSONObjects that contains all the componentData.
     * Each JSONObject needs to contain the filepath to the component it wants to add ("component") and the arguments for its constructor ("args")
     * @param location Whenever an error is thrown, which JSON file should the error blame?
     * @return Returns a list with the corresponding component class as a key and a list of arguments as the value.
     **/
    public static List<Component.Entry> getComponentClasses(JSONArray componentData, String location) {
        // Convert the componentData JSONArray to a list of JSONObject so it's easier to manage
        List<JSONObject> components = IntStream.range(0, componentData.length())
            .mapToObj(componentData::getJSONObject)
            .toList();

        // Create the list in which we will put our component classes in
        List<Component.Entry> componentClasses = new ArrayList<>();

        // Loop through all the component strings, get its corresponding class via the forName method, and add it to the componentClasses List
        for (JSONObject component : components) {
            // Make sure the necessary stuff is inside the JSONObject. If not, warn and continue
            if (!component.has(KEY_COMPONENT) || !(component.get(KEY_COMPONENT) instanceof String)) {
                Main.LOGGER.error("[Location: {}] A \"{}\" field is missing or is not a String.", KEY_COMPONENT, location);
                continue;
            }

            String componentPath = component.getString(KEY_COMPONENT);
            if (!component.has(KEY_ARGS) || !(component.get(KEY_ARGS) instanceof JSONArray)) {
                Main.LOGGER.error("[Location: {}] \"{}\" field in '{}' component is missing or is not a JSONArray.", location, KEY_ARGS, componentPath);
                continue;
            }

            // Get the component class. If it does not exist, it will throw a class not found exception. In that case, we continue and warn
            Class<? extends Component> componentClass;
            try {
                componentClass = Class.forName(componentPath).asSubclass(Component.class);
            } catch (ClassNotFoundException exception) {
                Main.LOGGER.error("[Location: {}] Component path \"{}\" is not a path to a component or does not implement the {} interface!", location, componentPath, Component.class.getSimpleName());
                continue;
            }

            // Convert the "args" JSONArray into a list and turn it into an entry, ready to be added
            JSONArray argsArray = component.getJSONArray(KEY_ARGS);
            List<Object> args = new ArrayList<>(IntStream.range(0, argsArray.length())
                            .mapToObj(argsArray::get)
                            .toList());
            int entryIndex = component.optInt(KEY_ENTRY_INDEX);
            Component.Entry entry = new Component.Entry(componentClass, args, entryIndex);

            // Check if the component has a duplicate entryIndex with any other componentClass. If so, error and don't add it
            boolean hasDuplicate = componentClasses.stream().anyMatch(existing -> existing.entryIndex() == entry.entryIndex());
            boolean isSameClass = componentClasses.stream().anyMatch(existing -> existing.componentClass() == entry.componentClass());
            if (hasDuplicate && isSameClass) {
                Main.LOGGER.error("[Location: {}] Entry index {} is already used for component {}! Did you add an \"{}\" int field?", location, entryIndex, componentClass.getSimpleName(), KEY_ENTRY_INDEX);
                continue;
            }

            // If it passes all that, add it in!
            componentClasses.add(entry);
        }
        return componentClasses;
    }

    /**
     * Used by {@code getModifiedComponentClasses} to return a result that cointains a list of additions and removals.
     * @param additions What needs to be added to the component classes.
     * @param removals What needs to be removed from the component classes.
     **/
    public record ComponentModifications(List<Component.Entry> additions, List<Component.Entry> removals) {}

    /**
     * Goes through the componentData seeing if a class needs to be removed or added.
     * Using this, it's convert to a class via {@code getComponentClasses} and then added to the appropriate list in {@code ComponentModifications} (either in additions or removals).
     * @param componentData A list of JSONObjects that contains all the componentData.
     * Each JSONObject needs to have a filepath string to the component ("component") and an action boolean ("action").
     * If the field is true, it's set to want to add a component. If false, it's set to want to remove.
     * A JSONObject that want to addcomponent must also provide a JSONArray of arguments ("args").
     * @param location Whenever an error is thrown, which JSON file should the error blame?
     * @return The {@code ComponentModifications} record with two lists of componentClasses: additions and removals.
     **/
    public static ComponentModifications getModifiedComponentClasses(JSONArray componentData, String location) {
        // Convert the componentData JSONArray to a list of JSONObject so it's easier to manage
        List<JSONObject> components = IntStream.range(0, componentData.length())
            .mapToObj(componentData::getJSONObject)
            .toList();

        // Declare two JSONArrays, with one holding all the components that are set to remove, the other holding all the ones to add
        JSONArray additionsArray = new JSONArray();
        JSONArray removalsArray = new JSONArray();

        // Loop through all the component strings, checking its "action" to see where to add it
        for (JSONObject component : components) {
            // Make sure the necessary stuff is inside the JSONObject. If not, warn and continue
            if (!component.has(KEY_ACTION) || !(component.get(KEY_ACTION) instanceof Boolean)) {
                Main.LOGGER.error("[Location: {}] An \"{}\" field is missing or is not a boolean.", location, KEY_ACTION);
                continue;
            }

            // Now add the component to either additions or removals
            if (component.getBoolean(KEY_ACTION)) additionsArray.put(component);
            else removalsArray.put(component);
        }

        // Now declare the return record and add the classes to add and remove to it
        List<Component.Entry> additions = getComponentClasses(additionsArray, location);
        List<Component.Entry> removals = getComponentClasses(removalsArray, location);
        ComponentModifications componentModifications = new ComponentModifications(additions, removals);

        return componentModifications;
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
            List<Component.Entry> componentClasses = getComponentClasses(components, fileNames.get(jsons.indexOf(json)));

            // Add in the name component
            componentClasses.add(new Component.Entry(NameComponent.class, List.of(json.getString("name")), 0));

            // Last but not least, add our list of componentClasses to the TEMPLATES HashMap
            TEMPLATES.put(json.getString("name"), componentClasses);
        }

        Main.LOGGER.info("Loaded {} templates!", TEMPLATES.size());
    }

    @SafeVarargs
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
    @SafeVarargs
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
            if (type.isInstance(element))
                return Optional.of((T) element);
        }
        return Optional.empty();
    }
    public static <T extends Component> List<T> getComponents(int identifier, Class<T> type) {
        List<T> result = new ArrayList<>();
        List<Component> entity = Main.world.getEntities().get(identifier);

        for (Component component : entity) {
            if (type.isInstance(component))
                result.add(type.cast(component));
        }
        return result;
    }
    @SafeVarargs
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
