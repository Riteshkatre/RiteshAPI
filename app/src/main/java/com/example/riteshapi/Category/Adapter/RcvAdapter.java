package com.example.riteshapi.Category.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.riteshapi.Category.EditCategoryActivity;
import com.example.riteshapi.NetworkResponce.CategoryListResponce;
import com.example.riteshapi.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.List;


public class RcvAdapter  extends RecyclerView.Adapter<RcvAdapter.RcvViewHolder>{
    List<CategoryListResponce.Category>categoryListResponceList;
    List<CategoryListResponce.Category> Searchlist;
    Context context;
    CategoryClick categoryClick;
    public interface CategoryClick{
        void DeleteClick(CategoryListResponce.Category category);
        void onSwitchChanged(CategoryListResponce.Category category, boolean isChecked);
    }

    public void SetUpInterface(CategoryClick categoryClick){
        this.categoryClick = categoryClick;
    }

    public RcvAdapter(Context context,List<CategoryListResponce.Category> categoryListResponceList) {
        this.categoryListResponceList = categoryListResponceList;
        this.Searchlist= new ArrayList<>(categoryListResponceList);
        this.context=context;
    }

    @NonNull
    @Override
    public RcvViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.rcvitem_file,parent,false);
        return new  RcvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RcvViewHolder holder, @SuppressLint("RecyclerView") int position) {
        CategoryListResponce.Category categoryListResponce=Searchlist.get(position);
        holder.itemtextview.setText(categoryListResponce.getCategoryName());
        holder.itemtextview1.setText(categoryListResponce.getCategoryId());
        holder.itemtextview2.setText(categoryListResponce.getCategoryStatus());
        holder.editimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EditCategoryActivity.class);
                intent.putExtra("categoryId", categoryListResponce.getCategoryId());
                intent.putExtra("categoryName", categoryListResponce.getCategoryName());
                view.getContext().startActivity(intent);

            }
        });

        holder.deleteimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                categoryClick.DeleteClick(categoryListResponce);
            }
        });
        holder.btnswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                categoryClick.onSwitchChanged(Searchlist.get(position), isChecked);
            }
        });




    }

    @Override
    public int getItemCount() {
        return Searchlist.size();
    }


    public class RcvViewHolder extends RecyclerView.ViewHolder {

        TextView itemtextview,itemtextview1,itemtextview2;
        ImageView editimage,deleteimage;
        SwitchMaterial btnswitch;

        public RcvViewHolder(@NonNull View itemView) {
            super(itemView);
            itemtextview=itemView.findViewById(R.id.itemtextview);
            itemtextview1=itemView.findViewById(R.id.itemtextview1);
            itemtextview2=itemView.findViewById(R.id.itemtextview2);
            editimage=itemView.findViewById(R.id.editimage);
            btnswitch=itemView.findViewById(R.id.btnswitch);
            deleteimage=itemView.findViewById(R.id.deleteimage);


        }
    }


    public void Search(CharSequence charSequence,  RecyclerView rcv) {
        try{
            String charString=charSequence.toString().toLowerCase().trim();
            if(charString.isEmpty()){
                Searchlist=categoryListResponceList;
                rcv.setVisibility(View.VISIBLE);
            }else{
                int flag=0;
                List<CategoryListResponce.Category> filterlist=new ArrayList<>();
                for(CategoryListResponce.Category Row:categoryListResponceList){
                    if(Row.getCategoryName().toString().toLowerCase().contains(charString.toLowerCase())){
                        filterlist.add(Row);
                        flag=1;
                    }
                }
                if (flag == 1) {
                    Searchlist=filterlist;
                    rcv.setVisibility(View.VISIBLE);
                }
                else{
                    rcv.setVisibility(View.GONE);
                }
            }
            notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
