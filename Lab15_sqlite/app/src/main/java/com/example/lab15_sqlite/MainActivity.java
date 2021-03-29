package com.example.lab15_sqlite;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText edit1;

    Integer i;
    String[] from;
    int[] to;
    static ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        from = new String[]{"Name"};
        to =new int[] {R.id.textView};
        Button btnadd = findViewById(R.id.add);
        final EditText editadd = findViewById(R.id.editTextTextPersonName);

        edit1 = findViewById(R.id.editTextTextPersonName);
        SharedPreferences save = getSharedPreferences("SAVE",0);
        edit1.setText(save.getString("text",""));

        SQLiteDatabase db = openOrCreateDatabase("DBname", MODE_PRIVATE,
                null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Mytable5 (_id INTEGER PRIMARY KEY AUTOINCREMENT, Name VARCHAR);");
        //db.execSQL("INSERT INTO Mytable5 VALUES ('2','some text 2');");

        Cursor cursor = db.rawQuery("SELECT * FROM Mytable5", null);
        cursor.moveToFirst();
        Log.d("ME", cursor.getString(cursor.getColumnIndex("Name")));

        cursor = db.rawQuery("SELECT * FROM Mytable5", null);
        i=cursor.getCount()+1;
        if (cursor.getCount()>0) {
            MyCursorAdapter scAdapter = new MyCursorAdapter(MainActivity.this,R.layout.list_item,cursor,from,to, this);
            listView = findViewById(R.id.list);
            listView.setAdapter(scAdapter);
        }

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db =
                        openOrCreateDatabase("DBname",MODE_PRIVATE,null);
                Cursor cursor2 = db.rawQuery("SELECT * FROM Mytable5", null);
                i=cursor2.getCount()+1;
                for (int k=1;k<=i;k++) {
                    Cursor cursor3 = db.rawQuery("SELECT * FROM Mytable5 WHERE _id="+k+"", null);
                    if (cursor3.getCount()==0) {
                        i=k;
                        break;
                    }
                }


                db.execSQL("INSERT INTO Mytable5 VALUES ('"+i+"','"+editadd.getText().toString()+"');");
//i++;
                Cursor cursor = db.rawQuery("SELECT * FROM Mytable5", null);
                MyCursorAdapter scAdapter = new MyCursorAdapter(MainActivity.this,R.layout.list_item,cursor,from,to, MainActivity.this);
                listView = findViewById(R.id.list);
                listView.setAdapter(scAdapter);
                db.close();
                Toast.makeText(findViewById(R.id.list).getContext(),"a row added to the table",Toast.LENGTH_LONG).show();
            }
                                  });

        db.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        if (id == R.id.about) {

            AlertDialog.Builder dialog = new
                    AlertDialog.Builder(MainActivity.this);
            try {
                dialog.setMessage(getTitle().toString() + " версия " +
                        getPackageManager().getPackageInfo(getPackageName(), 0).versionName +
                        "\r\n\nПрограмма с примером испльзования настроек и СУБД\r\n\n" +
                        " Автор - Мишуткин Александр, гр. БПИ-192");
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            dialog.setTitle("О программе");
            dialog.setNeutralButton("OK", new
                    DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            dialog.setIcon(R.mipmap.ic_launcher_round);
            AlertDialog alertDialog = dialog.create();
            alertDialog.show();

        }

        if (id == R.id.settings) {
            Intent intent = new Intent(MainActivity.this,
                    SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        SharedPreferences save = getSharedPreferences("SAVE",0);
        SharedPreferences.Editor editor = save.edit(); //создаём
        editor.putString("text",edit1.getText().toString()); //сохраняем
        editor.commit(); //применение редактирования shared preferences
        super.onStop();
    }
}