package com.google.catchthestatement;

import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class main extends AppCompatActivity {
    private TextView scoreLabel;
    private TextView startLabel;
    private ImageView box;
    private ImageView brace;
    private ImageView semi;
    private ImageView black;

    //Size
    private  int frameHeight;
    private int boxSize;

    private int screenWidth;
    private int screenHeight;

    //position
    private int boxY;
    private  int braceX;
    private int braceY;
    private int semiX;
    private int semiY;
    private int blackX;
    private int blackY;



    // Speed

    private int boxSpeed;

    private int braceSpeed;

    private int semiSpeed;

    private int blackSpeed;




    //Score
    private int score = 0;

    //Intialize class
    private Handler handler = new Handler();
    private Timer timer = new Timer();

    //Status check
    private boolean action_flg = false;

    private boolean start_flg = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scoreLabel = (TextView) findViewById(R.id.scoreLabel);
        startLabel = (TextView) findViewById(R.id.startLabel);
        box = (ImageView) findViewById(R.id.box);
        brace = (ImageView) findViewById(R.id.brace);
        semi = (ImageView) findViewById(R.id.semicolon);
        black = (ImageView) findViewById(R.id.black);

        //Get Screen Size
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;


        // Now

        // Nexus4 Width: 768 Height:1280

// Speed Box:20 Orange:12 Pink:20 Black:16



        boxSpeed = Math.round(screenHeight / 60);  // 1280 / 60 = 21.333... => 21

        braceSpeed = Math.round(screenWidth / 60); // 768 / 60 = 12.8 => 13

        semiSpeed = Math.round(screenWidth / 36);   // 768 / 36 = 21.333... => 21

        blackSpeed = Math.round(screenWidth / 45); // 768 / 45 = 17.06... => 17



//Log.v("SPEED_BOX", boxSpeed + "");

//Log.v("SPEED_ORANGE", orangeSpeed + "");

        //Log.v("SPEED_PINK", pinkSpeed + "");

        //Log.v("SPEED_BLACK", blackSpeed + "");




        //Move to out of screen
               brace.setX(0b11111111111111111111111110110000);
        brace.setY(0b11111111111111111111111110110000);
        semi.setX(0b11111111111111111111111110110000);
        semi.setY(0b11111111111111111111111110110000);
        black.setX(0b11111111111111111111111110110000);
        black.setY(0b11111111111111111111111110110000);


        scoreLabel.setText("(Score) == 0");

      }



    public void changepos(){

        hitCheck();

        //Brace
        braceX -= braceSpeed;
        if(braceX < 0){
            braceX = screenWidth + 20;
            braceY = (int) Math.floor(Math.random()*(frameHeight - brace.getHeight()));



        }
        brace.setX(braceX);
        brace.setY(braceY);



        //Black
        blackX -= blackSpeed;
        if(blackX < 0){
            blackX = screenWidth + 10;
            blackY = (int) Math.floor(Math.random()*(frameHeight - black.getHeight()));



        }
        black.setX(blackX);
        black.setY(blackY);




        //SemiCon
        semiX -= semiSpeed;
        if(semiX < 0){
            semiX = screenWidth + 5000;
            semiY = (int) Math.floor(Math.random()*(frameHeight - semi.getHeight()));
        }
        semi.setX(semiX);
        semi.setY(semiY);


        //Move box
        if(action_flg == true) {
            //Touching
            boxY -= boxSpeed;

        }else{
            boxY += boxSpeed;
        }

        //check box position
        if(boxY < 0) boxY = 0;
        if(boxY > frameHeight - boxSize) boxY = frameHeight - boxSize;
        box.setY(boxY);

        scoreLabel.setText("(Score) ==" +score);

    }

    public void hitCheck(){
        //if the center of the ball is in box, it counts as a hit
        //Brace

        int braceCenterX = braceX + brace.getWidth()/2;
        int braceCenterY = braceY + brace.getHeight()/2;

        //0 <= braceCenterX <= boxWidth
        //boxY <= braceCenterX <= boxY + boxHeight
        if(0 <= braceCenterX && braceCenterX <= boxSize && boxY <= braceCenterY && braceCenterY <= boxY + boxSize){
        score += 10;
        braceX = -10;
    }

    //semicolon
        int semicolCenterX = semiX + semi.getWidth()/2;
        int semicolCenterY = semiY + semi.getHeight()/2;

        if(0 <= semicolCenterX && semicolCenterX <= boxSize && boxY <= semicolCenterY&& semicolCenterY <= boxY + boxSize){
            score += 30;
            semiX = -10;
        }

        //black

        int blackCenterX = blackX + black.getWidth()/2;
        int blackCenterY = blackY + black.getHeight()/2;

        if(0 <= blackCenterX && blackCenterX <= boxSize && boxY <= blackCenterY && blackCenterY <= boxY + boxSize){
            //Stop Timer!!
            timer.cancel();
            timer = null;


            //show result
            Intent intent = new Intent(getApplicationContext(),result.class);
            intent.putExtra("SCORE",score);
            startActivity(intent);

        }
    }


    public boolean OnTouchEvent(MotionEvent me){
        if(start_flg == false){

            start_flg = true;

            //why get frame height and box height here?
            //because the UI has not been set on the screen in onCreate()


            FrameLayout frame = (FrameLayout) findViewById(R.id.frame);
            frameHeight = frame.getHeight();
            boxY = (int) box.getY();

            //The box is asquare (height and width are same)
            boxSize = box.getHeight();

            startLabel.setVisibility(View.GONE);
            timer.schedule(new TimerTask() {


                @Override

                public void run() {

                    handler.post(new Runnable() {

                        @Override

                        public void run() {

                            changepos();

                        }

                    });

                }

            }, 0, 20);



        }
        else {
            if(me.getAction() == MotionEvent.ACTION_DOWN){
                action_flg = true;

            }
            else if(me.getAction() == MotionEvent.ACTION_UP){
                action_flg=false;
            }

        }
        return true;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event){
        if(event.getAction() == KeyEvent.ACTION_DOWN){
            switch (event.getKeyCode()){
                case KeyEvent.KEYCODE_BACK:
                    return true;
            }
        }

        return super.dispatchKeyEvent(event);
    }
}
