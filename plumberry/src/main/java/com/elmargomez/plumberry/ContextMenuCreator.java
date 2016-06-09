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
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.support.annotation.MenuRes;
import android.view.View;
import android.widget.ListPopupWindow;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContextMenuCreator {

    private static final String ITEM = "item";
    private static final String NAMESPACE = "http://schemas.android.com/apk/res/android";
    //attributes
    private static final String TITLE = "title";
    private static final String ICON = "icon";

    private Context context;
    private ContextMenuAdapter contextMenuAdapter;

    public ContextMenuCreator(Context context, int id) {
        this.context = context;

        List<MenuModel> menuModels = SingletonCache.getInstance().getMenus(id);
        if (menuModels == null) {
            menuModels = createMenusFromResource(id);
            SingletonCache.getInstance().add(id, menuModels);
        }
        this.contextMenuAdapter = new ContextMenuAdapter(context, menuModels);
    }

    /**
     * Sets the coordinate of the mini Dialog.
     *
     * @param v the taget view.
     */
    public void anchor(View v) {
        ListPopupWindow popupWindow = new ListPopupWindow(context);
        popupWindow.setAdapter(contextMenuAdapter);
        popupWindow.setWidth(400);
        popupWindow.setAnchorView(v);
        popupWindow.show();
    }

    /**
     * Get the menu datas from xml.
     *
     * @param id the menu resource.
     * @return
     */
    private List<MenuModel> createMenusFromResource(int id) {
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
