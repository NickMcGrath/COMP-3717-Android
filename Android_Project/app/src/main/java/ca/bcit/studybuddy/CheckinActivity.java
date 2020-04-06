package ca.bcit.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class CheckinActivity extends Fragment {

    TextView library;
    TextView address;
    Button btnCheckIn;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String locationPk;
    Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_checkin);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_checkin, container, false);
        library = view.findViewById(R.id.checkin_location);
        address = view.findViewById(R.id.checkin_address);
        btnCheckIn = view.findViewById(R.id.check_in);
        btnCheckIn.setOnClickListener(btnListener);
        if (getArguments() != null) {
            bundle = getArguments();
        }

        String locationName = bundle.getString("locationName");
        String locationAddress = bundle.getString("locationAddress");
        locationPk = bundle.getString("locationPk");

        library.setText(locationName);
        address.setText(locationAddress);

        return view;
    }

    private View.OnClickListener btnListener = new View.OnClickListener() {
        public void onClick(View v) {
            ((LandingActivity) getActivity()).checkIn(locationPk);
            AfterCheckinginActivity afterCheckinginActivity = new AfterCheckinginActivity();
            afterCheckinginActivity.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, afterCheckinginActivity).commit();

        }
    };


}
