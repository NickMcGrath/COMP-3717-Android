package ca.bcit.studybuddy;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.HashMap;

public class HomeFragment extends Fragment {
    private static String TAG = "HomeFragment";
    private TextView name;

    public String[] librariesNearby = {"nearest", "second-nearest", "third-nearest"};
    private DrawerLayout drawer;

    GoogleSignInClient mGoogleSignInClient;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        // ListView
        ArrayAdapter adapter = new ArrayAdapter<>(getActivity(),
                R.layout.activity_listview, librariesNearby);

        ListView listView = v.findViewById(R.id.library_list);
        listView.setAdapter(adapter);

        name = v.findViewById(R.id.testing_text);

        googleSignIn();

        return v;
    }
    protected void googleSignIn(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getActivity());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();

            name.setText(personName);
        }
    }

}
