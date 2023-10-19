package com.example.riteshapi.Catelog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.example.riteshapi.Catelog.NetworkResponce.CategoryList;
import com.example.riteshapi.R;

import java.util.List;

public class CatalogueAdapter extends Adapter<CatalogueAdapter.ViewHolder> {
    private final Context context;
    private final List<CategoryList> categoryList;

    public CatalogueAdapter(Context context, List<CategoryList> categoryList) {
        this.context = context;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_catalogue, parent, false);
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryList model = categoryList.get(position);

        holder.tvcatname.setText(model.getCategoryName());
        int totalProducts = model.getSubCategoryList().get(0).getProductList().size();
        holder.tvsubname.setText(model.getSubCategoryList().get(0).getSubcategoryName() +"  "+  ("No Of Product"+" "+(totalProducts)));
        holder.rvProductList.setVisibility(View.GONE);

        // Set a click listener for tvsubname to toggle product list visibility
        holder.tvsubname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.rvProductList.getVisibility() == View.VISIBLE) {
                    holder.rvProductList.setVisibility(View.GONE);
                } else {
                    holder.rvProductList.setVisibility(View.VISIBLE);
                }
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        holder.rvProductList.setLayoutManager(layoutManager);

        ProductListAdapter productListAdapter = new ProductListAdapter(context, model.getSubCategoryList().get(0).getProductList());
        holder.rvProductList.setAdapter(productListAdapter);
    }


    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvcatname, tvsubname;
        RecyclerView rvProductList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvcatname = itemView.findViewById(R.id.tvcatname);
            tvsubname = itemView.findViewById(R.id.tvsubname);
            rvProductList = itemView.findViewById(R.id.rvProductList);
        }
    }
}