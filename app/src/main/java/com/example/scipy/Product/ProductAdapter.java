package com.example.scipy.Product;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scipy.ConstantSp;
import com.example.scipy.R;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.myholder> {

    Context context;
    ArrayList<productlist> arrayList;

    SharedPreferences sharedPreferences;
    public ProductAdapter(ArrayList<productlist> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        sharedPreferences = context.getSharedPreferences(ConstantSp.pref, Context.MODE_PRIVATE);
    }
    @NonNull
    @Override
    public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list, parent, false);
        return new myholder(view);
    }

    public class myholder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView price,discription,name;
        public myholder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.product_image);
            price = itemView.findViewById(R.id.product_price);
            name = itemView.findViewById(R.id.product_name);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull myholder holder, int position) {
        holder.name.setText(arrayList.get(position).getProductName());
        holder.img.setImageResource(arrayList.get(position).getImageId());
        holder.price.setText(String.valueOf(arrayList.get(position).getProductPrice()));


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferences.edit().putInt(ConstantSp.productId, arrayList.get(position).getProductId()).apply();
                sharedPreferences.edit().putInt(ConstantSp.subCatogryId, arrayList.get(position).getSubCatogryId()).apply();
                sharedPreferences.edit().putString(ConstantSp.productName, arrayList.get(position).getProductName()).apply();
                sharedPreferences.edit().putString(ConstantSp.productDiscription, arrayList.get(position).getProductDiscription()).apply();
                sharedPreferences.edit().putInt(ConstantSp.productPrice, arrayList.get(position).getProductPrice()).apply();
                sharedPreferences.edit().putInt(ConstantSp.productImageId, arrayList.get(position).getImageId()).apply();
                Navigation.findNavController(view).navigate(R.id.action_productFragment_to_productDetailsFragment);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


}
