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
import android.support.annotation.MenuRes;
import android.view.Window;
import android.widget.ListView;

import com.elmargomez.plumberry.MenuModel;
import com.elmargomez.plumberry.R;
import com.elmargomez.plumberry.util.MenuUtil;

import java.util.ArrayList;
import java.util.List;

public class PlumBerryCheckBox extends Dialog {

    private List<MenuModel> mMenuArray;
    private CheckBoxAdapter mAdapter;
    private ListView mListView;

    public PlumBerryCheckBox(Context context) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.check_dialog);
        mListView = (ListView) findViewById(R.id.list);
        mMenuArray = new ArrayList<>();
        mAdapter = new CheckBoxAdapter(context, mMenuArray);
        mListView.setAdapter(mAdapter);
    }

    public PlumBerryCheckBox setMenu(@MenuRes int menu) {
        mMenuArray.clear();
        mMenuArray.addAll(MenuUtil.getMenuModels(getContext(), menu));
        mAdapter.notifyDataSetChanged();
        return this;
    }

}
