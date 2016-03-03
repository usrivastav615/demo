package beacon.demo.com.demo2.smartbag;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import beacon.demo.com.demo2.AllCardsFragment;
import beacon.demo.com.demo2.MainActivity;

/**
 * Created by utsrivas on 3/4/2016.
 */
public class GetCarts {

    MainActivity mainActivity = null;
    String url = "";
    private List<sparkFun> itemsList;

    public GetCarts(MainActivity mainActivity, String url)
    {
        this.mainActivity = mainActivity;
        this.url = url;
    }

    private boolean query = false;

    public void startQuery() {

        final Handler h = new Handler();
        final int delay = 5000; //milliseconds

        h.postDelayed(new Runnable() {
            public void run() {
                Log.d("tagname", "in start query");
                query = true;
                Toast.makeText(mainActivity, "Starting query", Toast.LENGTH_SHORT)
                        .show();

                getData(url);
                h.postDelayed(this, delay);
            }
        }, delay);


    }

    public void getData(String uri) {

        if (isOnline()) {
            MyTask task = new MyTask();
            task.execute(uri);
        } else {
            Toast.makeText(mainActivity, "net connect kar le bhai", Toast.LENGTH_LONG)
                    .show();

        }

    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public class MyTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

            // pb.setVisibility(View.VISIBLE);

        }

        @Override
        protected String doInBackground(String... params) {

            //String response = HttpManager.getData(params[0]);
            String response = loadJSONFromAsset();
            return response;

        }

        @Override
        protected void onPostExecute(String result) {

            if (result == null) {
                getData(url);

            }
            if (result != null) {
                itemsList = sparkFunJSONParser.parseFeed(result);
                MainActivity.Carts.clear();
                MainActivity.TotalPrice = 0;
                for(int i = 0 ; i < itemsList.size(); i++)
                {
                    MainActivity.Carts.add(itemsList.get(i));
                    MainActivity.TotalPrice += atoi(itemsList.get(i).getProductprice());
                }
                if(AllCartsFragment.allCartsFragment != null) {
                    AllCartsFragment.allCartsFragment.notifyDataSetChanged();
                }
            }

        }

        public int atoi(String str) {
            if (str == null || str.length() < 1)
                return 0;

            // trim white spaces
            str = str.trim();

            char flag = '+';

            // check negative or positive
            int i = 0;
            if (str.charAt(0) == '-') {
                flag = '-';
                i++;
            } else if (str.charAt(0) == '+') {
                i++;
            }
            // use double to store result
            double result = 0;

            // calculate value
            while (str.length() > i && str.charAt(i) >= '0' && str.charAt(i) <= '9') {
                result = result * 10 + (str.charAt(i) - '0');
                i++;
            }

            if (flag == '-')
                result = -result;

            // handle max and min
            if (result > Integer.MAX_VALUE)
                return Integer.MAX_VALUE;

            if (result < Integer.MIN_VALUE)
                return Integer.MIN_VALUE;

            return (int) result;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            // tvData.append(values[0]);
            // tvData.append("\n");
        }

        public String loadJSONFromAsset() {
            String json = null;
            try {
                InputStream is = mainActivity.getAssets().open("carts.txt");
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

    }
}
