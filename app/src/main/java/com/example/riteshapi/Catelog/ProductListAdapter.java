package com.example.riteshapi.Catelog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riteshapi.Catelog.NetworkResponce.ProductList;
import com.example.riteshapi.R;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {
    private final Context context;
    private final List<ProductList> productLists;

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
        ProductList model = productLists.get(position);

        if (model.getIsVeg().equalsIgnoreCase("0")) {
            // Tint the drawable to green
            holder.ivVegNonVeg.setColorFilter(ContextCompat.getColor(context, R.color.green), PorterDuff.Mode.SRC_IN);
        } else {
            // Tint the drawable to red
            holder.ivVegNonVeg.setColorFilter(ContextCompat.getColor(context, R.color.green), PorterDuff.Mode.SRC_IN);
        }

        holder.txtProductName.setText(model.getProductName());
        String rupeeSymbol = "₹"; // code of rupeeSymbol "\u20B9"
        holder.txtProductPrice.setText(rupeeSymbol + " " + model.getProductPrice());
        holder.txtProductDescription.setText(model.getProductDesc());

        // Implement displayImage function or use your preferred method for loading images
        // Example: displayImage(holder.binding.getRoot().getContext(), model.getProductImage(), holder.binding.ivFoodImage);
    }

    @Override
    public int getItemCount() {
        return productLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivVegNonVeg,ivFoodImage;
        TextView txtProductName,txtProductPrice,txtProductDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFoodImage=itemView.findViewById(R.id.ivFoodImage);
            ivVegNonVeg=itemView.findViewById(R.id.ivVegNonVeg);
            txtProductName=itemView.findViewById(R.id.txtProductName);
            txtProductPrice=itemView.findViewById(R.id.txtProductPrice);
            txtProductDescription=itemView.findViewById(R.id.txtProductDescription);
        }
    }
}
