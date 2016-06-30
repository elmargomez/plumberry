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

package com.elmargomez.plumberry.math;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class ScreenCoordinate {

    public static final int TOP_LEFT = 0;
    public static final int TOP_RIGHT = 1;
    public static final int BOTTOM_LEFT = 2;
    public static final int BOTTOM_RIGHT = 3;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT})
    public @interface ScreenLocation {

    }

    public int width;
    public int height;
    public int x;
    public int y;

    public ScreenCoordinate(int width, int height, int x, int y) {
        if (x > width) {
            throw new IllegalArgumentException("X must not be greater than width!");
        }

        if (y > height) {
            throw new IllegalArgumentException("Y must not be greater than height!");
        }

        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public int leftSpace() {
        return x;
    }

    public int rightSpace() {
        return width - x;
    }

    public int topSpace() {
        return y;
    }

    public int bottomSpace() {
        return height - y;
    }

    /**
     * Returns the location is which space is bigger.
     *
     * @return either {@link ScreenLocation} constants.
     */
    @ScreenLocation
    public int getMostSpace() {
        if (bottomSpace() > topSpace()) {
            if (leftSpace() > rightSpace()) {
                return BOTTOM_LEFT;
            } else {
                return BOTTOM_RIGHT;
            }
        } else {
            if (leftSpace() > rightSpace()) {
                return TOP_LEFT;
            } else {
                return TOP_RIGHT;
            }
        }
    }

}
