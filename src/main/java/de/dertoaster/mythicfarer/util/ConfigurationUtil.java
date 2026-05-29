package de.dertoaster.mythicfarer.util;

import net.countercraft.movecraft.util.SerializationUtil;
import net.countercraft.movecraft.util.Tags;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.*;

public class ConfigurationUtil extends SerializationUtil {

    public static Object serializeEnumSet(EnumSet<Material> materials) {
        List<String> result = new ArrayList<>(materials.size());
        materials.forEach(m -> result.add(m.name()));
        return result;
    }

    public static Object serializeNamespacedKeySet(Set<NamespacedKey> materials) {
        List<String> result = new ArrayList<>(materials.size());
        materials.forEach(m -> result.add(m.toString()));
        return result;
    }

    @Deprecated(forRemoval = true)
    /*
     * Deprecated, Material enum should not be used! Use NamespacedKey instead
     */
    public static EnumSet<Material> deserializeEnumSet(Object rawDataObject, EnumSet<Material> defaultValue) {
        EnumSet<Material> result = EnumSet.noneOf(Material.class);
        if (rawDataObject != null) {
            if (rawDataObject instanceof List list) {
                for (Object obj : list) {
                    if (obj instanceof Material material) {
                        result.add(material);
                    } else if (obj instanceof String string) {
                        result.addAll(Tags.parseMaterials(string));
                    }
                }
            } else if (rawDataObject instanceof Material) {
                result.add((Material) rawDataObject);
            } else if (rawDataObject instanceof String) {
                result.addAll(Tags.parseMaterials((String) rawDataObject));
            }
        }

        if (result.isEmpty()) {
            result.addAll(defaultValue);
        }
        return result;
    }

    public static List<String> deserializeStringList(Object rawDataObject, List<String> defaultValue) {
        List<String> typeList;
        try {
            typeList = (List<String>) rawDataObject;
            if (!typeList.isEmpty()) {
                List<String> tmpList = new ArrayList<>(typeList);
                typeList.clear();
                final List<String> typeListTmp = typeList;
                tmpList.forEach(s -> {
                    typeListTmp.add(s.toUpperCase());
                });
            }
        } catch(ClassCastException cce) {
            typeList = List.copyOf(defaultValue);
        }
        if (typeList == null) {
            typeList = List.copyOf(defaultValue);
        }
        return typeList;
    }

    public static boolean deserializeBoolean(Object rawDataObject, boolean defaultValue) {
        boolean result = defaultValue;
        if (rawDataObject != null && (rawDataObject instanceof Boolean)) {
            result = ((Boolean) rawDataObject).booleanValue();
        }
        return result;
    }

    public static boolean deserializeBoolean(String key, Map<String, Object> rawDataMap, boolean defaultValue) {
        Object raw = rawDataMap.getOrDefault(key, null);
        return deserializeBoolean(raw, defaultValue);
    }

    public static String deserializeString(String key, Map<String, Object> rawData, String defaultValue) {
        return String.valueOf(rawData.getOrDefault(key, defaultValue));
    }
}
