package com.youtube.hempfest.goldeco.util.libraries;

import java.util.HashMap;
import java.util.Map;


import org.bukkit.Material;

public final class ItemLibrary
{
    private static final Map<String, Material> MATERIAL_ALIAS = new HashMap<>();

    static
    {
        for (Material material : Material.values())
        {
            MATERIAL_ALIAS.put(material.name().toLowerCase().replace("_", ""), material);
        }
    }

    public static Material getMaterial(String name)
    {
        return MATERIAL_ALIAS.get(name.toLowerCase().replaceAll("_", ""));
    }

    private ItemLibrary() { }
}
