package ca.bcit.studybuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class CheckinActivity extends AppCompatActivity {

    TextView library;
    TextView address;
    Button btnCheckIn;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInAccount acct;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String locationPk;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);

        library = findViewById(R.id.checkin_location);
        address = findViewById(R.id.checkin_address);
        btnCheckIn = findViewById(R.id.check_in);
        btnCheckIn.setOnClickListener(btnListener);

        bundle = getIntent().getExtras();

        String locationName = bundle.getString("locationName");
        String locationAddress = bundle.getString("locationAddress");
        locationPk = bundle.getString("locationPk");

        library.setText(locationName);
        address.setText(locationAddress);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        acct = GoogleSignIn.getLastSignedInAccount(this);


    }

    private View.OnClickListener btnListener = new View.OnClickListener() {
        public void onClick(View v) {
//            Map<String, Object> data = new HashMap<>();
//            data.put("location", locationPk);
//            db.collection("students").document(acct.getId()).set(data, SetOptions.merge());
            checkIn(locationPk);

            Intent myIntent = new Intent(v.getContext(), AfterCheckinginActivity.class);
            myIntent.putExtras(bundle);
            startActivity(myIntent);
        }
    };
    public void checkIn(String locationID) {
        db.collection("locations").document(locationID)
                .update("students", FieldValue.arrayUnion(acct.getId()));
        db.collection("students").document(acct.getId())
                .update("location", locationID);
    }


}
