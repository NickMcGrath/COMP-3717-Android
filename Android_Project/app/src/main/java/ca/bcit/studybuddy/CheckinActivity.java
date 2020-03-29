package ca.bcit.studybuddy;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class CheckinActivity extends AppCompatActivity {

    TextView library;
    TextView address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);

        library = findViewById(R.id.checkin_location);
        address = findViewById(R.id.checkin_address);

        Bundle bundle = getIntent().getExtras();

        String locationName = bundle.getString("locationName");
        String locationAddress = bundle.getString("locationAddress");

        // hard coded for now; these should be passed from the landing activity
        library.setText(locationName);
        address.setText(locationAddress);



    }
}
