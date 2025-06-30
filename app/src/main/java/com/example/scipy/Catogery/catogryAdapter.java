package com.example.scipy.Catogery;

import static androidx.core.content.SharedPreferencesKt.edit;

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

import com.example.scipy.Catogery.catogerylist;
import com.example.scipy.ConstantSp;
import com.example.scipy.R;

import java.util.ArrayList;

public class catogryAdapter extends RecyclerView.Adapter<catogryAdapter.ViewHolder> {

    private final ArrayList<catogerylist> categoryList;
    private final Context context;

    SharedPreferences sharedPreferences;

    public catogryAdapter(ArrayList<catogerylist> categoryList, Context context) {
        this.categoryList = categoryList;
        this.context = context;
        sharedPreferences = context.getSharedPreferences(ConstantSp.pref, Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ithem_ilst, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        catogerylist item = categoryList.get(position);
        holder.catogryText.setText(item.getCatogry());
        holder.imageView.setImageResource(item.getImageId());

        // Set click listener for the item
        holder.itemView.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_navigation_category_to_subcategoryFragment);
            sharedPreferences.edit().putInt(ConstantSp.catogryId, item.getCatogryId()).apply();
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView catogryText;
        ImageView imageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            catogryText = itemView.findViewById(R.id.category_text);
            imageView = itemView.findViewById(R.id.category_image);
        }
    }
}
