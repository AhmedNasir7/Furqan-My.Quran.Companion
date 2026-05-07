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
    private static final int DATABASE_VERSION = 5;

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

    // Table Progress (New)
    private static final String TABLE_PROGRESS = "reading_progress";
    private static final String COL_PROGRESS_USER_ID = "user_id";
    private static final String COL_PROGRESS_TYPE = "type"; // "surah" or "juz"
    private static final String COL_PROGRESS_ID = "id"; // surah number or juz number
    private static final String COL_PROGRESS_AYAH_ID = "ayah_id"; // global_id of last read ayah
    private static final String COL_PROGRESS_TIMESTAMP = "timestamp";

    // Table User Activity (New)
    private static final String TABLE_ACTIVITY = "user_activity";
    private static final String COL_ACTIVITY_USER_ID = "user_id";
    private static final String COL_ACTIVITY_TOTAL_DAYS = "total_days";
    private static final String COL_ACTIVITY_CURRENT_STREAK = "current_streak";
    private static final String COL_ACTIVITY_LAST_DATE = "last_date"; // Format: YYYY-MM-DD

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.enableWriteAheadLogging();
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

        String createProgressTable = "CREATE TABLE " + TABLE_PROGRESS + " (" +
                COL_PROGRESS_USER_ID + " TEXT," +
                COL_PROGRESS_TYPE + " TEXT," +
                COL_PROGRESS_ID + " INTEGER," +
                COL_PROGRESS_AYAH_ID + " INTEGER," +
                COL_PROGRESS_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP," +
                "PRIMARY KEY (" + COL_PROGRESS_USER_ID + ")" +
                ")";

        String createActivityTable = "CREATE TABLE " + TABLE_ACTIVITY + " (" +
                COL_ACTIVITY_USER_ID + " TEXT PRIMARY KEY," +
                COL_ACTIVITY_TOTAL_DAYS + " INTEGER DEFAULT 0," +
                COL_ACTIVITY_CURRENT_STREAK + " INTEGER DEFAULT 0," +
                COL_ACTIVITY_LAST_DATE + " TEXT" +
                ")";

        db.execSQL(createSurahTable);
        db.execSQL(createAyahTable);
        db.execSQL(createBookmarkTable);
        db.execSQL(createProgressTable);
        db.execSQL(createActivityTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_BOOKMARKS + " (" +
                    COL_BOOKMARK_USER_ID + " TEXT," +
                    COL_BOOKMARK_AYAH_ID + " INTEGER," +
                    "PRIMARY KEY (" + COL_BOOKMARK_USER_ID + ", " + COL_BOOKMARK_AYAH_ID + ")" +
                    ")");
        }
        if (oldVersion < 4) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_PROGRESS + " (" +
                    COL_PROGRESS_USER_ID + " TEXT," +
                    COL_PROGRESS_TYPE + " TEXT," +
                    COL_PROGRESS_ID + " INTEGER," +
                    COL_PROGRESS_AYAH_ID + " INTEGER," +
                    COL_PROGRESS_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP," +
                    "PRIMARY KEY (" + COL_PROGRESS_USER_ID + ")" +
                    ")");
        }
        if (oldVersion < 5) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ACTIVITY + " (" +
                    COL_ACTIVITY_USER_ID + " TEXT PRIMARY KEY," +
                    COL_ACTIVITY_TOTAL_DAYS + " INTEGER DEFAULT 0," +
                    COL_ACTIVITY_CURRENT_STREAK + " INTEGER DEFAULT 0," +
                    COL_ACTIVITY_LAST_DATE + " TEXT" +
                    ")");
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

    public void addSurahsMetadata(List<Surah_Metadata> surahs) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (Surah_Metadata surah : surahs) {
                ContentValues values = new ContentValues();
                values.put(COL_SURAH_ID, surah.getSurah_number());
                values.put(COL_SURAH_ARABIC_NAME, surah.getSurah_arabic_name());
                values.put(COL_SURAH_ENGLISH_NAME, surah.getSurah_english_name());
                values.put(COL_SURAH_ENGLISH_MEANING, surah.getSurah_english_meaning());
                values.put(COL_SURAH_AYAH_COUNT, surah.getSurah_ayahs());
                db.insertWithOnConflict(TABLE_SURAHS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public List<Surah_Metadata> getAllSurahMetadata() {
        List<Surah_Metadata> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_SURAHS + " ORDER BY " + COL_SURAH_ID + " ASC", null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(COL_SURAH_ID);
            int arabicNameIndex = cursor.getColumnIndex(COL_SURAH_ARABIC_NAME);
            int englishNameIndex = cursor.getColumnIndex(COL_SURAH_ENGLISH_NAME);
            int englishMeaningIndex = cursor.getColumnIndex(COL_SURAH_ENGLISH_MEANING);
            int ayahCountIndex = cursor.getColumnIndex(COL_SURAH_AYAH_COUNT);

            do {
                Surah_Metadata surah = new Surah_Metadata(
                        cursor.getInt(idIndex),
                        cursor.getString(arabicNameIndex),
                        cursor.getString(englishNameIndex),
                        cursor.getString(englishMeaningIndex),
                        cursor.getInt(ayahCountIndex)
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

    public void addAyahs(List<Ayah_Data> ayahs, int surahId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (Ayah_Data ayah : ayahs) {
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
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
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
            int globalIdIndex = cursor.getColumnIndex(COL_AYAH_GLOBAL_ID);
            int numberInSurahIndex = cursor.getColumnIndex(COL_AYAH_NUMBER_IN_SURAH);
            int arabicTextIndex = cursor.getColumnIndex(COL_AYAH_ARABIC_TEXT);
            int translationIndex = cursor.getColumnIndex(COL_AYAH_TRANSLATION);
            int juzIndex = cursor.getColumnIndex(COL_AYAH_JUZ);
            int pageIndex = cursor.getColumnIndex(COL_AYAH_PAGE);
            int manzilIndex = cursor.getColumnIndex(COL_AYAH_MANZIL);
            int rukuIndex = cursor.getColumnIndex(COL_AYAH_RUKU);
            int hizbIndex = cursor.getColumnIndex(COL_AYAH_HIZB);
            int sajdaIndex = cursor.getColumnIndex(COL_AYAH_SAJDA);
            int bookmarkedIndex = cursor.getColumnIndex("is_bookmarked");

            do {
                Ayah_Data ayah = new Ayah_Data();
                ayah.setGlobalVerseNumber(cursor.getInt(globalIdIndex));
                ayah.setVerseNumber(cursor.getInt(numberInSurahIndex));
                ayah.setArabicText(cursor.getString(arabicTextIndex));
                ayah.setTranslation(cursor.getString(translationIndex));
                ayah.setJuzNumber(cursor.getInt(juzIndex));
                ayah.setPageNumber(cursor.getInt(pageIndex));
                ayah.setManzilNumber(cursor.getInt(manzilIndex));
                ayah.setRukuNumber(cursor.getInt(rukuIndex));
                ayah.setHizbQuarter(cursor.getInt(hizbIndex));
                ayah.setIsSajda(cursor.getInt(sajdaIndex) == 1);
                ayah.setBookmarked(bookmarkedIndex != -1 && !cursor.isNull(bookmarkedIndex) && cursor.getInt(bookmarkedIndex) == 1);
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
            int globalIdIndex = cursor.getColumnIndex(COL_AYAH_GLOBAL_ID);
            int numberInSurahIndex = cursor.getColumnIndex(COL_AYAH_NUMBER_IN_SURAH);
            int arabicTextIndex = cursor.getColumnIndex(COL_AYAH_ARABIC_TEXT);
            int translationIndex = cursor.getColumnIndex(COL_AYAH_TRANSLATION);
            int juzIndex = cursor.getColumnIndex(COL_AYAH_JUZ);
            int pageIndex = cursor.getColumnIndex(COL_AYAH_PAGE);
            int manzilIndex = cursor.getColumnIndex(COL_AYAH_MANZIL);
            int rukuIndex = cursor.getColumnIndex(COL_AYAH_RUKU);
            int hizbIndex = cursor.getColumnIndex(COL_AYAH_HIZB);
            int sajdaIndex = cursor.getColumnIndex(COL_AYAH_SAJDA);
            int bookmarkedIndex = cursor.getColumnIndex("is_bookmarked");

            do {
                Ayah_Data ayah = new Ayah_Data();
                ayah.setGlobalVerseNumber(cursor.getInt(globalIdIndex));
                ayah.setVerseNumber(cursor.getInt(numberInSurahIndex));
                ayah.setArabicText(cursor.getString(arabicTextIndex));
                ayah.setTranslation(cursor.getString(translationIndex));
                ayah.setJuzNumber(cursor.getInt(juzIndex));
                ayah.setPageNumber(cursor.getInt(pageIndex));
                ayah.setManzilNumber(cursor.getInt(manzilIndex));
                ayah.setRukuNumber(cursor.getInt(rukuIndex));
                ayah.setHizbQuarter(cursor.getInt(hizbIndex));
                ayah.setIsSajda(cursor.getInt(sajdaIndex) == 1);
                ayah.setBookmarked(bookmarkedIndex != -1 && !cursor.isNull(bookmarkedIndex) && cursor.getInt(bookmarkedIndex) == 1);
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
            int globalIdIndex = cursor.getColumnIndex(COL_AYAH_GLOBAL_ID);
            int surahIdIndex = cursor.getColumnIndex(COL_AYAH_SURAH_ID);
            int numberInSurahIndex = cursor.getColumnIndex(COL_AYAH_NUMBER_IN_SURAH);
            int arabicTextIndex = cursor.getColumnIndex(COL_AYAH_ARABIC_TEXT);
            int translationIndex = cursor.getColumnIndex(COL_AYAH_TRANSLATION);
            int juzIndex = cursor.getColumnIndex(COL_AYAH_JUZ);
            int pageIndex = cursor.getColumnIndex(COL_AYAH_PAGE);
            int manzilIndex = cursor.getColumnIndex(COL_AYAH_MANZIL);
            int rukuIndex = cursor.getColumnIndex(COL_AYAH_RUKU);
            int hizbIndex = cursor.getColumnIndex(COL_AYAH_HIZB);
            int sajdaIndex = cursor.getColumnIndex(COL_AYAH_SAJDA);
            
            int sArabicNameIndex = cursor.getColumnIndex(COL_SURAH_ARABIC_NAME);
            int sEnglishNameIndex = cursor.getColumnIndex(COL_SURAH_ENGLISH_NAME);
            int sEnglishMeaningIndex = cursor.getColumnIndex(COL_SURAH_ENGLISH_MEANING);
            int sAyahCountIndex = cursor.getColumnIndex(COL_SURAH_AYAH_COUNT);

            do {
                Ayah_Data ayah = new Ayah_Data();
                ayah.setGlobalVerseNumber(cursor.getInt(globalIdIndex));
                int surahId = cursor.getInt(surahIdIndex);
                ayah.setVerseNumber(cursor.getInt(numberInSurahIndex));
                ayah.setArabicText(cursor.getString(arabicTextIndex));
                ayah.setTranslation(cursor.getString(translationIndex));
                ayah.setJuzNumber(cursor.getInt(juzIndex));
                ayah.setPageNumber(cursor.getInt(pageIndex));
                ayah.setManzilNumber(cursor.getInt(manzilIndex));
                ayah.setRukuNumber(cursor.getInt(rukuIndex));
                ayah.setHizbQuarter(cursor.getInt(hizbIndex));
                ayah.setIsSajda(cursor.getInt(sajdaIndex) == 1);
                ayah.setBookmarked(true);

                if (surahId != -1 && sArabicNameIndex != -1 && !cursor.isNull(sArabicNameIndex)) {
                    Surah_Metadata surah = new Surah_Metadata(
                            surahId,
                            cursor.getString(sArabicNameIndex),
                            cursor.getString(sEnglishNameIndex),
                            cursor.getString(sEnglishMeaningIndex),
                            cursor.getInt(sAyahCountIndex)
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

    public void updateReadingProgress(String userId, String type, int id, int lastAyahGlobalId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_PROGRESS_USER_ID, userId);
        values.put(COL_PROGRESS_TYPE, type);
        values.put(COL_PROGRESS_ID, id);
        values.put(COL_PROGRESS_AYAH_ID, lastAyahGlobalId);
        values.put(COL_PROGRESS_TIMESTAMP, System.currentTimeMillis());
        db.insertWithOnConflict(TABLE_PROGRESS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public Cursor getReadingProgress(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PROGRESS + " WHERE " + COL_PROGRESS_USER_ID + " = ?", new String[]{userId});
    }

    public void updateActivity(String userId, String todayDate, boolean isYesterday) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ACTIVITY + " WHERE " + COL_ACTIVITY_USER_ID + " = ?", new String[]{userId});

        if (cursor.moveToFirst()) {
            int lastDateIndex = cursor.getColumnIndex(COL_ACTIVITY_LAST_DATE);
            int totalDaysIndex = cursor.getColumnIndex(COL_ACTIVITY_TOTAL_DAYS);
            int currentStreakIndex = cursor.getColumnIndex(COL_ACTIVITY_CURRENT_STREAK);

            String lastDate = cursor.getString(lastDateIndex);
            if (todayDate.equals(lastDate)) {
                cursor.close();
                return; // Already updated today
            }

            int totalDays = cursor.getInt(totalDaysIndex);
            int currentStreak = cursor.getInt(currentStreakIndex);

            totalDays++;
            if (isYesterday) {
                currentStreak++;
            } else {
                currentStreak = 1;
            }

            ContentValues values = new ContentValues();
            values.put(COL_ACTIVITY_TOTAL_DAYS, totalDays);
            values.put(COL_ACTIVITY_CURRENT_STREAK, currentStreak);
            values.put(COL_ACTIVITY_LAST_DATE, todayDate);
            db.update(TABLE_ACTIVITY, values, COL_ACTIVITY_USER_ID + " = ?", new String[]{userId});
        } else {
            ContentValues values = new ContentValues();
            values.put(COL_ACTIVITY_USER_ID, userId);
            values.put(COL_ACTIVITY_TOTAL_DAYS, 1);
            values.put(COL_ACTIVITY_CURRENT_STREAK, 1);
            values.put(COL_ACTIVITY_LAST_DATE, todayDate);
            db.insert(TABLE_ACTIVITY, null, values);
        }
        cursor.close();
    }

    public Cursor getActivity(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ACTIVITY + " WHERE " + COL_ACTIVITY_USER_ID + " = ?", new String[]{userId});
    }
}
