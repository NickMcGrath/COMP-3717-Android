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
import com.firebase.ui.auth.AuthUI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    // Choose authentication providers
    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.EmailBuilder().build(),
            new AuthUI.IdpConfig.PhoneBuilder().build(),
            new AuthUI.IdpConfig.GoogleBuilder().build(),
            new AuthUI.IdpConfig.FacebookBuilder().build(),
            new AuthUI.IdpConfig.TwitterBuilder().build());

    // Create and launch sign-in intent
    startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
    RC_SIGN_IN);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //This method is used for testing queries.
        vancouverAPI(49.2899, -123.0, 100000, 7);

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
    }

    /**
     * Helper method takes a JSONObject and sets TextFields.
     *
     * @param apiResponse JSON from vancouver data api
     * @throws JSONException
     */
    private void setTextFromVancouverAPI(JSONObject apiResponse) throws JSONException {
        final TextView textView = findViewById(R.id.TestText);

        JSONArray records = apiResponse.getJSONArray("records");

        String basicTextTest = "";
        for (int i = 0; i < records.length(); i++) {
            JSONObject record = records.getJSONObject(i);
            basicTextTest += "Name of Library:";
            basicTextTest += record.getJSONObject("fields").getString("name");
            basicTextTest += "\n\t\tDistance (meters):";
            basicTextTest += record.getJSONObject("fields").getString("dist");
            basicTextTest += "\n";
        }
        textView.setText(basicTextTest);
    }
}
