Paperboy
========

A changelog library for Android

[![Screenshot 1](https://raw.githubusercontent.com/porokoro/paperboy/release/2.0.0/art/screenshot-1-thumbnail.png)](https://raw.githubusercontent.com/porokoro/paperboy/release/2.0.0/art/screenshot-1.png)
[![Screenshot 2](https://raw.githubusercontent.com/porokoro/paperboy/release/2.0.0/art/screenshot-2-thumbnail.png)](https://raw.githubusercontent.com/porokoro/paperboy/release/2.0.0/art/screenshot-2.png)
[![Screenshot 3](https://raw.githubusercontent.com/porokoro/paperboy/release/2.0.0/art/screenshot-3-thumbnail.png)](https://raw.githubusercontent.com/porokoro/paperboy/release/2.0.0/art/screenshot-3.png)
[![Screenshot 4](https://raw.githubusercontent.com/porokoro/paperboy/release/2.0.0/art/screenshot-4-thumbnail.png)](https://raw.githubusercontent.com/porokoro/paperboy/release/2.0.0/art/screenshot-4.png)

Download
--------

Grab via Gradle:
```groovy
compile 'com.github.porokoro.paperboy:paperboy:2.0.0'
```
or Maven:
```xml
<dependency>
    <groupId>com.github.porokoro.paperboy</groupId>
    <artifactId>paperboy</artifactId>
    <version>2.0.0</version>
</dependency>
```

Usage
-----

This library mainly consists of one **Fragment** that you can include wherever
you like. This class is the `PaperboyFragment`. It also features an simple
**Builder** residing inside the fragment which allows you to configure its
visual representation.

The changelog elements are loaded from a **json** file that normally resides
inside your `assets` Folder. The library supports language resolving
as you should know it from the standard Android resource folders.

Please place your **json** files under `assets/paperboy/changelog.json`.
To provide alternate versions for different languages proceed as follows:
* `assets/paperboy/changelog-de.json`
* `assets/paperboy/changelog-jp.json`
* `...`

As an alternative you can use the `res/raw` folder for changelog files. Just
tell paperboy which file to use by calling `setFileRes(int)` on the builder.


### JSON Structure ###

You write the json files in the following structure:
```json
[
    {
        "name": "1.0.0",
        "items": [
            {
                "type": "F",
                "title": "This is a new feature!"
            },
            {
                "type": "B",
                "title": "This is a fixed bug!"
            },
            {
                "type": "I",
                "title": "This is an improvement!"
            }
        ]
    }
]
```

For further examples please see the [sample project](https://github.com/porokoro/paperboy/blob/develop/sample/src/main/assets/paperboy/changelog.json).

The structure should be very simple and straight forward. Please note that all
**keys** are handled **case insensitive**. So you don't need to worry about
that.

For example:
```json
[
    {
        "NAMe": "1.0.0",
        "ITEMS": [
            {
                "TyPe": "F",
                "tITLe": "This is a feature."
            }
        ]
    }
]
```

### Item types ###

You can define an item type in different ways.

* Feature: `1`, `F` or `FEATURE`
* Bug: `2`, `B` or `BUG`
* Improvement: `3`, `I` or `IMPROVEMENT`

Please note that the **string representations** are handled the same way as the
json keys, **case insensitive**.

In addition you can define your own item types using the `ItemTypeBuilder` in
combination with the `addItemType(ItemType)` method on `PaperboyBuilder`. You
can even override the default ones.

License
-------

```
Copyright 2015 porokoro

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
