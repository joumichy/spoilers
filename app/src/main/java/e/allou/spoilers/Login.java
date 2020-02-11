package e.allou.spoilers;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import androidx.room.Room;
import e.allou.spoilers.roomdb.DataEntity;
import e.allou.spoilers.usersName.UsersEntity;
import e.allou.spoilers.usersName.UsersRoom;

public class Login extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private String email;
    private String password;
    private FirebaseAuth mAuth;
    private UsersRoom usersRoom;
    private UsersEntity usersEntity;
    private boolean isConnected = false;
    private final String TAG = "Login";
    private final String KEY_EMAIL = "email";
    private final String KEY_USERNAME = "username";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextEmail = (EditText)findViewById(R.id.emailId);
        editTextPassword = (EditText)findViewById(R.id.paswordId);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    public boolean checkUserInfoOk(){
        email = editTextEmail.getText().toString();
        password = editTextPassword.getText().toString();
        boolean result = true;
        if(email == null || email.equals("")||!isEmail(email) ) {
            Toast.makeText(this, "Email non valide !", Toast.LENGTH_SHORT).show();
            result = false;
        }
        if(password.length() < 4) {
            Toast.makeText(this, "Mot de passe trop court !", Toast.LENGTH_SHORT).show();
            result = false;
        }

        return result;
    }
    public void signInUser(View view) {


        usersRoom = Room.databaseBuilder(getApplicationContext(),UsersRoom.class,"users").allowMainThreadQueries().build();
        if (checkUserInfoOk()){
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(Login.this, "Connexion Réussi ", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();

                                //updateUI(user);
                                isConnected = true;

                                sendInfoToActivity();

                                accessCameraAcitivity();





                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Login.this, "Connexion Echoué ", Toast.LENGTH_SHORT).show();

                                //updateUI(null);

                                //TODO: Prooposer de creer un compte (Optional)
                            }

                            // ...
                        }
                    });
        }


    }

    private void sendInfoToActivity() {
        usersEntity = usersRoom.usersDao().findUserByEmail(email);




        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("MyPref",0);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_USERNAME,usersEntity.username);

        editor.commit();

    }

    private void accessCameraAcitivity() {

            Intent intentCameraActivity = new Intent(this, CameraActivity.class);
            intentCameraActivity.putExtra(KEY_EMAIL,email);
            startActivity(intentCameraActivity);
    }

    public void signUpUser(View view) {
        //Fonction sur Bouton d'Inscription

        Intent intentInscription = new Intent(this, Inscription.class );
        startActivity(intentInscription);

    }

    public boolean isEmail(String email){
        return email.contains("@");
    }

}
