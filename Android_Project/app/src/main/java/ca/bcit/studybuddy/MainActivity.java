package ca.bcit.studybuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("nictest", "In MainActivity");
        setContentView(R.layout.activity_main);
//        DatabaseQueries.addUser();
//        DatabaseQueries.realTimeRequestsListener("yQZkN8cVmfYopmn6XkUA");
//        DatabaseQueries.viewCurrentUser("1234");
//        DatabaseQueries.realTimeCurrentUser("1234");
//        DatabaseQueries.sendRequest("1234", "1235");
//        DatabaseQueries.acceptRequest("1235", "1234");
//        DatabaseQueries.checkIn("1234", "4Q7nTqq0kAkEDEkWkHPX");
        DatabaseQueries.checkOut("1234", "4Q7nTqq0kAkEDEkWkHPX");
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
