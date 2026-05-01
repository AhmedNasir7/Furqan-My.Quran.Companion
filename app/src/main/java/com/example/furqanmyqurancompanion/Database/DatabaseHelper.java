package com.example.furqanmyqurancompanion.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.furqanmyqurancompanion.Model.Ayah_Data;
import com.example.furqanmyqurancompanion.Model.Surah_Metadata;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "QuranCache.db";
    private static final int DATABASE_VERSION = 3;

    // Table Surahs
    private static final String TABLE_SURAHS = "surahs";
    private static final String COL_SURAH_ID = "id";
    private static final String COL_SURAH_ARABIC_NAME = "arabic_name";
    private static final String COL_SURAH_ENGLISH_NAME = "english_name";
    private static final String COL_SURAH_ENGLISH_MEANING = "english_meaning";
    private static final String COL_SURAH_AYAH_COUNT = "ayah_count";

    // Table Ayahs
    private static final String TABLE_AYAHS = "ayahs";
    private static final String COL_AYAH_GLOBAL_ID = "global_id";
    private static final String COL_AYAH_SURAH_ID = "surah_id";
    private static final String COL_AYAH_NUMBER_IN_SURAH = "number_in_surah";
    private static final String COL_AYAH_ARABIC_TEXT = "arabic_text";
    private static final String COL_AYAH_TRANSLATION = "translation";
    private static final String COL_AYAH_JUZ = "juz";
    private static final String COL_AYAH_PAGE = "page";
    private static final String COL_AYAH_MANZIL = "manzil";
    private static final String COL_AYAH_RUKU = "ruku";
    private static final String COL_AYAH_HIZB = "hizb";
    private static final String COL_AYAH_SAJDA = "sajda";

    // Table Bookmarks (New)
    private static final String TABLE_BOOKMARKS = "bookmarks";
    private static final String COL_BOOKMARK_USER_ID = "user_id";
    private static final String COL_BOOKMARK_AYAH_ID = "ayah_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createSurahTable = "CREATE TABLE " + TABLE_SURAHS + " (" +
                COL_SURAH_ID + " INTEGER PRIMARY KEY," +
                COL_SURAH_ARABIC_NAME + " TEXT," +
                COL_SURAH_ENGLISH_NAME + " TEXT," +
                COL_SURAH_ENGLISH_MEANING + " TEXT," +
                COL_SURAH_AYAH_COUNT + " INTEGER" +
                ")";

        String createAyahTable = "CREATE TABLE " + TABLE_AYAHS + " (" +
                COL_AYAH_GLOBAL_ID + " INTEGER PRIMARY KEY," +
                COL_AYAH_SURAH_ID + " INTEGER," +
                COL_AYAH_NUMBER_IN_SURAH + " INTEGER," +
                COL_AYAH_ARABIC_TEXT + " TEXT," +
                COL_AYAH_TRANSLATION + " TEXT," +
                COL_AYAH_JUZ + " INTEGER," +
                COL_AYAH_PAGE + " INTEGER," +
                COL_AYAH_MANZIL + " INTEGER," +
                COL_AYAH_RUKU + " INTEGER," +
                COL_AYAH_HIZB + " INTEGER," +
                COL_AYAH_SAJDA + " INTEGER" +
                ")";

        String createBookmarkTable = "CREATE TABLE " + TABLE_BOOKMARKS + " (" +
                COL_BOOKMARK_USER_ID + " TEXT," +
                COL_BOOKMARK_AYAH_ID + " INTEGER," +
                "PRIMARY KEY (" + COL_BOOKMARK_USER_ID + ", " + COL_BOOKMARK_AYAH_ID + ")" +
                ")";

        db.execSQL(createSurahTable);
        db.execSQL(createAyahTable);
        db.execSQL(createBookmarkTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_BOOKMARKS + " (" +
                    COL_BOOKMARK_USER_ID + " TEXT," +
                    COL_BOOKMARK_AYAH_ID + " INTEGER," +
                    "PRIMARY KEY (" + COL_BOOKMARK_USER_ID + ", " + COL_BOOKMARK_AYAH_ID + ")" +
                    ")");
            // Optionally migrate data from ayahs.is_bookmarked if needed, 
            // but we don't know the userId for existing bookmarks.
            // For now, just drop the column from ayahs if possible, or just ignore it.
        }
    }

    public void addSurahMetadata(Surah_Metadata surah) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_SURAH_ID, surah.getSurah_number());
        values.put(COL_SURAH_ARABIC_NAME, surah.getSurah_arabic_name());
        values.put(COL_SURAH_ENGLISH_NAME, surah.getSurah_english_name());
        values.put(COL_SURAH_ENGLISH_MEANING, surah.getSurah_english_meaning());
        values.put(COL_SURAH_AYAH_COUNT, surah.getSurah_ayahs());
        db.insertWithOnConflict(TABLE_SURAHS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public List<Surah_Metadata> getAllSurahMetadata() {
        List<Surah_Metadata> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SURAHS + " ORDER BY " + COL_SURAH_ID + " ASC", null);
        if (cursor.moveToFirst()) {
            do {
                Surah_Metadata surah = new Surah_Metadata(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getInt(4)
                );
                list.add(surah);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public void addAyah(Ayah_Data ayah, int surahId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_AYAH_GLOBAL_ID, ayah.getGlobalVerseNumber());
        values.put(COL_AYAH_SURAH_ID, surahId);
        values.put(COL_AYAH_NUMBER_IN_SURAH, ayah.getVerseNumber());
        values.put(COL_AYAH_ARABIC_TEXT, ayah.getArabicText());
        values.put(COL_AYAH_TRANSLATION, ayah.getTranslation());
        values.put(COL_AYAH_JUZ, ayah.getJuzNumber());
        values.put(COL_AYAH_PAGE, ayah.getPageNumber());
        values.put(COL_AYAH_MANZIL, ayah.getManzilNumber());
        values.put(COL_AYAH_RUKU, ayah.getRukuNumber());
        values.put(COL_AYAH_HIZB, ayah.getHizbQuarter());
        values.put(COL_AYAH_SAJDA, ayah.isSajda() ? 1 : 0);
        db.insertWithOnConflict(TABLE_AYAHS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public List<Ayah_Data> getAyahsForSurah(int surahId, String userId) {
        List<Ayah_Data> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT a.*, " +
                "(SELECT 1 FROM " + TABLE_BOOKMARKS + " b WHERE b." + COL_BOOKMARK_AYAH_ID + " = a." + COL_AYAH_GLOBAL_ID + " AND b." + COL_BOOKMARK_USER_ID + " = ?) as is_bookmarked " +
                "FROM " + TABLE_AYAHS + " a " +
                "WHERE a." + COL_AYAH_SURAH_ID + " = ? " +
                "ORDER BY a." + COL_AYAH_NUMBER_IN_SURAH + " ASC";
        
        Cursor cursor = db.rawQuery(query, new String[]{userId, String.valueOf(surahId)});
        if (cursor.moveToFirst()) {
            do {
                Ayah_Data ayah = new Ayah_Data();
                ayah.setGlobalVerseNumber(cursor.getInt(0));
                ayah.setVerseNumber(cursor.getInt(2));
                ayah.setArabicText(cursor.getString(3));
                ayah.setTranslation(cursor.getString(4));
                ayah.setJuzNumber(cursor.getInt(5));
                ayah.setPageNumber(cursor.getInt(6));
                ayah.setManzilNumber(cursor.getInt(7));
                ayah.setRukuNumber(cursor.getInt(8));
                ayah.setHizbQuarter(cursor.getInt(9));
                ayah.setIsSajda(cursor.getInt(10) == 1);
                ayah.setBookmarked(cursor.getInt(11) == 1);
                list.add(ayah);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public List<Ayah_Data> getAyahsForJuz(int juzId, String userId) {
        List<Ayah_Data> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT a.*, " +
                "(SELECT 1 FROM " + TABLE_BOOKMARKS + " b WHERE b." + COL_BOOKMARK_AYAH_ID + " = a." + COL_AYAH_GLOBAL_ID + " AND b." + COL_BOOKMARK_USER_ID + " = ?) as is_bookmarked " +
                "FROM " + TABLE_AYAHS + " a " +
                "WHERE a." + COL_AYAH_JUZ + " = ? " +
                "ORDER BY a." + COL_AYAH_GLOBAL_ID + " ASC";
        
        Cursor cursor = db.rawQuery(query, new String[]{userId, String.valueOf(juzId)});
        if (cursor.moveToFirst()) {
            do {
                Ayah_Data ayah = new Ayah_Data();
                ayah.setGlobalVerseNumber(cursor.getInt(0));
                ayah.setVerseNumber(cursor.getInt(2));
                ayah.setArabicText(cursor.getString(3));
                ayah.setTranslation(cursor.getString(4));
                ayah.setJuzNumber(cursor.getInt(5));
                ayah.setPageNumber(cursor.getInt(6));
                ayah.setManzilNumber(cursor.getInt(7));
                ayah.setRukuNumber(cursor.getInt(8));
                ayah.setHizbQuarter(cursor.getInt(9));
                ayah.setIsSajda(cursor.getInt(10) == 1);
                ayah.setBookmarked(cursor.getInt(11) == 1);
                list.add(ayah);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public boolean isQuranLoaded() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_AYAHS, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count >= 6236;
    }

    public boolean isSurahMetadataLoaded() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_SURAHS, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count >= 114;
    }

    public void toggleBookmark(int globalId, boolean isBookmarked, String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (isBookmarked) {
            ContentValues values = new ContentValues();
            values.put(COL_BOOKMARK_USER_ID, userId);
            values.put(COL_BOOKMARK_AYAH_ID, globalId);
            db.insertWithOnConflict(TABLE_BOOKMARKS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        } else {
            db.delete(TABLE_BOOKMARKS, COL_BOOKMARK_USER_ID + " = ? AND " + COL_BOOKMARK_AYAH_ID + " = ?", 
                    new String[]{userId, String.valueOf(globalId)});
        }
    }

    public List<Ayah_Data> getBookmarkedAyahs(String userId) {
        List<Ayah_Data> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT a.*, s.arabic_name, s.english_name, s.english_meaning, s.ayah_count " +
                "FROM " + TABLE_AYAHS + " a " +
                "INNER JOIN " + TABLE_BOOKMARKS + " b ON a." + COL_AYAH_GLOBAL_ID + " = b." + COL_BOOKMARK_AYAH_ID + " " +
                "LEFT JOIN " + TABLE_SURAHS + " s ON a." + COL_AYAH_SURAH_ID + " = s." + COL_SURAH_ID + " " +
                "WHERE b." + COL_BOOKMARK_USER_ID + " = ? ORDER BY a." + COL_AYAH_GLOBAL_ID + " ASC";
        
        Cursor cursor = db.rawQuery(query, new String[]{userId});
        if (cursor.moveToFirst()) {
            do {
                Ayah_Data ayah = new Ayah_Data();
                ayah.setGlobalVerseNumber(cursor.getInt(0));
                int surahId = cursor.getInt(1);
                ayah.setVerseNumber(cursor.getInt(2));
                ayah.setArabicText(cursor.getString(3));
                ayah.setTranslation(cursor.getString(4));
                ayah.setJuzNumber(cursor.getInt(5));
                ayah.setPageNumber(cursor.getInt(6));
                ayah.setManzilNumber(cursor.getInt(7));
                ayah.setRukuNumber(cursor.getInt(8));
                ayah.setHizbQuarter(cursor.getInt(9));
                ayah.setIsSajda(cursor.getInt(10) == 1);
                ayah.setBookmarked(true);

                if (surahId != -1 && !cursor.isNull(12)) {
                    Surah_Metadata surah = new Surah_Metadata(
                            surahId,
                            cursor.getString(12), // arabic_name
                            cursor.getString(13), // english_name
                            cursor.getString(14), // english_meaning
                            cursor.getInt(15)     // ayah_count
                    );
                    ayah.setSurah(surah);
                }
                list.add(ayah);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public int getBookmarkCount(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_BOOKMARKS + " WHERE " + COL_BOOKMARK_USER_ID + " = ?", new String[]{userId});
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public boolean isBookmarked(int globalId, String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE_BOOKMARKS + " WHERE " + COL_BOOKMARK_AYAH_ID + " = ? AND " + COL_BOOKMARK_USER_ID + " = ?", 
                new String[]{String.valueOf(globalId), userId});
        boolean isBookmarked = cursor.moveToFirst();
        cursor.close();
        return isBookmarked;
    }
}
