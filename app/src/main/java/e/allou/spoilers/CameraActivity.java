package e.allou.spoilers;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.Manifest;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ThrowOnExtraProperties;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CameraActivity extends AppCompatActivity {
    private SurfaceView cameraView;
    private TextView textView;
    private CameraSource cameraSource;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference note;

    final int RequestCameraPermissionID = 1001;
    private final String KEY_TITRE = "titre";
    private final String KEY_COLLECTION = "resume";
    private final String KEY_SYNOPSIS = "synopsis";
    private final String TAG = "CameraActivity";
    private String spoilerTitre;
    private String spoilerSynopsis;
    private int justOne = 0;




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case RequestCameraPermissionID:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA)  != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    try {
                        cameraSource.start(cameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);


        cameraView = (SurfaceView)findViewById(R.id.surfaceId);
        textView = (TextView)findViewById(R.id.textCameraId);

        final TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        Gson gson = new Gson();
        Intent loginIntent = getIntent();
        String userMail = loginIntent.getStringExtra("email");

        final List<String> titres = new ArrayList<>();
        final List<String> list = new ArrayList<>();

        db.collection(KEY_COLLECTION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot documentSnapshot: task.getResult()){

                        list.add(documentSnapshot.getId());
                        titres.add(documentSnapshot.getString(KEY_TITRE));

                    }
                    Toast.makeText(CameraActivity.this, "Document Récupéré !", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(CameraActivity.this, "Erreur Document not FOUND !", Toast.LENGTH_SHORT).show();
                }

            }
        });

//

        if(!textRecognizer.isOperational()){
            Log.w("CameraActivity", "DetectorDependencies not available");
        }
        else{
            cameraSource = new CameraSource.Builder(getApplicationContext(),textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280,1024)
                    .setRequestedFps(2.0f)
                    .setAutoFocusEnabled(true)
                    .build();
            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {
                        if(ActivityCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(CameraActivity.this,
                                    new String[]{Manifest.permission.CAMERA},RequestCameraPermissionID);
                            return;

                        }
                        cameraSource.start(cameraView.getHolder());

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {


                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    cameraSource.stop();

                }
            });

            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if(items.size() != 0) {
                        textView.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder = new StringBuilder();
                                for (int i = 0; i < items.size(); ++i){
                                    final TextBlock item = items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    stringBuilder.append("\n");


                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            for (int j = 0; j<titres.size(); j++){
                                                String titre = titres.get(j);

                                                String newItem = item.getValue();
                                                newItem = newItem.toLowerCase();

                                                if(newItem.equals(titre) || newItem.contains(titre) && justOne == 0){
                                                    Intent intentShowSpoiler = new Intent(CameraActivity.this,ShowSpoiler.class);
                                                    getSpoiler(titre);
                                                    intentShowSpoiler.putExtra(KEY_TITRE,spoilerTitre);
                                                    intentShowSpoiler.putExtra(KEY_SYNOPSIS,spoilerSynopsis);
                                                    intentShowSpoiler.putExtra("tag",TAG);
                                                    intentShowSpoiler.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    intentShowSpoiler.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    justOne++;

                                                        //TODO: Reussir à lancer l'application q'une fois
                                                    startActivity(intentShowSpoiler);






                                                }
                                            }


                                        }
                                    },300);


                                }
                                textView.setText(stringBuilder.toString());


                            }
                        });

                    }

                }
            });
        }
    }


    public void startbdd(View view) {
        Intent intent = new Intent(this,LoadSpoiler.class);
             startActivity(intent);

    }

    public void accessParameter(View view) {
        Intent intentParameter = new Intent(this,Parametre.class);
        startActivity(intentParameter);
    }
    public void getSpoiler(String titre){
        note = db.collection(KEY_COLLECTION).document(titre);
        note.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override

                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){

                            spoilerSynopsis = documentSnapshot.getString(KEY_SYNOPSIS);
                            spoilerTitre = documentSnapshot.getString(KEY_TITRE);




                        }
                        else{
                            Toast.makeText(CameraActivity.this,"le document n'existe pas",Toast.LENGTH_SHORT).show();

                        }

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CameraActivity.this,"Erreur !",Toast.LENGTH_SHORT).show();


                    }
                });
    }


}
