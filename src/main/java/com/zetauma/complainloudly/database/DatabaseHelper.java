package main.java.com.zetauma.complainloudly.database;

import java.util.ArrayList;
import java.util.List;

import main.java.com.zetauma.complainloudly.model.Complaint;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "ComplainLoudly";
	private static final int DATABASE_VERSION = 1;

	private static final String TABLE_COMPLAINT = "complaint";

	private static final String COMPLAINT_COLUMN_USER = "user";
	private static final String COMPLAINT_COLUMN_IMAGE = "image";
	private static final String COMPLAINT_COLUMN_COMMENTARY = "commentary";
	private static final String COMPLAINT_COLUMN_LATITUDE = "latitude";
	private static final String COMPLAINT_COLUMN_LONGITUDE = "longitude";

	private static final String COMPLAINT_TABLE_CREATE = "CREATE TABLE " + TABLE_COMPLAINT + " ("
					+ COMPLAINT_COLUMN_USER + " TEXT,"
					+ COMPLAINT_COLUMN_IMAGE + " TEXT,"
					+ COMPLAINT_COLUMN_COMMENTARY + " TEXT,"
					+ COMPLAINT_COLUMN_LATITUDE + " TEXT,"
					+ COMPLAINT_COLUMN_LONGITUDE + " TEXT);";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(COMPLAINT_TABLE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS" + TABLE_COMPLAINT);
		onCreate(db);
	}

	public void insertComplaint(Complaint complaint) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(COMPLAINT_COLUMN_USER, complaint.getUser());
		values.put(COMPLAINT_COLUMN_IMAGE, complaint.getImage());
		values.put(COMPLAINT_COLUMN_COMMENTARY, complaint.getCommentary());
		values.put(COMPLAINT_COLUMN_LATITUDE, complaint.getLatitude());
		values.put(COMPLAINT_COLUMN_LONGITUDE, complaint.getLongitude());

		db.insert(TABLE_COMPLAINT, null, values);
	}

	public List<Complaint> selectAllComplaints() {
		List<Complaint> complaints = new ArrayList<Complaint>();
		SQLiteDatabase db = this.getReadableDatabase();

		String sql = "SELECT  * FROM " + TABLE_COMPLAINT;
		Cursor cursor = db.rawQuery(sql, null);

		if (cursor.moveToFirst()) {
			do {
				Complaint complaint = new Complaint();

				complaint.setUser(cursor.getString(cursor
						.getColumnIndex(COMPLAINT_COLUMN_USER)));
				complaint.setImage(cursor.getString(cursor
						.getColumnIndex(COMPLAINT_COLUMN_IMAGE)));
				complaint.setCommentary(cursor.getString(cursor
						.getColumnIndex(COMPLAINT_COLUMN_COMMENTARY)));
				complaint.setLatitude(Double.parseDouble(cursor
						.getString(cursor.getColumnIndex(COMPLAINT_COLUMN_LATITUDE))));
				complaint.setLatitude(Double.parseDouble(cursor
						.getString(cursor.getColumnIndex(COMPLAINT_COLUMN_LONGITUDE))));

				complaints.add(complaint);
			} while (cursor.moveToNext());
		}
		return complaints;
	}

	public void updateComplaint() {

	}

	public void deleteComplaint() {

	}

}
