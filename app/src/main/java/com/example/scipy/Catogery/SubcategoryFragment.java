package com.example.scipy.Catogery;

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

public class SubcategoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private SharedPreferences sp;
    private ArrayList<subcategorylist> arrayList;

    private final int[] catogryId = {1, 1, 2,2,3,3,4, 4};
    private final int[] subCatogryId = {1, 2, 3, 4,5,6,7,8};
    private final String[] subCatogryName = {"mouse", "mouse","keyboard", "keyboard", "cpu", "cpu", "moniter", "moniter",};
    private final int[] imageId = {R.drawable.mouse, R.drawable.mouse, R.drawable.keyboard, R.drawable.keyboard, R.drawable.cpu, R.drawable.cpu, R.drawable.moniter, R.drawable.moniter};

    public SubcategoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_subcategory, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        sp = requireActivity().getSharedPreferences(ConstantSp.pref, Context.MODE_PRIVATE);
        int selectedSubCatId = sp.getInt(ConstantSp.catogryId, 0);

        DBHelper db = new DBHelper(requireContext());
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();

        // Create table
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS subcategory(" +
                "catogryId INTEGER," +
                "subCatogryId INTEGER PRIMARY KEY," +
                "subCatogryName TEXT," +
                "imageId INTEGER)");

        // Insert sample data if not exists
        for (int i = 0; i < subCatogryName.length; i++) {
            Cursor cursor = sqLiteDatabase.rawQuery(
                    "SELECT * FROM subcategory WHERE subCatogryId = " + subCatogryId[i], null);
            if (cursor.getCount() == 0) {
                sqLiteDatabase.execSQL("INSERT INTO subcategory(catogryId, subCatogryId, subCatogryName, imageId) VALUES(" +
                        catogryId[i] + "," +
                        subCatogryId[i] + ",'" +
                        subCatogryName[i] + "'," +
                        imageId[i] + ")");
            }
            cursor.close();
        }

        // Fetch subcategories for selected category
        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM subcategory WHERE catogryId = " + selectedSubCatId, null);
        arrayList = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                subcategorylist item = new subcategorylist();
                item.setCatogryId(cursor.getInt(0));
                item.setSubCatogryId(cursor.getInt(1));
                item.setSubCatogryName(cursor.getString(2));
                item.setImageId(cursor.getInt(3));
                arrayList.add(item);
            }
        }
        cursor.close();

        subcategoryAdapter adapter = new subcategoryAdapter(arrayList, requireContext());
        recyclerView.setAdapter(adapter);
    }
}
