package com.elmargomez.plumberry.test;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.IntDef;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.elmargomez.plumberry.ContextMenuAdapter;
import com.elmargomez.plumberry.MenuModel;
import com.elmargomez.plumberry.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestContextMenu {
    private static final String ITEM = "item";
    private static final String NAMESPACE = "http://schemas.android.com/apk/res/android";
    private static final String TITLE = "title";
    private static final String ICON = "icon";

    private int mOffset;
    private int mCellHeight;
    private float mWidthUnit;

    private final WindowManager.LayoutParams mLayoutParams = new WindowManager
            .LayoutParams(400, 400, 10, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
            | WindowManager.LayoutParams.FLAG_DIM_BEHIND, PixelFormat.OPAQUE);

    private final Point mRealSize = new Point();

    private Context mContext;
    private WindowManager mWindowManager;
    private List<MenuModel> mMenuModels;
    private ContextMenuAdapter mAdapter;
    private ListView mListview;
    private View mDialogView;
    private View mAnchoredView;

    public TestContextMenu(Context context, int menu) {
        // values
        mWidthUnit = context.getResources().getDimension(R.dimen.width_unit);
        mOffset = (int) context.getResources().getDimension(R.dimen.listview_offset);
        mCellHeight = (int) context.getResources().getDimension(R.dimen.item_height);

        mContext = context;
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mMenuModels = createMenusFromResource(menu);
        mAdapter = new ContextMenuAdapter(context, mMenuModels);
        LayoutInflater inflater = LayoutInflater.from(context);
        mDialogView = inflater.inflate(R.layout.popup_dialog, null);
        mListview = (ListView) mDialogView.findViewById(R.id.popup_menu_holder);
        mListview.setAdapter(mAdapter);

        Display display = mWindowManager.getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealSize(mRealSize);
        } else {
            mRealSize.x = display.getWidth();
            mRealSize.y = display.getHeight();
        }
    }

    public void anchor(View view) {
        mAnchoredView = view;
        final int[] coordinates = new int[2];
        getLocationOnScreenCompat(view, coordinates);

        int listHeight = 160;
        int listWidth = (int) (mWidthUnit * 2);
        int x = -500;
        int y = -50;

//        final int maxCellHeight = (mMenuModels.size() * mCellHeight) + (mOffset * 2);
//
//        if (fitOnTopOfView(coordinates)) {
//            listHeight = suggestedListHeight(coordinates[1], maxCellHeight);
//            y = coordinates[1] - listHeight;
//        } else {
//            int freeVerticalSpace = mRealSize.y - coordinates[1];
//            listHeight = suggestedListHeight(freeVerticalSpace, maxCellHeight);
//            y = freeVerticalSpace;
//        }
//
//        if (fitOnLeftSideOfView(coordinates)) {
//            x = coordinates[0] - listWidth;
//        } else {
//            x = coordinates[0];
//        }

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, 0);
        layoutParams.width = listWidth - 100;
        layoutParams.height = listHeight;

        mDialogView.setLayoutParams(layoutParams);

        // Sets the window Layout
        mLayoutParams.x = 0;
        mLayoutParams.y = y;
//        mLayoutParams.width = 600;
//        mLayoutParams.height = 100;

        mWindowManager.addView(mDialogView, mLayoutParams);
    }

    private void getLocationOnScreenCompat(View view, int[] coordinates) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            coordinates[0] = (int) view.getX();
            coordinates[1] = (int) view.getY();
        } else {
            view.getLocationOnScreen(coordinates);
        }
    }

    private boolean fitOnTopOfView(int[] coordinates) {
        return (mRealSize.y - coordinates[1]) < (mRealSize.y / 2);
    }

    private boolean fitOnLeftSideOfView(int[] coordinates) {
        return (mRealSize.x - coordinates[0]) < (mRealSize.x / 2);
    }

    private int suggestedListHeight(int freeSpace, int occupantSize) {
        if (freeSpace > occupantSize) {
            return occupantSize;
        } else {
            return (int) (freeSpace * 0.7f);
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
