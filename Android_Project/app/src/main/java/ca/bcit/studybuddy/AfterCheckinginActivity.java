package ca.bcit.studybuddy;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AfterCheckinginActivity extends Fragment {

    private TextView library;
    private TextView address;
    private ListView potentialBuddyListView;
    private ArrayList<User> users = new ArrayList<User>();
    private Bundle bundle;
    private String locationPk;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    int[] images;
    String[] names;
    String[] schools;
    String TAG = "AfterCheckinginActivity";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_after_checkingin, container, false);

        if (getArguments() != null) {
            bundle = getArguments();
            locationPk = bundle.getString("locationPk");
        }

        library = v.findViewById(R.id.checkin_location);
        address = v.findViewById(R.id.checkin_address);

        // hard coded for now; these should be passed from the landing activity
        //Bundle bundle = getIntent().getExtras();

        String locationStr = getArguments().getString("locationName");
        String addressStr = getArguments().getString("locationAddress");
        library.setText(locationStr);
        address.setText(addressStr);

        potentialBuddyListView = v.findViewById(R.id.potential_buddy_list);


        //fun zone
        getUsersAtLocation(locationPk);


        return v;
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String rNames[];
        String rSchools[];
        int rImages[];


        MyAdapter(Context c, String name[], String school[], int img[]) {
            super(c, R.layout.activity_buddy_listview, R.id.textView1, name);
            this.context = c;
            this.rNames = name;
            this.rSchools = school;
            this.rImages = img;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.activity_buddy_listview, parent, false);
            ImageView imageView = row.findViewById(R.id.profile_image);
            TextView aName = row.findViewById(R.id.textView1);
            TextView aSchool = row.findViewById(R.id.textView2);

            // now set out resources on viewss
            imageView.setImageResource(rImages[position]);
            aName.setText(rNames[position]);
            aSchool.setText(rSchools[position]);
            final Button b = row.findViewById(R.id.btn_request);
            if (((LandingActivity) getActivity()).user.friends.contains(users.get(position).pk)) {
                b.setAlpha(0);
            } else {
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onPressRequest(position);
                        b.setAlpha((float) 0.5);
                    }
                });
            }


            return row;
        }
    }

    /**
     * Takes the position of the button and sends a request to the corresponding user.
     *
     * @param pos
     */
    public void onPressRequest(int pos) {
        Log.d(TAG, "made it here " + pos);
        ((LandingActivity) getActivity()).sendRequest(users.get(pos).pk);
    }

    /**
     * Gets all the users at a location and sets them in Adapter.
     * This is the nastiest code I have ever written, I apologize for anyone that has to see this.
     * It all started when I realized that making 2 calls to a firebase sequentially would be harder
     * than I though 🤔 then when I realized these calls where async methods that cant return like
     * normal methods, that is when it got bad
     *
     * @param locationID
     * @return
     */
    public void getUsersAtLocation(String locationID) {
        final ArrayList<String> usersIDs = new ArrayList<String>();
        Task<DocumentSnapshot> taskGetIds = db.collection("locations").document(locationID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "Current data: " + document.getData());
                                Map<String, Object> data = document.getData();
                                usersIDs.addAll((ArrayList<String>) data.get("students"));
                                Log.d("mylog", usersIDs.toString());
                            } else {
                                Log.d(TAG, "No such document");
                            }
//                                Log.d(TAG, usersIDs.toString());
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
        Task<DocumentSnapshot> taskGetUsers = taskGetIds.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                for (int i = 0; i < usersIDs.size(); i++) {
                    db.collection("students").document(usersIDs.get(i))
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d(TAG, "Current data: " + document.getData());
                                    Map<String, Object> data = document.getData();

                                    User aUser = new User(
                                            (String) data.get("name"),
                                            (String) data.get("location"),
                                            (String) data.get("major"),
                                            (String) data.get("phone"),
                                            (String) data.get("pk"),
                                            (String) data.get("school"),
                                            (ArrayList<String>) data.get("friends"),
                                            (ArrayList<String>) data.get("requests"),
                                            (ArrayList<String>) data.get("sentRequests"),
                                            (String) data.get("photoUrl")
                                    );
                                    users.add(aUser);
                                    Log.d("mylog in view", aUser.toString());
                                    if (users.size() > 0) {
                                        names = new String[users.size()];
                                        schools = new String[users.size()];
                                        images = new int[users.size()];
                                        for (int i = 0; i < users.size(); i++) {
                                            names[i] = users.get(i).name;
                                            schools[i] = users.get(i).school;
                                            images[i] = R.drawable.logo;
                                        }
                                    } else {
                                        names = new String[1];
                                        schools = new String[1];
                                        images = new int[1];
                                        names[0] = "No one Yet :(";
                                        schools[0] = "";
                                        images[0] = R.drawable.logo;
                                    }
                                    Log.d("mylog", Arrays.toString(names));
                                    MyAdapter adapter = new MyAdapter(getContext(), names, schools, images);
                                    potentialBuddyListView.setAdapter(adapter);
                                } else {
                                    Log.d(TAG, "No such document");
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });
                }
            }
        });
    }

}
