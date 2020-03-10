package ca.bcit.studybuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("nictest", "In MainActivity");
        setContentView(R.layout.activity_main);
        DatabaseQueries.initFireStore();
        Log.d("DatabaseQueries", DatabaseQueries.getFireStoreDataPoints().toString());
    }

    /**
     * This method is a little bit jank and should not be here LOL
     * It is here because getBaseContext() needs to be passed in from an activity.
     * For initially setting up all the libraries and schools found in the json files under app/assets
     */
    private void initFirebaseWithPositions() {
        PositionDataPoint[] schools = DatabaseQueries.getSchoolsJSON(getBaseContext());
        PositionDataPoint[] libraries = DatabaseQueries.getLibrariesJSON(getBaseContext());
        for (PositionDataPoint school : schools) {
            DatabaseQueries.addToFireStoreCollection("locations", DatabaseQueries.DataPointToMap(school));
        }
        for (PositionDataPoint library : libraries) {
            DatabaseQueries.addToFireStoreCollection("locations", DatabaseQueries.DataPointToMap(library));
        }
    }
//    /**
//     * Sets test text field with data queried from DatabaseQueries class.
//     */
//    private void setTestField() {
//        final TextView textView = findViewById(R.id.TestText);
//        String basicTextTest = "";
//
//        PositionDataPoint[] positionDataPoints = DatabaseQueries.getLibraries(getBaseContext());
//        for (PositionDataPoint point : positionDataPoints) {
//            basicTextTest += point.type;
//            basicTextTest += point.name;
//            basicTextTest += point.address;
//            basicTextTest += point.x;
//            basicTextTest += point.y;
//            basicTextTest += '\n';
//
//        }
//        positionDataPoints = DatabaseQueries.getSchools(getBaseContext());
//        for (PositionDataPoint point : positionDataPoints) {
//            basicTextTest += point.type;
//            basicTextTest += point.name;
//            basicTextTest += point.address;
//            basicTextTest += point.x;
//            basicTextTest += point.y;
//            basicTextTest += '\n';
//
//        }
//        textView.setText(basicTextTest);
//    }
}
