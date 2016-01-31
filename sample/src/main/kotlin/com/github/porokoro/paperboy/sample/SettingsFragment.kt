/*
 * Copyright (C) 2015-2016 porokoro
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.porokoro.paperboy.sample

import android.os.Bundle
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat

class SettingsFragment(private val listener: (Preference) -> Boolean) : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.navdrawer)

        preferenceScreen.findPreference("pref_java_default").setOnPreferenceClickListener(listener)
        preferenceScreen.findPreference("pref_java_custom").setOnPreferenceClickListener(listener)
        preferenceScreen.findPreference("pref_java2_default").setOnPreferenceClickListener(listener)
        preferenceScreen.findPreference("pref_java2_custom").setOnPreferenceClickListener(listener)
    }
}
