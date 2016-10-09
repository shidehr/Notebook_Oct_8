package com.example.shidehrahmanian.notebook;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import static android.R.attr.button;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoteEditFragment extends Fragment {
    private EditText title, message;
    private AlertDialog confirmDialogObject;
    private boolean newNote = false;
    private long noteId=0;




    public NoteEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentLayout=inflater.inflate(R.layout.fragment_note_edit, container, false);

        title= (EditText) fragmentLayout.findViewById(R.id.editNoteTitle);
        message=(EditText) fragmentLayout.findViewById(R.id.editNoteMessage);
        Button savedButton= (Button) fragmentLayout.findViewById(R.id.saveNote);

        Intent intent=getActivity().getIntent();
        title.setText(intent.getExtras().getString(MainActivity.NOTE_TITLE_EXTRA, ""));
        message.setText(intent.getExtras().getString(MainActivity.NOTE_MESSAGE_EXTRA, ""));
        noteId=intent.getExtras().getLong(MainActivity.NOTE_ID_EXTRA, 0);

        buildConfirmDialog();

        savedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialogObject.show();
            }
        });



      return fragmentLayout;

    }

    private void buildConfirmDialog (){
        AlertDialog.Builder confirmBuilder=new AlertDialog.Builder(getActivity());
        confirmBuilder.setTitle("Save changes?");
        confirmBuilder.setMessage("Please confirm you want to keep these changes.");

        confirmBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Log.d("Save Note", "Note title:" + title.getText() + "Note message:" + message.getText());

               NotebookDbAdapter dbAdapter=new NotebookDbAdapter(getActivity().getBaseContext());
                dbAdapter.open();

                if (newNote){
                    dbAdapter.createNote(title.getText() + "", message.getText() + "");

                }else {
                    dbAdapter.updateNote(noteId, title.getText() + "", message.getText() + "");

                }

                dbAdapter.close();

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

            }
        });

        confirmBuilder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        confirmDialogObject=confirmBuilder.create();
    }

}
