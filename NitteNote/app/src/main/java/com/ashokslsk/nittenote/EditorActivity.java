package com.ashokslsk.nittenote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;

import com.ashokslsk.nittenote.data.NoteItem;

public class EditorActivity extends AppCompatActivity {

    private NoteItem note;
    EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);


        Intent intent = this.getIntent();
        note = new NoteItem();
        note.setKey(intent.getStringExtra("key"));
        note.setText(intent.getStringExtra("text"));
        et = (EditText)findViewById(R.id.AshokNoteText);
        et.setText(note.getText());
        et.setSelection(note.getText().length());
    }


    private void saveAndFinish(){
        String NoteText = et.getText().toString();
        if(NoteText.isEmpty()){
            finish();
        }else{
            Intent intent = new Intent();
            intent.putExtra("key", note.getKey());
            intent.putExtra("text", NoteText);
            setResult(RESULT_OK,intent);
            finish();
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            saveAndFinish();
        }return false;
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        saveAndFinish();
    }
}
