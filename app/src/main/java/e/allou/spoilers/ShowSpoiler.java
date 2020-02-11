package e.allou.spoilers;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.UnicodeSetSpanner;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import e.allou.spoilers.roomdb.DataEntity;

public class ShowSpoiler extends AppCompatActivity {
    private TextView titreView;
    private TextView synopsisView;
    private ImageView imagePersonnage;

    private Bitmap bmp;
    private ByteArrayOutputStream stream;
    private Bundle bundle = new Bundle();
    private final int KEY_BITMAP = 1;
    private final String TAG = "tag";
    private final String TAG_CAMERA_ACTIVITY = "CameraActivity";
    private final String TAG_MY_SPOILERS = "MySpoilers";
    private final String KEY_TITRE = "titre";
    private final String KEY_SYNOPSIS = "synopsis";
    private final String KEY_FORMAT = ".jpg";
    private final String KEY_IMAGE = "image1";

    private final String NARUTO = "naruto";
    private final String ONE_PIECE = "one piece";
    private final String BLEACH = "bleach";
    private boolean imageExist = true;

    private StorageReference mStorageRef;


    private int i = 1;

    private String tagActivity;
    private String titre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_spoiler);
        titreView = findViewById(R.id.showSpoilerTitreId);
        synopsisView = findViewById(R.id.showSpoilerSynopsisId);
        synopsisView.setMovementMethod(new ScrollingMovementMethod());
        imagePersonnage = findViewById(R.id.imagePersonnage);
        Intent intentSpoiler = getIntent();
        titre = "titre null";
        String synopsis = "synopsis null";
        DataEntity spoiler;
        Gson gson = new Gson();
        tagActivity = getIntent().getStringExtra(TAG);


        mStorageRef = FirebaseStorage.getInstance().getReference();


        switch (tagActivity) {
            case TAG_MY_SPOILERS:
                spoiler = gson.fromJson(getIntent().getStringExtra("spoiler"), DataEntity.class);

                titre = spoiler.titre;
                synopsis = spoiler.synopsis;

                break;
            case TAG_CAMERA_ACTIVITY:
                titre = getIntent().getStringExtra(KEY_TITRE);
                synopsis = getIntent().getStringExtra(KEY_SYNOPSIS);






        }



        titreView.setText(titre);
        synopsisView.setText(synopsis);


    }




    public void loadPersonnage(View view) {

        String image = "image";



        StorageReference riversRef = mStorageRef.child(titre + "/image1.jpg");

        final long myImage = 1024 * 1024;

        riversRef.getBytes(myImage)
                .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                        stream = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.PNG,100,stream);

                        byte[] cpBytes = stream.toByteArray();

                        bundle.putByteArray("byteskey",cpBytes);

                        File tempFile = null;
                        try {
                            tempFile = File.createTempFile("tmpFile", ".txt", null);
                            FileOutputStream fos = new FileOutputStream(tempFile);
                            fos.write(bytes);
                            bundle.putString("file",tempFile.getAbsolutePath());
                            Toast.makeText(ShowSpoiler.this, "Fichier ecris avec succes", Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }





                        Toast.makeText(ShowSpoiler.this, "image Recupéré", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(ShowSpoiler.this, "Image introuvable", Toast.LENGTH_SHORT).show();
            }
        });

        


            bundle.putByteArray("myarray","Voila mon array".getBytes());

            bundle.putString("ok","okj");

            PersonnagesFragment personnagesFragment = new PersonnagesFragment();

           personnagesFragment.setArguments(bundle);
           FragmentManager fragmentManager = getFragmentManager();


            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragmentConteneur,personnagesFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();




    }
}
