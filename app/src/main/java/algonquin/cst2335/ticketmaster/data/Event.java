package algonquin.cst2335.ticketmaster.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Event {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    public String id;

    @ColumnInfo(name = "name")
    protected String name;

    @ColumnInfo(name = "venue")
    protected String venue;

    @ColumnInfo(name = "date")
    protected String date;

    @ColumnInfo(name = "address")
    protected String address;

    @ColumnInfo(name = "postalCode")
    protected String postalCode;

    @ColumnInfo(name = "city")
    protected String city;

    @ColumnInfo(name = "state")
    protected String state;

    @ColumnInfo(name = "country")
    protected String country;

    @ColumnInfo(name = "buyUrl")
    protected String buyUrl;

    @ColumnInfo(name = "imgUrl")
    protected String imgUrl;

    @ColumnInfo(name = "minPrice")
    protected String minPrice;

    @ColumnInfo(name = "maxPrice")
    protected String maxPrice;

    @ColumnInfo(name = "currency")
    protected String currency;

    @ColumnInfo(name = "isSaved")
    protected boolean isSaved;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getVenue() {
        return venue;
    }

    public String getDate() {
        return date;
    }

    public String getAddress() {
        return address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getBuyUrl() {
        return buyUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public String getCurrency() {
        return currency;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setSaved(boolean saved) {
        isSaved = saved;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setBuyUrl(String buyUrl) {
        this.buyUrl = buyUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
