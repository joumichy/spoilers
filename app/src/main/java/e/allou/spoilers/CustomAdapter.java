package e.allou.spoilers;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import e.allou.spoilers.roomdb.DataEntity;

public class CustomAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    int[]logos;
    int logo;
    int key = 0;
    List<DataEntity> dataEntities;
    List<Bitmap> bitmapList;
    public CustomAdapter(Context applicationContext, List<DataEntity> dataEntities){
        this.context = applicationContext;
        this.dataEntities = dataEntities;
        layoutInflater = (LayoutInflater.from(applicationContext));
    }
    public CustomAdapter(Context applicationContext, List<Bitmap> bitmapList,int key){
        this.context = applicationContext;
        this.bitmapList = bitmapList;
        this.key = key;
        layoutInflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        //Renvoie la taille
        if (key == 0) return dataEntities.size();
        else return bitmapList.size();
    }

    @Override
    public Object getItem(int position) {
        //Renvoie l'oobjet
        if (key == 0) return dataEntities.get(position);
        else  return bitmapList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //convertView = layoutInflater.inflate(R.layout.activity_my_spoilers, null);

        TextView textView = new TextView(context);
        //textView.setText(String.valueOf(position));
        if (key == 0) textView.setText(dataEntities.get(position).titre);
        else textView.setText(bitmapList.get(position).toString());
        //ImageView icon = convertView.findViewById(R.id.gridViewSpoiler);//CHANGER ICON
        //icon.setImageResource(logo);
        return textView;

    }
}
