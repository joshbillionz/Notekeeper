package com.codebillionz.android.notekeeper;

import org.hamcrest.core.AllOf;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
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


//step 1 this annotation
@RunWith(AndroidJUnit4.class)
public class NoteCreationTest {
    //we need an activity to be tested
    //CREATES THISactivity before each test and clean up after each test

    static DataManager sDataManager;

    @BeforeClass
    public static void classSetUp(){
        sDataManager = DataManager.getInstance();
    }

    @Rule
    public ActivityTestRule<NoteListActivity> mListActivityRule =  new ActivityTestRule<>(NoteListActivity.class);

    @Test
    public void createNewNote(){

        final CourseInfo course = sDataManager.getCourse("java_lang");
      final  String title  = "The note titleT";
        final String  text = " this is the text of the note.";


//        //get a view
//        ViewInteraction fabNewNote = onView(withId(R.id.fab));
//        //perform action
//        fabNewNote.perform(click());

        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.spinner)).perform(click());
        onData(allOf(instanceOf(CourseInfo.class),equalTo(course))).perform(click());

        onView(withId(R.id.spinner)).check(matches(withSpinnerText(containsString(course.getTitle()))));

        onView(withId(R.id.text_title)).perform(typeText(title)).check(matches(withText(containsString(title))));

        onView(withId(R.id.text_body)).perform((typeText(text)), closeSoftKeyboard());

         onView(withId(R.id.text_body)).check(matches(withText(containsString(text))));

        pressBack();
        int index = sDataManager.getNotes().size()-1;
        NoteInfo  compareNote = sDataManager.getNotes().get(index);
        assertEquals(course, compareNote.getCourse());
        assertEquals(title,compareNote.getTitle());
        assertEquals(text,compareNote.getText());



//
    }


}