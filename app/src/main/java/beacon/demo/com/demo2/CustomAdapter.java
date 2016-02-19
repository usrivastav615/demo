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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter{

    ArrayList<ShopObject> shops = new ArrayList<ShopObject>();
    Context context;
    private static LayoutInflater inflater=null;
    public CustomAdapter(Activity mainActivity, ArrayList<ShopObject> shopObjects) {
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
        rowView = inflater.inflate(R.layout.shopping_tile, null);
        CardView cardTile = (CardView)rowView.findViewById(R.id.cardTile);
        cardTile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.CurrentShopId = String.valueOf(shops.get(position).getShopId());
                Intent intent = new Intent(context, CardDetailActivity.class);
                context.startActivity(intent);
            }
        });
        TextView t1 = (TextView)rowView.findViewById(R.id.mainCard_shopName);
        TextView t2 = (TextView)rowView.findViewById(R.id.mainCard_Offer);
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

        final ImageButton likedRadioButton = (ImageButton)rowView.findViewById(R.id.mainCard_like_button);
        likedRadioButton.setSelected(shops.get(position).isLiked());
        if(likedRadioButton.isSelected()) {
            likedRadioButton.setImageDrawable(context.getResources().getDrawable(R.drawable.heart_checked));
            likedRadioButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
        else
        {
            likedRadioButton.setImageDrawable(context.getResources().getDrawable(R.drawable.heart_unchecked));
            likedRadioButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
        likedRadioButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopObject sh = MainActivity.Shops.get(position);
                likedRadioButton.setSelected(!likedRadioButton.isSelected());
                sh.setLiked(likedRadioButton.isSelected());
                if(likedRadioButton.isSelected())
                {
                    MainActivity.MyLikedOffers.add(0, sh);
                    likedRadioButton.setImageDrawable(context.getResources().getDrawable(R.drawable.heart_checked));
                    likedRadioButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                }
                else
                {
                    likedRadioButton.setImageDrawable(context.getResources().getDrawable(R.drawable.heart_unchecked));
                    likedRadioButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                }
            }
        });

        final ImageButton bookMarkedRadioButton = (ImageButton)rowView.findViewById(R.id.mainCard_bookmark_button);
        bookMarkedRadioButton.setSelected(shops.get(position).isBookmarked());
        if(bookMarkedRadioButton.isSelected())
        {
            bookMarkedRadioButton.setImageDrawable(context.getResources().getDrawable(R.drawable.bookmark_checked));
            bookMarkedRadioButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
        else
        {
            bookMarkedRadioButton.setImageDrawable(context.getResources().getDrawable(R.drawable.bookmark_unchecked));
            bookMarkedRadioButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
        bookMarkedRadioButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopObject sh = MainActivity.Shops.get(position);
                bookMarkedRadioButton.setSelected(!bookMarkedRadioButton.isSelected());
                sh.setBookmarked(bookMarkedRadioButton.isSelected());
                if(bookMarkedRadioButton.isSelected())
                {
                    MainActivity.MyBookMarkedOffers.add(sh);
                    bookMarkedRadioButton.setImageDrawable(context.getResources().getDrawable(R.drawable.bookmark_checked));
                }
                else
                {
                    MainActivity.MyBookMarkedOffers.remove(sh);
                    bookMarkedRadioButton.setImageDrawable(context.getResources().getDrawable(R.drawable.bookmark_unchecked));
                }
                notifyDataSetChanged();
                bookMarkedRadioButton.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            }
        });
        return rowView;
    }

}
