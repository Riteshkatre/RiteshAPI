package com.example.riteshapi.Catelog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riteshapi.NetworkResponce.CatalogListResponse;
import com.example.riteshapi.R;

import java.util.List;

public class CatalogSubCategoryAdapter extends RecyclerView.Adapter<CatalogSubCategoryAdapter.CatalogSubCategoryViewHolder> {

    List<CatalogListResponse.Category.SubCategory> subCategoryList;
    Context context;

    public CatalogSubCategoryAdapter(List<CatalogListResponse.Category.SubCategory> subCategoryList, Context context) {
        this.subCategoryList = subCategoryList;
        this.context = context;

        for (int i=0;i<subCategoryList.size();i++){
            subCategoryList.get(i).setSelect(false);
        }
    }

    @NonNull
    @Override
    public CatalogSubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_catalog_sub_category,parent,false);
        return new CatalogSubCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatalogSubCategoryViewHolder holder, int position) {
        holder.tvSubCategoryName.setText(subCategoryList.get(position).getSubcategoryName());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        CatalogProductAdapter catalogProductAdapter = new CatalogProductAdapter(subCategoryList.get(position).getProductList(),context);
        holder.recyclerViewCatalogProduct.setLayoutManager(layoutManager);
        holder.recyclerViewCatalogProduct.setAdapter(catalogProductAdapter);


        if (subCategoryList.get(position).getSelect()){
            holder.recyclerViewCatalogProduct.setVisibility(View.GONE);
        }else {
            holder.recyclerViewCatalogProduct.setVisibility(View.VISIBLE);
        }

        holder.ivDropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subCategoryList.get(position).getSelect() == false){
                    subCategoryList.get(position).setSelect(true);
                    notifyDataSetChanged();
                }
                else {
                    subCategoryList.get(position).setSelect(false);
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return subCategoryList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class CatalogSubCategoryViewHolder extends RecyclerView.ViewHolder {

        TextView tvSubCategoryName;
        ImageView ivDropDown;
        RecyclerView recyclerViewCatalogProduct;
        public CatalogSubCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubCategoryName = itemView.findViewById(R.id.tvSubCategoryName);
            ivDropDown = itemView.findViewById(R.id.ivDropDown);
            recyclerViewCatalogProduct = itemView.findViewById(R.id.recyclerViewCatalogProduct);
        }
    }
}
