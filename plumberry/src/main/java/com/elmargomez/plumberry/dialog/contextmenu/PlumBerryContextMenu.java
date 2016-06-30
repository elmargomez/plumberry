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

package com.elmargomez.plumberry.dialog.contextmenu;

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
import android.widget.AdapterView;
import android.widget.ListView;

import com.elmargomez.plumberry.MenuModel;
import com.elmargomez.plumberry.R;
import com.elmargomez.plumberry.SingletonCache;
import com.elmargomez.plumberry.math.ScreenCoordinate;

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

    private final int mOffset;
    private final int mCellHeight;
    private final float mWidthUnit;

    private final Point mDeviceSize = new Point();
    private Context mContext;
    private ListView mListview;
    private View mViewGroupHolder;
    private List<MenuModel> mMenuModels;
    private ContextMenuAdapter mAdapter;
    private View mAnchoredView;
    private OnItemClickListener mOnItemClickListener;

    public PlumBerryContextMenu(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        setContentView(R.layout.popup_dialog);

        mWidthUnit = context.getResources().getDimension(R.dimen.width_unit);
        mOffset = (int) context.getResources().getDimension(R.dimen.listview_offset);
        mCellHeight = (int) context.getResources().getDimension(R.dimen.item_height);

        mContext = context;
        mViewGroupHolder = findViewById(R.id.base);
        mListview = (ListView) findViewById(R.id.popup_menu_holder);
        mMenuModels = new ArrayList<>();
        mAdapter = new ContextMenuAdapter(context, mMenuModels);
        mListview.setAdapter(mAdapter);
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOnItemClickListener != null) {
                    MenuModel model = (MenuModel) parent.getItemAtPosition(position);
                    mOnItemClickListener.onItemClick(PlumBerryContextMenu.this, mAnchoredView,
                            model.getTitle());
                }
            }

        });

        Display display = getWindow().getWindowManager().getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealSize(mDeviceSize);
        } else {
            mDeviceSize.x = display.getWidth();
            mDeviceSize.y = display.getHeight();
        }
    }

    public PlumBerryContextMenu setMenu(@MenuRes int menu) {
        mMenuModels.clear();
        mMenuModels.addAll(getMenuModels(menu));
        mAdapter.notifyDataSetChanged();
        return this;
    }

    public PlumBerryContextMenu listener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
        return this;
    }

    /**
     * Shows and align the dialog in the anchored view coordinates.
     *
     * @param view the view in which the Dialog should anchored.
     */
    public void anchor(View view) {
        mAnchoredView = view;
        ViewGroup.LayoutParams listlayoutParams = mViewGroupHolder.getLayoutParams();
        final int[] viewLocationInScreen = new int[2];
        view.getLocationOnScreen(viewLocationInScreen);

        ScreenCoordinate coordinate = new ScreenCoordinate(mDeviceSize.x, mDeviceSize.y,
                viewLocationInScreen[0], viewLocationInScreen[1]);

        /**
         * Note: The windows coordinate system follows the similar Cartesian Coordinate system.
         * Therefore we need to assume that (0,0) is always at the center of the
         * screen.
         *
         * To convert our linear representation value 'L' to Cartesian coordinate value,
         * we can follow this formula 'C' = (L-zeroCoordinate)
         */
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();

        switch (coordinate.getMostSpace()) {
            case ScreenCoordinate.BOTTOM_LEFT: {
                int w = coordinate.leftSpace();
                int h = coordinate.bottomSpace();
                listlayoutParams.width = (int) mWidthUnit * 2;
                listlayoutParams.height = preferedSize(h, getMaxListHeight());
                attributes.x = viewLocationInScreen[0] - (mDeviceSize.x / 2) + (listlayoutParams.width / 2);
                attributes.y = viewLocationInScreen[1] - (mDeviceSize.y / 2) + (listlayoutParams.height / 2);
                break;
            }
            case ScreenCoordinate.BOTTOM_RIGHT: {
                int w = coordinate.rightSpace();
                int h = coordinate.bottomSpace();
                listlayoutParams.width = (int) mWidthUnit * 2;
                listlayoutParams.height = preferedSize(h, getMaxListHeight());
                attributes.x = viewLocationInScreen[0] - (mDeviceSize.x / 2) + (listlayoutParams.width / 2);
                attributes.y = viewLocationInScreen[1] - (mDeviceSize.y / 2) + (listlayoutParams.height / 2);
                break;
            }
            case ScreenCoordinate.TOP_LEFT: {
                int w = coordinate.leftSpace();
                int h = coordinate.topSpace();
                listlayoutParams.width = (int) mWidthUnit * 2;
                listlayoutParams.height = preferedSize(h, getMaxListHeight());
                attributes.x = viewLocationInScreen[0] - (mDeviceSize.x / 2) + (listlayoutParams.width / 2);
                attributes.y = viewLocationInScreen[1] - (mDeviceSize.y / 2) - (listlayoutParams.height / 2) + view.getHeight();
                break;
            }
            case ScreenCoordinate.TOP_RIGHT: {
                int w = coordinate.rightSpace();
                int h = coordinate.topSpace();
                listlayoutParams.width = (int) mWidthUnit * 2;
                listlayoutParams.height = preferedSize(h, getMaxListHeight());
                attributes.x = (viewLocationInScreen[0] - (mDeviceSize.x / 2) + (listlayoutParams.width / 2));
                attributes.y = viewLocationInScreen[1] - (mDeviceSize.y / 2) - (listlayoutParams.height / 2) + view.getHeight();
                break;
            }
        }
        window.setAttributes(attributes);
        show();
    }

    /**
     * Compute the Max Height of Listview assuming you wanted to see
     * all the menus whithout having a to have scrollbar.
     *
     * @return the max size of the listview
     */
    private int getMaxListHeight() {
        return (mOffset * 2) + (mCellHeight * mMenuModels.size());
    }

    /**
     * Suggest the proper size that fits to its container.
     *
     * @param freeSpace    the container size
     * @param occupantSize the occupant size
     * @return the suggest occupant size.
     */
    private int preferedSize(int freeSpace, int occupantSize) {
        if (freeSpace > occupantSize) {
            return occupantSize;
        } else {
            return (int) (freeSpace * 0.8f);
        }
    }

    /**
     * We need to cached the menu to avoid re-inflation.
     *
     * @param id the menu resource ID
     * @return returns the array of menus.
     */
    private List<MenuModel> getMenuModels(int id) {
        List<MenuModel> menuModels = SingletonCache.getInstance().getMenus(id);
        if (menuModels == null) {
            menuModels = createMenusFromResource(id);
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

    /**
     * This is called when the user clicks an item in the list.
     */
    public interface OnItemClickListener {

        public void onItemClick(Dialog dialog, View anchorView, String title);

    }

}
