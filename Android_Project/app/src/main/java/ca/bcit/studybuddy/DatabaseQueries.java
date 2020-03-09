//package ca.bcit.studybuddy;
//
//import android.content.Context;
//import android.util.Log;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.io.InputStream;
//
///**
// * PositionDataPoint models a point on a map.
// *
// * @author nickmcgrath
// */
//class PositionDataPoint {
//    public String name;
//    public String address;
//    public String type;
//    public double x;
//    public double y;
//
//    public PositionDataPoint(String name, String address, String type, double x, double y) {
//        this.name = name;
//        this.address = address;
//        this.type = type;
//        this.x = x;
//        this.y = y;
//
//    }
//}
//
///**
// * DatabaseQueries is for requesting different types of data relating to the app.
// *
// * @author nickmcgrath
// */
//public class DatabaseQueries {
//    /**
//     * Helper method to get JSON objects from context assets.
//     *
//     * @param context
//     * @param fileName
//     * @return
//     */
//    private static JSONObject getJSON(Context context, String fileName) {
//        try {
//            InputStream is = context.getAssets().open(fileName);
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//            String json = new String(buffer, "UTF-8");
//            return new JSONObject(json);
//
//        } catch (Exception e) {
//            Log.e("Querys.getJSON", e.getMessage());
//        }
//        return null;
//    }
//
//    /**
//     * Gets the libraries and sets them in an array of PositionDataPoints.
//     */
//    public static PositionDataPoint[] getLibraries(Context context) {
//        try {
//            JSONObject schoolsJSON = getJSON(context, "libraries.json");
//            JSONArray records = schoolsJSON.getJSONArray("records");
//            PositionDataPoint[] positionDataPoints = new PositionDataPoint[records.length()];
//            for (int i = 0; i < records.length(); i++) {
//                JSONObject record = records.getJSONObject(i);
//                String name = record.getJSONObject("fields").getString("name");
//                String address = record.getJSONObject("fields").getString("address");
//                double x = record.getJSONObject("fields").getJSONObject("geom").getJSONArray("coordinates").getDouble(0);
//                double y = record.getJSONObject("fields").getJSONObject("geom").getJSONArray("coordinates").getDouble(1);
//                positionDataPoints[i] = new PositionDataPoint(name, address, "Library", x, y);
//            }
//            return positionDataPoints;
//        } catch (Exception e) {
//            Log.e("Queries.getLibraries", e.getMessage());
//        }
//        return null;
//    }
//    /**
//     * Gets the schools and sets them in an array of PositionDataPoints.
//     */
//    public static PositionDataPoint[] getSchools(Context context) {
//        try {
//            JSONObject schoolsJSON = getJSON(context, "schools.json");
//            JSONArray records = schoolsJSON.getJSONArray("records");
//            PositionDataPoint[] positionDataPoints = new PositionDataPoint[records.length()];
//            for (int i = 0; i < records.length(); i++) {
//                JSONObject record = records.getJSONObject(i);
//                String name = record.getString("name");
//                String address = record.getString("address");
//                double x = record.getJSONArray("coordinates").getDouble(0);
//                double y = record.getJSONArray("coordinates").getDouble(1);
//                positionDataPoints[i] = new PositionDataPoint(name, address, "School", x, y);
//            }
//            return positionDataPoints;
//        } catch (Exception e) {
//            Log.e("Queries.getLibraries", e.getMessage());
//        }
//        return null;
//    }
//}
//
///**
// * DEPRECIATED, This can be used in an activity to query the Vancouver Data API
// * but it makes more sense to have a local JSON file containing all of the library points.
// * <p>
// * Creates a query to Vancouver Data API, Response is sent to a helper method.
// *
// * @param xCoord the x coordinate
// * @param yCoord the y coordinate
// * @param radius radius in meters
// * @param rows   the amount of entities to return
// * @deprecated
// */
////    private void vancouverAPI(double xCoord, double yCoord, int radius, int rows) {
////        // Instantiate the RequestQueue.
////        final TextView textView = (TextView) findViewById(R.id.TestText);
////        RequestQueue queue = Volley.newRequestQueue(this); // this has to be in an activity class
////        String url = "https://opendata.vancouver.ca/api/records/1.0/search//?dataset=libraries&lang=en&rows=" + rows + "&geofilter.distance=" + xCoord + "%2C+" + yCoord + "%2C+" + radius;
////        // Request a string response from the provided URL.
////        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
////                new Response.Listener<String>() {
////                    @Override
////                    public void onResponse(String response) {
////                        try {
////                            setTextFromVancouverAPI(new JSONObject(response));
////                        } catch (JSONException e) {
////                            e.printStackTrace();
////                        }
////                    }
////                }, new Response.ErrorListener() {
////            @Override
////            public void onErrorResponse(VolleyError error) {
////                textView.setText("That didn't work!");
////            }
////        });
////        // Add the request to the RequestQueue.
////        queue.add(stringRequest);
////    }
//
