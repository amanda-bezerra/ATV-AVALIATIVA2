package br.com.amanda.atv_avaliativa2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ManagerDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cinema.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "IngressosCinema";
    private static final String COL_ID = "ID";
    private static final String COL_NOME_FILME = "NomeFilme";
    private static final String COL_HORARIO_SESSAO = "HorarioSessao";
    private static final String COL_QUANTIDADE = "Quantidade";

    public ManagerDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NOME_FILME + " TEXT, " +
                COL_HORARIO_SESSAO + " TEXT, " +
                COL_QUANTIDADE + " INTEGER)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertIngresso(String nomeFilme, String horarioSessao, int quantidade) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_NOME_FILME, nomeFilme);
        contentValues.put(COL_HORARIO_SESSAO, horarioSessao);
        contentValues.put(COL_QUANTIDADE, quantidade);

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Cursor getAllIngressos() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
}
