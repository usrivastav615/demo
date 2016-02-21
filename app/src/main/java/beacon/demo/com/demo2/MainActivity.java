package beacon.demo.com.demo2;

import android.app.ActionBar;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

public class MainActivity extends FragmentActivity implements ActionBar.TabListener, OnFragmentInteractionListener, AdapterView.OnItemClickListener {
    public static HashMap<String, ShopObject> ShopsCollection;
    public static ArrayList<ShopObject> Shops;
    public static ArrayList<ShopObject> MyBookMarkedOffers;
    public static ArrayList<ShopObject> MyLikedOffers;
    public static String CurrentShopId = "1";
    ActionBar actionBar = null;
    ViewPager mainViewPager = null;
    private Toolbar mActionBarToolbar;
    private TabLayout tabLayout;
    private android.support.v4.widget.DrawerLayout mDrawerLayout;
    private BluetoothAdapter bTAdapter;
    public static ListView drawerList;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        SignInActivity.getMainActivityObject(this);
        mainViewPager = (ViewPager) findViewById(R.id.mainActivityViewPager);
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
                if(BookmarkedCardsFragment.bookMarkedCardAdapter != null) {
                    synchronized (BookmarkedCardsFragment.bookMarkedCardAdapter) {
                        BookmarkedCardsFragment.bookMarkedCardAdapter.notifyDataSetChanged();
                        if(MainActivity.MyBookMarkedOffers.size() > 0)
                        {
                            BookmarkedCardsFragment.textView.setVisibility(View.GONE);
                        }
                        else
                        {
                            BookmarkedCardsFragment.textView.setVisibility(View.VISIBLE);
                        }
                    }
                }

                if(LikedCardsFragment.likedCardAdapter != null) {
                    synchronized (LikedCardsFragment.likedCardAdapter) {
                        LikedCardsFragment.likedCardAdapter.notifyDataSetChanged();
                        if(MainActivity.MyLikedOffers.size() > 0)
                        {
                            LikedCardsFragment.textView.setVisibility(View.GONE);
                        }
                        else
                        {
                            LikedCardsFragment.textView.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        getAllCards();
        copyAssets();

        Toolbar actionbar = (Toolbar) findViewById(R.id.toolbar);
        if (null != actionbar) {
            actionbar.setNavigationIcon(R.drawable.icon_navigation);

            actionbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   mDrawerLayout.openDrawer(Gravity.LEFT);
                }
            });

            // Inflate a menu to be displayed in the toolbar
            actionbar.inflateMenu(R.menu.menu_main);
        }

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(bReciever, filter);
        bTAdapter = BluetoothAdapter.getDefaultAdapter();

        drawerList = (ListView)findViewById(R.id.list_slidermenu);
        drawerList.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bTAdapter.startDiscovery();
    }

    private final BroadcastReceiver bReciever = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Create a new device item
                //DeviceItem newDevice = new DeviceItem(device.getName(), device.getAddress(), "false");
                // Add it to our adapter
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        bTAdapter.cancelDiscovery();
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
        adapter.addFrag(new LikedCardsFragment(), "LIKED");
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
                String detailedOffer = jo_inside.getString("full_detail");

                boolean liked = jo_inside.getBoolean("liked");
                boolean bookmarked = jo_inside.getBoolean("bookmarked");

                ShopObject shopObject = new ShopObject(id, type, shopName, offer, shopDetails, imageUrl, detailedOffer, liked, bookmarked);

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
            mDrawerLayout.openDrawer(Gravity.LEFT);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0)
        {
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
        }
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
