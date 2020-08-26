package com.example.tictactoehpv3;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class BoardView extends View{

    private static final int LINE_THICK = 10;
    private static final int LINE_THIN = 5;
    private static final int ELT_MARGIN = 20;
    private Canvas canvas;
    private static final int ELT_STROKE_WIDTH = 15;
    public int bWidth, bHeight, sWidth, sHeight, tWidth, tHeight;
    private Paint gridPaint, oPaint, xPaint, rPaint, yPaint;
    private GameEngine gameEngine;
    private twoPlayerActivity activity;
    private compActivity cActivity;
    private hiddenGameActivity hActivity;
    private Tasks task;

    public BoardView(Context context) {
        super(context);
    }

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        gridPaint = new Paint();
        canvas = new Canvas();
        oPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        oPaint.setColor(Color.BLACK);
        oPaint.setStyle(Paint.Style.STROKE);
        oPaint.setStrokeWidth(ELT_STROKE_WIDTH);
        xPaint = new Paint(oPaint);
        xPaint.setColor(Color.RED);
        rPaint = new Paint();
        rPaint.setColor(Color.WHITE);
        yPaint = new Paint();
        yPaint.setColor(Color.YELLOW);
    }

    public void setTwoPlayerActivity(twoPlayerActivity a) {
        activity = a;
    }

    public void setcompActivity(compActivity a) { cActivity = a; }

    public void setHiddenGameActivity(hiddenGameActivity a) { hActivity = a; }

    public void setTask(Tasks t) {task = t;}

    public void setGameEngine(GameEngine g) {
        gameEngine = g;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        bHeight = View.MeasureSpec.getSize(heightMeasureSpec);
        bWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        sWidth = (bWidth) / 3;
        sHeight = (bHeight) / 3;
        tWidth = (sWidth) / 3;
        tHeight = (sHeight) / 3;

        setMeasuredDimension(bWidth, bHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawGrid(canvas);
        drawBoard(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(hActivity !=null) { hActivity.cancelTimer(); }

        if (!gameEngine.isGameOver()  &&  event.getAction() == MotionEvent.ACTION_DOWN) {
            System.out.println("Player Turn");
            int y = (int) (event.getX() / sWidth);
            int x = (int) (event.getY() / sHeight);
            int b = (int) ((event.getX() - (y * sWidth)) / tWidth);
            int a = (int) ((event.getY() - (x * sHeight))/ tHeight);


            char win = gameEngine.play(x, y, a, b);

            invalidate();

            if (win != ' ') {

                if(activity != null) { activity.gameEnded(win); }
                if(hActivity != null) { hActivity.gameEnded(win); }

            } else {

                if (cActivity != null) {
                    System.out.println("Comp Turn");
                    char currBoard[][][][] = new char[3][3][3][3];
                    for (int i = 0; i < 3; i++) {
                        for (int j = 0; j < 3; j++) {
                            for (int k = 0; k < 3; k++) {
                                for (int l = 0; l < 3; l++) {
                                    currBoard[i][j][k][l] = gameEngine.getPiece(i, j, k, l);
                                }
                            }
                        }
                    }

                    System.out.println("Finding Move");

                    int move = cActivity.compMove(currBoard, a, b);
                    System.out.println("Got Move");
                    System.out.println(move);
                    int i = move/1000;
                    move -= i*1000;
                    int j = move / 100;
                    move -= j*100;
                    int k = move/10;
                    move -= k*10;
                    int l = move;


                    System.out.println("Comp played move");
                    win = gameEngine.play(i,j,k,l);

                    if( win!= ' '){
                        cActivity.gameEnded(win);
                    }
                }
            }
        }

        return super.onTouchEvent(event);
    }

    public void playAnywhere(Canvas canvas){
        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++){
                if(!(gameEngine.getIsOver(i,j))){
                    localBoard(canvas, i,j);
                }
            }
        }
    }

    public void timerEnded(){
        Random random = new Random();
        int x;
        int y;
        int a;
        int b;

        do {
            x = random.nextInt(2);
            y = random.nextInt(2);
            a = random.nextInt(2);
            b = random.nextInt(2);
            System.out.println("In loop");
        } while (!(!gameEngine.getIsOver(x,y) && gameEngine.getPiece(x, y, a, b) == ' ' && gameEngine.checkAllowed(x, y)));

        char win = gameEngine.play(x, y, a, b);
        invalidate();
        if (win != ' ') {
            hActivity.gameEnded(win);
        }
    }

    public void localBoard(Canvas canvas, int x, int y){
        float width = (sWidth + LINE_THICK) * y;
        float height = (sHeight + LINE_THICK) * x;
        for (int j = 1; j < 3; j++) {
            // Small Baord : vertical lines
            float sLeft = width + (tWidth * (j) + LINE_THIN * (j - 1));
            float sRight = sLeft + LINE_THIN;
            float sTop = height;
            float sBottom = sTop + sHeight;

            canvas.drawRect(sLeft, sTop, sRight, sBottom, yPaint);

            // Small Board : horizontal lines
            float sLeft2 = width;
            float sRight2 = sLeft2 + sWidth;
            float sTop2 = height + (tHeight * (j) + LINE_THIN * (j - 1));
            float sBottom2 = sTop2 + LINE_THIN;

            canvas.drawRect(sLeft2, sTop2, sRight2, sBottom2, yPaint);
        }
    }

    private void drawBoard(Canvas canvas) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int a = 0; a < 3; a++) {
                    for (int b = 0; b < 3; b++) {
                        drawElt(canvas, gameEngine.getPiece(i, j, a, b), i, j, a, b);
                    }
                }

                if(gameEngine.getIsOver(i,j)) {
                    drawRect(canvas, i, j);
                    drawElt(canvas, gameEngine.getIsWon(i, j), i, j, -1, -1);
                }

                if(gameEngine.lastA == -1){
                    playAnywhere(canvas);
                } else if (gameEngine.getIsOver(gameEngine.lastA, gameEngine.lastB)){
                    playAnywhere(canvas);
                } else if(i == gameEngine.lastA && j == gameEngine.lastB)
                    localBoard(canvas, i, j);
            }
        }
    }

    private void drawGrid(Canvas canvas) {
        for (int i = 1; i < 3; i++) {
            // Big Baord : vertical lines
            float left = sWidth * (i) + LINE_THICK * (i - 1);
            float right = left + LINE_THICK;
            float top = 0;
            float bottom = bHeight;

            canvas.drawRect(left, top, right, bottom, gridPaint);

            // Big Board : horizontal lines
            float left2 = 0;
            float right2 = bWidth;
            float top2 = sHeight * (i) + LINE_THICK * (i - 1);
            float bottom2 = top2 + LINE_THICK;

            canvas.drawRect(left2, top2, right2, bottom2, gridPaint);
        }

        for (int i = 0; i < 3; i++) {
            float right = (sWidth + LINE_THICK) * (i);
            float top2 = (sHeight + LINE_THICK) * (i);

            for(int j = 1; j < 3; j++){
                // Small Baord : vertical lines
                float sLeft = right + (tWidth * (j) + LINE_THIN * (j-1));
                float sRight = sLeft + LINE_THIN;
                float sTop = 0;
                float sBottom =bHeight;

                canvas.drawRect(sLeft, sTop, sRight, sBottom, gridPaint);

                // Small Board : horizontal lines
                float sLeft2 = 0;
                float sRight2 = bWidth;
                float sTop2 = top2 + (tHeight * (j) + LINE_THIN * (j-1));
                float sBottom2 = sTop2 + LINE_THIN;

                canvas.drawRect(sLeft2, sTop2, sRight2, sBottom2, gridPaint);
            }
        }
    }

    private void drawRect(Canvas canvas, int x, int y){
        float Left = ((sWidth + LINE_THICK) * y);
        float Right = Left + sWidth;
        float Top = ((sHeight + LINE_THICK) * x);
        float Bottom = Top + sHeight;

        canvas.drawRect(Left, Top, Right, Bottom, rPaint);
    }

    private void drawElt(Canvas canvas, char c, int x, int y, int a, int b) {
        if(a == -1 && a == b){
            if( c == 'O'){
                float cenX = ((sWidth + LINE_THICK) * y) + (sWidth / 2);
                float cenY = ((sHeight + LINE_THICK) * x) + sHeight / 2;

                canvas.drawCircle(cenX, cenY, Math.min(sWidth, sHeight) / 2 - ELT_MARGIN, oPaint);

            } else if (c == 'X'){
                float startX = ((sWidth + LINE_THICK) * y) + ELT_MARGIN;
                float startY = ((sHeight + LINE_THICK) * x) + ELT_MARGIN;
                float endX = startX + sWidth - ELT_MARGIN * 2;
                float endY = startY + sHeight - ELT_MARGIN * 2;

                canvas.drawLine(startX, startY, endX, endY, xPaint);

                float startX2 = ((sWidth + LINE_THICK) * y) + sWidth - ELT_MARGIN;
                float startY2 = ((sHeight + LINE_THICK) * x) + ELT_MARGIN;
                float endX2 = startX2 - (sWidth) + ELT_MARGIN * 2;
                float endY2 = startY2 + (sHeight) - ELT_MARGIN * 2;

                canvas.drawLine(startX2, startY2, endX2, endY2, xPaint);
            }
        }

        else {
            if (c == 'O') {
                float cenX = ((sWidth + LINE_THICK) * y) + ((tWidth + LINE_THIN) * b) + (tWidth / 2);
                float cenY = ((sHeight + LINE_THICK) * x) + ((tHeight + LINE_THIN) * a) + tHeight / 2;

                canvas.drawCircle(cenX, cenY, Math.min(tWidth, tHeight) / 2 - ELT_MARGIN, oPaint);

            } else if (c == 'X') {
                float startX = ((sWidth + LINE_THICK) * y) + ((tWidth + LINE_THIN) * b) + ELT_MARGIN;
                float startY = ((sHeight + LINE_THICK) * x) + ((tHeight + LINE_THIN) * a) + ELT_MARGIN;
                float endX = startX + tWidth - ELT_MARGIN * 2;
                float endY = startY + tHeight - ELT_MARGIN * 2;

                canvas.drawLine(startX, startY, endX, endY, xPaint);

                float startX2 = ((sWidth + LINE_THICK) * y) + ((tWidth + LINE_THIN) * b) + tWidth - ELT_MARGIN;
                float startY2 = ((sHeight + LINE_THICK) * x) + ((tHeight + LINE_THIN) * a) + ELT_MARGIN;
                float endX2 = startX2 - (tWidth) + ELT_MARGIN * 2;
                float endY2 = startY2 + (tHeight) - ELT_MARGIN * 2;

                canvas.drawLine(startX2, startY2, endX2, endY2, xPaint);
            }
        }
    }
}
