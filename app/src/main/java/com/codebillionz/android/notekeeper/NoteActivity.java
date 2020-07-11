package com.codebillionz.android.notekeeper;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class NoteActivity extends AppCompatActivity {

    public static final String NOTE_POSITION = "com.codebillionz.android.notekeeper.NOTE_POSITION";
    public static final int POSITION_NOT_SET = -1;
    private NoteInfo mNote;
    private boolean mIsNewNote;
    private EditText mTitle;
    private EditText mBody;
    private Spinner mSpinner;
    private int mNotePosition;
    private boolean mIsCancelling;
    private NoteActivityViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewModelProvider viewModelProvider = new ViewModelProvider(getViewModelStore(),ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication()));
        mViewModel = viewModelProvider.get(NoteActivityViewModel.class);

        if(mViewModel.mIsNewlyCreated &&  savedInstanceState !=null){
            //our ViewModel  still exists in the case of a configuration change so we dont need to use the bundle.
            //Only use the bundle when the view model gets destroyed with the activity
            mViewModel.restoreState(savedInstanceState);
        }

        mViewModel.mIsNewlyCreated= false;

        mSpinner = findViewById(R.id.spinner);
        List<CourseInfo> courses = DataManager.getInstance().getCourses();
        ArrayAdapter<CourseInfo> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,courses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        readDisplayStateValues();
        saveOriginalNoteValues(); 

        mTitle = findViewById(R.id.text_title);
        mBody = findViewById(R.id.text_body);


        if(!mIsNewNote)displayNote(mSpinner, mTitle, mBody);



    }

    private void saveOriginalNoteValues() {
        if (mIsNewNote)return;
        mViewModel.mOriginalCourseId = mNote.getCourse().getCourseId();
        mViewModel.mOriginalNoteTitle = mNote.getTitle();
        mViewModel.mOriginalNoteText = mNote.getText();

    }

    private void displayNote(Spinner spinner, TextView title, TextView body){

       List<CourseInfo> courses = DataManager.getInstance().getCourses();
       int courseIndex = courses.indexOf(mNote.getCourse());
       spinner.setSelection(courseIndex);
        title.setText(mNote.getTitle());
        body.setText(mNote.getText());




    }

    private void readDisplayStateValues(){
        Intent intent =  getIntent();
        mNotePosition = intent.getIntExtra(NOTE_POSITION, POSITION_NOT_SET);
        mIsNewNote = mNotePosition==POSITION_NOT_SET;
        if(mIsNewNote) {
           createNewNote();
        }
        else{
            mNote = DataManager.getInstance().getNotes().get(mNotePosition);

        }

    }

    private void createNewNote() {
        DataManager dm = DataManager.getInstance();
        mNotePosition = dm.createNewNote();
        mNote = dm.getNotes().get(mNotePosition);


    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(outState !=null){
            mViewModel.saveState(outState);
        }
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
        item.setEnabled(mNotePosition<lastNoteIndex);
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
        ++mNotePosition;
        mNote = DataManager.getInstance().getNotes().get(mNotePosition);

        saveOriginalNoteValues();
         displayNote(mSpinner,mTitle,mBody);
         invalidateOptionsMenu();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mIsCancelling){
                if(mIsNewNote){
                    DataManager.getInstance().removeNote(mNotePosition);
                } else {
                    storePreviousNoteValues();
                }
        } else{
            saveNote();
        }
    }

    private void storePreviousNoteValues() {
        CourseInfo course = DataManager.getInstance().getCourse(mViewModel.mOriginalCourseId);
        mNote.setCourse(course);
        mNote.setTitle(mViewModel.mOriginalNoteTitle);
        mNote.setText(mViewModel.mOriginalNoteText);
    }

    private void saveNote() {
        mNote.setCourse((CourseInfo) mSpinner.getSelectedItem());
        mNote.setTitle(mTitle.getText().toString());
        mNote.setText(mBody.getText().toString());
    }

    private void sendEmail() {
        CourseInfo  course = (CourseInfo) mSpinner.getSelectedItem();
        String subject = mTitle.getText().toString();
        String  text = "check out this course \" " +course.getTitle() +"\"\n" + mBody.getText();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc2822");
        intent.putExtra(Intent.EXTRA_SUBJECT,subject);
        intent.putExtra(Intent.EXTRA_TEXT , text);

        startActivity(intent);

    }
}