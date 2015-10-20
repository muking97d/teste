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

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewParent;

/**
 * A {@link AppCompatTextView} that dispatches its touch events to its parent if it's clickable. The touch event is
 * dispatched to support clickable {@link AppCompatTextView}s and {@link ViewGroup}s with animated backgrounds at the
 * same time.
 */
public class TouchDispatchingTextView extends AppCompatTextView {
    public TouchDispatchingTextView(Context context) {
        super(context);
    }

    public TouchDispatchingTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchDispatchingTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (isClickable()) {
            ViewParent parent = getParent();
            if (parent != null && parent instanceof ViewGroup)
                ((ViewGroup) parent).onTouchEvent(event);
        }

        return super.onTouchEvent(event);
    }
}
