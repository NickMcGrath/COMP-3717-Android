package ca.bcit.studybuddy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class CheckoutFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_checkout, container, false);
        Button btnCheckOut = v.findViewById(R.id.btn_check_out);
        btnCheckOut.setOnClickListener(btnListener);

        TextView location = v.findViewById(R.id.current_location);
        TextView address = v.findViewById(R.id.address);
        String locationStr = getArguments().getString("locationName");
        String addressStr = getArguments().getString("locationAddress");
        location.setText(locationStr);
        address.setText(addressStr);


        return v;

    }

    private View.OnClickListener btnListener = new View.OnClickListener(){
        public void onClick(View v){
            ((LandingActivity) getActivity()).checkOut();
        }
    };
}
