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
import com.example.riteshapi.NetworkResponce.ProductListResponce;
import com.example.riteshapi.R;

import java.util.List;

public class CatelogProductAdapter extends RecyclerView.Adapter<CatelogProductAdapter.CatelogProductViewHolder> {
    List<ProductListResponce.Product> productListResponces ;
    Context context;

    public CatelogProductAdapter(List<ProductListResponce.Product> productListResponces, Context context) {
        this.productListResponces = productListResponces;
        this.context = context;
    }

    @NonNull
    @Override
    public CatelogProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.catelog_productlist_itemfile,parent,false);
        return new CatelogProductAdapter.CatelogProductViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CatelogProductViewHolder holder, int position) {
        ProductListResponce.Product product = productListResponces.get(position);
        holder.txtproductName.setText(product.getProductName());
        holder.txtproductPrice.setText(product.getProductPrice());
        holder.txtproductDescription.setText(product.getProductDesc());
        holder.txtproductVegNonVeg.setText(product.getIsVeg());

        Glide.with(context)
                .load(productListResponces.get(position).getProductImage())
                .placeholder(R.drawable.background)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.imgprodList);


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class CatelogProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgprodList;
        TextView txtproductName,txtproductPrice,txtproductDescription,txtproductVegNonVeg;
        public CatelogProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgprodList=itemView.findViewById(R.id.imgProdList);
            txtproductPrice=itemView.findViewById(R.id.txtproductPrice);
            txtproductName=itemView.findViewById(R.id.txtProductName);
            txtproductDescription=itemView.findViewById(R.id.txtproductDescription);
            txtproductVegNonVeg=itemView.findViewById(R.id.txtproductVegNonVeg);



        }
    }
}
