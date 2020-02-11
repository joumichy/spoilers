package e.allou.spoilers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.room.PrimaryKey;

public class Parametre extends AppCompatActivity {

    private TextView emailUser;
    private TextView nomUser;
    private String userEmail;
    private String userName;
    private final String KEY_EMAIL = "email";
    private final String KEY_USERNAME = "username";

    private final String TAG_INTENT_PARAMETER = "intentParameter";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametre);
        emailUser = findViewById(R.id.parametre_email);
        nomUser = findViewById(R.id.parametre_nom_user);
        findViewById(R.id.deconnexion);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Toast.makeText(this, "Vous n'etes pas connecté", Toast.LENGTH_SHORT).show();
            Intent goBackLoginActivity = new Intent(this, Login.class);
            goBackLoginActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(goBackLoginActivity);

        }

        sharedPreferences = getApplicationContext().getSharedPreferences("MyPref",0);
        editor = sharedPreferences.edit();

        userEmail = sharedPreferences.getString(KEY_EMAIL,null);
        userName =  sharedPreferences.getString(KEY_USERNAME,null);

        emailUser.setText(userEmail);
        nomUser.setText(userName);



    }

    public void logout(View view) {
        //Deleting The User DATA in Shared Pref.
        sharedPreferences = getApplicationContext().getSharedPreferences("MyPref",0);
        editor = sharedPreferences.edit();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        editor.remove(KEY_EMAIL);
        editor.remove(KEY_USERNAME);
        editor.commit();

        Toast.makeText(this, "Utilisateur Deconnecté", Toast.LENGTH_SHORT).show();
        Intent goBackLoginActivity = new Intent(this, Login.class);
        goBackLoginActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(goBackLoginActivity);

        //TODO : Deconnecter l'utilisateur

    }

    public void startMySpoilerActivity(View view) {
        Intent intentMySpoilers = new Intent(this, MySpoilers.class);
        startActivity(intentMySpoilers);
    }


}
