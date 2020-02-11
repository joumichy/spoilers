package e.allou.spoilers;

import android.icu.text.UnicodeSetSpanner;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.room.Room;
import e.allou.spoilers.roomdb.SpoilerDB;
import e.allou.spoilers.usersName.UsersEntity;
import e.allou.spoilers.usersName.UsersRoom;

public class Inscription extends AppCompatActivity {
    private EditText textEmail;
    private EditText textPassword;
    private EditText textConfirmPassword;
    private EditText userName;
    private UsersEntity usersEntity;
    private String email ="",password="",confirmPassword="",inputUserName="";
    private FirebaseAuth mAuths;
    private final String TAG = "Inscription";
    private UsersRoom usersRoom;




    private boolean passwordConfirmed = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);
        textEmail = findViewById(R.id.inscription_email);
        textPassword = findViewById(R.id.inscription_mot_de_passe);
        textConfirmPassword = findViewById(R.id.inscription_confirme_mot_de_passe);
        userName = findViewById(R.id.inscription_nom_user);
        mAuths = FirebaseAuth.getInstance();



    }

    public void submit(View view) {
        //Creer un utisateur
        inputUserName = userName.getText().toString();
        email = textEmail.getText().toString();
        password = textPassword.getText().toString();
        confirmPassword = textConfirmPassword.getText().toString();
        usersRoom = Room.databaseBuilder(getApplicationContext(),UsersRoom.class,"users").allowMainThreadQueries().build();
        usersEntity = new UsersEntity();

        usersEntity.email = email;
        usersEntity.username = inputUserName;



        if(isPasswordConfirm(password,confirmPassword) && password.length()>6){
            passwordConfirmed = true;
        }
        else Toast.makeText(this, "Mot de passe invalide", Toast.LENGTH_SHORT).show();

        if(passwordConfirmed && isEmail(email)){
            mAuths.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(Inscription.this, "Inscription Réussi", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuths.getCurrentUser();

                                usersRoom.usersDao().insertAll(usersEntity);

                                //updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(Inscription.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }

                            // ...
                        }
                    });
        }
        else Toast.makeText(this, "Information Non Valide !", Toast.LENGTH_SHORT).show();

    }



    public boolean isPasswordConfirm(String password, String confirmPassword){
        return password.equals(confirmPassword);
    }
    public boolean isEmail(String email){
        return email.contains("@");
    }

}
