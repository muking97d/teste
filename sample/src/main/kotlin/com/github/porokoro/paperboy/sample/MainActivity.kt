/*
 * Copyright (C) 2015-2016 Dominik Hibbeln
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

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.widget.FrameLayout
import com.github.porokoro.paperboy.ViewTypes
import com.github.porokoro.paperboy.builders.buildItemType
import com.github.porokoro.paperboy.builders.buildPaperboy
import org.jetbrains.anko.defaultSharedPreferences
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {
    private var drawerLayout by Delegates.notNull<DrawerLayout>()
    private var drawerNavigation by Delegates.notNull<FrameLayout>()
    private var drawerToggle by Delegates.notNull<ActionBarDrawerToggle>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(find(R.id.toolbar))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        drawerLayout = find(R.id.drawer_layout)
        drawerNavigation = find(R.id.drawer_navigation)

        drawerToggle = ActionBarDrawerToggle(this, drawerLayout, find(R.id.toolbar),
                R.string.navdrawer_open, R.string.navdrawer_close)
        drawerLayout.setDrawerListener(drawerToggle)

        if (savedInstanceState == null) {
            val fragment = buildPaperboy(this) {
                itemTypes = listOf(
                        buildItemType(this@MainActivity, 1000, "Custom", "c") {
                            colorRes = R.color.item_type_custom
                            titleSingularRes = R.string.item_type_custom
                            titlePluralRes = R.string.item_type_customs
                            icon = R.drawable.ic_build_black_24dp
                            sortOrder = 0
                        }
                )
            }

            val settings = SettingsFragment() {
                when (it.key) {
                    "pref_java_default" ->
                        startActivity<JavaSampleActivity>(JavaSampleActivity.ARG_SAMPLE to JavaSampleActivity.SAMPLE1_DEFAULT)
                    "pref_java_custom" ->
                        startActivity<JavaSampleActivity>(JavaSampleActivity.ARG_SAMPLE to JavaSampleActivity.SAMPLE1_CUSTOM)
                    "pref_java2_default" ->
                        startActivity<JavaSampleActivity>(JavaSampleActivity.ARG_SAMPLE to JavaSampleActivity.SAMPLE2_DEFAULT)
                    "pref_java2_custom" ->
                        startActivity<JavaSampleActivity>(JavaSampleActivity.ARG_SAMPLE to JavaSampleActivity.SAMPLE2_CUSTOM)
                }
                true
            }

            supportFragmentManager.beginTransaction()
                    .add(R.id.content, fragment)
                    .add(R.id.drawer_navigation, settings)
                    .commit()
        }

        onSharedPreferenceChanged(defaultSharedPreferences, null)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onResume() {
        super.onResume()
        defaultSharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        defaultSharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (sharedPreferences == null) return

        val fragment = buildPaperboy(this) {
            viewType = when (sharedPreferences.getString("pref_view_types", "1")) {
                "2" -> ViewTypes.LABEL
                "3" -> ViewTypes.ICON
                "4" -> ViewTypes.HEADER
                else -> ViewTypes.NONE
            }
            sectionLayout = if (sharedPreferences.getBoolean("pref_custom_section", false))
                R.layout.view_section_custom else 0
            typeLayout = if (sharedPreferences.getBoolean("pref_custom_type", false))
                R.layout.view_type_custom else 0
            itemLayout = if (sharedPreferences.getBoolean("pref_custom_item", false))
                R.layout.view_item_custom else 0
            sortItems = sharedPreferences.getBoolean("pref_sort", false)
            itemTypes = listOf(
                    buildItemType(this@MainActivity, 1000, "Custom", "c") {
                        colorRes = R.color.item_type_custom
                        titleSingularRes = R.string.item_type_custom
                        titlePluralRes = R.string.item_type_customs
                        icon = R.drawable.ic_build_black_24dp
                        sortOrder = 0
                    }
            )
        }

        supportFragmentManager.beginTransaction()
                .replace(R.id.content, fragment)
                .commit()

        drawerLayout.closeDrawer(drawerNavigation)
    }
}
