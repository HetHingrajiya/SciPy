package com.example.scipy.Catogery;

import android.content.Context;
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

public class subcategoryAdapter extends RecyclerView.Adapter<subcategoryAdapter.myholder> {

    Context context;
    ArrayList<subcategorylist> arrayList;

    SharedPreferences sharedPreferences;

    public subcategoryAdapter(ArrayList<subcategorylist> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
        sharedPreferences = context.getSharedPreferences(ConstantSp.pref, Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ithem_ilst, parent, false);
        return new myholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull myholder holder, int position) {
        subcategorylist item = arrayList.get(position);

        holder.img.setImageResource(item.getImageId());
        holder.text.setText(item.getSubCatogryName());

        holder.itemView.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_subcategoryFragment_to_productFragment);
            sharedPreferences.edit().putInt(ConstantSp.subCatogryId, item.getSubCatogryId()).apply();
            sharedPreferences.edit().putString(ConstantSp.subCatogryName, item.getSubCatogryName()).apply();
            sharedPreferences.edit().putInt(ConstantSp.subImageId, item.getImageId()).apply();
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class myholder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView text;

        public myholder(@NonNull View itemView) {
            super(itemView);

            img = itemView.findViewById(R.id.category_image);
            text = itemView.findViewById(R.id.category_text);
        }
    }


}
