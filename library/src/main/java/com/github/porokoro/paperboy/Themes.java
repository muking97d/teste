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

public class Themes {
    public static final int NONE  = 0;
    public static final int DARK  = 1;
    public static final int LIGHT = 2;

    private Themes() {
    }

    @Theme
    public static int fromValue(int value) {
        switch (value) {
            case DARK:
            case LIGHT:
                return value;
            default:
                return NONE;
        }
    }
}
