Migration
=========

1.0.0 or later -> 3.0.0
-----------------------
The `PaperboyBuilder` now uses Kotlin Lambdas for easy configuration. To get
the old method chaining back again, use `PaperboyChainBuilder`.

#### PaperboyChainBuilder

The methods pattern has changed. The _set_ at the beginning has been removed.
For example `setViewType(...)` is now `viewType(...)`.

#### Kotlin

If you are using Kotlin you should use the new method `buildPaperboy(Context)`
instead. For example:
```kotlin
import com.github.porokoro.paperboy.builders.buildItemType
import com.github.porokoro.paperboy.builders.buildPaperboy

val fragment = buildPaperboy(this) {
    viewType = ViewTypes.HEADER
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
```
You can look at the code of the [sample app](https://github.com/porokoro/paperboy/blob/develop/sample/src/main/kotlin/com/github/porokoro/paperboy/sample/MainActivity.kt)
for more variations.
