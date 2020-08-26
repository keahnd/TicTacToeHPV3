package com.example.tictactoehpv3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.DialogInterface;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import static com.example.tictactoehpv3.R.id.board;

public class twoPlayerActivity extends AppCompatActivity {

    private BoardView boardView;
    private GameEngine game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_player);
        boardView = (BoardView) findViewById(board);
        game = new GameEngine();
        boardView.setGameEngine(game);
        game.setBoardView(boardView);
        boardView.setTwoPlayerActivity(this);
        game.setTwoPlayerActivity(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
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
}
