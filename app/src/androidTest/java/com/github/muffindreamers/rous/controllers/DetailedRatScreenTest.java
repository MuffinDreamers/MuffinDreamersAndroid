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
 * Created by carty on 11/15/2017.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DetailedRatScreenTest {
    @Rule
    public IntentsTestRule<DetailedRatScreen> intentsRule =
            new IntentsTestRule<>(DetailedRatScreen.class);

    @Test
    public void verifyReturnButton() {
        onView(withId(R.id.return_to_main))
                .perform(click());

        intended(allOf(
                hasComponent(hasShortClassName(".controllers.FetchRatDataActivity")),
                toPackage("com.github.muffindreamers.rous")));
    }
}
