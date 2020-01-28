package ca.bcit.studybuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //This method is used for testing queries.
        vancouverAPI();
    }

    /**
     * Currently a test method!
     *
     * This method creates a query to Vancouver Data API.
     */
    public void vancouverAPI() {
        // Instantiate the RequestQueue.
        final TextView textView = (TextView) findViewById(R.id.TestText);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://opendata.vancouver.ca/api/records/1.0/search//?dataset=libraries&lang=en&rows=20&geofilter.distance=49.2899%2C+-123%2C+1000000";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        textView.setText("Response is: " + response.substring(0, 500));
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
