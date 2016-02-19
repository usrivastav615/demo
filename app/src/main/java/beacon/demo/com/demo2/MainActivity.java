package beacon.demo.com.demo2;

import android.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener, OnFragmentInteractionListener {
    public static HashMap<String, ShopObject> ShopsCollection;
    public static ArrayList<ShopObject> Shops;
    public static ArrayList<ShopObject> MyBookMarkedOffers;
    public static ArrayList<ShopObject> MyLikedOffers;
    public static String CurrentShopId = "1";
    ActionBar actionBar = null;
    ViewPager mainViewPager = null;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainViewPager = (ViewPager) findViewById(R.id.mainActivityViewPager);
        mainViewPager.setAdapter(new MainActivityAdapter(getSupportFragmentManager()));
        mainViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
                synchronized (BookmarkedCardsFragment.bookMarkedCardAdapter) {
                    BookmarkedCardsFragment.bookMarkedCardAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        getAllCards();
        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab tab1 = actionBar.newTab();
        tab1.setText("All");
        tab1.setTabListener(this);

        ActionBar.Tab tab2 = actionBar.newTab();
        tab2.setText("Bookmarked");
        tab2.setTabListener(this);

        ActionBar.Tab tab3 = actionBar.newTab();
        tab3.setText("Liked");
        tab3.setTabListener(this);

        actionBar.addTab(tab1);
        actionBar.addTab(tab2);
        actionBar.addTab(tab3);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }

    public void getAllCards() {
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("shopdetails");
            ShopsCollection = new HashMap<String, ShopObject>();
            Shops = new ArrayList<ShopObject>();
            MyBookMarkedOffers = new ArrayList<ShopObject>();
            MyLikedOffers = new ArrayList<ShopObject>();
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

                ShopObject shopObject = new ShopObject(id, type, shopName, offer, shopDetails, imageUrl, liked, bookmarked);

                Shops.add(shopObject);
                ShopsCollection.put(id, shopObject);
                if (bookmarked) {
                    MyBookMarkedOffers.add(shopObject);
                }
                if(liked)
                {
                    MyLikedOffers.add(0,shopObject);
                }
            }
        } catch (JSONException e) {
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

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {
        mainViewPager.setCurrentItem(tab.getPosition());
        synchronized (BookmarkedCardsFragment.bookMarkedCardAdapter) {
            BookmarkedCardsFragment.bookMarkedCardAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction ft) {

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    class MainActivityAdapter extends FragmentPagerAdapter {

        public MainActivityAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;
            if (position == 0) {
                fragment = new AllCardsFragment();
            } else if (position == 1) {
                fragment = new BookmarkedCardsFragment();
            } else if (position == 2) {
                fragment = new AllCardsFragment();
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
