package com.dzhuchinski.csvviewer

import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.TableLayout
import android.widget.TableRow
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class CsvViewerInstrumentedTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun setup() {
        (activityRule.activity as MainActivity).FILE_NAME = "issues.csv"
    }

    @Test
    fun testFirstColumn() {
        onView(
            allOf(
                isDescendantOfA(isAssignableFrom(TableLayout::class.java)),
                isInRowBelow(withText("Theo")),
                hasChildPosition(0)
            )
        ).check(matches(withText("Fiona")))

        onView(
            allOf(
                isDescendantOfA(isAssignableFrom(TableLayout::class.java)),
                isInRowBelow(withText("Fiona")),
                hasChildPosition(0)
            )
        ).check(matches(withText("Petra")))
    }

    @Test
    fun testSecondColumn() {
        onView(
            allOf(
                isDescendantOfA(isAssignableFrom(TableLayout::class.java)),
                isInRowBelow(withText("Jansen")),
                hasChildPosition(1)
            )
        ).check(matches(withText("de Vries")))

        onView(
            allOf(
                isDescendantOfA(isAssignableFrom(TableLayout::class.java)),
                isInRowBelow(withText("de Vries")),
                hasChildPosition(1)
            )
        ).check(matches(withText("Boersma")))
    }

    private fun isInRowBelow(viewInRowAbove: Matcher<View?>): Matcher<View> {
        return object : TypeSafeMatcher<View>() {

            override fun describeTo(description: Description?) {
                description?.appendText("is below a: ")
                viewInRowAbove.describeTo(description)
            }

            override fun matchesSafely(view: View): Boolean {
                val viewParent: ViewParent = view.parent as? TableRow ?: return false
                val currentRow: TableRow = viewParent as TableRow
                // Find the row above
                val table = currentRow.parent as TableLayout
                val currentRowIndex = table.indexOfChild(currentRow)
                if (currentRowIndex < 1) {
                    return false
                }
                val rowAbove: TableRow = table.getChildAt(currentRowIndex - 1) as TableRow
                // Does the row above contains at least one view that matches viewInRowAbove?
                for (i in 0 until rowAbove.childCount) {
                    if (viewInRowAbove.matches(rowAbove.getChildAt(i))) {
                        return true
                    }
                }
                return false
            }
        }
    }

    private fun hasChildPosition(position: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun matchesSafely(view: View): Boolean {
                val viewParent: ViewParent = view.parent as? ViewGroup ?: return false
                val viewGroup = viewParent as ViewGroup
                return viewGroup.indexOfChild(view) == position
            }

            override fun describeTo(description: Description?) {
                description?.appendText("is child at position: $position")
            }
        }
    }
}
