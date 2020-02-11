package e.allou.spoilers;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import androidx.room.Room;
import e.allou.spoilers.roomdb.DataEntity;
import e.allou.spoilers.roomdb.SpoilerDB;

public class CreateSpoiler extends AppCompatActivity {

    private EditText editTextSynopsis;
    private EditText editTextTitre;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Map<String, Object> dt = new HashMap<>();
    private String titre = "titre vide";
    private String synopsis = "synopsis vide";
    private SpoilerDB spoilerDB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_spoiler);
        editTextSynopsis = (EditText)findViewById(R.id.textcreatespoiler);
        editTextTitre = (EditText)findViewById(R.id.mySpoilerTitre);
        String spoilerdt = "nothing";


    }

    public void savedata(View view) {
        //Save Data in local #Persistence de donnée
        spoilerDB = Room.databaseBuilder(getApplicationContext(), SpoilerDB.class, "dataentity").allowMainThreadQueries().build();
        titre = editTextTitre.getText().toString();
        synopsis = editTextSynopsis.getText().toString();

        if(!isSynopsisEmpty(synopsis) && !isTitleEmpty(titre)){
            DataEntity entity = new DataEntity();
            entity.synopsis = synopsis;
            entity.titre = titre;
            spoilerDB.dataDao().insertAll(entity);

            DataEntity entity1 = new DataEntity();
            entity1 = spoilerDB.dataDao().findByTitre(titre);
            Toast.makeText(this, entity1.titre, Toast.LENGTH_SHORT).show();
        }
        else Toast.makeText(this, "Contenu Non Valide", Toast.LENGTH_SHORT).show();

    }
    public boolean isTitleEmpty(String title){
        return title.isEmpty();
    }

    public boolean isSynopsisEmpty(String spoiler){
        return spoiler.isEmpty();
    }

    public void senddata(View view) {
        //Envoyer les données dans la BDD
        dt.put("titre", editTextTitre.getText().toString());
        dt.put("synopsis", editTextSynopsis.getText().toString());

        db.collection("resume")
                .add(dt)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(CreateSpoiler.this, "Document Ajouté avec succès",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateSpoiler.this, "Erreur lors de l'ajout du document",
                                Toast.LENGTH_SHORT).show();
                    }
                });



    }
}
