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
import android.support.annotation.MenuRes;

public class PlumBerry {

    private static PlumBerry pbInstance;
    private Context context;

    private PlumBerry(Context context) {
        this.context = context;
    }

    public static synchronized PlumBerry build(Context context) {
        if (pbInstance == null) {
            pbInstance = new PlumBerry(context);
        }
        return pbInstance;
    }

    public ContextMenuCreator menu(@MenuRes int id) {
        return new ContextMenuCreator(context, id);
    }
}
