package ca.bcit.studybuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    Button test_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        test_btn = findViewById(R.id.login);

        //This uses the DatabaseQueries class to get a PositionDataPoint object then set it in this activity.
//        setTestField();
    }

    public void moveToGoogleSignIn(View view){
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }

    /**
     * Sets test text field with data queried from DatabaseQueries class.
     */
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