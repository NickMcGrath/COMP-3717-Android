package ca.bcit.studybuddy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CheckoutFragment extends Fragment {
    private TextView location;
    private TextView address;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String locationPK;
    private String TAG = "CheckOutActivity";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_checkout, container, false);
        Button btnCheckOut = v.findViewById(R.id.btn_check_out);
        btnCheckOut.setOnClickListener(btnListener);
        location = v.findViewById(R.id.current_location);
        address = v.findViewById(R.id.address);
        locationPK = ((LandingActivity) getActivity()).user.location;
        db.collection("locations").document(locationPK)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        if (document.getId().equals(locationPK)){
                            String locationStr = document.get("name").toString();
                            String addressStr = document.get("address").toString();
                            location.setText(locationStr);
                            address.setText(addressStr);
                        }
                    }
                }
            }
        });

        return v;

    }

    private View.OnClickListener btnListener = new View.OnClickListener(){
        public void onClick(View v){
            if (((LandingActivity) getActivity()).user.location != ""){
                ((LandingActivity) getActivity()).checkOut();
                Toast toast = Toast.makeText(v.getContext(), "You have successfully checked-out!", Toast.LENGTH_SHORT);
                toast.show();
                location.setText("");
                address.setText("");
            } else {
                Toast toast = Toast.makeText(v.getContext(), "You are not checked-in", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    };
}
