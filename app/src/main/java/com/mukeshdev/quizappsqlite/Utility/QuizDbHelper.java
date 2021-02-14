package com.mukeshdev.quizappsqlite.Utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mukeshdev.quizappsqlite.Models.Questions;
import com.mukeshdev.quizappsqlite.Utility.QuizContract.QuestionTable;

import java.util.ArrayList;

public class QuizDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Quiz.db";
    private static final int DATBASE_VERSION = 2;

    private SQLiteDatabase db;
    Questions q;


    public QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATBASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        this.db = db;

        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionTable.TABLE_NAME + " ( " +
                QuestionTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionTable.COLUMN_QUESTION + " TEXT, " +
                QuestionTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionTable.COLUMN_OPTION4 + " TEXT, " +
                QuestionTable.COLUMN_ANSWER_NR + " INTEGER, " +
                QuestionTable.COLUMN_CATEGORY + " TEXT" +
                ")";

        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillQuestionsTable();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + QuestionTable.TABLE_NAME);
        onCreate(db);

    }


    private void fillQuestionsTable() {

        q = new Questions("Computers : Android is what ?", "OS", "Drivers", "Software", "Hardware", 1, Questions.CATEGORY_COMPUTERS);
        addQuestions(q);


        q = new Questions("Computers : Full form of PC is ?", "OS", "Personal Computer", "Pocket Computer", "Hardware", 2, Questions.CATEGORY_COMPUTERS);
        addQuestions(q);


        q = new Questions("Computers : Windows is what ?", "Easy Software", "Hardware Device", "Operating System", "Hardware", 3, Questions.CATEGORY_COMPUTERS);
        addQuestions(q);


        q = new Questions("Computers : Unity is used for what ?", "Game Development", "Movie Making", "Firmware", "Hardware", 1, Questions.CATEGORY_COMPUTERS);
        addQuestions(q);


        q = new Questions("Computers : RAM stands for ", "Windows", "Drivers", "GUI", "Random Access Memory", 4, Questions.CATEGORY_COMPUTERS);
        addQuestions(q);


        q = new Questions("Computers : Chrome is what ?", "OS", "Browser", "Tool", "New Browser", 2, Questions.CATEGORY_COMPUTERS);
        addQuestions(q);


        q = new Questions("Maths : 5 + 5 ", "0", "10", "23", "None of these", 2, Questions.CATEGORY_MATHS);
        addQuestions(q);

        q = new Questions("Maths : 2 * 60 ", "0", "12", "120", "None of these", 3, Questions.CATEGORY_MATHS);
        addQuestions(q);

        q = new Questions("History : Bank is for what ?", "0", "10", "23", "Money", 4, Questions.CATEGORY_HISTORY);
        addQuestions(q);

        q = new Questions("English : Pharasal verb ?", "English Gram", "10", "23", "Money", 1, Questions.CATEGORY_ENGLISH);
        addQuestions(q);

        q = new Questions("English : Pharasal verb2 is  ?", "English Gram", "10", "English Gram 2", "Money", 3, Questions.CATEGORY_ENGLISH);
        addQuestions(q);

        q = new Questions("Graphics : GUI stands for -", "Graphics uniform interaction", "Graphical user interaction", "Graphical user interface", "None of the above", 3, Questions.CATEGORY_GRAPHICS);
        addQuestions(q);

    }

    private void addQuestions(Questions question) {

        ContentValues cv = new ContentValues();
        cv.put(QuestionTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionTable.COLUMN_OPTION4, question.getOption4());
        cv.put(QuestionTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        cv.put(QuestionTable.COLUMN_CATEGORY, question.getCategory());
        db.insert(QuestionTable.TABLE_NAME, null, cv);

    }


    public ArrayList<Questions> getAllQuestionsWithCategory(String category) {

        ArrayList<Questions> questionList = new ArrayList<>();
        db = getReadableDatabase();


        String Projection[] = {

                QuestionTable._ID,
                QuestionTable.COLUMN_QUESTION,
                QuestionTable.COLUMN_OPTION1,
                QuestionTable.COLUMN_OPTION2,
                QuestionTable.COLUMN_OPTION3,
                QuestionTable.COLUMN_OPTION4,
                QuestionTable.COLUMN_ANSWER_NR,
                QuestionTable.COLUMN_CATEGORY
        };


        String selection = QuestionTable.COLUMN_CATEGORY + " = ? ";
        String selectionArgs[] = {category};


        Cursor c = db.query(QuestionTable.TABLE_NAME,
                Projection,
                selection,
                selectionArgs,
                null,
                null,
                null);


        if (c.moveToFirst()) {
            do {

                Questions question = new Questions();
                question.setQuestion(c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION3)));
                question.setOption4(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION4)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_ANSWER_NR)));
                question.setCategory(c.getString(c.getColumnIndex(QuestionTable.COLUMN_CATEGORY)));

                questionList.add(question);

            } while (c.moveToNext());

        }
        c.close();
        return questionList;

    }


}
