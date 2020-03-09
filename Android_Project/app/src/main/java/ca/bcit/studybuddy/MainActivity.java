package ca.bcit.studybuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.core.Amplify;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AWSMobileClient.getInstance().initialize(getApplicationContext(), new Callback<UserStateDetails>() {
            @Override
            public void onResult(UserStateDetails userStateDetails) {
                try {
                    Amplify.addPlugin(new AWSApiPlugin());
                    Amplify.configure(getApplicationContext());
                    Log.i("ApiQuickstart", "All set and ready to go!");
                } catch (Exception e) {
                    Log.e("ApiQuickstart", e.getMessage());
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("ApiQuickstart", "Initialization error.", e);
            }
        });
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
