package ca.bcit.studybuddy;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LandingActivity extends AppCompatActivity {

    String[] librariesNearby = {"nearest", "second-nearest", "third-nearest"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, librariesNearby);

        ListView listView = findViewById(R.id.library_list);
        listView.setAdapter(adapter);
    }
}
