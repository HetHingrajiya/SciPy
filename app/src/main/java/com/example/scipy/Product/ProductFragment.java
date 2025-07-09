package com.example.scipy.Product;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scipy.ConstantSp;
import com.example.scipy.DBHelper;
import com.example.scipy.R;

import java.util.ArrayList;

public class ProductFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<productlist> arrayList;
    SharedPreferences sharedPreferences;

    int[] subCatogryId = {1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8};
    int[] productId = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
    String[] productName = {"Logitech", "Asus", "HCL", "HP", "Samsung", "Intel", "Itel", "Vivo", "Dell", "Lenovo", "Xiaomi", "Realme", "Acer", "MSI", "Apple", "Nokia", "Boat", "OnePlus"};

    String[] productDiscription = {"Logitech is a global leader in computer peripherals like keyboards, mice, and webcams known for quality and durability.", "Asus produces high-performance laptops, motherboards, and gaming hardware loved by professionals and gamers alike.", "HCL is an Indian tech company known for its legacy in computer systems and robust IT solutions.", "HP is a trusted brand offering reliable desktops, laptops, and printers for home and office use.", "Samsung is a tech giant offering smartphones, TVs, and appliances with cutting-edge features and innovation.", "Intel is the world’s leading manufacturer of CPUs and chipsets powering millions of computers globally.", "Itel is a budget-friendly brand providing smartphones and accessories designed for emerging markets.", "Vivo focuses on smartphones with stylish designs and advanced camera features at competitive pricing.", "Dell is a leading PC manufacturer offering robust laptops and desktops ideal for work and gaming.", "Lenovo delivers innovative tech like ThinkPads and Yoga laptops, blending business performance and versatility.", "Xiaomi offers smartphones and smart gadgets with high specs at affordable prices, popular in many regions.", "Realme is known for its powerful smartphones and audio products, targeting youth with aggressive pricing.", "Acer provides budget-friendly laptops and monitors, great for students, casual users, and gamers alike.", "MSI specializes in gaming laptops, GPUs, and high-end PC components for enthusiast-level users.", "Apple is renowned for premium devices like iPhones, iPads, and MacBooks focused on design and ecosystem integration.", "Nokia combines durable phone design with Android software for a clean, reliable mobile experience.", "Boat is a leading Indian brand in audio accessories, offering stylish and affordable earphones and speakers.", "OnePlus makes flagship-level smartphones and smart TVs with fast performance and minimal UI experience."};
    int[] productPrice = {100, 200, 300, 400, 150, 250, 350, 500, 600, 700, 800, 900, 1000, 1100, 1200, 1300};

    int[] productimageId = {R.drawable.mouse, R.drawable.mouse, R.drawable.mouse2, R.drawable.mouse2, R.drawable.keyboard, R.drawable.keyboard, R.drawable.keyboard, R.drawable.keyboard, R.drawable.cpu, R.drawable.cpu, R.drawable.cpu, R.drawable.cpu, R.drawable.moniter, R.drawable.moniter, R.drawable.moniter, R.drawable.moniter,

    };

    public ProductFragment() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS product");


        sqLiteDatabase.execSQL("CREATE TABLE product(" + "subCatogryId INTEGER, " + "productId INTEGER PRIMARY KEY AUTOINCREMENT, " + "productName TEXT, " + "productDiscription TEXT, " + "productPrice INTEGER, " + "productimageId INTEGER)");


        // Insert dummy data if not already inserted
        for (int i = 0; i < productId.length; i++) {
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM product WHERE productId = ?", new String[]{String.valueOf(productId[i])});
            if (cursor.getCount() == 0) {
                sqLiteDatabase.execSQL("INSERT INTO product (subCatogryId, productId, productName, productDiscription, productPrice, productimageId) VALUES (" + subCatogryId[i] + ", " + productId[i] + ", " + "'" + productName[i].replace("'", "''") + "', " + "'" + productDiscription[i].replace("'", "''") + "', " + productPrice[i] + ", " + productimageId[i] + ")");
            }
            cursor.close();
        }


        // Fetch products from selected subcategory
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM product WHERE subCatogryId=" + selectedSubCatId, null);

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
