package cn.ilell.ihome;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import cn.ilell.ihome.base.BaseSubPageActivity;
import cn.ilell.ihome.utils.SharedPreference;

/**
 * Created by xubowen on 16/10/2.
 */
public class HomeActivity extends BaseSubPageActivity {
    private Button btn1,btn2,btn3,btn4,btn5,btn6,btn7;
    private String b1,b2,b3,b4,b5,b6,b7;
    private SharedPreference sharedPreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        INIT(R.layout.activity_home,"匹配房间");
        sharedPreference=new SharedPreference(this);
        findID();
        initView();
    }
    private void findID(){
        btn1=(Button)findViewById(R.id.button1);
        btn2=(Button)findViewById(R.id.button2);
        btn3=(Button)findViewById(R.id.button3);
        btn4=(Button)findViewById(R.id.button4);
        btn5=(Button)findViewById(R.id.button5);
        btn6=(Button)findViewById(R.id.button6);
        btn7=(Button)findViewById(R.id.button7);
        b1=btn1.getText().toString();
        b2=btn2.getText().toString();
        b3=btn3.getText().toString();
        b4=btn4.getText().toString();
        b5=btn5.getText().toString();
        b6=btn6.getText().toString();
        b7=btn7.getText().toString();
    }
    private void initView(){
        final Intent mIntent=new Intent();
        mIntent.setClass(HomeActivity.this,PipeiActivity.class);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIntent.putExtra("id",btn1.getText().toString());
                mIntent.putExtra("ids","1");
                startActivityForResult(mIntent, 1000);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIntent.putExtra("id",btn2.getText().toString());
                mIntent.putExtra("ids","2");
                startActivityForResult(mIntent, 1001);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIntent.putExtra("id",btn3.getText().toString());
                mIntent.putExtra("ids","3");
                startActivityForResult(mIntent, 1002);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIntent.putExtra("id",btn4.getText().toString());
                mIntent.putExtra("ids","4");
                startActivityForResult(mIntent, 1003);
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIntent.putExtra("id",btn5.getText().toString());
                mIntent.putExtra("ids","5");
                startActivityForResult(mIntent, 1004);
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIntent.putExtra("id",btn6.getText().toString());
                mIntent.putExtra("ids","6");
                startActivityForResult(mIntent, 1005);
            }
        });
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIntent.putExtra("id",btn7.getText().toString());
                mIntent.putExtra("ids","7");
                startActivityForResult(mIntent, 1006);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;
        if (requestCode == 1000) {
            btn1.setText(data.getStringExtra("ii"));
            btn1.setClickable(false);
            sharedPreference.HOME1(b1,data.getStringExtra("ii"));
            sharedPreference.KeepFinish();
            //String[] ap=data.getStringExtra(btn1.getText().toString()).split("\\|");

        }else if(requestCode==1001){
            btn2.setText(data.getStringExtra("ii"));
            btn2.setClickable(false);
            sharedPreference.HOME2(b2,data.getStringExtra("ii"));
            sharedPreference.KeepFinish();
            //String[] ap=data.getStringExtra(btn2.getText().toString()).split("\\|");

        }else if(requestCode==1002){
            btn3.setText(data.getStringExtra("ii"));
            btn3.setClickable(false);
            sharedPreference.HOME3(b3,data.getStringExtra("ii"));
            sharedPreference.KeepFinish();
            //String[] ap=data.getStringExtra(btn3.getText().toString()).split("\\|");

        }else if(requestCode==1003){
            btn4.setText(data.getStringExtra("ii"));
            btn4.setClickable(false);
            sharedPreference.HOME4(b4,data.getStringExtra("ii"));
            sharedPreference.KeepFinish();
            //String[] ap=data.getStringExtra(btn4.getText().toString()).split("\\|");

        }else if(requestCode==1004){
            btn5.setText(data.getStringExtra("ii"));
            btn5.setClickable(false);
            sharedPreference.HOME5(b5,data.getStringExtra("ii"));
            sharedPreference.KeepFinish();
            //String[] ap=data.getStringExtra(btn5.getText().toString()).split("\\|");

        }else if(requestCode==1005){
            btn6.setText(data.getStringExtra("ii"));
            btn6.setClickable(false);
            sharedPreference.HOME6(b6,data.getStringExtra("ii"));
            sharedPreference.KeepFinish();
            //String[] ap=data.getStringExtra(btn6.getText().toString()).split("\\|");

        }else if(requestCode==1006){
            btn7.setText(data.getStringExtra("ii"));
            btn7.setClickable(false);
            sharedPreference.HOME7(b7,data.getStringExtra("ii"));
            sharedPreference.KeepFinish();
            //String[] ap=data.getStringExtra(btn7.getText().toString()).split("\\|");

        }

    }

}
