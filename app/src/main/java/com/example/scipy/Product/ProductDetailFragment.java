package com.example.scipy.Product;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.scipy.ConstantSp;
import com.example.scipy.DBHelper;
import com.example.scipy.R;
import com.google.android.material.button.MaterialButton;
import com.razorpay.Checkout;

import org.json.JSONObject;

public class ProductDetailFragment extends Fragment {

    private ImageView productImage, wishlistIcon, cartIcon, qtyAdd, qtySubtract;
    private TextView productName, productPrice, productDescription, cartQty, cartTotal;
    private LinearLayout cartLayout;
    private MaterialButton payNowButton;

    private int quantity = 0;
    private int price = 0;
    private int productId = 0;
    private int img = 0;
    private boolean isWishlisted = false;
    private String name = "";

    public ProductDetailFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_detail_fragment, container, false);

        // Initialize views
        productImage = view.findViewById(R.id.product_image);
        productName = view.findViewById(R.id.product_detail_name);
        productPrice = view.findViewById(R.id.product_detail_price);
        productDescription = view.findViewById(R.id.product_detail_description);
        wishlistIcon = view.findViewById(R.id.product_detail_wishlist);
        cartIcon = view.findViewById(R.id.product_detail_cart);
        cartLayout = view.findViewById(R.id.product_detail_cart_layout);
        qtyAdd = view.findViewById(R.id.product_detail_qty_add);
        qtySubtract = view.findViewById(R.id.product_detail_qty_substract);
        cartQty = view.findViewById(R.id.product_detail_cart_qty);
        cartTotal = view.findViewById(R.id.product_detail_carttotal);
        payNowButton = view.findViewById(R.id.pay_now);

        SharedPreferences preferences = requireActivity().getSharedPreferences(ConstantSp.pref, Context.MODE_PRIVATE);

        DBHelper db = new DBHelper(getContext());
        SQLiteDatabase db1 = db.getWritableDatabase();

        db1.execSQL("CREATE TABLE IF NOT EXISTS product (productid INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, subcatogryid TEXT, price TEXT, img INTEGER)");
        db1.execSQL("CREATE TABLE IF NOT EXISTS cart (cartid INTEGER PRIMARY KEY AUTOINCREMENT, userid TEXT, productid TEXT, quantity INTEGER)");
        db1.execSQL("CREATE TABLE IF NOT EXISTS wishlist (wishlistid INTEGER PRIMARY KEY AUTOINCREMENT, userid TEXT, productid TEXT, img INTEGER)");


        // Fetch product info
        name = preferences.getString(ConstantSp.productName, "Product Name");
        String description = preferences.getString(ConstantSp.productDiscription, "No description available");
        int imageName = preferences.getInt(ConstantSp.productImageId, 0); // now store String
        productId = preferences.getInt(ConstantSp.productId, 0);

        if (preferences.contains(ConstantSp.productPrice)) {
            try {
                Object rawPrice = preferences.getAll().get(ConstantSp.productPrice);
                if (rawPrice instanceof Integer) {
                    price = (int) rawPrice;
                } else if (rawPrice instanceof String) {
                    price = Integer.parseInt((String) rawPrice);
                }
            } catch (Exception e) {
                price = 0;
            }
        }

        // Set product data in UI
        productName.setText(name);
        productDescription.setText(description);
        productPrice.setText(ConstantSp.ruppees + price);

        // Load image by name
        int resId = getResources().getIdentifier(String.valueOf(imageName), "drawable", requireContext().getPackageName());
        if (resId != 0) {
            Glide.with(this).load(resId).into(productImage);
        } else {
            productImage.setImageResource(R.drawable.ic_dashboard_black_24dp);
        }

        // Check wishlist state from DB
        String userId = preferences.getString(ConstantSp.userid, "");
        Cursor cursor = db1.rawQuery("SELECT * FROM wishlist WHERE userid=? AND productid=?", new String[]{userId, String.valueOf(productId)});
        isWishlisted = cursor.getCount() > 0;
        cursor.close();

        wishlistIcon.setImageResource(isWishlisted ? R.drawable.wishlist_fill : R.drawable.wishlist_empty);

        // Restore cart quantity
        quantity = preferences.getInt("cart_qty_" + name, 0);
        if (quantity > 0) {
            cartLayout.setVisibility(View.VISIBLE);
            cartIcon.setVisibility(View.GONE);
            updateCartView();
        }

        // Wishlist toggle logic
        wishlistIcon.setOnClickListener(v -> {
            isWishlisted = !isWishlisted;
            wishlistIcon.setImageResource(isWishlisted ? R.drawable.wishlist_fill : R.drawable.wishlist_empty);

            if (isWishlisted) {
                db1.execSQL("INSERT INTO wishlist(userid, productid,img) VALUES(?, ?,?)", new Object[]{userId, String.valueOf(productId),img});
                Toast.makeText(getContext(), "Added to Wishlist", Toast.LENGTH_SHORT).show();
            } else {
                db1.execSQL("DELETE FROM wishlist WHERE userid=? AND productid=? AND img=?", new Object[]{userId, String.valueOf(productId),img});
                Toast.makeText(getContext(), "Removed from Wishlist", Toast.LENGTH_SHORT).show();
            }
        });

        // Add to cart logic
        cartIcon.setOnClickListener(v -> {
            quantity = 1;
            updateCartView();
            cartLayout.setVisibility(View.VISIBLE);
            cartIcon.setVisibility(View.GONE);
        });

        qtyAdd.setOnClickListener(v -> {
            quantity++;
            updateCartView();
        });

        qtySubtract.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
            } else {
                quantity = 0;
                cartLayout.setVisibility(View.GONE);
                cartIcon.setVisibility(View.VISIBLE);
            }
            updateCartView();
        });

        payNowButton.setOnClickListener(v -> {
            if (quantity == 0) {
                Toast.makeText(getContext(), "Please add at least one item to cart", Toast.LENGTH_SHORT).show();
            } else {
                int totalAmount = quantity * price * 100; // Razorpay uses paise

                Checkout checkout = new Checkout();
                checkout.setKeyID("rzp_test_rGhSnxMQBMXmfL");

                Activity activity = requireActivity();
                try {
                    JSONObject options = new JSONObject();
                    options.put("name", getResources().getString(R.string.app_name));
                    options.put("description", "Purchase Deal From " + getResources().getString(R.string.app_name));
                    options.put("send_sms_hash", true);
                    options.put("allow_rotation", true);
                    options.put("image", R.mipmap.ic_launcher);
                    options.put("currency", "INR");
                    options.put("amount", totalAmount);

                    JSONObject preFill = new JSONObject();
                    preFill.put("email", "hethpatel8517@gmail.com");
                    preFill.put("contact", "7069267376");
                    options.put("prefill", preFill);

                    checkout.open(activity, options);
                } catch (Exception e) {
                    Toast.makeText(activity, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });



        return view;
    }

    private void updateCartView() {
        cartQty.setText(String.valueOf(quantity));
        cartTotal.setText("Total: " + ConstantSp.ruppees + (quantity * price));
        SharedPreferences.Editor editor = requireActivity().getSharedPreferences(ConstantSp.pref, Context.MODE_PRIVATE).edit();
        editor.putInt("cart_qty_" + name, quantity);
        editor.apply();
    }
}
