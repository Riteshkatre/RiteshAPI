package com.example.riteshapi.Catelog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riteshapi.NetworkResponce.CatalogListResponse;
import com.example.riteshapi.R;

import java.util.List;

public class CatalogCategoryAdapter extends RecyclerView.Adapter<CatalogCategoryAdapter.CatalogCategoryViewHolder> {

    List<CatalogListResponse.Category> catalogListResponse;
    Context context;

    public CatalogCategoryAdapter(List<CatalogListResponse.Category> catalogListResponse, Context context) {
        this.catalogListResponse = catalogListResponse;
        this.context = context;
    }

    @NonNull
    @Override
    public CatalogCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_catalog_category,parent,false);
        return new CatalogCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatalogCategoryViewHolder holder, int position) {
        holder.tvCategoryName.setText(catalogListResponse.get(position).getCategoryName());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);


        CatalogSubCategoryAdapter catalogSubCategoryAdapter = new CatalogSubCategoryAdapter(catalogListResponse.get(position).getSubCategoryList(),context);
        holder.recyclerViewCatalogSubCategory.setLayoutManager(layoutManager);
        holder.recyclerViewCatalogSubCategory.setAdapter(catalogSubCategoryAdapter);
    }

    @Override
    public int getItemCount() {
        return catalogListResponse.size();
    }

    public static class CatalogCategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;
        RecyclerView recyclerViewCatalogSubCategory;
        public CatalogCategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerViewCatalogSubCategory = itemView.findViewById(R.id.recyclerViewCatalogSubCategory);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }
    }
}
