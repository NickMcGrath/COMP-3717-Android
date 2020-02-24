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

        //This method is used for testing queries.
        vancouverAPI(49.2899, -123.0, 100000, 7);

    }

    public void moveToGoogleSignIn(View view){
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }

    /**
     * Creates a query to Vancouver Data API, Response is sent to a helper method.
     *
     * @param xCoord the x coordinate
     * @param yCoord the y coordinate
     * @param radius radius in meters
     * @param rows   the amount of entities to return
     */
    private void vancouverAPI(double xCoord, double yCoord, int radius, int rows) {
        // Instantiate the RequestQueue.
        final TextView textView = (TextView) findViewById(R.id.TestText);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://opendata.vancouver.ca/api/records/1.0/search//?dataset=libraries&lang=en&rows=" + rows + "&geofilter.distance=" + xCoord + "%2C+" + yCoord + "%2C+" + radius;
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            setTextFromVancouverAPI(new JSONObject(response));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
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
