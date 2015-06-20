/*
 * Copyright (C) 2015 porokoro
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.github.porokoro.paperboy;

import android.support.annotation.NonNull;

class ItemTypes {
    public static final int NONE        = 0;
    public static final int FEATURE     = 1;
    public static final int BUG         = 2;
    public static final int IMPROVEMENT = 3;

    private ItemTypes() {
    }

    @ItemType
    public static int fromValue(int value) {
        switch (value) {
            case FEATURE:
            case BUG:
            case IMPROVEMENT:
                return value;
            default:
                return NONE;
        }
    }

    @ItemType
    public static int fromValue(@NonNull String value) {
        switch (value) {
            case "f":
            case "feature":
                return FEATURE;
            case "b":
            case "bug":
                return BUG;
            case "i":
            case "improvement":
                return IMPROVEMENT;
            default:
                return NONE;
        }
    }
}
