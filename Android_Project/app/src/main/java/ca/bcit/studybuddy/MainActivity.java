package ca.bcit.studybuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //This uses the DatabaseQueries class to get a PositionDataPoint object then set it in this activity.
        setTestField();
    }


    /**
     * Sets test text field with data queried from DatabaseQueries class.
     */
    private void setTestField() {
        final TextView textView = findViewById(R.id.TestText);
        String basicTextTest = "";

        PositionDataPoint[] positionDataPoints = DatabaseQueries.getLibraries(getBaseContext());
        for (PositionDataPoint point : positionDataPoints) {
            basicTextTest += point.type;
            basicTextTest += point.name;
            basicTextTest += point.address;
            basicTextTest += point.x;
            basicTextTest += point.y;
            basicTextTest += '\n';

        }
        positionDataPoints = DatabaseQueries.getSchools(getBaseContext());
        for (PositionDataPoint point : positionDataPoints) {
            basicTextTest += point.type;
            basicTextTest += point.name;
            basicTextTest += point.address;
            basicTextTest += point.x;
            basicTextTest += point.y;
            basicTextTest += '\n';

        }
        textView.setText(basicTextTest);
    }
}
