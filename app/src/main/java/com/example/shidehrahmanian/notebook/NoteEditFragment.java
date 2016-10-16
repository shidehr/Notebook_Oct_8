package com.example.shidehrahmanian.notebook;


import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import static android.R.attr.button;
import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoteEditFragment extends Fragment {
    private EditText title, message;
    private AlertDialog confirmDialogObject;
    private boolean newNote = false;
    private long noteId=0;
    private static int RESULT_LOAD_IMG=1;


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
        ImageButton photoButton= (ImageButton) fragmentLayout.findViewById(R.id.editNoteAddPhoto);

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

    public void loadImageFromGallery(View view){
        Intent galleryIntent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode==RESULT_LOAD_IMG && resultCode==RESULT_OK && data!=null){
                Uri selectedImage=data.getData();

                String[]filePathColumn={MediaStore.Images.Media.DATA};

                Cursor cursor=getContentResolver().query(selectedImage, filePathColumn, null, null, null);

                cursor.moveToFirst();

                int columnIndex=cursor.getColumnIndex(filePathColumn[0]);

                imgDecodableString=cursor.getString(columnIndex);
                cursor.close();

                ImageView imgView=(ImageView).findViewById(R.id.imgView);

                imgView.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
            } else {
                Toast toast=Toast.makeText(this, "You haven't picked an image", Toast.LENGTH_LONG);
                toast.show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }
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
  //              dbAdapter.createNote(title.getText() + "", message.getText() + "");
  Log.d("INSIDE new Note =>",newNote+"");
                if (newNote){
                    dbAdapter.createNote(title.getText() + "", message.getText() + "");

                }else {
                    dbAdapter.createNote(title.getText() + "", message.getText() + "");
                 //   dbAdapter.updateNote(noteId, title.getText() + "", message.getText() + "");

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
