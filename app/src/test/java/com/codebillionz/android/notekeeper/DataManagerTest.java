package com.codebillionz.android.notekeeper;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class DataManagerTest {
    static DataManager sDataManager;

    @BeforeClass
    public static void classSetup(){
        sDataManager = DataManager.getInstance();
        
    }


    @Before
    public void setUp(){
        DataManager sDataManager = DataManager.getInstance();
        sDataManager.getNotes().clear(); 
        sDataManager.initializeExampleNotes();
    }


    @Test
    public void createNewNote() {
        final DataManager sDataManager = DataManager.getInstance();
        final  CourseInfo course  = sDataManager.getCourse("android_async");
        final String noteTitle ="Text note title";
        final String noteText = "this is the body of the note";

        int noteIndex = sDataManager.createNewNote();
        NoteInfo newNote = sDataManager.getNotes().get(noteIndex);
        newNote.setCourse(course);
        newNote.setTitle(noteTitle);
        newNote.setText(noteText);

        NoteInfo compareNote = sDataManager.getNotes().get(noteIndex);

         assertEquals(course,compareNote.getCourse());
        assertEquals(noteTitle,compareNote.getTitle());
        assertEquals(noteText,compareNote.getText());


    }

    @Test
    public void findSimilarNotes(){
        DataManager sDataManager = DataManager.getInstance();
        final CourseInfo course = sDataManager.getCourse("android_async");
        final String noteTitle ="Text note title";
        final String noteText1 = "this is the body of the note";
        final String noteText2 = "this is the body of the second  note";

        int noteIndex1 = sDataManager.createNewNote();
        NoteInfo newNote1 = sDataManager.getNotes().get(noteIndex1);
        newNote1.setCourse(course);
        newNote1.setTitle(noteTitle);
        newNote1.setText(noteText1);

        int noteIndex2 = sDataManager.createNewNote();
        NoteInfo newNote2 = sDataManager.getNotes().get(noteIndex2);
        newNote2.setCourse(course);
        newNote2.setTitle(noteTitle);
        newNote2.setText(noteText2 );

        int foundIndex1 = sDataManager.findNote(newNote1);
        assertEquals(noteIndex1,foundIndex1);


        int foundIndex2 = sDataManager.findNote(newNote2);
        assertEquals(noteIndex2,foundIndex2);


    }

    @Test
    public void createNewNoteOneStepCreation(){
            CourseInfo course = sDataManager.getCourse("android_async");
            String noteTitle = " create new note one step creation";
            String noteText = "this is the body of the new note";

             int index = sDataManager.createNewNote(course,noteTitle,noteText);
             NoteInfo  note = sDataManager.getNotes().get(index);

             assertEquals(course,note.getCourse());
             assertEquals(noteTitle,note.getTitle());
             assertEquals(noteText,note.getText());

    }

}