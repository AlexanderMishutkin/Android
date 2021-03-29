package com.example.project_kanban_whiteboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    List<Button> buttonList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonList.add(findViewById(R.id.add_column));
        buttonList.add(findViewById(R.id.add_sticker));
        buttonList.add(findViewById(R.id.delete_column));
        buttonList.add(findViewById(R.id.delete_sticker));
        buttonList.add(findViewById(R.id.edit_column));
        buttonList.add(findViewById(R.id.edit_sticker));

        String a = "";
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            a = "landscape";
        }
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            a = "portrait";
        }

        int BOOKSHELF_ROWS = 5;
        int BOOKSHELF_COLUMNS = 5;

        View table[][] = new Button[4][6];

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {
                Button btn = new Button(this);
                btn.setText("Проверочка");
                btn.setWidth(0);
                btn.setTypeface(ResourcesCompat.getFont(this, R.font.spritegraffiti));
                //btn.setTextSize(btn.getTextSize());
                table[i][j] = btn;
                buttonList.add(btn);
            }
        }

        displayTable(table);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Random r = new Random();
        buttonList.forEach((btn)->{
            Bitmap bmp;
            if (r.nextInt(3) == 0) {
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.paper_ripped1);
            } else {
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.paper_ripped2);
            }
            switch (r.nextInt(4)){
                case 0:
                    bmp = flipX(bmp);
                    break;
                case 1:
                    bmp = flipY(bmp);
                    break;
                case 3:
                    bmp = flipY(bmp);
                    bmp = flipX(bmp);
                    break;
                default:
                    break;
            }

            bmp = Bitmap.createScaledBitmap(bmp, btn.getWidth(), btn.getHeight(), true);
            btn.setBackground(new BitmapDrawable(null, bmp));
        });
    }

    Bitmap flipX(Bitmap src)
    {
        Matrix m = new Matrix();
        m.preScale(-1, 1);
        Bitmap dst = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), m, false);
        dst.setDensity(DisplayMetrics.DENSITY_DEFAULT);
        return dst;
    }

    Bitmap flipY(Bitmap src)
    {
        Matrix m = new Matrix();
        m.preScale(1, -1);
        Bitmap dst = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), m, false);
        dst.setDensity(DisplayMetrics.DENSITY_DEFAULT);
        return dst;
    }

    private void displayTable(View[][] table) {
        TableLayout table1 = findViewById(R.id.table1);
        TableLayout table2 = findViewById(R.id.table2);
        table1.removeAllViewsInLayout();
        table2.removeAllViewsInLayout();

        for (int i = 0; i < table.length; i++) {
            TableRow tableRow = new TableRow(this);
            //tableRow.setBackgroundColor(Color.CYAN);

            for (int j = 0; j < 3; j++) {

                tableRow.addView(table[i][j], j);
            }

            table1.addView(tableRow, i);
        }

        for (int i = 0; i < table.length; i++) {
            TableRow tableRow = new TableRow(this);
            //tableRow.setBackgroundColor(Color.CYAN);

            for (int j = 3; j < 6; j++) {
                tableRow.addView(table[i][j], j - 3);
            }

            table2.addView(tableRow, i);
        }
    }

}