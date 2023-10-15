package com.example.riteshapi.Product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.riteshapi.NetworkResponce.ProductListResponce;
import com.example.riteshapi.R;

import java.util.ArrayList;
import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductViewHolder> {

    List<ProductListResponce.Product> productListResponces ;
    List<ProductListResponce.Product> Searchlist;
    Context context;

    ProductClick productClick;
    public interface ProductClick{
        void DeleteClick(ProductListResponce.Product product);
    }
    public void SetUpInterface(ProductListAdapter.ProductClick productClick){
        this.productClick = productClick;
    }

    public ProductListAdapter(List<ProductListResponce.Product> productListResponces, Context context) {
        this.productListResponces = productListResponces;
        this.Searchlist =productListResponces;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.productitem_file,parent,false);
        return new ProductViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductListResponce.Product product = Searchlist.get(position);
//        Tools.displayImage(holder.itemView.getContext(),product.getProductImage(),holder.imgProdList);


        Glide.with(context)
                .load(productListResponces.get(position).getProductImage())
                .placeholder(R.drawable.background)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.imgProdList);



        holder.txtProductName.setText(product.getProductName());
        holder.txtProductPrice.setText(product.getProductPrice());
        holder.txtProductDescription.setText(product.getProductDesc());
        holder.txtProductVegNonVeg.setText(product.getIsVeg());

        holder.imgEditProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Edit Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        holder.imgDeleteProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (productClick != null) {
                    productClick.DeleteClick(product);
                }
            }
        });

    }
    






    @Override
    public int getItemCount() {
        return Searchlist.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProdList, imgEditProduct, imgDeleteProduct;
        TextView txtProductName, txtProductPrice, txtProductDescription, txtProductVegNonVeg;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProdList = itemView.findViewById(R.id.imgProdList);
            imgEditProduct = itemView.findViewById(R.id.imgEditProduct);
            imgDeleteProduct = itemView.findViewById(R.id.imgDeleteProduct);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductPrice = itemView.findViewById(R.id.txtProductPrice);
            txtProductDescription = itemView.findViewById(R.id.txtProductDescription);
            txtProductVegNonVeg = itemView.findViewById(R.id.txtProductVegNonVeg);
        }
    }
    public void Search(CharSequence charSequence,  RecyclerView rcv) {
        try{
            String charString=charSequence.toString().toLowerCase().trim();
            if(charString.isEmpty()){
                Searchlist=productListResponces;
                rcv.setVisibility(View.VISIBLE);
            }else{
                int flag=0;
                List<ProductListResponce.Product> filterlist=new ArrayList<>();
                for(ProductListResponce.Product Row:productListResponces){
                    if(Row.getProductName().toString().toLowerCase().contains(charString.toLowerCase())){
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
