package beacon.demo.com.demo2;

import android.app.ActionBar;
import android.content.res.AssetManager;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener, OnFragmentInteractionListener {
    public static HashMap<String, ShopObject> ShopsCollection;
    public static ArrayList<ShopObject> Shops;
    public static ArrayList<ShopObject> MyBookMarkedOffers;
    public static ArrayList<ShopObject> MyLikedOffers;
    public static String CurrentShopId = "1";
    ActionBar actionBar = null;
    ViewPager mainViewPager = null;
    private Toolbar mActionBarToolbar;
    private TabLayout tabLayout;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mainViewPager = (ViewPager) findViewById(R.id.mainActivityViewPager);
//        mainViewPager.setAdapter(new MainActivityAdapter(getSupportFragmentManager()));
        setupViewPager(mainViewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mainViewPager);
        mainViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //actionBar.setSelectedNavigationItem(position);
                synchronized (BookmarkedCardsFragment.bookMarkedCardAdapter) {
                    BookmarkedCardsFragment.bookMarkedCardAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        getAllCards();
        copyAssets();
    }

    private void copyAssets() {
        AssetManager assetManager = getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        if (files != null) for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(filename);
                File outFile = new File(Environment.getExternalStorageDirectory().toString(), filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
            } catch(IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            }
            finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
            }
        }
    }
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new AllCardsFragment(), "ALL");
        adapter.addFrag(new BookmarkedCardsFragment(), "BOOKMARKED");
        adapter.addFrag(new AllCardsFragment(), "LIKED");
        viewPager.setAdapter(adapter);
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

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
