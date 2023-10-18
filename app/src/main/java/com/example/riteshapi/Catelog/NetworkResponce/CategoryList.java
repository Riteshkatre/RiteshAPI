package com.example.riteshapi.Catelog.NetworkResponce;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CategoryList implements Serializable, Parcelable {

    public final static Creator<CategoryList> CREATOR = new Creator<CategoryList>() {


        public CategoryList createFromParcel(android.os.Parcel in) {
            return new CategoryList(in);
        }

        public CategoryList[] newArray(int size) {
            return (new CategoryList[size]);
        }

    };
    private final static long serialVersionUID = 9159007140622628192L;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("category_status")
    @Expose
    private String categoryStatus;
    @SerializedName("sub_category_list")
    @Expose
    private List<SubCategoryList> subCategoryList;

    protected CategoryList(android.os.Parcel in) {
        this.categoryId = ((String) in.readValue((String.class.getClassLoader())));
        this.categoryName = ((String) in.readValue((String.class.getClassLoader())));
        this.categoryStatus = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.subCategoryList, (SubCategoryList.class.getClassLoader()));
    }

    public CategoryList() {
    }

    public CategoryList(String categoryId, String categoryName, String categoryStatus, List<SubCategoryList> subCategoryList) {
        super();
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryStatus = categoryStatus;
        this.subCategoryList = subCategoryList;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryStatus() {
        return categoryStatus;
    }

    public void setCategoryStatus(String categoryStatus) {
        this.categoryStatus = categoryStatus;
    }

    public List<SubCategoryList> getSubCategoryList() {
        return subCategoryList;
    }

    public void setSubCategoryList(List<SubCategoryList> subCategoryList) {
        this.subCategoryList = subCategoryList;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(categoryId);
        dest.writeValue(categoryName);
        dest.writeValue(categoryStatus);
        dest.writeList(subCategoryList);
    }

    public int describeContents() {
        return 0;
    }

}
