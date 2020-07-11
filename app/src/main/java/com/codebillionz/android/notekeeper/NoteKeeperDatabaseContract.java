package com.codebillionz.android.notekeeper;

final  public class NoteKeeperDatabaseContract {
    private NoteKeeperDatabaseContract(){

    }

    public static class CourseInfoEntry{

        public static final String TABLE_NAME = "course_info";
        public static final String COLUMN_COURSE_ID="course_id";
        public static final String COLUMN_COURSE_TITLE="course_title";

        //CREATE TABLE  course_info (course_id, course_title)

        public static final String SQL_CREATE_TABLE =  "CREATE TABLE " + TABLE_NAME +"("
                +COLUMN_COURSE_ID + ","+
                COLUMN_COURSE_TITLE +")";

    }

    public static class NoteInfoClass{
        public static final String TABLE_NAME = "note_info";
        public static final String COLUMN_COURSE_ID="course_id";
        public static final String COLUMN_NOTE_TITLE="course_title" ;
        public static final String  COLUMN_NOTE_TEXT ="note_Text";


        public static final String SQL_CREATE_TABLE =  "CREATE TABLE " + TABLE_NAME +"("
                +COLUMN_COURSE_ID + ","+
                COLUMN_NOTE_TITLE +")";
    }
}
