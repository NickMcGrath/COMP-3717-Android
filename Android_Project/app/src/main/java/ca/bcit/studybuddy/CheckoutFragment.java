package ca.bcit.studybuddy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class CheckoutFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_checkout, container, false);

    }

    private void test() {
        //copy and paste on btn listener
        ((LandingActivity) getActivity()).checkOut();

         ArrayList<String> friends = ((LandingActivity) getActivity()).user.friends != null ? ((LandingActivity) getActivity()).user.friends : new ArrayList<String>();
    }
}
