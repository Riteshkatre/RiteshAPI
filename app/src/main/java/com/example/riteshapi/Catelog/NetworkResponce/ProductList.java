package com.example.riteshapi.Catelog.NetworkResponce;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProductList implements Serializable, Parcelable {

    public final static Creator<ProductList> CREATOR = new Creator<ProductList>() {


        public ProductList createFromParcel(android.os.Parcel in) {
            return new ProductList(in);
        }

        public ProductList[] newArray(int size) {
            return (new ProductList[size]);
        }

    };
    private final static long serialVersionUID = -7125278815351076333L;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("product_image")
    @Expose
    private String productImage;
    @SerializedName("old_product_image")
    @Expose
    private String oldProductImage;
    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("product_desc")
    @Expose
    private String productDesc;
    @SerializedName("is_veg")
    @Expose
    private String isVeg;
    @SerializedName("product_total_rating")
    @Expose
    private String productTotalRating;
    @SerializedName("product_average_rating")
    @Expose
    private String productAverageRating;

    protected ProductList(android.os.Parcel in) {
        this.productId = ((String) in.readValue((String.class.getClassLoader())));
        this.productName = ((String) in.readValue((String.class.getClassLoader())));
        this.productImage = ((String) in.readValue((String.class.getClassLoader())));
        this.oldProductImage = ((String) in.readValue((String.class.getClassLoader())));
        this.productPrice = ((String) in.readValue((String.class.getClassLoader())));
        this.productDesc = ((String) in.readValue((String.class.getClassLoader())));
        this.isVeg = ((String) in.readValue((String.class.getClassLoader())));
        this.productTotalRating = ((String) in.readValue((String.class.getClassLoader())));
        this.productAverageRating = ((String) in.readValue((String.class.getClassLoader())));
    }

    public ProductList() {
    }

    public ProductList(String productId, String productName, String productImage, String oldProductImage, String productPrice, String productDesc, String isVeg, String productTotalRating, String productAverageRating) {
        super();
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.oldProductImage = oldProductImage;
        this.productPrice = productPrice;
        this.productDesc = productDesc;
        this.isVeg = isVeg;
        this.productTotalRating = productTotalRating;
        this.productAverageRating = productAverageRating;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getOldProductImage() {
        return oldProductImage;
    }

    public void setOldProductImage(String oldProductImage) {
        this.oldProductImage = oldProductImage;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getIsVeg() {
        return isVeg;
    }

    public void setIsVeg(String isVeg) {
        this.isVeg = isVeg;
    }

    public String getProductTotalRating() {
        return productTotalRating;
    }

    public void setProductTotalRating(String productTotalRating) {
        this.productTotalRating = productTotalRating;
    }

    public String getProductAverageRating() {
        return productAverageRating;
    }

    public void setProductAverageRating(String productAverageRating) {
        this.productAverageRating = productAverageRating;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(productId);
        dest.writeValue(productName);
        dest.writeValue(productImage);
        dest.writeValue(oldProductImage);
        dest.writeValue(productPrice);
        dest.writeValue(productDesc);
        dest.writeValue(isVeg);
        dest.writeValue(productTotalRating);
        dest.writeValue(productAverageRating);
    }

    public int describeContents() {
        return 0;
    }

}
