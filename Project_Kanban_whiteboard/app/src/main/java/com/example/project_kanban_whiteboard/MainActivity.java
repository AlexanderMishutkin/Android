package com.example.project_kanban_whiteboard;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Button add_column;
    Button add_sticker;
    Button delete_column;
    Button delete_sticker;
    Button edit_column;
    Button edit_sticker;

    int BUTTON_WIDTH = 200;
    int BUTTON_HIGH = 30;

    List<Button> buttonList = new ArrayList<>();
    List<Note> notes = new ArrayList<>();
    List<String> columns = new ArrayList<>();
    View[][] table = new View[4][6];
    Integer[][] tints = new Integer[4][6];

    int selected_column = -1;
    int selected_row = -1;
    boolean many_selected = false;
    List<Button> selectedButtons = new ArrayList<>();
    List<Integer> selectedTints = new ArrayList<>();

    static public class Note {
        public int column;
        public int font;
        public String header;
        public String content;
        public int color;

        public Note() {

        }

        public Note(int column, String header, String content, int font, int color) {
            this.column = column;
            this.header = header;
            this.content = content;
            this.font = font;
            this.color = color;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add_column = findViewById(R.id.add_column);
        add_sticker = findViewById(R.id.add_sticker);
        delete_column = findViewById(R.id.delete_column);
        delete_sticker = findViewById(R.id.delete_sticker);
        edit_column = findViewById(R.id.edit_column);
        edit_sticker = findViewById(R.id.edit_sticker);
//        buttonList.add(add_column);
//        buttonList.add(add_sticker);
//        buttonList.add(delete_column);
//        buttonList.add(delete_sticker);
//        buttonList.add(edit_column);
//        buttonList.add(edit_sticker);

        delete_sticker.setOnClickListener((v) -> {
            if (selected_row != -1 && selected_column != -1) {
                Note note = (Note) table[selected_row][selected_column].getTag();
                removeSelection();
                notes.remove(note);
                fireDataChanged();
                styleButtons();
            }
        });
        delete_column.setOnClickListener((v) -> {
            if (selected_row != -1 && selected_column != -1 && many_selected) {
                String c = (String) table[selected_row][selected_column].getTag();
                removeSelection();
                int i = columns.indexOf(c);
                notes.removeIf((note -> note.column == i));
                columns.remove(c);
                notes.forEach(note -> {
                    if (note.column > i) {
                        note.column--;
                    }
                });
                fireDataChanged();
                styleButtons();
            }
        });

        add_sticker.setOnClickListener((v) -> {
            Note newNote;
            if (selected_row != -1 && selected_column != -1) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.note_dialog, null);
                dialog.setView(view);
                AlertDialog d = dialog.create();

                newNote = new Note();
                EditText h = view.findViewById(R.id.editTextTextPersonName);
                EditText m = view.findViewById(R.id.editTextTextPersonName2);
                RadioButton lem = view.findViewById(R.id.radioButton);
                RadioButton raz = view.findViewById(R.id.radioButton2);
                RadioButton spr = view.findViewById(R.id.radioButton3);
                RadioButton pink = view.findViewById(R.id.radioButton4);
                RadioButton blue = view.findViewById(R.id.radioButton5);
                RadioButton green = view.findViewById(R.id.radioButton6);
                RadioButton yellow = view.findViewById(R.id.radioButton7);

                dialog.setPositiveButton("ADD", (dialog1, which) -> {
                    newNote.column = selected_column;
                    removeSelection();
                    newNote.header = h.getText().toString();
                    newNote.content = m.getText().toString();
                    newNote.font = R.font.lemon_tuesday;
                    if (raz.isChecked()) {
                        newNote.font = R.font.razmahont;
                    }
                    if (spr.isChecked()) {
                        newNote.font = R.font.spritegraffiti;
                    }
                    newNote.color = R.color.pink;
                    if (blue.isChecked()) {
                        newNote.color = R.color.blue;
                    }
                    if (green.isChecked()) {
                        newNote.color = R.color.green;
                    }
                    if (yellow.isChecked()) {
                        newNote.color = R.color.yellow;
                    }
                    notes.add(newNote);
                    fireDataChanged();
                    styleButtons();
                });
                dialog.show();
            }
        });
        add_column.setOnClickListener((v) -> {
            if (selected_column != -1) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.column_dialog, null);
                dialog.setView(view);
                AlertDialog d = dialog.create();
                EditText c = view.findViewById(R.id.editTextTextPersonName3);

                dialog.setPositiveButton("ADD", (dialog1, which) -> {
                    int col = selected_column;
                    removeSelection();
                    notes.forEach((note) -> {
                        if (note.column >= col) {
                            note.column++;
                        }
                    });
                    columns.add(col, c.getText().toString());
                    fireDataChanged();
                    styleButtons();
                });
                dialog.show();
            }
        });

        edit_sticker.setOnClickListener((v) -> {
            if (selected_row != -1 && selected_column != -1) {
                Note newNote = (Note) table[selected_row][selected_column].getTag();
                final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.note_dialog, null);
                dialog.setView(view);
                AlertDialog d = dialog.create();

                EditText h = view.findViewById(R.id.editTextTextPersonName);
                EditText m = view.findViewById(R.id.editTextTextPersonName2);
                RadioButton lem = view.findViewById(R.id.radioButton);
                RadioButton raz = view.findViewById(R.id.radioButton2);
                RadioButton spr = view.findViewById(R.id.radioButton3);
                RadioButton pink = view.findViewById(R.id.radioButton4);
                RadioButton blue = view.findViewById(R.id.radioButton5);
                RadioButton green = view.findViewById(R.id.radioButton6);
                RadioButton yellow = view.findViewById(R.id.radioButton7);
                h.getText().append(newNote.header);
                m.getText().append(newNote.content);

                dialog.setPositiveButton("EDIT", (dialog1, which) -> {
                    newNote.column = selected_column;
                    removeSelection();
                    newNote.header = h.getText().toString();
                    newNote.content = m.getText().toString();
                    if (lem.isChecked()) {
                        newNote.font = R.font.lemon_tuesday;
                    }
                    if (raz.isChecked()) {
                        newNote.font = R.font.razmahont;
                    }
                    if (spr.isChecked()) {
                        newNote.font = R.font.spritegraffiti;
                    }
                    if (pink.isChecked()) {
                        newNote.color = R.color.pink;
                    }
                    if (blue.isChecked()) {
                        newNote.color = R.color.blue;
                    }
                    if (green.isChecked()) {
                        newNote.color = R.color.green;
                    }
                    if (yellow.isChecked()) {
                        newNote.color = R.color.yellow;
                    }
                    fireDataChanged();
                    styleButtons();
                });
                dialog.show();
            }
        });
        edit_column.setOnClickListener((v) -> {
            String column = (String) table[selected_row][selected_column].getTag();
            if (selected_row != -1 && selected_column != -1) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.column_dialog, null);
                dialog.setView(view);
                AlertDialog d = dialog.create();

                EditText c = view.findViewById(R.id.editTextTextPersonName3);
                c.getText().append(column);

                dialog.setPositiveButton("EDIT", (dialog1, which) -> {
                    int col = selected_column;
                    removeSelection();
                    columns.set(col, c.getText().toString());
                    fireDataChanged();
                    styleButtons();
                });
                dialog.show();
            }
        });

        String a = "";
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            a = "landscape";
        }
        if (getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            a = "portrait";
        }

        int BOOKSHELF_ROWS = 5;
        int BOOKSHELF_COLUMNS = 5;

        loadTable();

        fireDataChanged();

        removeSelection();
    }

    public void fireDataChanged() {
        removeSelection();
        table = new View[4][6];

        for (int i = 0; i < columns.size(); i++) {
            String label = columns.get(i);
            Button btn = new Button(this);
            btn.setText(label);
            btn.setWidth(0);
            btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, btn.getTextSize() * 1.2f);
            btn.setBackgroundColor(getResources().getColor(R.color.white, getResources().newTheme()));
            table[0][i] = btn;
            tints[0][i] = getResources().getColor(R.color.white, getResources().newTheme());
            int finalI = i;
            btn.setOnClickListener(v -> selectColumn(finalI));
            btn.setTag(columns.get(i));
            buttonList.add(btn);
        }

        for (Note note : notes) {
            int j = note.column;

            Button btn = new Button(this);
            btn.setText(String.format("%s%s%s", note.header, System.lineSeparator(), note.content));
            btn.setWidth(0);
            btn.setTypeface(ResourcesCompat.getFont(this, note.font));
            btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, btn.getTextSize() * 1.2f);
            btn.setBackgroundColor(getResources().getColor(note.color, getResources().newTheme()));

            for (int i = 0; i < table.length; i++) {
                if (table[i][j] == null) {
                    table[i][j] = btn;
                    tints[i][j] = getResources().getColor(note.color, getResources().newTheme());
                    buttonList.add(btn);
                    int finalI = i;
                    OnSwipeTouchListener listener = new OnSwipeTouchListener(this,
                            columns, btn, this);
                    btn.setOnTouchListener((v, e) -> {
                        selectOne(finalI, j);
                        listener.onTouch(v, e);
                        return false;
                    });
                    btn.setTag(note);
                    break;
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {
                if (table[i][j] == null) {
                    table[i][j] = new Button(this);
                    table[i][j].setBackgroundColor(Color.argb(0, 1, 1, 1));
                    ((Button) table[i][j]).setTextColor(Color.argb(0, 1, 1, 1));
                    ((Button) table[i][j]).setOnClickListener((v)->removeSelection());

                    if (((Button) table[i][j]).getBackground() instanceof ColorDrawable) {
                        int w = ((Button) table[i][j]).getWidth();
                        ColorDrawable buttonColor = (ColorDrawable) ((Button) table[i][j]).getBackground();
                        tints[i][j] = buttonColor.getColor();
                    }
//                    Button btn = new Button(this);
//                    btn.setText("Проверка");
//                    btn.setWidth(0);
//                    btn.setTypeface(ResourcesCompat.getFont(this, R.font.lemon_tuesday));
//                    btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, btn.getTextSize() * 1.2f);
//                    table[i][j] = btn;
//                    buttonList.add(btn);

                }
            }
        }

        displayTable(table);

        db.saveData(notes, columns);
    }

    boolean flag = false;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (buttonList.size() > 0) {
            BUTTON_WIDTH = buttonList.get(0).getWidth();
            BUTTON_HIGH = buttonList.get(0).getHeight();
        }
        if (!flag) {
            flag = true;
            styleButtons();
        }
    }

    void styleButtons() {
        Random r = new Random();
        buttonList.forEach((btn) -> {
            Bitmap bmp;
            if (r.nextInt(3) == 0) {
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.paper_ripped1);
            } else {
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.paper_ripped2);
            }
            switch (r.nextInt(4)) {
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

            bmp = Bitmap.createScaledBitmap(bmp, BUTTON_WIDTH, BUTTON_HIGH, true);
            if (btn.getBackground() instanceof ColorDrawable) {
                int w = btn.getWidth();
                ColorDrawable buttonColor = (ColorDrawable) btn.getBackground();
                btn.setBackground(new BitmapDrawable(null, bmp));
                btn.getBackground().setTint(buttonColor.getColor());
                btn.setWidth(w);
            } else {
                int w = btn.getWidth();
                btn.setBackground(new BitmapDrawable(null, bmp));
                btn.setWidth(w);
            }
            btn.refreshDrawableState();
        });
    }

    Bitmap flipX(Bitmap src) {
        Matrix m = new Matrix();
        m.preScale(-1, 1);
        Bitmap dst = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), m, false);
        dst.setDensity(DisplayMetrics.DENSITY_DEFAULT);
        return dst;
    }

    Bitmap flipY(Bitmap src) {
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
                TableRow.LayoutParams params = (TableRow.LayoutParams) table[i][j].getLayoutParams();
                params.setMargins(5, 5, 5, 5);
                table[i][j].setLayoutParams(params);
            }

            table1.addView(tableRow, i);

        }

        for (int i = 0; i < table.length; i++) {
            TableRow tableRow = new TableRow(this);
            //tableRow.setBackgroundColor(Color.CYAN);

            for (int j = 3; j < 6; j++) {
                tableRow.addView(table[i][j], j - 3);
                TableRow.LayoutParams params = (TableRow.LayoutParams) table[i][j].getLayoutParams();
                params.setMargins(5, 5, 5, 5);
                table[i][j].setLayoutParams(params);
            }

            table2.addView(tableRow, i);
        }
    }

    NotesDBHelper db;

    private void loadTable() {
        db = new NotesDBHelper(this, null, 1);
        columns = db.readCols();
        notes = db.readNotes();
    }

    void removeSelection() {
        selected_column = columns.size();
        selected_row = -1;
        many_selected = false;

        for (int i = 0; i < selectedButtons.size(); i++) {
            selectedButtons.get(i).getBackground().setTint((int) selectedTints.get(i));
            selectedButtons.get(i).refreshDrawableState();
        }

        selectedButtons = new ArrayList<>();
        selectedTints = new ArrayList<>();

        add_column.setEnabled(true);
        add_sticker.setEnabled(false);
        delete_column.setEnabled(false);
        delete_sticker.setEnabled(false);
        edit_column.setEnabled(false);
        edit_sticker.setEnabled(false);
    }

    private void selectOne(int row, int col) {
        removeSelection();
        selected_row = row;
        selected_column = col;

        selectedButtons.add((Button) table[row][col]);
        selectedTints.add(tints[row][col]);
        ((Button) table[row][col]).getBackground().setTint(getResources().getColor(R.color.selected, getResources().newTheme()));
        table[row][col].refreshDrawableState();

        add_sticker.setEnabled(true);
        edit_sticker.setEnabled(true);
        delete_sticker.setEnabled(true);
    }

    private void selectColumn(int col) {
        removeSelection();
        selected_row = 0;
        selected_column = col;
        many_selected = true;

        for (int i = 0; i < table.length; i++) {
            if (table[i][col] instanceof Button) {
                selectedButtons.add((Button) table[i][col]);
                selectedTints.add(tints[i][col]);
                ((Button) table[i][col]).getBackground().setTint(getResources().getColor(R.color.selected, getResources().newTheme()));
                table[i][col].refreshDrawableState();
            }
        }

        add_sticker.setEnabled(true);
        add_column.setEnabled(true);
        edit_column.setEnabled(true);
        delete_column.setEnabled(true);
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
                        "\r\n\nApp with board emulator\r\n\n" +
                        " Автор - Мишуткин Александр, гр. БПИ-192");
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            dialog.setTitle("About");
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

        return super.onOptionsItemSelected(item);
    }
}