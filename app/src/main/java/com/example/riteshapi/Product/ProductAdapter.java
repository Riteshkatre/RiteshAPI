package com.example.riteshapi.Product;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.riteshapi.NetworkResponce.ProductListResponce;
import com.example.riteshapi.R;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductDataViewHolder> {

    Context context;
    List<ProductListResponce.Product> productList, Searchlist;

    onProductItemClickListener onProductItemClickListener;

    public ProductAdapter(Context context, List<ProductListResponce.Product> productList) {
        this.context = context;
        this.productList = productList;
        this.Searchlist = productList;
    }
    public void clearData() {
        productList.clear();
        notifyDataSetChanged();
    }

    public interface onProductItemClickListener {
        void onProductItemClick(ProductListResponce.Product product);
        void onProductItemClickDelete(ProductListResponce.Product product);
    }

    public void setUpItemClickListener(onProductItemClickListener clickListener) {
        this.onProductItemClickListener = clickListener;
    }

    @NonNull
    @Override
    public ProductDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_product, parent, false);
        return new ProductDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductDataViewHolder holder, int position) {
        ProductListResponce.Product product = Searchlist.get(position);
        if (product.getIsVeg().equalsIgnoreCase("0")) {
            holder.vegNonVegIcon.setColorFilter(ContextCompat.getColor(context, R.color.green), PorterDuff.Mode.SRC_IN);
        } else {
            holder.vegNonVegIcon.setColorFilter(ContextCompat.getColor(context, R.color.red), PorterDuff.Mode.SRC_IN);
        }

        holder.txtName.setText(product.getProductName());
        holder.txtPrice.setText(product.getProductPrice());
        holder.txtDesc.setText(product.getProductDesc());

        Glide.with(context)
                .load(product.getProductImage())
                .placeholder(R.drawable.ic_imageholder)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.imageViewProduct);

        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProductItemClickListener.onProductItemClickDelete(product);
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onProductItemClickListener.onProductItemClick(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        return Searchlist.size();
    }

    public static class ProductDataViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtPrice, txtDesc;
        ImageButton btnDel, btnEdit;
        ImageView imageViewProduct,vegNonVegIcon;

        public ProductDataViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.ProductNameTextView);
            txtPrice = itemView.findViewById(R.id.textViewPrice);
            txtDesc = itemView.findViewById(R.id.ProductDescTextView);
            btnDel = itemView.findViewById(R.id.btn_product_delete);
            btnEdit = itemView.findViewById(R.id.btn_product_edit);
            imageViewProduct = itemView.findViewById(R.id.img_productt);
            vegNonVegIcon=itemView.findViewById(R.id.vegNonVegIcon);
        }
    }

    public void Search(CharSequence charSequence, RecyclerView rcv) {
        try {
            String charString = charSequence.toString().toLowerCase().trim();
            if (charString.isEmpty()) {
                Searchlist = productList;
                rcv.setVisibility(View.VISIBLE);
            } else {
                int flag = 0;
                List<ProductListResponce.Product> filterlist = new ArrayList<>();
                for (ProductListResponce.Product Row : productList) {
                    if (Row.getProductName().toLowerCase().contains(charString.toLowerCase())) {
                        filterlist.add(Row);
                        flag = 1;
                    }
                }
                if (flag == 1) {
                    Searchlist = filterlist;
                    rcv.setVisibility(View.VISIBLE);
                } else {
                    rcv.setVisibility(View.GONE);
                }
            }
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
