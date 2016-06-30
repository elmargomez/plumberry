/*
 * Copyright 2016 Elmar Rhex Gomez.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.elmargomez.plumberry.util;

import android.content.Context;
import android.content.res.XmlResourceParser;

import com.elmargomez.plumberry.MenuModel;
import com.elmargomez.plumberry.SingletonCache;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MenuUtil {

    private static final String NAMESPACE = "http://schemas.android.com/apk/res/android";
    private static final String ITEM = "item";
    private static final String TITLE = "title";
    private static final String ICON = "icon";

    /**
     * We need to cached the menu to avoid re-inflation.
     *
     * @param id the menu resource ID
     * @return returns the array of menus.
     */
    public static List<MenuModel> getMenuModels(Context context, int id) {
        List<MenuModel> menuModels = SingletonCache.getInstance().getMenus(id);
        if (menuModels == null) {
            menuModels = createMenusFromResource(context, id);
            SingletonCache.getInstance().add(id, menuModels);
        }
        return menuModels;
    }

    /**
     * Get menu from cache, if it does not exists recreate a new one and
     * store it in cache.
     *
     * @param id the menu resource ID
     * @return returns the array of menus.
     */
    private static List<MenuModel> createMenusFromResource(Context context, int id) {
        List<MenuModel> menuModels = new ArrayList<>();
        XmlResourceParser xml = context.getResources().getXml(id);
        try {
            int event = xml.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_TAG) {
                    if (xml.getName().equals(ITEM)) {
                        MenuModel menuModel = new MenuModel();
                        menuModel.setTitle(xml.getAttributeValue(NAMESPACE, TITLE));
                        menuModel.setIcon(xml.getAttributeIntValue(NAMESPACE, ICON, -1));
                        menuModels.add(menuModel);
                    }
                }
                event = xml.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            xml.close();
        }
        return menuModels;
    }

}
