// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONObject;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.component.*;

public class EntityHandler {
    public static final Map<String, Map<Class<? extends Component>, List<Object>>> TEMPLATES = new HashMap<>();

    /**
     * Loops through the given componentData and finds the corresponding classes returning them in a map with the args.
     * @param componentData A list of JSONObjects that contains all the componentData. The data needs to be the componentName ("component") and the arguments ("args")
     * @param location Whenever an error is thrown, which JSON file should the error blame?
     **/
    public static Map<Class<? extends Component>, List<Object>> getComponents(List<JSONObject> componentData, String location) {
        // Create the list in which we will put our component classes in
        Map<Class<? extends Component>, List<Object>> componentClasses = new HashMap<>();

        // Loop through all the component strings, get its corresponding class from the COMPONENTS HashMap, and add it to the componentClasses List
        for (JSONObject component : componentData) {
            // Make sure the necessary stuff is inside the JSONObject. If not, warn and continue
            if (!component.has("component") || !(component.get("component") instanceof String)) {
                Main.LOGGER.error("[Location: {}] A component field is missing or is not a String.", location);
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
                Main.LOGGER.error("[Location: {}] Component path '{}' is not a path to a class or does not extend the 'Component' interface!", location, componentPath);
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
            JSONArray componentsArray = json.getJSONArray("components");

            // Turn the components from a json array into a list, so it's easier to deal with
            List<JSONObject> components = IntStream.range(0, componentsArray.length())
                .mapToObj(componentsArray::getJSONObject)
                .toList();

            Map<Class<? extends Component>, List<Object>> componentClasses = getComponents(components, fileNames.get(jsons.indexOf(json)));

            // Add in the name component
            componentClasses.put(NameComponent.class, List.of(json.getString("name")));

            // Last but not least, add our list of componentClasses to the TEMPLATES HashMap
            TEMPLATES.put(json.getString("name"), componentClasses);
        }

        Main.LOGGER.info("Loaded {} templates!", TEMPLATES.size());
    }

    public static List<Integer> queryAll(Class<? extends Component>... components) {
        List<Integer> result = new ArrayList<>();

        for (int entity : Main.world.getEntities().keySet()) {
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
        for (int entity : Main.world.getEntities().keySet()) {
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
}
