package com.elmargomez.plumberry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArrayMenuCache implements MenuCache {

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
