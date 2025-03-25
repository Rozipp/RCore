package ua.rozipp.core.config;

import net.kyori.adventure.key.Key;
import ua.rozipp.core.items.CustomMaterial;

import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;

public class ConfigMaterialCategory {

	private static final TreeMap<String, ConfigMaterialCategory> categories = new TreeMap<>();

	public String id;
	public String title;
	public HashMap<Key, CustomMaterial> materials = new HashMap<>();

	public static void addMaterial(CustomMaterial mat) {
		if (mat.getCategory() == null || mat.getCategory().isEmpty()) return;
		ConfigMaterialCategory cat = categories.get(mat.getCategory());
		if (cat == null) {
			cat = new ConfigMaterialCategory();
			cat.title = mat.getCategory();
			cat.id = mat.getCategory();
		}

		cat.materials.put(mat.getMid(), mat);
		categories.put(cat.id, cat);
	}

	public static Collection<ConfigMaterialCategory> getCategories() {
		return categories.values();
	}

	public static ConfigMaterialCategory getCategory(String key) {
		return categories.get(key);
	}

}
