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

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.elmargomez.plumberry.MenuModel;
import com.elmargomez.plumberry.R;

import java.util.List;

public class CheckBoxAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<MenuModel> menuModels;

    public CheckBoxAdapter(Context context, List<MenuModel> menuModels) {
        this.inflater = LayoutInflater.from(context);
        this.menuModels = menuModels;
    }

    @Override
    public int getCount() {
        return menuModels.size();
    }

    @Override
    public Object getItem(int position) {
        return menuModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CheckBoxViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.check_item, parent, false);
            holder = new CheckBoxViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        } else {
            holder = (CheckBoxViewHolder) convertView.getTag();
        }

        MenuModel menuModel = menuModels.get(position);
        holder.title.setText(menuModel.getTitle());
        return convertView;
    }

}
