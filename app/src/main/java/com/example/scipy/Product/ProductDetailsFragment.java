package com.example.scipy.Product;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.scipy.ConstantSp;
import com.example.scipy.DBHelper;
import com.example.scipy.R;

public class ProductDetailsFragment extends Fragment {

    TextView name, price, description, total, qty;
    SharedPreferences sp;
    ImageView image, wishlist, cart, plus, minus;
    Button pay;
    LinearLayout cart_layout;
    boolean isWishlist = false;
    int productPrice = 0;
    int quantity = 0;
    SQLiteDatabase sqLiteDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_details, container, false);

        // Database setup
        DBHelper db = new DBHelper(requireContext());
        sqLiteDatabase = db.getWritableDatabase();
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS wishlist(wishlistid INTEGER PRIMARY KEY AUTOINCREMENT ,userid INTEGER, productid INTEGER)");
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS cart(cartid INTEGER PRIMARY KEY AUTOINCREMENT ,orderid INTEGER(10),userid INTEGER, productid INTEGER, qty INTEGER(3), price VARCHAR(10), TotalPrice VARCHAR(10))");

        // View binding
        name = view.findViewById(R.id.product_detail_name);
        price = view.findViewById(R.id.product_detail_price);
        description = view.findViewById(R.id.product_detail_description);
        image = view.findViewById(R.id.product_image);
        cart_layout = view.findViewById(R.id.product_detail_cart_layout);
        pay = view.findViewById(R.id.pay_now);
        qty = view.findViewById(R.id.product_detail_cart_qty);
        total = view.findViewById(R.id.product_detail_carttotal);
        wishlist = view.findViewById(R.id.product_detail_wishlist);
        cart = view.findViewById(R.id.product_detail_cart);
        plus = view.findViewById(R.id.product_detail_qty_add);
        minus = view.findViewById(R.id.product_detail_qty_substract);

        // SharedPreferences
        sp = requireContext().getSharedPreferences(ConstantSp.pref, getContext().MODE_PRIVATE);

        String productName = sp.getString(ConstantSp.productName, "Product Name");
        String productDescription = sp.getString(ConstantSp.productDiscription, "No Description");
        productPrice = sp.getInt(ConstantSp.productPrice, 0);
        int productImageId = sp.getInt(ConstantSp.productImageId, R.drawable.a);
        int userId = sp.getInt(ConstantSp.userid, 0);
        int productId = sp.getInt(ConstantSp.productId, 0);

        // Set initial product details
        name.setText(productName);
        price.setText("₹" + productPrice);
        description.setText(productDescription);
        image.setImageResource(productImageId);
        qty.setText(String.valueOf(quantity));
        total.setText("Total: ₹" + (productPrice * quantity));

        // Wishlist state check
        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM wishlist WHERE userid = '" + userId + "' AND productid = '" + productId + "'", null);
        if (cursor.getCount() > 0) {
            wishlist.setImageResource(R.drawable.wishlist_fill);
            isWishlist = true;
        } else {
            wishlist.setImageResource(R.drawable.wishlist_empty);
            isWishlist = false;
        }

        wishlist.setOnClickListener(v -> {
            if (isWishlist) {
                sqLiteDatabase.execSQL("DELETE FROM wishlist WHERE userid = '" + userId + "' AND productid = '" + productId + "'");
                wishlist.setImageResource(R.drawable.wishlist_empty);
                Toast.makeText(getContext(), "Removed From Wishlist", Toast.LENGTH_SHORT).show();
                isWishlist = false;
            } else {
                sqLiteDatabase.execSQL("INSERT INTO wishlist VALUES(null,'" + userId + "'," + productId + ")");
                wishlist.setImageResource(R.drawable.wishlist_fill);
                Toast.makeText(getContext(), "Added To Wishlist", Toast.LENGTH_SHORT).show();
                isWishlist = true;
            }
        });

        // Cart check
        Cursor cursor1 = sqLiteDatabase.rawQuery(
                "SELECT * FROM cart WHERE userid = '" + userId + "' AND productid = '" + productId + "'", null);
        if (cursor1.getCount() > 0) {
            while (cursor1.moveToNext()) {
                quantity = cursor1.getInt(4); // index 4 = qty
                qty.setText(String.valueOf(quantity));
            }
            cart.setVisibility(View.GONE);
            cart_layout.setVisibility(View.VISIBLE);
        } else {
            cart.setVisibility(View.VISIBLE);
            cart_layout.setVisibility(View.GONE);
        }

        cart.setOnClickListener(v -> {
            quantity = 1;
            int totalPrice = productPrice * quantity;

            sqLiteDatabase.execSQL("INSERT INTO cart VALUES(NULL,'0','" + userId + "','" + productId + "','" + quantity + "','" + productPrice + "','" + totalPrice + "')");

            qty.setText(String.valueOf(quantity));
            total.setText("Total: ₹" + totalPrice);
            Toast.makeText(getContext(), "Added To Cart", Toast.LENGTH_SHORT).show();
            cart.setVisibility(View.GONE);
            cart_layout.setVisibility(View.VISIBLE);
        });

        plus.setOnClickListener(v -> {
            quantity++;
            updateMethod(quantity, "update");
        });

        minus.setOnClickListener(v -> {
            quantity--;
            if (quantity == 0) {
                updateMethod(quantity, "Delete");
                cart_layout.setVisibility(View.GONE);
                cart.setVisibility(View.VISIBLE);
            } else {
                updateMethod(quantity, "update");
            }
        });

        pay.setOnClickListener(v -> Toast.makeText(getContext(), "Pay Now", Toast.LENGTH_SHORT).show());

        return view;
    }

    private void updateMethod(int iqty, String type) {
        int iProductAmount = sp.getInt(ConstantSp.productPrice, 0);
        int iTotalPrice = iqty * iProductAmount;
        int userId = sp.getInt(ConstantSp.userid, 0);
        int productId = sp.getInt(ConstantSp.productId, 0);

        if (type.equalsIgnoreCase("update")) {
            String updateCartQty = "UPDATE cart SET qty = '" + iqty + "', TotalPrice = '" + iTotalPrice + "' " +
                    "WHERE userid = '" + userId + "' AND productid = '" + productId + "' AND orderid = '0'";
            sqLiteDatabase.execSQL(updateCartQty);
            qty.setText(String.valueOf(iqty));
            total.setText("Total: ₹" + iTotalPrice);
            Toast.makeText(getContext(), "Cart Updated", Toast.LENGTH_SHORT).show();
        } else {
            String deleteCartQty = "DELETE FROM cart WHERE userid = '" + userId + "' AND productid = '" + productId + "' AND orderid = '0'";
            sqLiteDatabase.execSQL(deleteCartQty);
            Toast.makeText(getContext(), "Removed From Cart", Toast.LENGTH_SHORT).show();
        }
    }
}
