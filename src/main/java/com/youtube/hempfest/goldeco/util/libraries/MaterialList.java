package com.youtube.hempfest.goldeco.util.libraries;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class MaterialList {

    public List<Material> getMaterials() {
        List<Material> mats = new ArrayList<>();
        for (Material material : Material.values()) {
            mats.add(material);
        }
        return mats;
    }
    public List<String> getMaterialNames() {
        List<String> mats = new ArrayList<>();
        for (Material mat : getMaterials()) {
            mats.add(mat.name());
        }
        return mats;
    }

}
