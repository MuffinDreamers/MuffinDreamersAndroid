package com.github.muffindreamers.rous.controllers;

import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.github.muffindreamers.rous.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by leemr on 11/8/2017.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AddNewRatDataTest {
    @Rule
    public IntentsTestRule<AddNewRatData> intentsRule =
            new IntentsTestRule<AddNewRatData>(AddNewRatData.class);

    @Test
    public void verifyCancelButton() {
        onView(withId(R.id.cancel_add_rat))
                .perform(click());

        intended(allOf(
                hasComponent(hasShortClassName(".controllers.FetchRatDataActivity")),
                toPackage("com.github.muffindreamers.rous")));
    }
}
