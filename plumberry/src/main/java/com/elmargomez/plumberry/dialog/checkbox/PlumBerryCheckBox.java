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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.elmargomez.plumberry.MenuModel;
import com.elmargomez.plumberry.R;
import com.elmargomez.plumberry.dialog.contextmenu.PlumBerryContextMenu;
import com.elmargomez.plumberry.util.MenuUtil;

import java.util.List;

public class PlumBerryCheckBox extends Dialog implements View.OnClickListener {

    public static final int POSITIVE_BUTTON = 1;
    public static final int NEGATIVE_BUTTON = 2;

    private Object mTag;
    private int mLeftMargin;
    private int mLeftPadding;
    private int mItemHeight;
    private float mItemFontSize;
    private int mTextColor;
    private TextView mTitle;
    private RadioGroup mRadioGroup;
    private Button mNegativeButton;
    private Button mPositiveButton;
    private PlumBerryCheckBox.OnClickListener mOnNegativeClickListener;
    private PlumBerryCheckBox.OnClickListener mOnPositiveClickListener;

    public PlumBerryCheckBox(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.check_dialog);
        mLeftMargin = (int) context.getResources().getDimension(R.dimen.check_menu_item_left_margin);
        mLeftPadding = (int) context.getResources().getDimension(R.dimen.check_menu_item_padding);
        mItemHeight = (int) context.getResources().getDimension(R.dimen.item_height);
        mItemFontSize = context.getResources().getDimension(R.dimen.font_size);
        mTextColor = colorCompat(context, R.color.text_color);
        mTitle = (TextView) findViewById(R.id.title);
        mRadioGroup = (RadioGroup) findViewById(R.id.list);
        mNegativeButton = (Button) findViewById(R.id.negative_button);
        mPositiveButton = (Button) findViewById(R.id.positive_button);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mPositiveButton.setEnabled(true);
            }

        });

        mNegativeButton.setOnClickListener(this);
        mPositiveButton.setOnClickListener(this);
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

    public PlumBerryCheckBox setTitle(String title) {
        mTitle.setText(title);
        return this;
    }

    public PlumBerryCheckBox setOnNegativeClickListener(String name, OnClickListener listener) {
        mNegativeButton.setVisibility(View.VISIBLE);
        mNegativeButton.setText(name);
        this.mOnNegativeClickListener = listener;
        return this;
    }

    public PlumBerryCheckBox setOnPositiveClickListener(String name, OnClickListener listener) {
        mPositiveButton.setVisibility(View.VISIBLE);
        mPositiveButton.setText(name);
        this.mOnPositiveClickListener = listener;
        return this;
    }

    public RadioGroup getRadioGroup() {
        return mRadioGroup;
    }

    public PlumBerryCheckBox setTag(Object o) {
        mTag = o;
        return this;
    }

    public Object getTag(){
        return mTag;
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (mOnNegativeClickListener != null && id == R.id.negative_button) {
            mOnNegativeClickListener.onClick(this, NEGATIVE_BUTTON);
        } else if (mOnPositiveClickListener != null && id == R.id.positive_button) {
            mOnPositiveClickListener.onClick(this, POSITIVE_BUTTON);
        }
    }

    /**
     * The listener for our dialog buttons.
     */
    public interface OnClickListener {

        public void onClick(PlumBerryCheckBox dialog, int type);

    }
}
