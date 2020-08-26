package com.example.tictactoehpv3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.Random;

import static com.example.tictactoehpv3.R.id.board;

public class hiddenGameActivity extends AppCompatActivity {

    private BoardView boardView;
    private GameEngine game;
    private MainActivity mainActivity;
    private Tasks task;
    CountDownTimer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden_game);
        boardView = (BoardView) findViewById(board);
        game = new GameEngine();
        task = new Tasks();
        task.setHiddenGameActivity(this);
        mainActivity = new MainActivity();
        boardView.setGameEngine(game);
        boardView.setHiddenGameActivity(this);
        game.setHiddenGameActivity(this);

        final Button buttonStartTimer = findViewById(R.id.getQuestion);
        buttonStartTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonStartTimer.setVisibility(View.GONE);
                int seconds = selecttask();
                final TextView timeText = findViewById(R.id.time);
                 timer = new CountDownTimer(seconds, 1000) {

                    public void onTick(long millisUntilFinished) {
                        timeText.setText("seconds remaining: " + millisUntilFinished / 1000);
                    }

                    public void onFinish() {
                        Context context = getApplicationContext();
                        String msg = "Timer Done!";
                        int duration = Toast.LENGTH_LONG;
                        Toast toast = Toast.makeText(context, msg, duration);
                        toast.show();
                        boardView.timerEnded();
                    }
                }.start();
            }
        });
    }

    public void cancelTimer(){
        timer.cancel();
    }

    public void setButtonVisible(){
        Button startTimer = findViewById(R.id.getQuestion);
        startTimer.setVisibility(View.VISIBLE);
    }

    public int selecttask(){
        Random random = new Random();
        int num = random.nextInt(5) + 1;
        switch (num) {
            case 8:
                dareTask();
                return 30000;
            case 7:
                truthTask();
                return 30000;
            case 3:
                charadesTask();
                return 45000;
            case 4:
                tabooTask();
                return 45000;
            case 5:
                pictionaryTask();
                return 45000;
            case 6:
                questionTask();
                return 30000;
            case 1:
                completeLyricTask();
                return 30000;
            case 2:
                completeQuoteTask();
                return 30000;
        }
        return 0;
    }

    public void completeQuoteTask(){
        final String[] chosenQuote = task.completeQuote();
        final String msg = "Complete the movie quote or guess the movie its from. \n \n" + chosenQuote[0] + "\n \n";
        final AlertDialog quoteDialog = new AlertDialog.Builder(this)
                .setTitle("Movie Quotes!")
                .setMessage(msg)
                .setPositiveButton("Correct", null)
                .setNeutralButton("Get Answer", null)
                .setNegativeButton("Incorrect", null)
                .show();

        Button positiveButton = quoteDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                correctAnswer();
                quoteDialog.dismiss();
            }
        });

        Button negativeButton = quoteDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                wrong();
                quoteDialog.dismiss();
            }
        });

        Button neutralButton = quoteDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        neutralButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String answer = chosenQuote[1];
                quoteDialog.setMessage(msg + answer);
            }
        });
    }

    public void charadesTask(){
        final String chosenCharades = task.charades();
        final String msg = "Act out the word/phrase/movie and have your partner guess it." + "\n" + "\n";
        final AlertDialog charadesDialog = new AlertDialog.Builder(this)
                .setTitle("Charades!")
                .setMessage(msg)
                .setPositiveButton("Correct", null)
                .setNeutralButton("Get Word", null)
                .setNegativeButton("Incorrect", null)
                .show();

        Button positiveButton = charadesDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                correctAnswer();
                charadesDialog.dismiss();
            }
        });

        Button negativeButton = charadesDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                wrong();
                charadesDialog.dismiss();
            }
        });

        Button neutralButton = charadesDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        neutralButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String word = chosenCharades;
                charadesDialog.setMessage(msg + word);
            }
        });
    }

    public void pictionaryTask(){
        final String chosenPictionary = task.pictionary();
        final String msg = "Draw the word/phrase/movie and have your partner guess it." + "\n" + "\n";
        final AlertDialog pictionaryDialog = new AlertDialog.Builder(this)
                .setTitle("Pictionary!")
                .setMessage(msg)
                .setPositiveButton("Correct", null)
                .setNeutralButton("Get Word", null)
                .setNegativeButton("Incorrect", null)
                .show();

        Button positiveButton = pictionaryDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                correctAnswer();
                pictionaryDialog.dismiss();
            }
        });

        Button negativeButton = pictionaryDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                wrong();
                pictionaryDialog.dismiss();
            }
        });

        Button neutralButton = pictionaryDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        neutralButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String word = chosenPictionary;
                pictionaryDialog.setMessage(msg + word);
            }
        });
    }

    public void questionTask(){
        final String[] chosenQuestion = task.question();
        final String msg = "Answer the following question: " + "\n" + "\n" + chosenQuestion[0]+ "\n" + "\n";
        final AlertDialog questionDialog = new AlertDialog.Builder(this)
                .setTitle("Pop-Culture Question!")
                .setMessage(msg)
                .setPositiveButton("Correct", null)
                .setNeutralButton("Get Answer", null)
                .setNegativeButton("Incorrect", null)
                .show();

        Button positiveButton = questionDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                correctAnswer();
                questionDialog.dismiss();
            }
        });

        Button negativeButton = questionDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                wrong();
                questionDialog.dismiss();
            }
        });

        Button neutralButton = questionDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        neutralButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                questionDialog.setMessage(msg + chosenQuestion[1]);
            }
        });
    }

    public void completeLyricTask(){
        final String[] chosenSong = task.completeLyric();
        final String msg = "Complete the song lyric or guess which song its from." + "\n" + "\n" + chosenSong[0] + "\n \n";
        final AlertDialog songDialog = new AlertDialog.Builder(this)
                .setTitle("Song Lyrics!")
                .setMessage(msg)
                .setPositiveButton("Correct", null)
                .setNeutralButton("Get Answer", null)
                .setNegativeButton("Incorrect", null)
                .show();

        Button positiveButton = songDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                correctAnswer();
                songDialog.dismiss();
            }
        });

        Button negativeButton = songDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                wrong();
                songDialog.dismiss();
            }
        });

        Button neutralButton = songDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        neutralButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String answer = chosenSong[1];
                songDialog.setMessage(msg + answer);
            }
        });
    }

    public void tabooTask(){
        final String[] chosenTaboo = task.taboo();
        int size = chosenTaboo.length;
        String tabooWord = "";
        for(int j = 0; j < size; j++){
            tabooWord += chosenTaboo[j] + "\n";
            if(j==0){
                tabooWord += "\n";
            }
        }
        final String tabooCard = tabooWord;
        final String msg = "Make your partner guess the first word without saying the subsequent words listed." + "\n" + "\n";
        final AlertDialog tabooDialog = new AlertDialog.Builder(this)
                .setTitle("Taboo!")
                .setMessage(msg)
                .setPositiveButton("Correct", null)
                .setNeutralButton("Get Word", null)
                .setNegativeButton("Incorrect", null)
                .show();

        Button positiveButton = tabooDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                correctAnswer();
                tabooDialog.dismiss();
            }
        });

        Button negativeButton = tabooDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                wrong();
                tabooDialog.dismiss();
            }
        });

        Button neutralButton = tabooDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
        neutralButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                tabooDialog.setMessage(msg + tabooCard);
            }
        });
    }

    public void dareTask(){

    }

    public void truthTask(){

    }

    public void gameEnded(char c) {
        game.changePlayer();
        String msg = (c == 'T') ? "Game Ended. Tie" : "GameEnded. " + c + " win";

        new AlertDialog.Builder(this).setTitle("Tic Tac Toe").
                setMessage(msg).
                setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        newGame();
                    }
                }).show();
    }

    public void currentPlayer(char c){
        if(c == 'X')
            c = 'O';
        else
            c = 'X';
        TextView textView = (TextView) findViewById(R.id.Current_player);
        textView.setText("Current Player is "+ c);
    }


    private void newGame() {
        game.newGame();
        boardView.invalidate();
    }

    public void correctAnswer(){
        Context context = getApplicationContext();
        String msg = "Correct Answer! Place your piece";
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, msg, duration);
        toast.show();
    }

    public void wrongAnswer(String answer){
        Context context = getApplicationContext();
        String msg = "Incorrect!" + "\n " + "The correct answer is: " + answer + "\n" +"Have a drink and then place your piece";
        int duration = Toast.LENGTH_LONG;;
        Toast toast = Toast.makeText(context, msg, duration);
        toast.show();
    }

    public void wrong(){
        Context context = getApplicationContext();
        String msg = "Incorrect Answer! Have a drink and then place your piece";
        int duration = Toast.LENGTH_LONG;;
        Toast toast = Toast.makeText(context, msg, duration);
        toast.show();
    }

    public void bonus(char c){
        String []bonus = getBonus();
        String msg = "Congratulations on winning a local board, team " + c + "! \n \n As a result you get a bonus: \n \n" + bonus[0] + "\n \n" + bonus[1];
        final AlertDialog bonusDialog = new AlertDialog.Builder(this)
                .setTitle("Bonus!")
                .setMessage(msg)
                .setPositiveButton("Continue", null)
                .show();

        Button positiveButton = bonusDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                bonusDialog.dismiss();
            }
        });
    }

    public String[] getBonus(){
        String [][]bonus = {{"Bartender Bonus", "It seems your opponents weak drink resulted in them losing a local board. Since your dirnks are clearly better take their drink and make them a new one"},
                {"Too Much Chit-Chat", "It seems your oppoenents were talking too much and thats why they lost a local board. They must not talk until the next local board is won, everytime they are caught talking they much take a drink (Not applicable during an activity - charades, taboo etc."},
                {"Question-Master", "You and your teammate are question masters until the next local board is won. If your opponents answer any question you ask them they must have a drink"},
                {"Rule Makers", "You and your teammate can enforce a rule of your choosing until the next local board is won. Anyone in violation of this rule must drink (winning team included)"},
                {"Down With It", "Your opponents must finish half their drinks. If they have less than half, they must finish whatever they have"}};
        Random random = new Random();
        int num = random.nextInt(bonus.length - 1);
        return bonus[num];
    }
}
