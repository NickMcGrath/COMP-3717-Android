package ca.bcit.studybuddy;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CheckinActivity extends AppCompatActivity {

    TextView library;
    TextView address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);

        library = findViewById(R.id.checkin_location);
        address = findViewById(R.id.checkin_address);

        // hard coded for now; these should be passed from the landing activity
        library.setText("Central Branch");
        address.setText("350 W Georgia St, Vancouver, BC V6B 6B1");
    }
}
