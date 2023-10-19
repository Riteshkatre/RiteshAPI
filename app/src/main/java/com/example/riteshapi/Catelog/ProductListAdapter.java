package com.example.riteshapi.Catelog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.riteshapi.Catelog.NetworkResponce.ProductList;
import com.example.riteshapi.R;
import com.example.riteshapi.Tools;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {
    private final Context context;
    private final List<ProductList> productLists;
    Tools tools;

    public ProductListAdapter(Context context, List<ProductList> productLists) {
        this.context = context;
        this.productLists = productLists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_list, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductList productList = productLists.get(position);
        holder.tvProductName.setText(productList.getProductName());
        holder.tvProductPrice.setText( productList.getProductPrice());
        holder.tvProductDescription.setText(productList.getProductDesc());
        displayImage(holder.imageVeg.getContext(), holder.imageCap, productList.getProductImage());



    }

    @Override
    public int getItemCount() {
        return productLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageVeg;
        ImageView imageCap;
        TextView tvProductName,tvProductPrice,tvProductDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCap=itemView.findViewById(R.id.imageCap);
            imageVeg=itemView.findViewById(R.id.imageVeg);
            tvProductName=itemView.findViewById(R.id.tvProductName);
            tvProductPrice=itemView.findViewById(R.id.tvProductPrice);
            tvProductDescription=itemView.findViewById(R.id.tvProductDescription);
        }
    }
    private void displayImage(Context context, ImageView imageView, String currentPhotoPath) {

        Glide.with(context)

                .load(currentPhotoPath)
                .placeholder(R.drawable.ic_imageholder)
                .error(R.drawable.ic_launcher_background)
                .into(imageView);

    }
}