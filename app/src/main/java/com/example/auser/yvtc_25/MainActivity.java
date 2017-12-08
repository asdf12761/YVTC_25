package com.example.auser.yvtc_25;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class MainActivity extends AppCompatActivity {
    private MyDBHelper db = null;
    private Cursor cursor;
    private long myid = 1;
    private EditText et1, et2, et3;
    private Button btn_1, btn_2, btn_3;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et1 = (EditText) findViewById(R.id.et_name);
        et2 = (EditText) findViewById(R.id.et_phone);
        et3 = (EditText) findViewById(R.id.et_email);
        lv = (ListView) findViewById(R.id.listView);
        btn_1 = (Button) findViewById(R.id.btn_new);
        btn_2 = (Button) findViewById(R.id.btn_edit);
        btn_3 = (Button) findViewById(R.id.btn_delete);

        btn_1.setOnClickListener(listener);
        btn_2.setOnClickListener(listener);
        btn_3.setOnClickListener(listener);
        lv.setOnItemClickListener(ilistener);
        db = new MyDBHelper(this);
        db.open();
        cursor = db.getALL();
        UpdateAdapter(cursor);
    }

    private void UpdateAdapter(Cursor cursor) {
// TODO Auto-generated method stub
        if (cursor != null && cursor.getCount() > 0) {
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                    R.layout.mylayout, cursor, new String[] {
                    "name", "phome","email" }, new int[] { R.id.tv_name,
                    R.id.tv_phone,R.id.tv_email });
            lv.setAdapter(adapter);
        }
    }
    //ListView listener

    private OnItemClickListener ilistener = new OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
// TODO Auto-generated method stub
            myid=arg3;
            showdata(myid);
//將Cursor移動到id的位置 不過似乎沒加上這行對app也沒差
//cursor.moveToPosition(arg2);
        }
//依照id查詢資料
        private void showdata(long myid) {
// TODO Auto-generated method stub
            Cursor cursor = db.getsearchid(myid);
            et1.setText(cursor.getString(1));
            et2.setText(cursor.getString(2));
            et3.setText(cursor.getString(3));
            Log.d("mylog","serch："+ cursor.getString(0));
            Log.d("mylog","serch："+ cursor.getString(1));
            Log.d("mylog","serch："+ cursor.getString(2));
            Log.d("mylog","serch："+ cursor.getString(3));
        }
    };

    @Override
    protected void onDestroy() {
// TODO Auto-generated method stub
        super.onDestroy();
//app結束關閉db
        db.close();
    }
    //Button listener
    private OnClickListener listener = new OnClickListener() {
        @Override
        public void onClick(View v) {
// TODO Auto-generated method stub
            switch (v.getId()) {
//新增Button
                case R.id.btn_new: {
                    String name = et1.getText().toString();
                    String phome = et2.getText().toString();
                    String email = et3.getText().toString();
                    if (db.append(name, phome,email) > 0) {
                        cursor = db.getALL();
                        UpdateAdapter(cursor);
                        clearedit();
                    }
                    break;
                }
//修改Button
                case R.id.btn_edit: {
                    String name = et1.getText().toString();
                    String phome = et2.getText().toString();
                    String email = et3.getText().toString();
                    if (db.updata(myid, name, phome,email)) {
                        cursor = db.getALL();
                        UpdateAdapter(cursor);
                        clearedit();
                    }
                }
                break;
//刪除Button
                case R.id.btn_delete:
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setMessage(R.string.delele_confirm);
                    dialog.setNegativeButton(R.string.okay,new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            if (db.delete(myid)) {
                                cursor = db.getALL();
                                UpdateAdapter(cursor);
                                clearedit();
                            }
                        }
                    });
                    dialog.setNeutralButton(R.string.cancel,new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    });
                    dialog.show();
                    break;
            }
        }
        private void clearedit() {
// TODO Auto-generated method stub
            et1.setText("");
            et2.setText("");
            et3.setText("");
        }

    };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle(R.string.check)
                    .setMessage(R.string.exit_confirm)
                    .setPositiveButton(R.string.okay,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    finish();
                                }
                            })
                    .setNegativeButton(R.string.cancel,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub

                                }
                            }).show();
        }
        return true;
    }

}