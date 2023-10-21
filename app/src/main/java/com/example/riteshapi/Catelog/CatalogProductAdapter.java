package com.example.riteshapi.Catelog;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.riteshapi.NetworkResponce.CatalogListResponse;
import com.example.riteshapi.R;

import java.util.List;

public class CatalogProductAdapter extends RecyclerView.Adapter<CatalogProductAdapter.CatalogProductViewHolder> {

    List<CatalogListResponse.Category.SubCategory.Product> productList;
    Context context;

    public CatalogProductAdapter(List<CatalogListResponse.Category.SubCategory.Product> productList, Context context) {
        this.productList = productList;
        this.context = context;
    }

    @NonNull
    @Override
    public CatalogProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_catalog_product,parent,false);
        return new CatalogProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatalogProductViewHolder holder, int position) {
        holder.tvProductName.setText(productList.get(position).getProductName());
        holder.tvProductPrice.setText(productList.get(position).getProductPrice());
        holder.tvProductDescription.setText(productList.get(position).getProductDesc());
        if (productList.get(position).getIsVeg().equals("1")){
            holder.ivSymbolIsVeg.setImageResource(R.drawable.ic_non_veg_symbol);
        }
        else {
            holder.ivSymbolIsVeg.setImageResource(R.drawable.ic_veg_symbol);
        }
        Glide
                .with(context)
                .load(productList.get(position).getProductImage())
                .placeholder(R.drawable.ic_place_holder)
                .into(holder.ivProduct);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    class CatalogProductViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProduct,ivSymbolIsVeg;
        TextView tvProductName,tvProductPrice,tvProductDescription;

        public CatalogProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            tvProductPrice = itemView.findViewById(R.id.tvProductPrice);
            tvProductDescription = itemView.findViewById(R.id.tvProductDescription);
            ivSymbolIsVeg = itemView.findViewById(R.id.ivSymbolIsVeg);
        }
    }
}
