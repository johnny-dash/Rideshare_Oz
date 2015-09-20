package au.org.ridesharingoz.rideshare_oz;

import com.firebase.client.Firebase;
import com.firebase.client.ChildEventListener;
import com.firebase.client.ValueEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.*;
import com.firebase.client.Query;

/**
 * Created by Ocunidee on 20/09/2015.
 */
public class ProfileActivity extends FirebaseAuthenticatedActivity{

  /*  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView messagesView = (ListView) findViewById(R.id.messages_list);

        Firebase.setAndroidContext(this);

        Firebase ref = new Firebase("https://flickering-inferno-6814.firebaseio.com/users");
        mAdapter = new FirebaseListAdapter<User>();
        @Override
        protected void populateView(View view, ChatMessage chatMessage) {
            ((TextView)view.findViewById(android.R.id.text1)).setText(chatMessage.getName());
            ((TextView)view.findViewById(android.R.id.text2)).setText(chatMessage.getMessage());
        }
    };
    setListAdapter(mAdapter);


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot snapshot) {
            String result = snapshot.getValue() == null ? "is not" : "is";
            System.out.println("Mary " + result + " a member of alpha group");
        }
        @Override
        public void onCancelled(FirebaseError firebaseError) {
            // ignore
        }
    });
*/
}
