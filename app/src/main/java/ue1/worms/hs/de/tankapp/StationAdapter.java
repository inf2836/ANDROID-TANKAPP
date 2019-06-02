package ue1.worms.hs.de.tankapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class StationAdapter extends ArrayAdapter<Station> {

    private Context mContext;
    private List<Station> stationList;

    public StationAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes List<Station> list) {
        super(context, 0 , list);
        mContext = context;
        stationList = list;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.row,parent,false);

        Station currentStation = stationList.get(position);

        TextView name = listItem.findViewById(R.id.textView_name);
        name.setText(currentStation.getBrand());

        ImageView imageView= listItem.findViewById(R.id.imageView);
        if (name.getText().equals("ARAL")){
            imageView.setImageResource(R.drawable.aral);
        } else if(name.getText().equals("ESSO")){
            imageView.setImageResource(R.drawable.esso);
        }else if(name.getText().equals("JET")){

            imageView.setImageResource(R.drawable.jet);
        }else if(name.getText().equals("Shell")){
            imageView.setImageResource(R.drawable.shell);
        }else if(name.getText().equals("BFT")){
            imageView.setImageResource(R.drawable.bft);
        }else if(name.getText().equals("TOTAL")){
        imageView.setImageResource(R.drawable.total1);
        }else if(name.getText().equals("ELAN")){
        imageView.setImageResource(R.drawable.elan);
        }else if(name.getText().equals("STAR")){
        imageView.setImageResource(R.drawable.star);
        }else if(name.getText().equals("Sprint")){
        imageView.setImageResource(R.drawable.sprint);
    }
        else{
            imageView.setImageResource(R.drawable.def);
        }

        TextView street = (TextView) listItem.findViewById(R.id.textView_street);
        street.setText(currentStation.getStreet());

        TextView place = (TextView) listItem.findViewById(R.id.textView_place);
        place.setText(currentStation.getPlace());

        TextView dist = (TextView) listItem.findViewById(R.id.textView_dist);
        dist.setText(currentStation.getDist());

        TextView dPreis = (TextView) listItem.findViewById(R.id.textView_dPreis);
        dPreis.setText(currentStation.getdPreis());
        setFontTextView( mContext,dPreis );
        return listItem;
    }

    public void setFontTextView(Context c, TextView name) {
        Typeface font = Typeface.createFromAsset(c.getAssets(),"fonts/digit.ttf");
        name.setTypeface(font);
    }
}
