package ca.bcit.studybuddy;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;

import static com.facebook.FacebookSdk.getApplicationContext;

public class AfterCheckinginActivity extends Fragment {

    private TextView library;
    private TextView address;
    private ListView potentialBuddyListView;
    final int[] images = {R.drawable.logo, R.drawable.logo, R.drawable.logo};
    final String[] names = {"Rahul Kukreja", "Nathan McNinch", "Chi En Huang"};
    final String[] schools = {"British Columbia Institute of Technology","British Columbia Institute of Technology","British Columbia Institute of Technology"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_after_checkingin, container, false);
        library = v.findViewById(R.id.checkin_location);
        address = v.findViewById(R.id.checkin_address);

        // hard coded for now; these should be passed from the landing activity
        //Bundle bundle = getIntent().getExtras();

        String locationStr = getArguments().getString("locationName");
        String addressStr = getArguments().getString("locationAddress");
        library.setText(locationStr);
        address.setText(addressStr);

        potentialBuddyListView = v.findViewById(R.id.potential_buddy_list);

        MyAdapter adapter = new MyAdapter(getContext(), names, schools, images);
        potentialBuddyListView.setAdapter(adapter);

        return v;
    }

    class MyAdapter extends ArrayAdapter<String>{
        Context context;
        String rNames[];
        String rSchools[];
        int rImages[];

        MyAdapter(Context c, String name[], String school[],int img[]){
            super(c, R.layout.activity_buddy_listview, R.id.textView1, name);
            this.context = c;
            this.rNames = name;
            this.rSchools = school;
            this.rImages = img;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.activity_buddy_listview, parent, false);
            ImageView imageView = row.findViewById(R.id.profile_image);
            TextView aName = row.findViewById(R.id.textView1);
            TextView aSchool = row.findViewById(R.id.textView2);

            // now set out resources on viewss
            imageView.setImageResource(rImages[position]);
            aName.setText(rNames[position]);
            aSchool.setText(rSchools[position]);

            return row;
        }
    }

}
