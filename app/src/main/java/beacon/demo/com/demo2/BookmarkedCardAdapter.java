package beacon.demo.com.demo2;

/**
 * Created by utsrivas on 2/17/2016.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class BookmarkedCardAdapter extends BaseAdapter{

    ArrayList<ShopObject> shops = new ArrayList<ShopObject>();
    Context context;
    private static LayoutInflater inflater=null;
    public BookmarkedCardAdapter(Activity mainActivity, ArrayList<ShopObject> shopObjects) {
        // TODO Auto-generated constructor stub
        shops = shopObjects;
        context = mainActivity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return shops.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View rowView;
        rowView = inflater.inflate(R.layout.bookmarked_card_tile, null);
        CardView cardTile = (CardView)rowView.findViewById(R.id.cardTile);
        cardTile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.CurrentShopId = String.valueOf(shops.get(position).getShopId());
                Intent intent = new Intent(context, CardDetailActivity.class);
                context.startActivity(intent);
            }
        });
        TextView t1 = (TextView)rowView.findViewById(R.id.textView1);
        TextView t2 = (TextView)rowView.findViewById(R.id.textView2);
        t1.setText(shops.get(position).getShopName());
        t2.setText(shops.get(position).getOffer());
        ImageView i1 = (ImageView)rowView.findViewById(R.id.imageView);
        try
        {
            // get input stream
            InputStream ims = context.getAssets().open(shops.get(position).getImageUrl());
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            i1.setImageDrawable(d);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

//        final RadioButton likedRadioButton = (RadioButton)rowView.findViewById(R.id.radio_mainCard_like);
//        likedRadioButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ShopObject sh = MainActivity.Shops.get(position);
//                likedRadioButton.setChecked(!likedRadioButton.isChecked());
//                sh.setLiked(likedRadioButton.isChecked());
//                if(likedRadioButton.isChecked())
//                {
//                    MainActivity.MyBookMarkedOffers.add(sh);
//                }
//                else
//                {
//                    MainActivity.MyBookMarkedOffers.remove(sh);
//                }
//            }
//        });

//        holder.tv=(TextView) rowView.findViewById(R.id.textView1);
//        holder.img=(ImageView) rowView.findViewById(R.id.imageView1);
//        holder.tv.setText(result[position]);
//        holder.img.setImageResource(imageId[position]);
//        rowView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Toast.makeText(context, "You Clicked "+result[position], Toast.LENGTH_LONG).show();
//            }
//        });
        return rowView;
    }

}
