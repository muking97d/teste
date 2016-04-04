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

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import org.jetbrains.anko.find

class JavaSampleActivity : AppCompatActivity() {
    companion object {
        val SAMPLE1_DEFAULT = 1
        val SAMPLE1_CUSTOM = 2
        val SAMPLE2_DEFAULT = 3
        val SAMPLE2_CUSTOM = 4
        val ARG_SAMPLE = "sample"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_java_sample)
        setSupportActionBar(find(R.id.toolbar))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.content, when (intent?.getIntExtra(ARG_SAMPLE, 0)) {
                        SAMPLE1_DEFAULT -> JavaSample.buildDefault(this)
                        SAMPLE1_CUSTOM -> JavaSample.buildCustom(this)
                        SAMPLE2_DEFAULT -> JavaSample2.buildDefault(this)
                        SAMPLE2_CUSTOM -> JavaSample2.buildCustom(this)
                        else -> throw Exception()
                    })
                    .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?) =
            when (item?.itemId) {
                android.R.id.home -> {
                    finish()
                    true
                }
                else -> super.onOptionsItemSelected(item)
            }
}
