package com.ashokslsk.nittenote;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ashokslsk.nittenote.data.NoteItem;
import com.ashokslsk.nittenote.data.NotesDataSource;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int EDITOR_ACTIVITY_REQUEST = 1001;
    private static final int MENU_DELETE_ID = 1002;
    private int currentNoteId;
    private NotesDataSource datasource;
    List<NoteItem> notesList;
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview = (ListView) findViewById(R.id.mylist);
        datasource = new NotesDataSource(this);
        refreshDisplay();
    }

    private void refreshDisplay() {
        // TODO Auto-generated method stub
        notesList = datasource.findAll();
        ArrayAdapter<NoteItem> adapter = new ArrayAdapter<NoteItem>(this, R.layout.list_item, notesList);
        listview.setAdapter(adapter);
        registerForContextMenu(listview);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                NoteItem note = notesList.get(position);
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                intent.putExtra("key", note.getKey());
                intent.putExtra("text", note.getText());
                startActivityForResult(intent, EDITOR_ACTIVITY_REQUEST);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_create) {

            createNote();
        }

        return super.onOptionsItemSelected(item);
    }


    private void createNote() {
        NoteItem note = NoteItem.getNew();
        Intent intent = new Intent(this, EditorActivity.class);
        intent.putExtra("key", note.getKey());
        intent.putExtra("text", note.getText());
        startActivityForResult(intent, EDITOR_ACTIVITY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITOR_ACTIVITY_REQUEST && resultCode == RESULT_OK) {
            NoteItem note = new NoteItem();
            note.setKey(data.getStringExtra("key"));
            note.setText(data.getStringExtra("text"));
            datasource.update(note);
            refreshDisplay();
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        currentNoteId = (int) info.id;
        menu.add(0, MENU_DELETE_ID, 0, "Delete !!");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == MENU_DELETE_ID) {
            NoteItem note = notesList.get(currentNoteId);
            datasource.remove(note);
            refreshDisplay();
        }
        return super.onContextItemSelected(item);
    }
}
