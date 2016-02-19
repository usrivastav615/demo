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

public class LikedCardAdapter extends BaseAdapter{

    ArrayList<ShopObject> shops = new ArrayList<ShopObject>();
    Context context;
    private static LayoutInflater inflater=null;
    public LikedCardAdapter(Activity mainActivity, ArrayList<ShopObject> shopObjects) {
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
        rowView = inflater.inflate(R.layout.liked_card_tile, null);
        TextView t1 = (TextView)rowView.findViewById(R.id.textView_likedCard);
        ShopObject shop = MainActivity.Shops.get(position);
        String sourceString = "You liked " + shop.getOffer() + " from " + shop.getShopName();
        t1.setText(sourceString);
        return rowView;
    }

}
