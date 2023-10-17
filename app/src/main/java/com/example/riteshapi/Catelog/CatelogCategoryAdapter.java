package com.example.riteshapi.Catelog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riteshapi.NetworkResponce.CategoryListResponce;
import com.example.riteshapi.NetworkResponce.SubCategoryListResponce;
import com.example.riteshapi.R;

import java.util.List;

public class CatelogCategoryAdapter extends  RecyclerView.Adapter<CatelogCategoryAdapter.CatelogCategoryViewHolder>{
    CatelogSubCategoryAdapter catelogSubCategoryAdapter;
    List<CategoryListResponce.Category> categoryList;

    List<SubCategoryListResponce.SubCategory>subCategories;
    @NonNull
    @Override
    public CatelogCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.catelog_categori_itemfile,parent,false);
        return new CatelogCategoryAdapter.CatelogCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatelogCategoryViewHolder holder, int position) {
        CategoryListResponce.Category Category=categoryList.get(position);
        holder.catelogcatogy.setText(Category.getCategoryName());


        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(holder.rcvcatelogsub.getContext());
        holder.rcvcatelogsub.setLayoutManager(layoutManager);

//        catelogSubCategoryAdapter = new CatelogSubCategoryAdapter(subCategories,getItem);
        holder.rcvcatelogsub.setAdapter(catelogSubCategoryAdapter);


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class CatelogCategoryViewHolder extends RecyclerView.ViewHolder {
        RecyclerView rcvcatelogsub;
        TextView catelogcatogy;
        public CatelogCategoryViewHolder(@NonNull View itemView)
        {
            super(itemView);
            rcvcatelogsub=itemView.findViewById(R.id.rcvcatelogcat);
            catelogcatogy=itemView.findViewById(R.id.catelogcatogy);
        }
    }
}
