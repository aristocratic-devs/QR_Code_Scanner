package aristocratic.barcodescanner.qrscanner.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import aristocratic.barcodescanner.qrscanner.model.BARCODE_QR_History;

import static java.util.Collections.reverse;

public class BARCODE_QR_Database extends SQLiteOpenHelper {

    private static final String DATABASE = "qrcode";

    private static final String TABLE_SCANNED = "scanned";
    private static final String COL_SCANNED_DATA = "decoded";
    private static final String COL_SCANNED_type = "type";
    private static final String COL_SCANNED_time = "updated";

    private SQLiteDatabase sqLiteDatabase;

    public BARCODE_QR_Database(@Nullable Context context) {
        super(context, DATABASE, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_SCANNED + "(" + COL_SCANNED_DATA + " text," + COL_SCANNED_type + " text," + COL_SCANNED_time + " long)");
    }

    public void open() {
        sqLiteDatabase = getWritableDatabase();
    }

    public void close() {
        sqLiteDatabase.close();
    }


    public void insert(BARCODE_QR_History history) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_SCANNED_DATA, history.getContent());
        contentValues.put(COL_SCANNED_type, history.getType());
        contentValues.put(COL_SCANNED_time, history.getTime());
        sqLiteDatabase.insert(TABLE_SCANNED, null, contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public ArrayList<BARCODE_QR_History> getAllHistory() {
        ArrayList<BARCODE_QR_History> histories = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(TABLE_SCANNED, null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                histories.add(new BARCODE_QR_History(cursor.getString(cursor.getColumnIndex(COL_SCANNED_DATA)), cursor.getString(cursor.getColumnIndex(COL_SCANNED_type)), cursor.getLong(cursor.getColumnIndex(COL_SCANNED_time))));
            }
            cursor.close();
        }
        reverse(histories);
        return histories;
    }
}
