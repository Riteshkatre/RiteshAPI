package com.example.riteshapi.NetworkResponce;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CatalogueListResponse {


    public final static Parcelable.Creator<CatalogueListResponse> CREATOR = new Parcelable.Creator<CatalogueListResponse>() {


        public CatalogueListResponse createFromParcel(android.os.Parcel in) {
            return new CatalogueListResponse(in);
        }

        public CatalogueListResponse[] newArray(int size) {
            return (new CatalogueListResponse[size]);
        }

    };
    private final static long serialVersionUID = -3278339294232747719L;
    @SerializedName("categoryList")
    @Expose
    private List<CategoryListResponce> categoryList;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    protected CatalogueListResponse(android.os.Parcel in) {
        in.readList(this.categoryList, (CategoryListResponce.class.getClassLoader()));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
    }

    public CatalogueListResponse() {
    }

    public CatalogueListResponse(List<CategoryListResponce> categoryList, String message, String status) {
        super();
        this.categoryList = categoryList;
        this.message = message;
        this.status = status;
    }

    public List<CategoryListResponce> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<CategoryListResponce> categoryList) {
        this.categoryList = categoryList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeList(categoryList);
        dest.writeValue(message);
        dest.writeValue(status);
    }

    public int describeContents() {
        return 0;
    }

}

