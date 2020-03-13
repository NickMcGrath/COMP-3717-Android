package ca.bcit.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class YourProfileFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    String TAG = "Your Profile Frag";
    String[] schools = {"BCIT", "UBC", "SFU"};
    TextView name;
    TextView email;
    EditText phone;
    EditText major;
    Spinner school;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount acct;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_your_profile, container, false);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        name = v.findViewById(R.id.greet_text);
        email = v.findViewById((R.id.email_text_view));
        phone = v.findViewById(R.id.phone_edit_text);
        major = v.findViewById(R.id.major_edit_text);
        school = v.findViewById(R.id.school_spinner);
        Button onSubmit = v.findViewById(R.id.profile_update_btn);
        onSubmit.setOnClickListener(btnListener);
        Log.d(TAG, "What are they selling?" + major.getText().toString());

        acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (acct != null) {
            String personName = "Hey " + acct.getGivenName() + "!";
            String personEmail = acct.getEmail();

            name.setText(personName);
            email.setText(personEmail);
        }


        Spinner school_spinner = (Spinner) v.findViewById(R.id.school_spinner);

        school_spinner.setOnItemSelectedListener(this);
        ArrayAdapter adapter = new ArrayAdapter(this.getActivity(), android.R.layout.simple_spinner_item, schools);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        school_spinner.setAdapter(adapter);


        return v;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {

    }

    public void onNothingSelected(AdapterView<?> arg0) {

    }

    /**
     * OnClickListener for update profile button.
     * Verifies and uploads user information.
     */
    private View.OnClickListener btnListener = new View.OnClickListener() {

        public void onClick(View v) {
            //todo add input verification**
            Log.d(TAG, "What are they selling?" + major.getText().toString());

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> data = new HashMap<>();
            data.put("name", acct.getGivenName());
            data.put("major", major.getText().toString());
            data.put("school", school.getSelectedItem().toString());
            data.put("phone", phone.getText().toString());
            data.put("requests", new ArrayList<String>());
            data.put("sentRequests", new ArrayList<String>());
            data.put("friends", new ArrayList<String>());
            data.put("location", "");
            String googID = acct.getId();

            db.collection("students").document(googID)
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                        }
                    });

        }
    };


}
