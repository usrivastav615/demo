package beacon.demo.com.demo2.smartbag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import beacon.demo.com.demo2.R;

public class sparkFunJSONParser {

	public static List<sparkFun> parseFeed(String content) {

		try {
			JSONArray ar = new JSONArray(content);
			List<sparkFun> itemsList = new ArrayList<>();

			for (int i = 0; i < ar.length(); i++) {

				JSONObject obj = ar.getJSONObject(i);

				sparkFun SparkFun = new sparkFun();

				SparkFun.setPhonenumber(obj.getString("phonenumber"));
				SparkFun.setProductname(obj.getString("productname"));
				SparkFun.setProductprice(obj.getString("productprice"));
				SparkFun.setKaand(obj.getString("kaand"));
				SparkFun.setTimestamp(obj.getString("timestamp"));
				SparkFun.setNoOfItems(ar.length());

				String product = obj.getString("productname").toLowerCase();
				
				
//				switch (product) {
//				case  product.contains("oreo"):

//				}
				
				if (product.contains("arrow")){
					SparkFun.setProductname("Arrow T-shirt");
					SparkFun.setIconID(R.drawable.arrow);
				}
				if (product.contains("jam")){
					SparkFun.setProductname("Jam Summer Wear");
					SparkFun.setIconID(R.drawable.jam);
				}
				if (product.contains("sauce")){
					SparkFun.setProductname("Denim Jeans");
					SparkFun.setIconID(R.drawable.jeans);
				}
				if (product.contains("pil")){
					SparkFun.setIconID(R.drawable.icon_navigation);
				}
				if (product.contains("milk")){
					SparkFun.setProductname("Peter England Black Shirt");
					SparkFun.setIconID(R.drawable.milk);
				}
				if (product.contains("horlic")){
					SparkFun.setIconID(R.drawable.icon_navigation);
				}
				if (product.contains("invalid")){
					SparkFun.setIconID(R.drawable.icon_navigation);
				}
//


				itemsList.add(SparkFun);

			}
			
			
			

			return itemsList;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

	}
}
