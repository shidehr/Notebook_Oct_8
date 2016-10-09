package com.example.shidehrahmanian.notebook;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class NoteViewFragment extends Fragment {


    public NoteViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentLayout=inflater.inflate(R.layout.fragment_note_view, container, false);
        TextView title=(TextView) fragmentLayout.findViewById(R.id.viewNoteTitle);
        TextView message=(TextView) fragmentLayout.findViewById(R.id.viewNoteMessage);

        Intent intent=getActivity().getIntent();
        title.setText(intent.getExtras().getString(MainActivity.NOTE_TITLE_EXTRA));
        message.setText(intent.getExtras().getString(MainActivity.NOTE_MESSAGE_EXTRA));

        // Inflate the layout for this fragment
        return fragmentLayout;
    }

}
