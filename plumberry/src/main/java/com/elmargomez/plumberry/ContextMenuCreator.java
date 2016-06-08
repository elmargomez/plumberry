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

package com.elmargomez.plumberry;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.support.annotation.MenuRes;
import android.view.View;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContextMenuCreator {

    private static final String ITEM = "item";
    //attributes
    private static final String TITLE = "title";
    private static final String ICON = "icon";

    private Context context;

    public ContextMenuCreator(Context context) {
        this.context = context;
    }

    @MenuRes
    public ContextMenuCreator menu(int id) {
        List<MenuModel> menuModels = SingletonCache.getInstance().getMenus(id);
        if (menuModels == null) {
            menuModels = createMenusFromResource(id);
            SingletonCache.getInstance().add(id, menuModels);
        }
        return this;
    }

    public void anchor(View v) {

    }

    private List<MenuModel> createMenusFromResource(int id) {
        List<MenuModel> menuModels = new ArrayList<>();
        XmlResourceParser xml = context.getResources().getXml(id);
        try {
            int event = xml.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_DOCUMENT) {
                    if (xml.getName().equals(ITEM)) {
                        MenuModel menuModel = new MenuModel();
                        menuModel.setTitle(xml.getAttributeValue(null, TITLE));
                        String iconID = xml.getAttributeValue(null, ICON);
                        if (iconID != null) {
                            menuModel.setIcon(Integer.parseInt(iconID));
                        }
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
