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

package com.elmargomez.plumberry.test;

import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.elmargomez.plumberry.R;

public class TestContextMenu {

    private float mWidthUnit;
    private float mWindowMargin;

    private final WindowManager.LayoutParams mLayoutParams = new WindowManager.LayoutParams();
    private final Point mRealSize = new Point();
    private WindowManager mWindowManager;
    private View mAnchoredView;

    public TestContextMenu(Context context) {
        // values
        mWidthUnit = context.getResources().getDimension(R.dimen.width_unit);
        mWindowMargin = context.getResources().getDimension(R.dimen.window_margin);

        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

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
        view.getLocationOnScreen(coordinates);


        mWindowManager.addView(view, mLayoutParams);
    }

}
