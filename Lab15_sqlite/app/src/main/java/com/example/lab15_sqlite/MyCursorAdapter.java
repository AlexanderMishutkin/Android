package com.example.lab15_sqlite;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static android.content.Context.MODE_PRIVATE;

public class MyCursorAdapter extends SimpleCursorAdapter {
    private int layout_;
    private Cursor cursor;
    String[] from;
    int[] to;
    AppCompatActivity app;

    ListView listView;
    EditText edit2;

    public MyCursorAdapter(Context context, int layout, Cursor
            c, String[] from, int[] to, AppCompatActivity app) {
        super(context, layout, c, from, to);
        this.app = app;
        layout_ = layout;
        cursor = c;
    }

    @Override
    public void bindView(View view, Context _context, Cursor
            cursor) {
        String data =
                cursor.getString(cursor.getColumnIndex("Name"));
        final int[] id = {cursor.getInt(cursor.getColumnIndex("_id"))};
        TextView text =
                view.findViewById(R.id.textView);
        text.setText(data);
        Button butdel = view.findViewById(R.id.button);
        Button butedit = view.findViewById(R.id.button2);
        listView = MainActivity.listView;
        butdel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db =
                        _context.openOrCreateDatabase("DBname", MODE_PRIVATE, null);
                db.execSQL("DELETE FROM Mytable5 WHERE _id=" + id[0] + "");
                Cursor cursor = db.rawQuery("SELECT * FROM Mytable5", null);
                from = new String[]{"Name"};
                to = new int[]{R.id.textView};
                MyCursorAdapter scAdapter = new
                        MyCursorAdapter(_context, R.layout.list_item, cursor, from, to, app);
                listView.setAdapter(scAdapter);
                db.close();
                Toast.makeText(_context, "row deleted from the db id[0] =" + id[0], Toast.LENGTH_LONG).show();
            }
        });
        butedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new
                        AlertDialog.Builder(_context);
                dialog.setMessage("Enter new value:");
                dialog.setTitle("Changing the item");
//                LayoutInflater inflater = new
//                        LayoutInflater(_context) {
//                            @Override
//                            public LayoutInflater cloneInContext(Context context) {
//                                return null;
//                            }
//                        };
                LayoutInflater inflater = (LayoutInflater)app.getSystemService (Context.LAYOUT_INFLATER_SERVICE);

                View dialogview = inflater.inflate(R.layout.dialog, null);
                dialog.setView(dialogview);
                edit2 = dialogview.findViewById(R.id.editTextTextPersonName2);
                edit2.setText(text.getText().toString());
                dialog.setNeutralButton("OK", new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int i) {
                                SQLiteDatabase db = _context.openOrCreateDatabase("DBname", MODE_PRIVATE, null);
                                db.execSQL("UPDATE Mytable5 SET Name='" + edit2.getText().toString() + "' WHERE _id=" + id[0] + "");
                                Cursor cursor = db.rawQuery("SELECT * FROM Mytable5", null);
                                from = new String[]{"Name"};
                                to = new int[]{R.id.textView};
                                MyCursorAdapter scAdapter = new MyCursorAdapter(_context, R.layout.list_item, cursor, from, to, app);
                                listView.setAdapter(scAdapter);
                                db.close();
                                Toast.makeText(_context, "row edited from the db row id[0] =" + id[0], Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        });
                dialog.setIcon(R.mipmap.ic_launcher_round);
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
            }
        });

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(layout_, parent, false);
        return view;
    }
}
