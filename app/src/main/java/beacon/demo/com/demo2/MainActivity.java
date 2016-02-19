package beacon.demo.com.demo2;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    ListView l ;
    public static HashMap<String,ShopObject> ShopsCollection;
    public static ArrayList<ShopObject> Shops;
    public static String CurrentShopId = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        l = (ListView)findViewById(R.id.listView1);
        getAllCards();
        l.setAdapter(new CustomAdapter(this, MainActivity.Shops));

    }

    public void getAllCards()
    {
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("shopdetails");
            ShopsCollection = new HashMap<String, ShopObject>();
            Shops = new ArrayList<ShopObject>();
            HashMap<String, String> m_li;

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                String id = jo_inside.getString("id");
                String type = jo_inside.getString("type");

                String shopName = jo_inside.getString("name");
                String offer = jo_inside.getString("offer");

                String shopDetails = jo_inside.getString("details");
                String imageUrl = jo_inside.getString("imageUrl");

                boolean liked = jo_inside.getBoolean("liked");
                boolean bookmarked = jo_inside.getBoolean("bookmarked");

                ShopObject shopObject = new ShopObject(id, type, shopName,offer, shopDetails, imageUrl, liked, bookmarked);

                Shops.add(shopObject);
                ShopsCollection.put(id, shopObject);
            }
        }  catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("tilejson.txt");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
