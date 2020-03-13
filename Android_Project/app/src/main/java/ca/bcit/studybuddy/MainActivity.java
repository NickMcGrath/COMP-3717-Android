package ca.bcit.studybuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    String TAG = "MainDebug";
    GoogleSignInAccount acct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        GoogleSignIn.getClient(getBaseContext(), gso);
//        DatabaseQueries.addUser();
//        DatabaseQueries.realTimeCurrentUser("1234");
//        DatabaseQueries.viewUser("1234");
//        DatabaseQueries.realTimeCurrentUser("1234");
//        DatabaseQueries.sendRequest("1234", "1235");
//        DatabaseQueries.acceptRequest("1235", "1234");
//        DatabaseQueries.checkIn("1234", "4Q7nTqq0kAkEDEkWkHPX");
//        DatabaseQueries.checkOut("1234", "4Q7nTqq0kAkEDEkWkHPX");
    }









    /**
     * This method is a little bit jank and should not be here LOL
     * It is here because getBaseContext() needs to be passed in from an activity.
     * For initially setting up all the libraries and schools found in the json files under app/assets
     */
//    private void initFirebaseWithPositions() {
//        PositionDataPoint[] schools = DatabaseQueries.getSchoolsJSON(getBaseContext());
//        PositionDataPoint[] libraries = DatabaseQueries.getLibrariesJSON(getBaseContext());
//        for (PositionDataPoint school : schools) {
//            DatabaseQueries.addToFireStoreCollection("locations", DatabaseQueries.DataPointToMap(school));
//        }
//        for (PositionDataPoint library : libraries) {
//            DatabaseQueries.addToFireStoreCollection("locations", DatabaseQueries.DataPointToMap(library));
//        }
//    }
}
