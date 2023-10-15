package com.example.riteshapi.SubCategory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riteshapi.NetworkResponce.SubCategoryListResponce;
import com.example.riteshapi.R;

import java.util.ArrayList;
import java.util.List;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.SubCategoryViewHolder> {
    Context context;
    List<SubCategoryListResponce.SubCategory> subCategories , Searchlist;
    public void updateData(List<SubCategoryListResponce.SubCategory> newSubCategoryList) {
        subCategories.clear();
        subCategories.addAll(newSubCategoryList);
        notifyDataSetChanged();
    }
    SubCategoryClick subCategoryClick;

    public interface SubCategoryClick{
        void SubEditClick(SubCategoryListResponce.SubCategory subCategory);
        void SubDeleteClick(SubCategoryListResponce.SubCategory subCategory);
    }

    public void SetUpInterface(SubCategoryAdapter.SubCategoryClick subCategoryClick){
        this.subCategoryClick = subCategoryClick;
    }

    public SubCategoryAdapter(Context context, List<SubCategoryListResponce.SubCategory> subCategories) {
        this.context = context;
        this.subCategories = subCategories;
        this.Searchlist = subCategories;
    }

    @NonNull
    @Override
    public SubCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.subrcvitem_file ,parent,false);
        return new SubCategoryViewHolder(view);    }


    @Override
    public void onBindViewHolder(@NonNull SubCategoryViewHolder holder, int position) {
        SubCategoryListResponce.SubCategory categoryListResponse=Searchlist.get(position);
        holder.itemtextview2.setText(categoryListResponse.getSubCategoryId());
        holder.itemtextview1.setText(categoryListResponse.getSubcategoryName());
        holder.itemtextview.setText(categoryListResponse.getCategoryId());

        holder.subdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subCategoryClick.SubDeleteClick(categoryListResponse);

            }
        });
        holder.subedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subCategoryClick.SubEditClick(subCategories.get(position));

            }
        });

    }

    @Override
    public int getItemCount() {
        return Searchlist.size();
    }


    public class SubCategoryViewHolder extends RecyclerView.ViewHolder {
        TextView itemtextview,itemtextview1,itemtextview2;
        ImageView subedit,subdelete;

        public SubCategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            itemtextview=itemView.findViewById(R.id.itemtextview);
            itemtextview1=itemView.findViewById(R.id.itemtextview1);
            itemtextview2=itemView.findViewById(R.id.itemtextview2);
            subedit=itemView.findViewById(R.id.subEdit);
            subdelete=itemView.findViewById(R.id.subdelete);

        }
    }

   public void searchdata(CharSequence charSequence,RecyclerView subrcv){

        String charString = charSequence.toString().trim();
        if (charString.isEmpty()){
            Searchlist = subCategories;
            subrcv.setVisibility(View.VISIBLE);

        }
        else {
            subrcv.setVisibility(View.GONE);


            int flag = 0;
            List<SubCategoryListResponce.SubCategory> subCategoryArrayList = new ArrayList<>();
            for (SubCategoryListResponce.SubCategory row : subCategories){
                if (row.getSubcategoryName().toLowerCase().contains(charString.toLowerCase())){
                    subCategoryArrayList.add(row);
                    flag = 1;
                }
            }
            if (flag == 1){
                Searchlist = subCategoryArrayList;
                subrcv.setVisibility(View.VISIBLE);

            }
            else {
                subrcv.setVisibility(View.GONE);

            }
        }
        notifyDataSetChanged();
    }
}
