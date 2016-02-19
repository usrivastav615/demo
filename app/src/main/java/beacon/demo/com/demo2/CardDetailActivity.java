package beacon.demo.com.demo2;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

public class CardDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String fileName = MainActivity.ShopsCollection.get(MainActivity.CurrentShopId).getImageUrl();//Name of an image
                String externalStorageDirectory = Environment.getExternalStorageDirectory().toString();
                String myDir = externalStorageDirectory + ""; // the file will be in saved_images
                Uri uri = Uri.parse("file:///" + myDir + "/" + fileName);

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, MainActivity.ShopsCollection.get(MainActivity.CurrentShopId).getOffer());
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                shareIntent.setType("image/jpeg");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(shareIntent, "send"));
            }
        });

        ImageView shopImage = (ImageView)findViewById(R.id.imageView2);
        try
        {
            // get input stream
            InputStream ims = getApplicationContext().getAssets().open(MainActivity.ShopsCollection.get(MainActivity.CurrentShopId).getImageUrl());
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            shopImage.setImageDrawable(d);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        TextView shopName = (TextView)findViewById(R.id.shopDetail_shopName);
        shopName.setText(MainActivity.ShopsCollection.get(MainActivity.CurrentShopId).getShopName());
        TextView offerDetail = (TextView)findViewById(R.id.shopDetail_offerDetail);
        offerDetail.setText(MainActivity.ShopsCollection.get(MainActivity.CurrentShopId).getOffer());

    }

}


