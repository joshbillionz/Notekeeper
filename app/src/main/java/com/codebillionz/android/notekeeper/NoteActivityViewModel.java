package com.codebillionz.android.notekeeper;

import android.os.Bundle;

import androidx.lifecycle.ViewModel;

public class NoteActivityViewModel  extends ViewModel {
    public static final String ORIGINAL_NOTE_COURSE_ID= "com.codebillionz.android.notekeeper.ORIGINAL_COURSE_ID";
    public static final String ORIGINAL_NOTE_TITLE = "com.codebillionz.android.notekeeper.ORIGINAL_NOTE_TITLE";
    public static final String  ORIGINAL_NOTE_TEXT ="com.codebillionz.android.notekeeper.ORIGINAL_NOTE_TEXT";

    public String mOriginalCourseId;
    public  String mOriginalNoteTitle;
    public  String mOriginalNoteText;
    public boolean mIsNewlyCreated= true;

    public void saveState(Bundle outState) {
        outState.putString(ORIGINAL_NOTE_COURSE_ID, mOriginalCourseId);
        outState.putString(ORIGINAL_NOTE_TITLE,mOriginalNoteTitle);
        outState.putString(ORIGINAL_NOTE_TEXT,mOriginalNoteText);
    }

    public void restoreState(Bundle instate){
        mOriginalCourseId = instate.getString(ORIGINAL_NOTE_COURSE_ID);
        mOriginalNoteTitle = instate.getString(ORIGINAL_NOTE_TITLE);
        mOriginalNoteText = instate.getString(ORIGINAL_NOTE_TEXT);
    }
}
