package com.example.kotlin_sqlite_example

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.time.chrono.ChronoLocalDateTime

//context 데이터베이스명 버전이 필요함
//상속받은 helper에게 전달
class SqliteHelper(context : Context, name : String, version : Int) : SQLiteOpenHelper(context,name,null,version){
    override fun onCreate(db: SQLiteDatabase?) {
        val create = "create table memo(" +
                "num integer primary key,"+
                "content text, "+
                "datetime integer"+
                ")"
        db?.execSQL(create)
    }

    //버전 정보가 변경되었을 때 현재 생성된 db와 비교해 버전 정보가 높으면 호출된다.
    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }

    //ContentValues 사용해서 값을 넣는다.
    fun insert(memo : Memo){
        val values = ContentValues()
        values.put("content",memo.content)
        values.put("datetime",memo.datetime)

        val wd = writableDatabase
        wd.insert("memo",null,values)
        wd.close() //사용한 후 닫아주기
    }
    @SuppressLint("Range")
    fun selectMemo(): MutableList<Memo>{
        val list = mutableListOf<Memo>()
        val select = "select * from memo"
        var rd = readableDatabase
        //row query담아서 실행시 커서형태로 값이 변환된다
        //커서란? : 데이터 셋을 처리할때 현재 위치를 포함하는 데이터 요소
        //커서 사용 시 반복문을 통해 하나씩 처리가능하다.
        val cursor = rd.rawQuery(select,null)
        while (cursor.moveToNext()){
            val no = cursor.getLong(cursor.getColumnIndex("num"))
            val content = cursor.getString(cursor.getColumnIndex("content"))
            val datetime = cursor.getLong(cursor.getColumnIndex("datetime"))
            list.add(Memo(no, content, datetime))
        }
        cursor.close()
        rd.close()
        return list
    }
}

data class Memo(var no: Long?,var content : String, var datetime: Long)