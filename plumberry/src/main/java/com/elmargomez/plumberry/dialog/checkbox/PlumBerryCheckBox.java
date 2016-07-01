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

package com.elmargomez.plumberry.dialog.checkbox;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.MenuRes;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.elmargomez.plumberry.MenuModel;
import com.elmargomez.plumberry.R;
import com.elmargomez.plumberry.util.MenuUtil;

import java.util.List;

public class PlumBerryCheckBox extends Dialog {

    private int mLeftMargin;
    private int mLeftPadding;
    private int mItemHeight;
    private float mItemFontSize;
    private int mTextColor;
    private RadioGroup mRadioGroup;

    public PlumBerryCheckBox(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.check_dialog);
        mLeftMargin = (int) context.getResources().getDimension(R.dimen.check_menu_item_left_margin);
        mLeftPadding = (int) context.getResources().getDimension(R.dimen.check_menu_item_padding);
        mItemHeight = (int) context.getResources().getDimension(R.dimen.item_height);
        mItemFontSize = context.getResources().getDimension(R.dimen.font_size);
        mTextColor = colorCompat(context, R.color.text_color);
        mRadioGroup = (RadioGroup) findViewById(R.id.list);
    }

    public PlumBerryCheckBox setMenu(@MenuRes int menu) {
        mRadioGroup.removeAllViews();
        List<MenuModel> m = MenuUtil.getMenuModels(getContext(), menu);
        for (MenuModel model : m) {

            RadioGroup.LayoutParams layoutParams = new RadioGroup
                    .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mItemHeight);

            layoutParams.leftMargin = mLeftMargin;
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setPadding(mLeftPadding, 0, 0, 0);
            radioButton.setText(model.getTitle());
            radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, mItemFontSize);
            radioButton.setTextColor(mTextColor);
            mRadioGroup.addView(radioButton, layoutParams);
        }
        return this;
    }

    /**
     * Get the color from the resource. It is also backward compatible.
     *
     * @param context
     * @param id
     * @return
     */
    private int colorCompat(Context context, int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getResources().getColor(id, context.getTheme());
        } else {
            return context.getResources().getColor(id);
        }
    }

}
