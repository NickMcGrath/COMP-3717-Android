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

import static com.facebook.FacebookSdk.getApplicationContext;

public class NotificationFragment extends Fragment{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        // TODO need to replace these hardcoded data
        final String names[] = {"Gina Kim","Nick McGrath","Yuni Hur"};
        final String schools[] = {"British Columbia Institute of Technology","British Columbia Institute of Technology","British Columbia Institute of Technology"};
        final int images[] = {R.drawable.logo, R.drawable.logo, R.drawable.logo};

        ListView listView = (ListView) view.findViewById(R.id.requests_list);

        MyAdapter adapter = new MyAdapter(getActivity(), names, schools, images);
        listView.setAdapter(adapter);

        ((LandingActivity)getActivity()).testMethod();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0){
                    Toast.makeText(getActivity(), "first request", Toast.LENGTH_SHORT).show();

                }

            }
        });

        return view;
    }

    class MyAdapter extends ArrayAdapter<String>{
        Context context;
        String rNames[];
        String rSchools[];
        int rImages[];

        MyAdapter(Context c, String name[], String school[],int img[]){
            super(c, R.layout.fragment_notification_listview, R.id.textView1, name);
            this.context = c;
            this.rNames = name;
            this.rSchools = school;
            this.rImages = img;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.fragment_notification_listview, parent,false );
            //View view = inflater.inflate(R.layout.fragment_notification, container, false);
            ImageView imageView = row.findViewById(R.id.profile_image);
            TextView aName = row.findViewById(R.id.textView1);
            TextView aSchool = row.findViewById(R.id.textView2);

            imageView.setImageResource(rImages[position]);
            aName.setText(rNames[position]);
            aSchool.setText(rSchools[position]);

            //Handle buttons and add onClickListeners
            Button btnViewProfile = (Button)row.findViewById(R.id.btn_view_profile);
            //Button btnAccept = (Button)row.findViewById(R.id.btn_accept);
            //Button btnDecline = (Button)row.findViewById(R.id.btn_decline);

            btnViewProfile.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    showDialog();
                }
            });

            return row;
        }

        public void showDialog(){
            final Dialog dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            dialog.setContentView(R.layout.popup_profile);

            ImageView image = dialog.findViewById(R.id.profile_image);
            TextView name = dialog.findViewById(R.id.profile_name);
            TextView school = dialog.findViewById(R.id.profile_school);
            TextView major = dialog.findViewById(R.id.profile_major);

            // TODO need to replace these hardcoded data with user info from database or google account
            //image.setImageResource(rImages[0]);
            name.setText(rNames[0]);
            major.setText("Study "+"CST");
            school.setText("at "+rSchools[0]);

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

}
