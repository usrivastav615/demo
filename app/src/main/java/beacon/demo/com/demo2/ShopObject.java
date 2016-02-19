package beacon.demo.com.demo2;

/**
 * Created by utsrivas on 2/19/2016.
 */
public class ShopObject {

    private String shopId;
    private String type;
    private String shopName;
    private String offer;
    private String details;
    private String imageUrl;
    private boolean liked;
    private boolean bookmarked;

    public ShopObject(String shopId, String type, String shopName, String offer, String details,
                      String imageUrl, boolean liked, boolean bookmarked) {
        this.shopId = shopId;
        this.type = type;
        this.shopName = shopName;
        this.offer = offer;
        this.details = details;
        this.imageUrl = imageUrl;
        this.liked = liked;
        this.bookmarked = bookmarked;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean isBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        this.bookmarked = bookmarked;
    }
}
