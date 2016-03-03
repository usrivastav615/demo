package beacon.demo.com.demo2.smartbag;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import beacon.demo.com.demo2.CardDetailActivity;
import beacon.demo.com.demo2.MainActivity;
import beacon.demo.com.demo2.R;
import beacon.demo.com.demo2.ShopObject;

public class CartAdapter extends BaseAdapter{

    ArrayList<sparkFun> sparkFuns = new ArrayList<sparkFun>();
    Context context;
    private static LayoutInflater inflater=null;
    public CartAdapter(Activity mainActivity, ArrayList<sparkFun> sparkFuns) {
        // TODO Auto-generated constructor stub
        this.sparkFuns = sparkFuns;
        context = mainActivity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return sparkFuns.size();
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
        rowView = inflater.inflate(R.layout.cart_tile, null);
        CardView cardTile = (CardView)rowView.findViewById(R.id.cartTile);
        sparkFun s = sparkFuns.get(position);
        cardTile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                MainActivity.CurrentShopId = String.valueOf(sparkFuns.get(position).getShopId());
//                Intent intent = new Intent(context, CardDetailActivity.class);
//                context.startActivity(intent);
            }
        });

        TextView t1 = (TextView)rowView.findViewById(R.id.cartText1);
        t1.setText(s.getProductname());
        TextView t2 = (TextView)rowView.findViewById(R.id.cartText2);
        t2.setText("Rs. " + s.getProductprice());
        ImageView i1 = (ImageView)rowView.findViewById(R.id.cartImageView);
        i1.setImageResource(s.getIconID());
        return rowView;
    }

}
