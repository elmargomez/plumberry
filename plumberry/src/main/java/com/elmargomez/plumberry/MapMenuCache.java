package com.elmargomez.plumberry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores the inflated menus into {@link HashMap}.
 */
public class MapMenuCache implements MenuCache {

    private Map<Integer, List<MenuModel>> cacheHolder = new HashMap<>();

    @Override
    public void add(int id, List<MenuModel> menuModels) {
        cacheHolder.put(id, menuModels);
    }

    @Override
    public List<MenuModel> get(int id) {
        return cacheHolder.get(id);
    }
}
