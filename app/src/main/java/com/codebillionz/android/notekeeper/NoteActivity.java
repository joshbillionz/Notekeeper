package com.codebillionz.android.notekeeper;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
//import androidx.lifecycle.ViewModelProvider;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.codebillionz.android.notekeeper.NoteKeeperDatabaseContract.NoteInfoEntry;

import java.util.List;

public class NoteActivity extends AppCompatActivity {

    public static final String NOTE_ID = "com.codebillionz.android.notekeeper.NOTE_POSITION";
    public static final String ORIGINAL_NOTE_COURSE_ID = "com.codebillionz.android.notekeeper.ORIGINAL_NOTE_COURSE_ID";
    public static final String ORIGINAL_NOTE_TITLE = "ccom.codebillionz.android.notekeeper.ORIGINAL_NOTE_TITLE";
    public static final String ORIGINAL_NOTE_TEXT = "com.codebillionz.android.notekeeper.ORIGINAL_NOTE_TEXT";

    public static final int ID_NOT_SET = -1;
    private NoteInfo mNote = new NoteInfo(DataManager.getInstance().getCourses().get(0), "", "");

    private boolean mIsNewNote;
    private EditText mTextNoteTitle;
    private EditText mTextNoteText;
    private Spinner mSpinnerCourses;
    private int mNoteId;
    private boolean mIsCancelling;

    private String mOriginalNoteCourseId;
    private String mOriginalNoteTitle;
    private String mOriginalNoteText;
   // private NoteActivityViewModel mViewModel;
    private NoteKeeperOpenHelper mDbOpenHelper;
    private Cursor mNoteCursor;
    private int mCourseIdPos;
    private int mNoteTitlePos;
    private int mNoteTextPos;


    @Override
    protected void onDestroy() {
        mDbOpenHelper.close();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDbOpenHelper =new  NoteKeeperOpenHelper(this);
//        ViewModelProvider viewModelProvider = new ViewModelProvider(getViewModelStore(),ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()));
     //   mViewModel = viewModelProvider.get(NoteActivityViewModel.class);

//        if(mViewModel.mIsNewlyCreated &&  savedInstanceState !=null){
//            //our ViewModel  still exists in the case of a configuration change so we dont need to use the bundle.
//            //Only use the bundle when the view model gets destroyed with the activity
//            mViewModel.restoreState(savedInstanceState);
//        }
//
//        mViewModel.mIsNewlyCreated= false;

        mSpinnerCourses = findViewById(R.id.spinner);
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        ArrayAdapter<CourseInfo> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,courses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCourses.setAdapter(adapter);

        readDisplayStateValues();
        if(savedInstanceState == null) {
            saveOriginalNoteValues();
        } else {
            restoreOriginalNoteValues(savedInstanceState);
        }

        mTextNoteTitle = findViewById(R.id.text_title);
        mTextNoteText = findViewById(R.id.text_body);

        if(!mIsNewNote){
            loadNoteData();
        }

    }

    private void loadNoteData() {
        SQLiteDatabase db = mDbOpenHelper.getReadableDatabase();

//        String courseId = "android_intents";
//        String titleStart = "dynamic";

        String selection = NoteInfoEntry._ID + " = ?";

        String[] selectionArgs = {Integer.toString(mNoteId)};

        String[] noteColumns = {
                NoteInfoEntry.COLUMN_COURSE_ID,
                NoteInfoEntry.COLUMN_NOTE_TITLE,
                NoteInfoEntry.COLUMN_NOTE_TEXT
        };
        mNoteCursor = db.query(NoteInfoEntry.TABLE_NAME, noteColumns,
                selection, selectionArgs, null, null, null);
        mCourseIdPos = mNoteCursor.getColumnIndex(NoteInfoEntry.COLUMN_COURSE_ID);
        mNoteTitlePos = mNoteCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TITLE);
        mNoteTextPos = mNoteCursor.getColumnIndex(NoteInfoEntry.COLUMN_NOTE_TEXT);
        mNoteCursor.moveToNext();
        displayNote();
    }

    private void saveOriginalNoteValues() {
        if (mIsNewNote)return;
//        mViewModel.mOriginalCourseId
        mOriginalNoteCourseId = mNote.getCourse().getCourseId();
//        mViewModel.
       mOriginalNoteTitle = mNote.getTitle();
//        mViewModel.
        mOriginalNoteText = mNote.getText();

    }

    private void restoreOriginalNoteValues(Bundle savedInstanceState) {
        mOriginalNoteCourseId = savedInstanceState.getString(ORIGINAL_NOTE_COURSE_ID);
        mOriginalNoteTitle = savedInstanceState.getString(ORIGINAL_NOTE_TITLE);
        mOriginalNoteText = savedInstanceState.getString(ORIGINAL_NOTE_TEXT);
    }




    private void displayNote(){

        String courseId = mNoteCursor.getString(mCourseIdPos);
        String noteTitle = mNoteCursor.getString(mNoteTitlePos);
        String noteText = mNoteCursor.getString(mNoteTextPos);
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        CourseInfo course = DataManager.getInstance().getCourse(courseId);
        int courseIndex = courses.indexOf(course);
        mSpinnerCourses.setSelection(courseIndex);
        mTextNoteTitle.setText(noteTitle);
        mTextNoteText.setText(noteText);



    }

    private void readDisplayStateValues(){
        Intent intent =  getIntent();
        mNoteId = intent.getIntExtra(NOTE_ID, ID_NOT_SET);
        mIsNewNote = mNoteId == ID_NOT_SET;
        if(mIsNewNote) {
           createNewNote();
        }
        // mNote = DataManager.getInstance().getNotes().get(mNoteId);


    }

    private void createNewNote() {
        DataManager dm = DataManager.getInstance();
        mNoteId = dm.createNewNote();
        mNote = dm.getNotes().get(mNoteId);


    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        if(outState !=null){
//            mViewModel.saveState(outState);
//        }
        super.onSaveInstanceState(outState);
        outState.putString(ORIGINAL_NOTE_COURSE_ID, mOriginalNoteCourseId);
        outState.putString(ORIGINAL_NOTE_TITLE, mOriginalNoteTitle);
        outState.putString(ORIGINAL_NOTE_TEXT, mOriginalNoteText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_next);
        int lastNoteIndex = DataManager.getInstance().getNotes().size()-1;
        item.setEnabled(mNoteId <lastNoteIndex);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_email) {

            sendEmail();
            return true;
        }else if(id==R.id.action_cancel){
            mIsCancelling = true;
            finish();
        } else if(id==R.id.action_next){
            moveNext();
        }
        return super.onOptionsItemSelected(item);
    }

    private void moveNext() {
        saveNote();
        ++mNoteId;
        mNote = DataManager.getInstance().getNotes().get(mNoteId);

        saveOriginalNoteValues();
         displayNote();
         invalidateOptionsMenu();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mIsCancelling){
                if(mIsNewNote){
                    DataManager.getInstance().removeNote(mNoteId);
                } else {
                    storePreviousNoteValues();
                }
        } else{
            saveNote();
        }
    }

    private void storePreviousNoteValues() {
//        CourseInfo course = DataManager.getInstance().getCourse(mViewModel.mOriginalCourseId);
        CourseInfo course = DataManager.getInstance().getCourse(mOriginalNoteCourseId);

        mNote.setCourse(course);
//        mNote.setTitle(mViewModel.mOriginalNoteTitle);
//        mNote.setText(mViewModel.mOriginalNoteText);

        mNote.setTitle(mOriginalNoteTitle);
        mNote.setText(mOriginalNoteText);
    }

    private void saveNote() {
        mNote.setCourse((CourseInfo) mSpinnerCourses.getSelectedItem());
        mNote.setTitle(mTextNoteTitle.getText().toString());
        mNote.setText(mTextNoteText.getText().toString());
    }

    private void sendEmail() {
        CourseInfo  course = (CourseInfo) mSpinnerCourses.getSelectedItem();
        String subject = mTextNoteTitle.getText().toString();
        String  text = "check out this course \" " +course.getTitle() +"\"\n" + mTextNoteText.getText();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");
        intent.putExtra(Intent.EXTRA_SUBJECT,subject);
        intent.putExtra(Intent.EXTRA_TEXT , text);

        startActivity(intent);

    }
}