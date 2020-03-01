package com.example.mobileliveyoutube.utils

import android.content.Context
import android.util.TypedValue
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.mobileliveyoutube.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

// https://medium.com/@BladeCoder/kotlin-singletons-with-argument-194ef06edd9e
open class SingletonHolder<out OUT: Any, in INPUT>(creator: (INPUT) -> OUT) {
    private var creator: ((INPUT) -> OUT)? = creator
    @Volatile private var instance: OUT? = null

    /** Initializes and returns singleton if none was created. */
    fun initialize(arg: INPUT): OUT {
        val newInstance = instance
        if (newInstance != null) return newInstance
        return synchronized(this) {
            val newInstance2 = instance
            // Re-check if "instance" was assigned
            if (newInstance2 != null) {
                newInstance2
            } else {
                val created = creator!!(arg)
                instance = created
                creator = null
                created
            }
        }
    }
    /** Added for convenience without a [Context], if and only if, components are 100% sure
     * [initialize] was called somewhere else.
     */
    fun getInstance(): OUT =
        instance?:
        throw Error("Use \"${this::creator.javaClass.simpleName}#initialize(Context)\" at least once")
}

/** Sets up all related navigation components within an activity.
 * This method is used for those activities having:
 * * A [Toolbar]
 * * A [DrawerLayout] attached to the [Toolbar]
 * * A [NavigationView] within the [DrawerLayout]
 * * A [BottomNavigationView] - displayed like a Tab Layout with up to 5 items.
 * Both [BottomNavigationView] and [NavigationView] items are kept in sync.
 * @param navContainerId ID for navigation container - `<fragment ...>` tag inside
 *        a layout with `app:navGraph` attribute.
 *
 * @param toolbar Actual [Toolbar]
 * @param drawerLayout Actual [DrawerLayout]
 * @param bottomNavigationView Actual [BottomNavigationView]
 * @param navigationView Actual [NavigationView]
 * @param syncToolbarNavIcon If navigation icon in toolbar should be in sync with navigation.
 *        If true, then the icon will change to an arrow when navigating to other fragments
 *        and will act as a back-button.
 *        Default: `false` - because for fragments having the same level, app should not show a back-arrow.
 */
fun AppCompatActivity.uxStyleSetupNavigationComponents(
    @IdRes navContainerId: Int,
    title_textView : TextView? = null,
    toolbar: Toolbar? = null,
    updateToolbarTitle: Boolean = true
) {
    findNavController(navContainerId).apply nav@ {
        toolbar?.let {
            setSupportActionBar(toolbar)
            // IMPORTANT: For this specific case, we set the icon in the layout, and apply tint
            it.navigationIcon?.setTint(
                getStyledColorAttribute(
                    this@uxStyleSetupNavigationComponents,
                    R.attr.colorOnPrimary
                )
            )
        }

        // Sets up toolbar title when Activity is created.
        if (updateToolbarTitle) {
            //supportActionBar?.title = this.graph.findNode(this.currentDestination!!.id)?.label
            this.graph.findNode(this.currentDestination!!.id)?.let {
                supportActionBar?.setTitle(it.label.toString())
            }
        } else {
            supportActionBar?.setDisplayShowTitleEnabled(false)
        }
        // Sets up toolbar title when user navigates to a different fragment
        addOnDestinationChangedListener { _, destination, _ ->
            toolbar?.title = null
            if (updateToolbarTitle) {
                title_textView?.text = destination.label.toString()
            }
        }
    }
}

fun getStyledColorAttribute(ctx: Context, @AttrRes attrColor: Int): Int {
    val typedValue = TypedValue()
    val attr = ctx.obtainStyledAttributes(typedValue.data, intArrayOf(attrColor))
    val color = attr.getColor(0, 0)
    attr.recycle()
    return color
}

/** Simple wat to avoid writing ViewModel factories if ViewModel requires arguments.
 * Ref: [https://proandroiddev.com/kotlin-delegates-in-android-development-part-2-2c15c11ff438]
 */
inline fun <VM : ViewModel> viewModelFactory(crossinline f: () -> VM) =
    object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(aClass: Class<T>):T = f() as T
    }