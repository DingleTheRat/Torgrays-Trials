// Copyright (c) 2026 DingleTheRat. All Rights Reserved.
package net.dingletherat.torgrays_trials.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.stream.IntStream;

import org.json.JSONArray;
import org.json.JSONObject;

import net.dingletherat.torgrays_trials.Main;
import net.dingletherat.torgrays_trials.entity.component.Component;
import net.dingletherat.torgrays_trials.entity.component.MovementComponent;
import net.dingletherat.torgrays_trials.entity.component.NameComponent;
import net.dingletherat.torgrays_trials.entity.component.PositionComponent;
import net.dingletherat.torgrays_trials.entity.component.SpriteSheetComponent;
import net.dingletherat.torgrays_trials.main.UtilityTool;

public class EntityHandler {
    public static final HashMap<String, List<Class<? extends Component>>> TEMPLATES = new HashMap<>();
    public static final HashMap<String, Class<? extends Component>> COMPONENTS = new HashMap<>();

    public static void registerAllComponents() {
        COMPONENTS.clear();

        COMPONENTS.put(MovementComponent.class.getSimpleName(), MovementComponent.class);
        COMPONENTS.put(NameComponent.class.getSimpleName(), NameComponent.class);
        COMPONENTS.put(PositionComponent.class.getSimpleName(), PositionComponent.class);
        COMPONENTS.put(SpriteSheetComponent.class.getSimpleName(), SpriteSheetComponent.class);

        Main.LOGGER.info("Loaded {} vanilla components", COMPONENTS.size());
    }
    public static void generateTemplates() {
        TEMPLATES.clear();
        String filepath = "values/templates/entities/";

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
            List<String> components = new ArrayList<>(IntStream.range(0, componentsArray.length())
                .mapToObj(componentsArray::getString)
                .toList());

            // Create the list in which we will put our component classes in
            List<Class<? extends Component>> componentClasses = new ArrayList<>();

            // Loop through all the component strings, get its corresponding class from the COMPONENTS HashMap, and add it to the componentClasses List
            for (String componentName : components) {
                // Check if it even exists. If not, warn and continue
                if (!COMPONENTS.containsKey(componentName)) {
                    Main.LOGGER.warn("{} is not a component! Did you add it to the COMPONENTS list?", componentName);
                    continue;
                }

                componentClasses.add(COMPONENTS.get(componentName));
            }

            // Last but not least, add our list of componentClasses to the TEMPLATES HashMap
            TEMPLATES.put(json.getString("name"), componentClasses);
        }

        Main.LOGGER.info("Loaded {} templates!", TEMPLATES.size());
    }
}
