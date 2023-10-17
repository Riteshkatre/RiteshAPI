package com.example.riteshapi.Catelog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riteshapi.NetworkResponce.ProductListResponce;
import com.example.riteshapi.NetworkResponce.SubCategoryListResponce;
import com.example.riteshapi.R;

import java.util.List;

public class CatelogSubCategoryAdapter extends RecyclerView.Adapter<CatelogSubCategoryAdapter.CatelogSubCategoryViewHolder>{
    List<SubCategoryListResponce.SubCategory>subCategoryList;
    Context context;
    List<ProductListResponce.Product> productListResponce;


    public CatelogSubCategoryAdapter(List<SubCategoryListResponce.SubCategory> subCategoryList, Context context) {
        this.subCategoryList = subCategoryList;
        this.context = context;
    }

    CatelogProductAdapter catelogProductAdapter;


    @NonNull
    @Override
    public CatelogSubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.catelog_sabcategory_itemfile,parent,false);
        return new CatelogSubCategoryAdapter.CatelogSubCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatelogSubCategoryViewHolder holder, int position) {
        SubCategoryListResponce.SubCategory subCategory=subCategoryList.get(position);
        holder.catelogsubcategory.setText(subCategory.getSubcategoryName());


        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(holder.rcvcatelogproductlist.getContext());
        holder.rcvcatelogproductlist.setLayoutManager(layoutManager);

        CatelogProductAdapter catelogProductAdapter = new CatelogProductAdapter(productListResponce, context);
        holder.rcvcatelogproductlist.setAdapter(catelogProductAdapter);

    }

    @Override
    public int getItemCount() {

        return subCategoryList.size();
    }

    public class CatelogSubCategoryViewHolder extends RecyclerView.ViewHolder {
        TextView catelogsubcategory;
        RecyclerView rcvcatelogproductlist;
        public CatelogSubCategoryViewHolder(@NonNull View itemView) {

            super(itemView);
            catelogsubcategory=itemView.findViewById(R.id.catelogsubcategory);
            rcvcatelogproductlist=itemView.findViewById(R.id.rcvcatelogproductlist);
        }
    }
}
