package ca.bcit.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class YourProfileFragment extends Fragment implements AdapterView.OnItemSelectedListener{
    String[] schools = {"BCIT", "UBC", "SFU"};
    TextView name;
    TextView email;
    GoogleSignInClient mGoogleSignInClient;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_profile, container, false);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        name = v.findViewById(R.id.greet_text);
        email = v.findViewById((R.id.email_text_view));

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
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







}
