package softwarestudio.course.finalproject.flappyfriends;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import softwarestudio.course.finalproject.flappyfriends.Receiver.ReceiveDataStorage;
import softwarestudio.course.finalproject.flappyfriends.Wifidirect.WiFiDirectActivity;


public class TitleFragment extends Fragment {

    public TitleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_title, container, false);

        Button gsbutton = (Button) rootView.findViewById(R.id.button_gamestart);
        gsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReceiveDataStorage.PLAYER_LABEL = 0;
                ReceiveDataStorage.PLAYER_NUM = 1;
                ReceiveDataStorage.IS_CONNECTED = false;
                Intent intent = new Intent(getActivity(), GameActivity.class);
                startActivity(intent);
            }
        });

        Button mpbutton = (Button) rootView.findViewById(R.id.button_multiplayer);
        mpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WiFiDirectActivity.class);
                startActivity(intent);
            }
        });

        Button quitbutton = (Button) rootView.findViewById(R.id.button_quit);
        quitbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        return rootView;
    }
}
