package com.example.scipy.Wishlist;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.scipy.ConstantSp;
import com.example.scipy.DBHelper;
import com.example.scipy.R;

import java.util.ArrayList;

public class WishlistFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<WishlistList> arrayList;
    private SharedPreferences sp;
    private SQLiteDatabase db;

    public WishlistFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wishlist, container, false); // Reuse layout
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = requireContext();

        sp = context.getSharedPreferences(ConstantSp.pref, Context.MODE_PRIVATE);
        DBHelper helper = new DBHelper(context);
        db = helper.getWritableDatabase();

        recyclerView = view.findViewById(R.id.wishlist_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        arrayList = new ArrayList<>();

        String selectWishlistQuery = "SELECT * FROM wishlist WHERE userid = '" + sp.getString(ConstantSp.userid, "") + "'";
        Cursor cursor = db.rawQuery(selectWishlistQuery, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                WishlistList list = new WishlistList();
                list.setWishlistid(cursor.getString(0));

                String selectProductQuery = "SELECT * FROM product WHERE productid = '" + cursor.getInt(2) + "'";
                Cursor cursor1 = db.rawQuery(selectProductQuery, null);

                if (cursor1.moveToFirst()) {
                    list.setProductid(cursor1.getString(0));
                    list.setSubcategoryid(cursor1.getString(1));
                    list.setName(cursor1.getString(2));
                    list.setImage(cursor1.getInt(3));
                    list.setPrice(cursor1.getString(4));
                }

                cursor1.close();
                arrayList.add(list);
            }

            WishlistAdapter adapter = new WishlistAdapter(context, arrayList, db);
            recyclerView.setAdapter(adapter);
        }

        cursor.close();
    }
}
