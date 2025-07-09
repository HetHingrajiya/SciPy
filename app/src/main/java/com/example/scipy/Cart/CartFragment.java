package com.example.scipy.Cart;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scipy.ConstantSp;
import com.example.scipy.DBHelper;
import com.example.scipy.R;
import com.razorpay.Checkout;

import org.json.JSONObject;

import java.security.PrivateKey;
import java.util.ArrayList;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private ArrayList<CartItemModel> cartItems;
    private TextView cartTotalPriceText;
    private Button payNowButton;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = view.findViewById(R.id.cart_recycler);
        cartTotalPriceText = view.findViewById(R.id.cart_total_price);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        payNowButton = view.findViewById(R.id.cart_pay_now);

        loadCartItems();

        payNowButton.setOnClickListener(v -> {
            int totalAmount = calculateTotalAmount(); // You should implement this based on cartItems

            if (totalAmount <= 0) {
                Toast.makeText(getContext(), "Please add at least one item to cart", Toast.LENGTH_SHORT).show();
                return;
            }

            Checkout checkout = new Checkout();
            checkout.setKeyID("rzp_test_rGhSnxMQBMXmfL");

            Activity activity = getActivity();
            try {
                JSONObject options = new JSONObject();
                options.put("name", getString(R.string.app_name));
                options.put("description", "Purchase from " + getString(R.string.app_name));
                options.put("image", R.mipmap.ic_launcher);
                options.put("currency", "INR");
                options.put("amount", totalAmount * 100); // in paise

                JSONObject preFill = new JSONObject();
                preFill.put("email", "hethpatel8517@gmail.com");
                preFill.put("contact", "7069267376");
                options.put("prefill", preFill);

                checkout.open(activity, options);

            } catch (Exception e) {
                Toast.makeText(getContext(), "Payment error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;


    }

    private int calculateTotalAmount() {
        int total = 0;
        for (CartItemModel item : cartItems) {
            total += item.getProductPrice() * item.getQuantity();
        }
        return total;
    }

    private void loadCartItems() {
        cartItems = new ArrayList<>();
        SharedPreferences preferences = requireContext().getSharedPreferences(ConstantSp.pref, Context.MODE_PRIVATE);
        DBHelper dbHelper = new DBHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM product", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("productId"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("productName"));
                int price = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow("productPrice")));
                int imgRes = cursor.getInt(cursor.getColumnIndexOrThrow("productimageId"));

                int qty = preferences.getInt("cart_qty_" + name, 0);
                if (qty > 0) {
                    cartItems.add(new CartItemModel(id, name, price, imgRes, qty));
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        adapter = new CartAdapter(getContext(), cartItems, this::updateTotalPrice);
        recyclerView.setAdapter(adapter);
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        int totalAmount = 0;
        SharedPreferences preferences = requireContext().getSharedPreferences(ConstantSp.pref, Context.MODE_PRIVATE);
        for (CartItemModel item : cartItems) {
            int qty = preferences.getInt("cart_qty_" + item.getProductName(), 0);
            totalAmount += qty * item.getProductPrice();
        }
        cartTotalPriceText.setText("Total: " + ConstantSp.ruppees + totalAmount);
    }
}
