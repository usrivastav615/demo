package beacon.demo.com.demo2;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
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
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import beacon.demo.com.demo2.smartbag.AllCartsFragment;
import beacon.demo.com.demo2.smartbag.GetCarts;
import beacon.demo.com.demo2.smartbag.sparkFun;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener, OnFragmentInteractionListener, AdapterView.OnItemClickListener, Toolbar.OnMenuItemClickListener {
    public static HashMap<String, ShopObject> ShopsCollection;
    public static ArrayList<ShopObject> Shops;
    public static ArrayList<ShopObject> MyBookMarkedOffers;
    public static ArrayList<ShopObject> MyLikedOffers;
    public static ArrayList<sparkFun> Carts;
    public static String CurrentShopId = "1";
    private static boolean fl  = false;
    ActionBar actionBar = null;
    ViewPager mainViewPager = null;
    private Toolbar mActionBarToolbar;
    private TabLayout tabLayout;
    private android.support.v4.widget.DrawerLayout mDrawerLayout;
    private BluetoothAdapter bTAdapter;
    public static ListView drawerList;

    private BluetoothAdapter mBluetoothAdapter;
    private int REQUEST_ENABLE_BT = 1;
    private Handler mHandler;
    private static final long SCAN_PERIOD = 100000;
    private BluetoothLeScanner mLEScanner;
    private ScanSettings settings;
    private List<ScanFilter> filters;
    private BluetoothGatt mGatt;
    private GetCarts mGetCarts;
    public static int TotalPrice = 0;

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
                if (BookmarkedCardsFragment.bookMarkedCardAdapter != null) {
                    synchronized (BookmarkedCardsFragment.bookMarkedCardAdapter) {
                        BookmarkedCardsFragment.bookMarkedCardAdapter.notifyDataSetChanged();
                        if (MainActivity.MyBookMarkedOffers.size() > 0) {
                            BookmarkedCardsFragment.textView.setVisibility(View.GONE);
                        } else {
                            BookmarkedCardsFragment.textView.setVisibility(View.VISIBLE);
                        }
                    }
                }

                if (LikedCardsFragment.likedCardAdapter != null) {
                    synchronized (LikedCardsFragment.likedCardAdapter) {
                        LikedCardsFragment.likedCardAdapter.notifyDataSetChanged();
                        if (MainActivity.MyLikedOffers.size() > 0) {
                            LikedCardsFragment.textView.setVisibility(View.GONE);
                        } else {
                            LikedCardsFragment.textView.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //getAllCards();
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
            actionbar.setOnMenuItemClickListener(this);
        }

        mHandler = new Handler();
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE Not Supported",
                    Toast.LENGTH_SHORT).show();
            finish();
        }
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        drawerList = (ListView)findViewById(R.id.list_slidermenu);
        drawerList.setOnItemClickListener(this);

        ShopsCollection = new HashMap<String, ShopObject>();
        Shops = new ArrayList<ShopObject>();
        MyBookMarkedOffers = new ArrayList<ShopObject>();
        MyLikedOffers = new ArrayList<ShopObject>();
        Carts = new ArrayList<sparkFun>();
        new HttpAsyncTask().execute("http://52.87.223.104:8000/polls/");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            if (Build.VERSION.SDK_INT >= 21) {
                mLEScanner = mBluetoothAdapter.getBluetoothLeScanner();
                settings = new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                        .build();
                filters = new ArrayList<ScanFilter>();
            }
            scanLeDevice(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()) {
            scanLeDevice(false);
        }
    }

    @Override
    protected void onDestroy() {
        if (mGatt == null) {
            return;
        }
        mGatt.close();
        mGatt = null;
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_CANCELED) {
                //Bluetooth not enabled.
                finish();
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (Build.VERSION.SDK_INT < 21) {
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    } else {
                        mLEScanner.stopScan(mScanCallback);

                    }
                }
            }, SCAN_PERIOD);
            if (Build.VERSION.SDK_INT < 21) {
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            } else {
                mLEScanner.startScan(filters, settings, mScanCallback);
            }
        } else {
            if (Build.VERSION.SDK_INT < 21) {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            } else {
                mLEScanner.stopScan(mScanCallback);
            }
        }
    }


    boolean flag =true;

    private Notification myNotication;
    private NotificationManager manager;
    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            Log.i("callbackType", String.valueOf(callbackType));
            Log.i("result", result.toString());
            if(!flag)
                return;
            flag = false;
            Intent resultIntent = new Intent(MainActivity.this, CardDetailActivity.class);
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 1, resultIntent, 0);

            Notification.Builder builder = new Notification.Builder(MainActivity.this);

            builder.setAutoCancel(false);
            builder.setTicker("this is ticker text");
            builder.setContentTitle("Shoppers stop" + " Notification");
            builder.setContentText("Buy 1, Get 1 Free");
            builder.setSmallIcon(R.drawable.heart_checked);
            builder.setContentIntent(pendingIntent);
            builder.setOngoing(true);
            builder.setNumber(100);
            builder.build();

            myNotication = builder.getNotification();
            myNotication.flags = Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS;
            manager.notify(11, myNotication);

        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            for (ScanResult sr : results) {
                Log.i("ScanResult - Results", sr.toString());
            }
        }

        @Override
        public void onScanFailed(int errorCode) {
            Log.e("Scan Failed", "Error Code: " + errorCode);
        }
    };

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("onLeScan", device.toString());
                            connectToDevice(device);
                        }
                    });
                }
            };

    public void connectToDevice(BluetoothDevice device) {
//        if (mGatt == null) {
//            mGatt = device.connectGatt(this, false, gattCallback);
//            scanLeDevice(false);// will stop after first device detection
//        }
        Log.d("abc", "def");
    }

    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            Log.i("onConnectionStateChange", "Status: " + status);
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    Log.i("gattCallback", "STATE_CONNECTED");
                    gatt.discoverServices();
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    Log.e("gattCallback", "STATE_DISCONNECTED");
                    break;
                default:
                    Log.e("gattCallback", "STATE_OTHER");
            }

        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            List<BluetoothGattService> services = gatt.getServices();
            Log.i("onServicesDiscovered", services.toString());
            gatt.readCharacteristic(services.get(1).getCharacteristics().get
                    (0));
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic
                                                 characteristic, int status) {
            Log.i("onCharacteristicRead", characteristic.toString());
            gatt.disconnect();
        }
    };

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
        adapter.addFrag(new AllCartsFragment(), "MY CART");
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
                    MyLikedOffers.add(0, shopObject);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(position == 0)
        {
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
        }
        else if(position == 2)
        {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        if(item.getItemId() == R.id.action_settings1) {
            mGetCarts = new GetCarts(this, "http://52.91.164.220:8080/output/Aq3zOOAY0kSv2V9d4EOpIgyVMVv.json");
            mGetCarts.startQuery();
        }
        else
        {
            Intent intent = new Intent(MainActivity.this, endPage.class);
            startActivity(intent);
        }
        return false;
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



    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        try {
            JSONArray shopsJson = new JSONArray(result);

            for(int i = 0; i < shopsJson.length(); i++)
            {
                JSONObject obj = shopsJson.getJSONObject(i);
                JSONObject jo_inside = obj.getJSONObject("fields");

                String id = jo_inside.getString("shop_id");
                String type = jo_inside.getString("shop_type");

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
                    MyLikedOffers.add(0, shopObject);
                }
                fl = true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }


    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            fl = false;
            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            if(!fl)
            {
                getAllCards();
            }

            if(AllCardsFragment.allCardsAdapter != null)
            {
                AllCardsFragment.allCardsAdapter.notifyDataSetChanged();
            }
        }
    }
}
