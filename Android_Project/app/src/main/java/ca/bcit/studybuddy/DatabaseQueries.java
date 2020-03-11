package ca.bcit.studybuddy;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DatabaseQueries is for requesting different types of data relating to the app.
 *
 * @author nickmcgrath
 */
public class DatabaseQueries {
    //    static FirebaseFirestore db;
    //    static HashMap<String, PositionDataPoint> positionDataPoints;
    static String TAG = "DatabaseQueries";


    public static void viewLocations() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("locations").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                //data values:
                                // document.getId(), (String) data.get("name"), (String) data.get("address"), (String) data.get("type"), (double) data.get("x"), (double) data.get("y"), (ArrayList) data.get("students")

                                if (data.containsKey("students")) {
                                    Log.d(TAG, document.getId() + " => " + "has: " + ((ArrayList) data.get("students")).size() + " students");
                                } else {
                                    Log.d(TAG, document.getId() + " => " + "has: " + 0 + " students");
                                }
                                //here is where you would use data.get(key) to set location info
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public static void addUser() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put("name", "Jerry");
        data.put("major", "Computer Science");
        data.put("school", "BCIT");
        data.put("phone", "6045551234");
        data.put("requests", new ArrayList<String>());
        data.put("sentRequests", new ArrayList<String>());
        data.put("friends", new ArrayList<String>());
        data.put("location", "");
        String googID = "1235";

        db.collection("students").document(googID)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    public static void viewUser(String googID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("students").document(googID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public static void realTimeCurrentUser(String googID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("students").document(googID)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            Log.d(TAG, "Current data: " + snapshot.getData());
                        } else {
                            Log.d(TAG, "Current data: null");
                        }
                    }
                });
    }
    public static void sendRequest(String senderGoogID, String receiverGoogID){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("students").document(senderGoogID)
                .update("sentRequests", FieldValue.arrayUnion(receiverGoogID));
        db.collection("students").document(receiverGoogID)
                .update("requests", FieldValue.arrayUnion(senderGoogID));

    }
    public static void acceptRequest(String accepterGoogID, String accepteGoogID){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //remove request
        db.collection("students").document(accepterGoogID)
                .update("requests", FieldValue.arrayRemove(accepteGoogID));
        //add friend
        db.collection("students").document(accepterGoogID)
                .update("friends", FieldValue.arrayUnion(accepteGoogID));
        //remove request
        db.collection("students").document(accepteGoogID)
                .update("sentRequests", FieldValue.arrayRemove(accepterGoogID));
        //add friend
        db.collection("students").document(accepteGoogID)
                .update("friends", FieldValue.arrayUnion(accepterGoogID));

    }
    public static void checkIn(String googID, String locationID){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("locations").document(locationID)
                .update("students", FieldValue.arrayUnion(googID));
        db.collection("students").document(googID)
                .update("location", locationID);
    }
    public static void checkOut(String googID, String locationID){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("locations").document(locationID)
                .update("students", FieldValue.arrayRemove(googID));
        db.collection("students").document(googID)
                .update("location", "");
    }

}


/**
 * Old attempts down below (dont use)
 * ----------------------------------------------------------------------------------------------------------------------------------
 */
///**
// * PositionDataPoint models a point on a map.
// *
// * @author nickmcgrath
// */
//class PositionDataPoint {
//    public String id;
//    public String name;
//    public String address;
//    public String type;
//    public double x;
//    public double y;
//    public ArrayList<String> students;
//
//    public PositionDataPoint(String id, String name, String address, String type, double x, double y) {
//        this.id = id;
//        this.name = name;
//        this.address = address;
//        this.type = type;
//        this.x = x;
//        this.y = y;
//
//    }
//
//    public PositionDataPoint(String id, String name, String address, String type, double x, double y, ArrayList<String> students) {
//        this.id = id;
//        this.name = name;
//        this.address = address;
//        this.type = type;
//        this.x = x;
//        this.y = y;
//        this.students = students;
//
//    }
//}


//    public static void initFireStore() {
//        // Access a Cloud Firestore instance from your Activity
//        db = FirebaseFirestore.getInstance();
//    }
//
//    /**
//     * Because of firebases async nature it is actually better to have specific queries in
//     * activities and update ui elements there. this was an attempt to keep everything in this file.
//     * <p>
//     * The fallbacks to doing it this way is things can get very complex because you need to do a
//     * lot of jank procedures to make it work LOL
//     *
//     * @return
//     */
//    public static HashMap<String, PositionDataPoint> getFireStoreDataPoints() {
//        boolean done = false;
//        if (positionDataPoints == null) {
//            positionDataPoints = new HashMap<String, PositionDataPoint>();
//            db = FirebaseFirestore.getInstance();
//            db.collection("locations").get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if (task.isSuccessful()) {
//                                for (QueryDocumentSnapshot document : task.getResult()) {
////                                    Log.d(TAG, document.getId() + " => " + document.getData());
//                                    Map<String, Object> data = document.getData();
//                                    if (data.containsKey("students")) {
//                                        Log.d(TAG, document.getId() + " => " + document.getData());
//                                        PositionDataPoint point = new PositionDataPoint(document.getId(), (String) data.get("name"), (String) data.get("address"), (String) data.get("type"), (double) data.get("x"), (double) data.get("y"), (ArrayList) data.get("students"));
//                                        Log.d(TAG, point.students.toString());
//                                        positionDataPoints.put(document.getId(), point);
//                                    } else {
//                                        PositionDataPoint point = new PositionDataPoint(document.getId(), (String) data.get("name"), (String) data.get("address"), (String) data.get("type"), (double) data.get("x"), (double) data.get("y"));
//                                        Log.d(TAG, point.id);
//                                        positionDataPoints.put(document.getId(), point);
//                                    }
//                                }
//                            } else {
//                                Log.d(TAG, "Error getting documents: ", task.getException());
//                            }
//                        }
//                    });
//
//        }
//        return positionDataPoints;
//    }
//
//    public static void addToFireStoreCollection(String collection, Map<String, Object> point) {
//        db.collection(collection).add(point).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
//            }
//        })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error adding document", e);
//                    }
//                });
//    }
//
//    public static Map<String, Object> DataPointToMap(PositionDataPoint dataPoint) {
//        Map<String, Object> result = new HashMap<>();
//        result.put("name", dataPoint.name);
//        result.put("address", dataPoint.address);
//        result.put("x", dataPoint.x);
//        result.put("y", dataPoint.y);
//        result.put("type", dataPoint.type);
//        return result;
//    }
//
//
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
//     * Gets the libraries json and sets them in an array of PositionDataPoints.
//     */
//    public static PositionDataPoint[] getLibrariesJSON(Context context) {
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
//                positionDataPoints[i] = new PositionDataPoint("none", name, address, "library", x, y);
//            }
//            return positionDataPoints;
//        } catch (Exception e) {
//            Log.e("Queries.getLibraries", e.getMessage());
//        }
//        return null;
//    }
//
//    /**
//     * Gets the schools json and sets them in an array of PositionDataPoints.
//     */
//    public static PositionDataPoint[] getSchoolsJSON(Context context) {
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
//                positionDataPoints[i] = new PositionDataPoint("none", name, address, "school", x, y);
//            }
//            return positionDataPoints;
//        } catch (Exception e) {
//            Log.e("Queries.getLibraries", e.getMessage());
//        }
//        return null;
//    }
//}

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
