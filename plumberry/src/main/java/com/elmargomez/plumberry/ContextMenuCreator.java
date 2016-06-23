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

import android.app.Dialog;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

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
    public static final int DEFAULT_WIDTH = 160;

    private Context context;
    private PopupWindow window;
    private Integer width, height;
    private ContextMenuAdapter contextMenuAdapter;
    private View view, anchorView;
    private ListView listView;

    private int offset;
    private int cellHeight;
    private PlumBerry.OnItemClickListener listener;

    public ContextMenuCreator(Context context) {
        this.context = context;
        this.window = new PopupWindow(context);

        // Get the resource value
        offset = (int) context.getResources().getDimension(R.dimen.listview_offset);
        cellHeight = (int) context.getResources().getDimension(R.dimen.item_height);

        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.popup_dialog, null);
        listView = (ListView) view.findViewById(R.id.popup_menu_holder);

        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    /**
     * Inflate the menu.
     *
     * @param id the resource ID of the menu.
     */
    public void menu(int id) {
        List<MenuModel> menuModels = SingletonCache.getInstance().getMenus(id);
        if (menuModels == null) {
            menuModels = createMenusFromResource(id);
            SingletonCache.getInstance().add(id, menuModels);
        }
        this.contextMenuAdapter = new ContextMenuAdapter(context, menuModels);

        listView.setAdapter(contextMenuAdapter);
        window.setContentView(view);
    }

    /**
     * Set the width of the dialog.
     *
     * @param pixel
     * @see #height
     */
    public void width(int pixel) {
        width = pixel;
    }

    /**
     * Set the height of the dialog.
     *
     * @param pixel
     * @see #width
     */
    public void height(int pixel) {
        height = pixel;
    }

    /**
     * Sets the coordinate of the mini Dialog.
     *
     * @param v the target view.
     */
    public void anchor(View v) {
        this.anchorView = v;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    MenuModel menuModel = (MenuModel) parent.getItemAtPosition(position);
                    listener.onClick(ContextMenuCreator.this, getAnchorView(), menuModel.getTitle());
                }
            }
        });

        if (width != null) {
            window.setWidth(width);
        } else {
            window.setWidth(DEFAULT_WIDTH);
        }

        if (height != null) {
            window.setHeight(height);
        } else {
            window.setHeight(dynamicListHeight());
        }

        window.setOutsideTouchable(true);
        window.showAsDropDown(v);
        window.update();
    }

    public void dismiss() {
        window.dismiss();
    }


    /**
     * Return the anchored view.
     *
     * @return
     */
    public View getAnchorView() {
        return anchorView;
    }


    public ContextMenuCreator listener(PlumBerry.OnItemClickListener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * Get menu from cache, if it does not exists recreate a new one and
     * store it in cache.
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

    private int dynamicListHeight() {
        int c = contextMenuAdapter.getCount();
        if (c < 4) {
            return (offset * 2) + (cellHeight * c);
        } else {
            return (offset * 2) + (cellHeight * 4);
        }
    }

}
