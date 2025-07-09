package com.example.scipy.Cart;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scipy.ConstantSp;
import com.example.scipy.R;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    public interface OnCartChangeListener {
        void onCartChanged();
    }

    private final Context context;
    private final ArrayList<CartItemModel> cartItems;
    private final SharedPreferences preferences;
    private final OnCartChangeListener listener;

    public CartAdapter(Context context, ArrayList<CartItemModel> cartItems, OnCartChangeListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.preferences = context.getSharedPreferences(ConstantSp.pref, Context.MODE_PRIVATE);
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItemModel item = cartItems.get(position);

        holder.name.setText(item.getProductName());
        holder.price.setText(ConstantSp.ruppees + item.getProductPrice());
        holder.qty.setText(String.valueOf(item.getQuantity()));
        holder.image.setImageResource(item.getProductImageId());
        holder.total.setText("Total: " + ConstantSp.ruppees + (item.getProductPrice() * item.getQuantity()));

        holder.btnAdd.setOnClickListener(v -> {
            int newQty = item.getQuantity() + 1;
            item.setQuantity(newQty);
            preferences.edit().putInt("cart_qty_" + item.getProductName(), newQty).apply();
            notifyItemChanged(position);
            listener.onCartChanged();
        });

        holder.btnRemove.setOnClickListener(v -> {
            int newQty = item.getQuantity() - 1;
            if (newQty <= 0) {
                preferences.edit().remove("cart_qty_" + item.getProductName()).apply();
                cartItems.remove(position);
                notifyItemRemoved(position);
            } else {
                item.setQuantity(newQty);
                preferences.edit().putInt("cart_qty_" + item.getProductName(), newQty).apply();
                notifyItemChanged(position);
            }
            listener.onCartChanged();
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, qty, total;
        ImageView image, btnAdd, btnRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cart_item_name);
            price = itemView.findViewById(R.id.cart_item_price);
            qty = itemView.findViewById(R.id.cart_item_quantity);
            total = itemView.findViewById(R.id.cart_item_total);
            image = itemView.findViewById(R.id.cart_item_image);
            btnAdd = itemView.findViewById(R.id.cart_item_add);
            btnRemove = itemView.findViewById(R.id.cart_item_subtract);
        }
    }
}
