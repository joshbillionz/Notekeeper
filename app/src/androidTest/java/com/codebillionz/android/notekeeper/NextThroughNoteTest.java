package com.codebillionz.android.notekeeper;

import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import static org.junit.Assert.*;

import static  androidx.test.espresso.Espresso.*;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;

import static org.hamcrest.Matchers.*;
import static androidx.test.espresso.Espresso.pressBack;
import  static androidx.test.espresso.assertion.ViewAssertions.*;

public class NextThroughNoteTest {
    @Rule
    public ActivityTestRule<MainActivity>  mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void NextThroughNotes(){
        int position = 0;
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_notes));
        onView(withId(R.id.list_items)).perform(RecyclerViewActions.actionOnItemAtPosition(position,click()));

        List<NoteInfo> notes = DataManager.getInstance().getNotes();
        for(int index =  position ; index  < notes.size() ; index++){

            NoteInfo note = notes.get(index);
            onView(withId(R.id.spinner)).check(matches(withSpinnerText(note.getCourse().getTitle())));
            onView(withId(R.id.text_title)).check(matches(withText(note.getTitle())))  ;
            onView(withId(R.id.text_body)).check(matches(withText(note.getText())));

            if(index<notes.size()-1){
                onView(allOf(withId(R.id.action_next),isEnabled())).perform(click());
            }
        }
        onView(withId(R.id.action_next)).check(matches(not(isEnabled())));


 }

}