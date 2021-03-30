package com.example.project_kanban_whiteboard;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;

public class NotesDBHelper extends SQLiteOpenHelper {
    private final String DB_NAME = "notes.db";
    private final String NOTES_TABLE = "notes";
    private final String COLUMNS_TABLE = "cols";

    public NotesDBHelper(@Nullable Context context, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "notes.db", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        List<MainActivity.Note> notes = new ArrayList<>();
        List<String> columns = new ArrayList<>();

        columns.add("Ice box");
        columns.add("Backlog");
        columns.add("Active");
        columns.add("Review");

        notes.add(new MainActivity.Note(1, "Back", "log", R.font.lemon_tuesday, R.color.green));
        notes.add(new MainActivity.Note(2, "A", "log", R.font.razmahont, R.color.pink));
        notes.add(new MainActivity.Note(0, "I", "log", R.font.spritegraffiti, R.color.yellow));
        notes.add(new MainActivity.Note(3, "RE", "log", R.font.lemon_tuesday, R.color.blue));
        notes.add(new MainActivity.Note(3, "RE2", "log", R.font.razmahont, R.color.yellow));

        db.execSQL("DROP TABLE IF EXISTS " + NOTES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + COLUMNS_TABLE);

        db.execSQL("CREATE TABLE " + NOTES_TABLE + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "col INTEGER, header TEXT, content TEXT, font INTEGER, color INTEGER);");
        db.execSQL("CREATE TABLE " + COLUMNS_TABLE + " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);");

        for (String col : columns) {
            db.execSQL("INSERT INTO " + COLUMNS_TABLE + " (name) VALUES (\"" + col + "\")");
        }

        for (MainActivity.Note note : notes) {
            db.execSQL("INSERT INTO " + NOTES_TABLE + " (col, header, content, font, color) VALUES (" + note.column + ", \"" + note.header + "\", \"" + note.content + "\"," + note.font + "," + note.color + ")");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NOTES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + COLUMNS_TABLE);
        onCreate(db);
    }

    public List<MainActivity.Note> readNotes() {
        SQLiteDatabase db = getReadableDatabase();
        List<MainActivity.Note> answer = new ArrayList<>();

        // Зададим условие для выборки - список столбцов
        // String[] projection = {"col", "header", "content", "font", "color"};

        // Делаем запрос
        Cursor cursor = db.query(
                NOTES_TABLE,   // таблица
                null,            // столбцы
                null,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки

        while (cursor.moveToNext()) {
            // Используем индекс для получения строки или числа
            answer.add(new MainActivity.Note(cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getInt(4),
                    cursor.getInt(5)));
        }

        cursor.close();

        return answer;
    }

    public List<String> readCols() {
        SQLiteDatabase db = getReadableDatabase();
        List<String> answer = new ArrayList<>();

        // Зададим условие для выборки - список столбцов
        // String[] projection = {"col", "header", "content", "font", "color"};

        // Делаем запрос
        Cursor cursor = db.query(
                COLUMNS_TABLE,   // таблица
                null,            // столбцы
                null,                  // столбцы для условия WHERE
                null,                  // значения для условия WHERE
                null,                  // Don't group the rows
                null,                  // Don't filter by row groups
                null);                   // порядок сортировки

        while (cursor.moveToNext()) {
            // Используем индекс для получения строки или числа
            answer.add(cursor.getString(1));
        }

        cursor.close();

        return answer;
    }

    public void saveData(List<MainActivity.Note> notes, List<String> columns) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + NOTES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + COLUMNS_TABLE);

        db.execSQL("CREATE TABLE " + NOTES_TABLE + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "col INTEGER, header TEXT, content TEXT, font INTEGER, color INTEGER);");
        db.execSQL("CREATE TABLE " + COLUMNS_TABLE + " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT);");

        for (String col : columns) {
            db.execSQL("INSERT INTO " + COLUMNS_TABLE + " (name) VALUES (\"" + col + "\")");
        }

        for (MainActivity.Note note : notes) {
            db.execSQL("INSERT INTO " + NOTES_TABLE + " (col, header, content, font, color) VALUES (" + note.column + ", \"" + note.header + "\", \"" + note.content + "\"," + note.font + "," + note.color + ")");
        }
    }
}
