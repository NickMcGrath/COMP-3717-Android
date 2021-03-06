package ca.bcit.studybuddy;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.media.TimedText;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

public class NotificationFragment extends Fragment {
    private ArrayList<User> users = new ArrayList<User>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    int[] images;
    String[] names;
    String[] schools;
    String TAG = "NotificationFrag";
    int requestSize;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        getUsers();

        return view;
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String rNames[];
        String rSchools[];
        int rImages[];

        MyAdapter(Context c, String name[], String school[], int img[]) {
            super(c, R.layout.fragment_notification_listview, R.id.textView1, name);
            this.context = c;
            this.rNames = name;
            this.rSchools = school;
            this.rImages = img;
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.fragment_notification_listview, parent, false);
//            View view = inflater.inflate(R.layout.fragment_notification, container, false);
            ImageView imageView = row.findViewById(R.id.profile_image);
            TextView aName = row.findViewById(R.id.textView1);
            TextView aSchool = row.findViewById(R.id.textView2);

            String url = users.get(position).photoUrl;
            if(url == "")
                imageView.setImageResource(R.drawable.logo);
            else
                Glide.with(getContext()).load(url).into(imageView);
            aName.setText(rNames[position]);
            aSchool.setText(rSchools[position]);
            //Handle buttons and add onClickListeners
            Button btnViewProfile = (Button) row.findViewById(R.id.btn_view_profile);
            final Button btnAccept = (Button) row.findViewById(R.id.btn_accept);
            final Button btnDecline = (Button) row.findViewById(R.id.btn_decline);
            final int pos = position;

            btnViewProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(pos);
                }
            });

            if (((LandingActivity) getActivity()).user.friends.contains(users.get(position).pk)) {
                btnAccept.setVisibility(View.GONE);
                btnDecline.setVisibility(View.GONE);
                aSchool.setText(rSchools[position] + "\n" + users.get(position).phone);
            } else {
                btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        acceptRequest(pos);
                        btnAccept.setVisibility(View.GONE);
                        btnDecline.setVisibility(View.GONE);
                    }
                });
                btnDecline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        declineRequest(pos);
                        btnAccept.setVisibility(View.GONE);
                        btnDecline.setVisibility(View.GONE);
                    }
                });
            }


            //btnViewProfile.setOnClickListener(new View.OnClickListener() {
            //    @Override
            //    public void onClick(View v) {
            //        showDialog();
            //    }
            //});
            return row;
        }

        /**
         * Accept a request.
         *
         * @param pos
         */
        public void acceptRequest(int pos) {
            ((LandingActivity) getActivity()).acceptRequest(users.get(pos).pk);
        }

        /**
         * decline a request.
         *
         * @param pos
         */
        public void declineRequest(int pos) {
            ((LandingActivity) getActivity()).declineRequest(users.get(pos).pk);
        }

        public void showDialog(int position) {
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.setContentView(R.layout.popup_profile);

            ImageView image = dialog.findViewById(R.id.profile_image);
            TextView name = dialog.findViewById(R.id.profile_name);
            TextView school = dialog.findViewById(R.id.profile_school);
            TextView major = dialog.findViewById(R.id.profile_major);

            //image.setImageResource(rImages[0]);
            String url = users.get(position).photoUrl;
            if(url == "")
                image.setImageResource(R.drawable.logo);
            else
                Glide.with(getContext()).load(url).into(image);
            name.setText(users.get(position).name);
            major.setText("Study " + users.get(position).major);
            school.setText("at " + users.get(position).school);

            Button btnClose = dialog.findViewById(R.id.btn_close);
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }
    }

    /**
     * This sets the users in the Adapter.
     * <p>
     * This is the nastiest code I have ever written, I apologize for anyone that has to see this.
     * It all started when I realized that making 2 calls to a firebase sequentially would be harder
     * than I though 🤔 then when I realized these calls where async methods that cant return like
     * normal methods, that is when it got bad
     */
    public void getUsers() {
        final ArrayList<String> usersIDs = new ArrayList<String>();
        Task<DocumentSnapshot> taskGetIds = db.collection("students").document(((LandingActivity) getActivity()).user.pk)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "Current data: " + document.getData());
                                Map<String, Object> data = document.getData();
                                usersIDs.addAll((ArrayList<String>) data.get("requests"));
                                usersIDs.addAll((ArrayList<String>) data.get("friends"));
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
                requestSize = usersIDs.size();
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
                                    if (users.size() == requestSize) {
                                        names = new String[users.size()];
                                        schools = new String[users.size()];
                                        images = new int[users.size()];
                                        for (int i = 0; i < users.size(); i++) {
                                            names[i] = users.get(i).name;
                                            schools[i] = users.get(i).school;
                                            images[i] = R.drawable.logo;
                                        }
                                        Log.d("mylog", Arrays.toString(names));
                                        ListView listView = (ListView) getView().findViewById(R.id.requests_list);
                                        MyAdapter adapter = new MyAdapter(getActivity(), names, schools, images);
                                        listView.setAdapter(adapter);
                                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                if (i == 0) {
                                                    Toast.makeText(getActivity(), "first request", Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });
                                    }
                                    if (requestSize == 0) {
                                        names = new String[1];
                                        schools = new String[1];
                                        images = new int[1];
                                        names[0] = "No one Yet :(";
                                        schools[0] = "";
                                        images[0] = R.drawable.logo;
                                        Log.d("mylog", Arrays.toString(names));
                                        ListView listView = (ListView) getView().findViewById(R.id.requests_list);
                                        MyAdapter adapter = new MyAdapter(getActivity(), names, schools, images);
                                        listView.setAdapter(adapter);
                                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                if (i == 0) {
                                                    Toast.makeText(getActivity(), "first request", Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });
                                    }

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