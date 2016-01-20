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

import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.github.porokoro.paperboy.ViewTypes
import com.github.porokoro.paperboy.builders.buildItemType
import com.github.porokoro.paperboy.builders.buildPaperboy
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity
import kotlin.properties.Delegates

public class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var drawerLayout by Delegates.notNull<DrawerLayout>()
    private var drawerNavigation by Delegates.notNull<NavigationView>()
    private var drawerToggle by Delegates.notNull<ActionBarDrawerToggle>()

    private var viewType = ViewTypes.NONE
    private var sectionLayout = 0
    private var typeLayout = 0
    private var itemLayout = 0
    private var sort = false

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
        drawerNavigation.setNavigationItemSelectedListener(this)

        if (savedInstanceState == null) {
            val fragment = buildPaperboy(this) {
                viewType = this@MainActivity.viewType
                sectionLayout = this@MainActivity.sectionLayout
                typeLayout = this@MainActivity.typeLayout
                itemLayout = this@MainActivity.itemLayout
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
                    .add(R.id.content, fragment)
                    .commit()
        }
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onNavigationItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.section_view_type_none -> viewType = ViewTypes.NONE
            R.id.section_view_type_labels -> viewType = ViewTypes.LABEL
            R.id.section_view_type_icons -> viewType = ViewTypes.ICON
            R.id.section_view_type_headers -> viewType = ViewTypes.HEADER
            R.id.section_custom_section_default -> sectionLayout = 0
            R.id.section_custom_section_custom -> sectionLayout = R.layout.view_section_custom
            R.id.section_custom_type_default -> typeLayout = 0
            R.id.section_custom_type_custom -> typeLayout = R.layout.view_type_custom
            R.id.section_custom_item_default -> itemLayout = 0
            R.id.section_custom_item_custom -> itemLayout = R.layout.view_item_custom
            R.id.section_sort_none -> sort = false
            R.id.section_sort_items -> sort = true
            R.id.section_java_default ->
                startActivity<JavaSampleActivity>(JavaSampleActivity.ARG_SAMPLE to JavaSampleActivity.SAMPLE1_DEFAULT)
            R.id.section_java_custom ->
                startActivity<JavaSampleActivity>(JavaSampleActivity.ARG_SAMPLE to JavaSampleActivity.SAMPLE1_CUSTOM)
            R.id.section_java2_default ->
                startActivity<JavaSampleActivity>(JavaSampleActivity.ARG_SAMPLE to JavaSampleActivity.SAMPLE2_DEFAULT)
            R.id.section_java2_custom ->
                startActivity<JavaSampleActivity>(JavaSampleActivity.ARG_SAMPLE to JavaSampleActivity.SAMPLE2_CUSTOM)
        }

        onSectionSelected()
        return true;
    }

    private fun onSectionSelected() {
        val fragment = buildPaperboy(this) {
            viewType = this@MainActivity.viewType
            sectionLayout = this@MainActivity.sectionLayout
            typeLayout = this@MainActivity.typeLayout
            itemLayout = this@MainActivity.itemLayout
            sortItems = this@MainActivity.sort
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
