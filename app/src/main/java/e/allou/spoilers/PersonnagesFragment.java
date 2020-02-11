package e.allou.spoilers;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class PersonnagesFragment extends Fragment {
    private ImageView imageView;


    public PersonnagesFragment(){

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

      // byte[] bytes = getArguments().getByteArray("bytes");
        //Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        View view = inflater.inflate(R.layout.fragment_personnages, container,false);

        Bundle bundle = getArguments();

        if(bundle != null){


            //byte[] bytes = bundle.getByteArray("byteskey");

            //Gson gson = new Gson();
            //Type typeList = new TypeToken<ArrayList<Byte>>(){}.getType();
            //byte[] bytes = gson.fromJson(getArguments().getString("json"), typeList);
         // Toast.makeText(getActivity(), bytes.length, Toast.LENGTH_SHORT).show();
            if(getArguments().getString("file") != null){
                Toast.makeText(getActivity(), "Clef non vide", Toast.LENGTH_SHORT).show();
                File file = new File(getArguments().getString("file"));
                byte[] bytes = new byte[(int) file.length()];

                FileInputStream fis = null;
                try {
                    fis = new FileInputStream(file);
                    fis.read(bytes); //read file into bytes[]
                    fis.close();
                    Toast.makeText(getActivity(), "Donnée récupérée", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                imageView = view.findViewById(R.id.imagePersonnage);
                imageView.setImageBitmap(Bitmap.createScaledBitmap(bmp, imageView.getWidth(), imageView.getHeight(), false));
            }
            else Toast.makeText(getActivity(), "Clef vide", Toast.LENGTH_SHORT).show();

        }


       return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }


}
