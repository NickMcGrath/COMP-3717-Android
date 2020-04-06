package ca.bcit.studybuddy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class CheckoutFragment extends Fragment {
    private TextView location;
    private TextView address;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_checkout, container, false);
        Button btnCheckOut = v.findViewById(R.id.btn_check_out);
        btnCheckOut.setOnClickListener(btnListener);

        location = v.findViewById(R.id.current_location);
        address = v.findViewById(R.id.address);

        if (((LandingActivity) getActivity()).user.location != ""){
            String locationStr = getArguments().getString("locationName");
            String addressStr = getArguments().getString("locationAddress");
            location.setText(locationStr);
            address.setText(addressStr);
        }
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
