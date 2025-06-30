package com.example.scipy.Wishlist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scipy.ConstantSp;
import com.example.scipy.Product.ProductDetailsFragment;
import com.example.scipy.R;

import java.util.ArrayList;


public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.myholder> {
    ArrayList<wishlist_list> arrayList;
    SQLiteDatabase db;
    SharedPreferences sp;
    Context context;

    public WishlistAdapter(ArrayList<wishlist_list> arrayList, SQLiteDatabase db, Context context) {
        this.arrayList = arrayList;
        this.db = db;
        this.context = context;
        sp = context.getSharedPreferences(ConstantSp.pref, Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_list, parent, false);
        return new myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myholder holder, int position) {
        holder.img.setImageResource(arrayList.get(position).getImg());
        holder.name.setText(arrayList.get(position).getName());
        holder.price.setText(arrayList.get(position).getPrice());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.execSQL("delete from wishlist where wishlistid=?", new String[]{arrayList.get(position).getWishlistid()});
                arrayList.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putInt(ConstantSp.productId, arrayList.get(position).getImg()).commit();
                sp.edit().putString(ConstantSp.name, arrayList.get(position).getName()).commit();
                sp.edit().putString(ConstantSp.productPrice, arrayList.get(position).getPrice()).commit();
                sp.edit().putInt(ConstantSp.imageId, arrayList.get(position).getImg()).commit();
                sp.edit().putString(ConstantSp.productDiscription, arrayList.get(position).getDescription()).commit();

                Intent intent = new Intent(context, ProductDetailsFragment.class);
                context.startActivity(intent);

            }
        });
    }

    public class myholder extends RecyclerView.ViewHolder {

        ImageView img, delete;
        TextView name, price;


        public myholder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            delete = itemView.findViewById(R.id.btnRemove);


        }
    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

}



