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
package com.github.porokoro.paperboy.sample;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.github.porokoro.paperboy.PaperboyFragmentBuilder;
import com.github.porokoro.paperboy.ViewType;
import com.github.porokoro.paperboy.ViewTypes;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout          m_drawerLayout;
    private NavigationView        m_drawerNavigation;
    private ActionBarDrawerToggle m_drawerToggle;

    @ViewType
    private int     m_viewType = ViewTypes.NONE;
    private boolean m_sort     = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        m_drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        m_drawerNavigation = (NavigationView) findViewById(R.id.drawer_navigation);

        m_drawerToggle = new ActionBarDrawerToggle(this, m_drawerLayout, (Toolbar) findViewById(R.id.toolbar),
                                                   R.string.navdrawer_open, R.string.navdrawer_close);
        m_drawerLayout.setDrawerListener(m_drawerToggle);
        m_drawerNavigation.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            Fragment fragment = new PaperboyFragmentBuilder(this)
                    .withDefinition(1000, "Custom", "c")
                    .setColorRes(R.color.item_type_custom)
                    .setTitleSingular(R.string.item_type_custom)
                    .setTitlePlural(R.string.item_type_customs)
                    .setIcon(R.drawable.ic_build)
                    .setSortOrder(0)
                    .add()
                    .build();

            getSupportFragmentManager().beginTransaction()
                                       .add(R.id.content, fragment)
                                       .commit();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        m_drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        m_drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.section_view_type_none:
                m_viewType = ViewTypes.NONE;
                break;
            case R.id.section_view_type_labels:
                m_viewType = ViewTypes.LABEL;
                break;
            case R.id.section_view_type_icons:
                m_viewType = ViewTypes.ICON;
                break;
            case R.id.section_view_type_headers:
                m_viewType = ViewTypes.HEADER;
                break;
            case R.id.section_sort_none:
                m_sort = false;
                break;
            case R.id.section_sort_items:
                m_sort = true;
                break;
        }

        onSectionSelected(item);
        return true;
    }

    private void onSectionSelected(@NonNull MenuItem item) {
        if (item.isChecked())
            return;

        item.setChecked(true);

        Fragment fragment = new PaperboyFragmentBuilder(this)
                .setViewType(m_viewType)
                .setSortItems(m_sort)
                .withDefinition(1000, "Custom", "c")
                .setColorRes(R.color.item_type_custom)
                .setTitleSingular(R.string.item_type_custom)
                .setTitlePlural(R.string.item_type_customs)
                .setIcon(R.drawable.ic_build)
                .setSortOrder(0)
                .add()
                .build();

        getSupportFragmentManager().beginTransaction()
                                   .replace(R.id.content, fragment)
                                   .commit();

        m_drawerLayout.closeDrawer(m_drawerNavigation);
    }
}
