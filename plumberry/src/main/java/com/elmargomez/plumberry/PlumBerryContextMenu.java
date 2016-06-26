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
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.MenuRes;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlumBerryContextMenu extends Dialog {

    private static final String ITEM = "item";
    private static final String NAMESPACE = "http://schemas.android.com/apk/res/android";
    private static final String TITLE = "title";
    private static final String ICON = "icon";

    private int mOffset;
    private int mCellHeight;
    private float mWidthUnit;
    private float mWindowMargin;

    private final Point mRealSize = new Point();
    private Context mContext;
    private ListView mListview;
    private List<MenuModel> mMenuModels;
    private ContextMenuAdapter mAdapter;
    private View mAnchoredView;

    public PlumBerryContextMenu(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        setContentView(R.layout.popup_dialog);

        mWidthUnit = context.getResources().getDimension(R.dimen.width_unit);
        mWindowMargin = context.getResources().getDimension(R.dimen.window_margin);
        mOffset = (int) context.getResources().getDimension(R.dimen.listview_offset);
        mCellHeight = (int) context.getResources().getDimension(R.dimen.item_height);

        mContext = context;
        mListview = (ListView) findViewById(R.id.popup_menu_holder);
        mMenuModels = new ArrayList<>();
        mAdapter = new ContextMenuAdapter(context, mMenuModels);
        mListview.setAdapter(mAdapter);

        Display display = getWindow().getWindowManager().getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealSize(mRealSize);
        } else {
            mRealSize.x = display.getWidth();
            mRealSize.y = display.getHeight();
        }
    }

    public PlumBerryContextMenu setMenu(@MenuRes int menu) {
        mMenuModels.clear();
        mMenuModels.addAll(createMenusFromResource(menu));
        mAdapter.notifyDataSetChanged();
        return this;
    }

    public void anchor(View view) {
        mAnchoredView = view;
        ViewGroup.LayoutParams layoutParams = mListview.getLayoutParams();
        layoutParams.width = (int) mWidthUnit * 2;
        final int[] viewLocationInScreen = new int[2];
        getLocationOnScreenCompat(view, viewLocationInScreen);

        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.x = viewLocationInScreen[0] - (mRealSize.x / 2) + (layoutParams.width/2);
        attributes.y = viewLocationInScreen[1];
        window.setAttributes(attributes);
        show();
    }

    private boolean hasMoreSpaceOnTopOfAnchorView(int[] coordinates) {
        return (mRealSize.y - coordinates[1]) < (mRealSize.y / 2);
    }

    private boolean hasMoreSpaceOnLeftSideOfAnchorView(int[] coordinates) {
        return (mRealSize.x - coordinates[0]) < (mRealSize.x / 2);
    }

    private void getLocationOnScreenCompat(View view, int[] coordinates) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            coordinates[0] = (int) view.getX();
            coordinates[1] = (int) view.getY();
        } else {
            view.getLocationOnScreen(coordinates);
        }
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
        XmlResourceParser xml = mContext.getResources().getXml(id);
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
