package com.example.scipy.Catogery;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.scipy.DBHelper;
import com.example.scipy.R;

import java.util.ArrayList;

public class CategoryFragment extends Fragment {

    RecyclerView recyclerView;

    int[] catogryId = {1, 2, 3, 4};
    String[] catogry = {"Mouse", "KeyBoard", "Cpu", "Moniter"};
    int[] imageId = {R.drawable.mouse, R.drawable.keyboard, R.drawable.cpu, R.drawable.moniter};

    ArrayList<catogerylist> arrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catogry, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        DBHelper db = new DBHelper(requireContext());
        SQLiteDatabase sqLiteDatabase = db.getWritableDatabase();
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS catogery(catogryId INTEGER PRIMARY KEY AUTOINCREMENT,catogry TEXT,imageId INTEGER)");

        for (int i = 0; i < catogry.length; i++) {
            Cursor cursor = sqLiteDatabase.rawQuery("Select * FROM catogery WHERE catogry='" + catogry[i] + "'", null);
            if (cursor.getCount() == 0) {
                sqLiteDatabase.execSQL("INSERT INTO catogery VALUES(" + catogryId[i] + ",'" + catogry[i] + "'," + imageId[i] + ")");
            }
            cursor.close();
        }

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM catogery", null);
        if (cursor.getCount() > 0) {
            arrayList = new ArrayList<>();
            while (cursor.moveToNext()) {
                catogerylist item = new catogerylist();
                item.setCatogryId(cursor.getInt(0));
                item.setCatogry(cursor.getString(1));
                item.setImageId(cursor.getInt(2));
                arrayList.add(item);
            }
            cursor.close();

            catogryAdapter adapter = new catogryAdapter(arrayList, requireContext());
            recyclerView.setAdapter(adapter);
        }

        return view;
    }
}
