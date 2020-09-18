package com.example.parth.bingo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private  static  final int REQUEST_ENABLE_BT=0;
    private static final int PERMISSION_BLUETOOTH=9;
    private TextView comp,user;
    private Button play,random;
    private static boolean play_flag=false;
    private boolean[] flag=new boolean[26];
    private int id[][]=new int[26][26];
    private int[][] user_id=new int[26][26];
    private int[][] user_value=new int[26][26];
    private int[][] comp_value=new int[26][26];
    private boolean[] user_Flag=new boolean[26];
    private boolean user_flag[][]=new boolean[26][26];
    private boolean comp_flag[][]= new boolean[26][26];
    private  BluetoothAdapter bluetoothAdapter;
    private BluetoothHeadset bluetoothHeadset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        flagClear();
        create();
        idSetter();
        userCreate();
         checkPermission(Manifest.permission.BLUETOOTH,PERMISSION_BLUETOOTH);


}

    public void checkPermission(String permission,int requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] { permission },
                    requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==PERMISSION_BLUETOOTH) {
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch(resultCode) {
            case REQUEST_ENABLE_BT:
                if(resultCode==RESULT_OK){
                    Toast.makeText(this,"Permmission Granted",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(this,"Permission decline",Toast.LENGTH_LONG).show();
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //CALL ON APP LAUNCH
    public void flagClear() {
        int i,j;
        for(i=1;i<=25;i++) {
            for(j=1;j<=25;j++) {
                user_flag[i][j]=false;
                comp_flag[i][j]=false;
                comp_value[i][j]=0;
                user_value[i][j]=0;


            }
        }
    }
    // USER CAN GENERATE RANDOM VALUE THROUGH CLICK ON RANDOM BUTTON
    public void random(View v) {
        int i, j, r;
        EditText temp;
        String s ;
        for (i = 1; i < 26; i++)
            flag[i] = false;

        for (i = 1; i <= 5; i++) {
            for (j = 1; j <= 5; j++) {
                s = "R" + i + "C" + j;
                System.out.println(s);
                temp = findViewById(getResources().getIdentifier(s, "id", getPackageName()));

                temp.setTextSize(18);
                Random rand = new Random();
                do {
                    r = rand.nextInt(25) + 1;
                } while (flag[r] == true);
                flag[r] = true;
                temp.setText(Integer.toString(r));
                //t.setText((char)5);

            }
        }
    }

    // CHECK VALIDATIONS WHILE CLICK ON PLAY BUTTON
    public void errorCheck(View view) {
        int i,j;
        int value[][]=new int[6][6];
        boolean flag=true;
        int check[]=new int[26];
        String s=null;
        System.out.println(view.getId());
        outer:
        for(i=1;i<=5;i++) {
            for(j=1;j<=5;j++) {
                s = "R" + i + "C" + j;
                user = (EditText) findViewById(getResources().getIdentifier(s, "id", getPackageName()));
                if (!user.getText().toString().equals("")) {
                    value[i][j] = Integer.parseInt(user.getText().toString());
                    if (value[i][j] <= 0 || value[i][j] >= 26) {
                        flag = false;
                        Toast.makeText(MainActivity.this, "Please insert Value between 0 to 26", Toast.LENGTH_LONG).show();
                        break outer;
                    } else {
                        check[value[i][j]]++;
                        if (check[value[i][j]] > 1) {
                            flag = false;
                            Toast.makeText(MainActivity.this, "Remove Duplicate Value " + value[i][j], Toast.LENGTH_SHORT).show();
                            break outer;
                        }
                    }


                }
                else {
                    flag = false;
                    Toast.makeText(MainActivity.this, "Please Insert All Values", Toast.LENGTH_SHORT).show();
                    break outer;
                }
            }
        }
        if(flag) {
            play_flag=true;
            play=findViewById(R.id.play);
            random=findViewById(R.id.random);
            play.setVisibility(View.INVISIBLE);
            random.setVisibility(View.INVISIBLE);
            for(i=1;i<=5;i++) {
                for (j = 1; j <= 5; j++) {
                    s = "R" + i + "C" + j;
                    user = (EditText) findViewById(getResources().getIdentifier(s, "id", getPackageName()));
                    user.setFocusable(false);
                    user.setClickable(true);
                    user.setClickable(true);
                    user.setTextColor(Color.BLACK);
                }
            }
            for(i=1;i<=5;i++) {
                for(j=1;j<=5;j++) {
                    String user_string = "R" + i + "C" + j;
                    user = (EditText) findViewById(getResources().getIdentifier(user_string, "id", getPackageName()));
                    user_value[i][j]=Integer.parseInt(user.getText().toString());
                    String comp_string = "r" + i + "c" + j;
                    comp =  findViewById(getResources().getIdentifier(comp_string, "id", getPackageName()));
                    comp_value[i][j]=Integer.parseInt(comp.getText().toString());

                }
            }
            Random rand=new Random();
            boolean b=rand.nextBoolean();
            if(b)
                comp_algorithm();
        }

    }
    //CALL ON APP LANUCH FOR USER INTERFACE
    void userCreate() {
        int i,j;
        String s=null;
        for (i = 1; i < 26; i++)
            user_Flag[i] = false;
        for(i=1;i<=5;i++) {
            for (j = 1; j <= 5; j++) {
                s = "R" + i + "C" + j;
                comp = findViewById(getResources().getIdentifier(s, "id", getPackageName()));
                comp.setTextSize(18);

                comp.setBackground(comp.getBackground());
                if(i==j||j==6-i) {
                    comp.setBackgroundColor(Color.LTGRAY);
                }
                comp.setInputType(InputType.TYPE_CLASS_NUMBER);
                comp.setText(null);

                comp.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            }
        }



    }


    //GENERATE COMPUTER TABLE
    void create() {
        int i, j, r;
        String s = null;
        for (i = 1; i < 26; i++)
            flag[i] = false;

        for(i=1;i<=5;i++) {
            for (j = 1; j <= 5; j++) {
                s = "r" + i + "c" + j;

                comp = (TextView) findViewById(getResources().getIdentifier(s, "id", getPackageName()));
                id[i][j]=getResources().getIdentifier(s, "id", getPackageName());
                comp.setTextSize(18);
                comp.setBackgroundColor(Color.CYAN);
                Random rand=new Random();
                do {
                    r = rand.nextInt(25) + 1;
                }while(flag[r]==true);
                flag[r]=true;
                comp.setText(Integer.toString(r));
                comp.setTextColor(Color.BLACK);

            }

        }
    }

    // Actual Logic

    public void winner(View v) {

        user(v);

    }



    public void user(View v) {
        if(play_flag) {
            int value, i, j;
            boolean flag;
            user = findViewById(v.getId());
            value = Integer.parseInt(user.getText().toString());
            for (i = 1; i <= 5; i++) {
                for (j = 1; j <= 5; j++) {
                    if (value == user_value[i][j]) {
                        user_flag[i][j] = true;
                    }
                }
            }
            user.setCursorVisible(false);
            user.setText("");
            user.setHint("");
            user.setEnabled(false);
            flag = declare_winner_user();
            if (!flag) {


                for (i = 1; i <= 5; i++) {
                    for (j = 1; j <= 5; j++) {
                        comp = findViewById(id[i][j]);
                        if (!comp.getText().toString().equals("")) {
                            if (Integer.parseInt(comp.getText().toString()) == value) {
                                comp.setText("");
                                comp_flag[i][j] = true;
                                if (!checkCompWin())
                                    comp_algorithm();
                                else
                                    playAgain();

                            }
                        }
                    }
                }
            } else
                playAgain();

        }
        else
            Toast.makeText(getApplicationContext(),"Please first click on Play Button",Toast.LENGTH_SHORT).show();
    }
    public boolean checkCompWin() {
        int i, j;
        int count = 0, win_count = 0;

        for(i=1;i<=5;i++) {
            if(comp_flag[i][i]==true) {
                count++;
            }
        }
        if(count==5) {
            win_count++;
        }
        count=0;
        for(i=1;i<=5;i++) {
            if(comp_flag[i][5-i+1]==true) {
                count++;
            }
        }
        if(count==5) {
            win_count++;
        }

        for(i=1;i<=5;i++) {
            count=0;
            for (j = 1; j <= 5; j++) {
                if(comp_flag[i][j]==true) {
                    count++;
                }

            }
            if(count==5) {
                win_count++;
            }
        }

        for(i=1;i<=5;i++) {
            count=0;
            for (j = 1; j <= 5; j++) {
                if(comp_flag[j][i]==true) {
                    count++;
                }

            }
            if(count==5) {
                win_count++;
            }
        }
        if(win_count>=5) {
            Toast.makeText(MainActivity.this,"Computer Win",Toast.LENGTH_LONG).show();
            TextView winner=findViewById(R.id.winner_text);
            winner.setText("COMPUTER WINS!");
            winner.setTextColor(Color.RED);
            return true;
        }
        else
            return false;

    }
    public void comp_algorithm() {
        int i=1,j=1,value,a,b,count=0,temp_count=0,temp_i=0,temp_j=0;
        boolean user_win_flag;
        Random rand=new Random();
        boolean win=checkCompWin();
        if(!win) {
            /*do {
                i=rand.nextInt(5)+1;
                j=rand.nextInt(5)+1;
            }while(comp_flag[i][j]);*/
            outer:
            {
                if(comp_flag[3][3]==false) {
                    i = 3;
                    j=3;
                    break outer;
                }
                if(comp_flag[1][1]==false) {
                    i=1;
                    j=1;
                    break outer;
                }
                if(comp_flag[1][5]==false) {
                    i=1;
                    j=5;
                    break outer;
                }
                if(comp_flag[5][1]==false) {
                    i=5;
                    j=1;
                    break outer;
                }

               for(a=1;a<=5;a++) {
                    if(comp_flag[a][a]==false) {
                        i = a;
                        j = a;
                        break outer;
                    }
                }
                /*for(a=1;a<=5;a++) {
                    if (comp_flag[a][5-a+1] == false) {
                        i = a;
                        j = 5-a+1;
                        break outer;

                    }
                }*/
                for(a=1;a<=5;a++) {
                    temp_count=0;
                    for(b=1;b<=5;b++) {
                        if(comp_flag[a][b]==true) {

                            temp_count++;
                        }
                        else {
                            temp_i=a;
                            temp_j=b;
                        }
                    }
                    if(count<temp_count) {
                        count=temp_count;
                        i=temp_i;
                        j=temp_j;
                    }
                }
                for(a=1;a<=5;a++) {
                    temp_count=0;
                    for(b=1;b<=5;b++) {
                        if(comp_flag[b][a]==true) {

                            temp_count++;
                        }
                        else{
                            temp_i=b;
                            temp_j=a;
                        }
                    }
                    if(count<temp_count) {
                        count=temp_count;
                        i=temp_i;
                        j=temp_j;


                    }
                }
                if(i==0&&j==0) {
                    for(i=1;i<=5;i++) {
                        for(j=1;j<=5;j++) {
                            if(comp_flag[i][j]==false){
                                break outer;
                            }
                        }
                    }
                }

            }


            System.out.println("i="+i+" j="+j);
            comp = findViewById(id[i][j]);
            value = Integer.parseInt(comp.getText().toString());
            Toast.makeText(MainActivity.this,"Computer: "+value,Toast.LENGTH_SHORT).show();
            TextView c=findViewById(R.id.comp_value);
            c.setText("Computer: "+value);
            c.setTextColor(Color.MAGENTA);
            comp.setText("");
            comp_flag[i][j] = true;
            if(!checkCompWin())
                for (i = 1; i <= 5; i++) {
                    for (j = 1; j <= 5; j++) {
                        if (value == user_value[i][j]) {
                            user = findViewById(user_id[i][j]);
                            user.setCursorVisible(false);
                            user.setText("");
                            user.setHint("");
                            user.setEnabled(false);
                            user_flag[i][j] = true;
                            user_win_flag=declare_winner_user();
                            if(user_win_flag) {
                                playAgain();
                            }
                        }

                    }
                }
            else
                playAgain();

        }
        else
            playAgain();

    }
    public void playAgain() {
        Button play;
        int i,j;
        Button random;
        play=findViewById(R.id.play);
        random=findViewById(R.id.random);
        play.setVisibility(View.INVISIBLE);
        random.setVisibility(View.INVISIBLE);
        for (i=1;i<=5;i++) {
            for(j=1;j<=5;j++) {
                user=findViewById(user_id[i][j]);
                user.setEnabled(false);
            }
        }
        Button reset=findViewById(R.id.reset);
        reset.setVisibility(View.VISIBLE);
    }
    public boolean declare_winner_user() {
        int i, j;
        TextView bingo=findViewById(R.id.bingo);
        int count = 0, win_count = 0;

        for(i=1;i<=5;i++) {
            if(user_flag[i][i]==true) {
                count++;
            }
        }
        if(count==5) {
            win_count++;
            for(i=1;i<=5;i++) {
                user=findViewById(user_id[i][i]);
                user.setBackgroundColor(Color.DKGRAY);
            }
        }
        count=0;
        for(i=1;i<=5;i++) {
            if(user_flag[i][5-i+1]==true) {
                count++;
            }
        }
        if(count==5) {
            win_count++;
            for(i=1;i<=5;i++) {
                user=findViewById(user_id[i][5-i+1]);
                user.setBackgroundColor(Color.DKGRAY);
                }
            }

        for(i=1;i<=5;i++) {
            count=0;
            for (j = 1; j <= 5; j++) {
                if(user_flag[i][j]==true) {
                    count++;
                }

            }
            if(count==5) {
                for(int k=1;k<=5;k++) {
                    user=findViewById(user_id[i][k]);
                    user.setBackgroundColor(Color.DKGRAY);
                }
                win_count++;
            }
        }

        for(i=1;i<=5;i++) {
            count=0;
            for (j = 1; j <= 5; j++) {
                if(user_flag[j][i]==true) {
                    count++;
                }

            }
            if(count==5) {
                win_count++;
                for(int k=1;k<=5;k++) {
                    user=findViewById(user_id[k][i]);
                    user.setBackgroundColor(Color.DKGRAY);
                }
            }
        }
        if(win_count==1) {
            String s="B";
            bingo.setText(s);
            bingo.setTextColor(Color.RED);
        }
        if(win_count==2) {
            String s="BI";
            bingo.setText(s);
            bingo.setTextColor(Color.RED);

        }
        if(win_count==3) {
            String s="BIN";
            bingo.setText(s);
            bingo.setTextColor(Color.RED);

        }
        if(win_count==4) {
            String s="BING";
            bingo.setText(s);
            bingo.setTextColor(Color.RED);

        }
        if(win_count==5) {
            String s="BINGO";
            bingo.setText(s);
            bingo.setTextColor(Color.GREEN);

        }
        if(win_count>=5) {
            Toast.makeText(MainActivity.this,"User Win",Toast.LENGTH_LONG).show();
            TextView winner=findViewById(R.id.winner_text);
            winner.setText("YOU WON!");
            winner.setTextColor(Color.BLUE);
            return true;
        }
        else
            return false;


    }
    public void idSetter()  {
        int i,j;
        String s;
        for(i=1;i<=5;i++) {
            for (j = 1; j <= 5; j++) {
                s = "r" + i + "c" + j;

                comp = (TextView) findViewById(getResources().getIdentifier(s, "id", getPackageName()));
                id[i][j] = getResources().getIdentifier(s, "id", getPackageName());
            }
        }
        for(i=1;i<=5;i++) {
            for (j = 1; j <= 5; j++) {
                s = "R" + i + "C" + j;

                user = (TextView) findViewById(getResources().getIdentifier(s, "id", getPackageName()));
                user_id[i][j] = getResources().getIdentifier(s, "id", getPackageName());
            }
        }

    }

    public void reset(View v) {
        Intent intent=getIntent();
        finish();
        startActivity(intent);
    }
}


