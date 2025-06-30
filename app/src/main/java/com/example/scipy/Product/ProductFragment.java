package com.example.scipy.Product;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.scipy.ConstantSp;
import com.example.scipy.DBHelper;
import com.example.scipy.R;

import java.util.ArrayList;

public class ProductFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<productlist> arrayList;
    SharedPreferences sharedPreferences;

    int[] subCatogryId = {1, 1, 2, 1};
    int[] productId = {1, 2, 3, 4};
    String[] productName = {"Logitech", "Asus", "Hcl", "hp"};
    String[] productDiscription = {
            "Logitech wireless mouse with ergonomic design",
            "Asus gaming keyboard with RGB lights",
            "HCL 16GB USB 3.0 pen drive",
            "HP 27-inch Full HD LED Monitor"
    };
    int[] productPrice = {100, 200, 300, 400};
    int[] productimageId = {
            R.drawable.mouse,
            R.drawable.mouse1,
            R.drawable.mouse2,
            R.drawable.mouse3
    };

    public ProductFragment() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.productrcy);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        sharedPreferences = requireActivity().getSharedPreferences(ConstantSp.pref, Context.MODE_PRIVATE);
        int selectedSubCatId = sharedPreferences.getInt(ConstantSp.subCatogryId, 0);

        DBHelper db = new DBHelper(requireContext());
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

        // Create product table if not exists
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS product(" +
                "subCatogryId INTEGER, " +
                "productId INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "productName TEXT, " +
                "productDiscription TEXT, " +
                "productPrice INTEGER, " +
                "productimageId VARCHAR)");

        // Insert dummy data if not already inserted
        for (int i = 0; i < productName.length; i++) {
            Cursor cursor = sqLiteDatabase.rawQuery(
                    "SELECT * FROM product WHERE productName='" + productName[i] + "'", null);
            if (cursor.getCount() == 0) {
                sqLiteDatabase.execSQL("INSERT INTO product VALUES(" +
                        subCatogryId[i] + "," +
                        productId[i] + ",'" +
                        productName[i] + "','" +
                        productDiscription[i] + "'," +
                        productPrice[i] + "," +
                        productimageId[i] + ")");
            }
            cursor.close();
        }

        // Fetch products from selected subcategory
        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM product WHERE subCatogryId=" + selectedSubCatId, null);

        arrayList = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                productlist product = new productlist();
                product.setSubCatogryId(cursor.getInt(0));
                product.setProductId(cursor.getInt(1));
                product.setProductName(cursor.getString(2));
                product.setProductDiscription(cursor.getString(3));
                product.setProductPrice(cursor.getInt(4));
                product.setImageId(cursor.getInt(5));
                arrayList.add(product);
            }
        }
        cursor.close();

        ProductAdapter adapter = new ProductAdapter(arrayList, requireContext());
        recyclerView.setAdapter(adapter);
    }
}
