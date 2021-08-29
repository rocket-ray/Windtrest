package com.example.app_team35



import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import org.hamcrest.CoreMatchers.not


import org.junit.Rule
import org.junit.Test



/**
 * UI test av funksjonalitetene til 'Favoritt'
 *
 * Hensikten med denne testen er å se om favorittelementene blir lagret/slettet
 * ved relaunch av applikasjonen.
 * Å kunne slette ble ikke implementer og testen for den vil derfor faile.
 * Men de står igjen, da man egentlig skal implementere funksjonalitetene utifra at de skal bestå testen.
 *
 * Denne testen har blitt endret for å illustrere hvordan vi har brukt espresso og
 * automatiske tester. I utganspunktet skulle koden justeres utifra koden, og ikke motsatt.
 *
 *
 *
 *
 */


class favouriteLocationTest {



    @Rule
    @JvmField
    val rule: ActivityTestRule<MainActivity> = ActivityTestRule(MainActivity::class.java)



    //passed hvis listen er tom
    @Test
    fun favIsEmpty() {
        Thread.sleep(500)
        onView(withId(R.id.favouriteLocationsFragment)).perform(click())
        onView(withId(R.id.fragment0)).check(matches(not(isDisplayed())))
        // Sjekk at lagringslista er tom
        Thread.sleep(2000)
    }

    //passed hvis man kan trykke lagre, gjørs på "din posison"
    @Test
    fun userCanSave(){
        Thread.sleep(5000)
        onView(withId(R.id.forecast_favorite)).perform(click())
        Thread.sleep(2000)

    }


    //Passed hvis listen ikke er tom.
    @Test
    fun favIsSaved() {
        onView(withId(R.id.favouriteLocationsFragment)).perform(click())
        onView(withId(R.id.fragment0)).check(matches(isDisplayed()))

    }


    //Ikke eksisterende funksjonalitet, derfor FAIL
    @Test
    fun userCanRemoveFavItem(){
        onView(withId(R.id.mapFragment)).perform(click())
        //Sletting ved swiping

    }


        // Denne er lik favIsEmpty(),
    @Test
    fun itemIsDeleted(){
        Thread.sleep(500)
        onView(withId(R.id.favouriteLocationsFragment)).perform(click())
        onView(withId(R.id.fragment0)).check(matches(not(isDisplayed())))
        // Sjekk at lagringslista er tom
        Thread.sleep(2000)

    }

}