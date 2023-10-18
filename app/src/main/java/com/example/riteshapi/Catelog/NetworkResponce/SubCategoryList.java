package com.example.riteshapi.Catelog.NetworkResponce;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SubCategoryList implements Serializable, Parcelable {

    public final static Creator<SubCategoryList> CREATOR = new Creator<SubCategoryList>() {


        public SubCategoryList createFromParcel(android.os.Parcel in) {
            return new SubCategoryList(in);
        }

        public SubCategoryList[] newArray(int size) {
            return (new SubCategoryList[size]);
        }

    };
    private final static long serialVersionUID = -5931960017667629601L;
    @SerializedName("sub_category_id")
    @Expose
    private String subCategoryId;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("subcategory_name")
    @Expose
    private String subcategoryName;
    @SerializedName("subcategory_status")
    @Expose
    private String subcategoryStatus;
    @SerializedName("product_list")
    @Expose
    private List<ProductList> productList;

    protected SubCategoryList(android.os.Parcel in) {
        this.subCategoryId = ((String) in.readValue((String.class.getClassLoader())));
        this.categoryId = ((String) in.readValue((String.class.getClassLoader())));
        this.subcategoryName = ((String) in.readValue((String.class.getClassLoader())));
        this.subcategoryStatus = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.productList, (ProductList.class.getClassLoader()));
    }

    public SubCategoryList() {
    }

    public SubCategoryList(String subCategoryId, String categoryId, String subcategoryName, String subcategoryStatus, List<ProductList> productList) {
        super();
        this.subCategoryId = subCategoryId;
        this.categoryId = categoryId;
        this.subcategoryName = subcategoryName;
        this.subcategoryStatus = subcategoryStatus;
        this.productList = productList;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSubcategoryName() {
        return subcategoryName;
    }

    public void setSubcategoryName(String subcategoryName) {
        this.subcategoryName = subcategoryName;
    }

    public String getSubcategoryStatus() {
        return subcategoryStatus;
    }

    public void setSubcategoryStatus(String subcategoryStatus) {
        this.subcategoryStatus = subcategoryStatus;
    }

    public List<ProductList> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductList> productList) {
        this.productList = productList;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(subCategoryId);
        dest.writeValue(categoryId);
        dest.writeValue(subcategoryName);
        dest.writeValue(subcategoryStatus);
        dest.writeList(productList);
    }

    public int describeContents() {
        return 0;
    }

}
