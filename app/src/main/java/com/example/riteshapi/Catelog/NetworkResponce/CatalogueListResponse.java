package com.example.riteshapi.Catelog.NetworkResponce;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CatalogueListResponse implements Serializable, Parcelable {

    public final static Creator<CatalogueListResponse> CREATOR = new Creator<CatalogueListResponse>() {


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
    private List<CategoryList> categoryList;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    protected CatalogueListResponse(android.os.Parcel in) {
        in.readList(this.categoryList, (CategoryList.class.getClassLoader()));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
    }

    public CatalogueListResponse() {
    }

    public CatalogueListResponse(List<CategoryList> categoryList, String message, String status) {
        super();
        this.categoryList = categoryList;
        this.message = message;
        this.status = status;
    }

    public List<CategoryList> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<CategoryList> categoryList) {
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

/*
package com.example.riteshapi.NetworkResponce;

        import java.io.Serializable;
        import javax.annotation.Generated;
        import android.os.Parcelable;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class CatelogResponce implements Serializable, Parcelable
{

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    public final static Creator<CatelogResponce> CREATOR = new Creator<CatelogResponce>() {


        public CatelogResponce createFromParcel(android.os.Parcel in) {
            return new CatelogResponce(in);
        }

        public CatelogResponce[] newArray(int size) {
            return (new CatelogResponce[size]);
        }

    }
            ;
    private final static long serialVersionUID = 4296548483213527588L;

    @SuppressWarnings({
            "unchecked"
    })
    protected CatelogResponce(android.os.Parcel in) {
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
    }

    public CatelogResponce() {
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
        dest.writeValue(message);
        dest.writeValue(status);
    }

    public int describeContents() {
        return  0;
    }

}

*/

