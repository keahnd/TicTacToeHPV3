package com.example.tictactoehpv3;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import static com.example.tictactoehpv3.R.id.board;

public class compActivity extends AppCompatActivity {

    private BoardView boardView;
    private GameEngine game;
    private boolean gameOver;
    private char[][] won = new char[3][3];
    private char winner = ' ';
    private boolean[][] isOver = new boolean [3][3];
    private int lastA, lastB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comp_player);
        boardView = (BoardView) findViewById(board);
        game = new GameEngine();
        boardView.setGameEngine(game);
        game.setBoardView(boardView);
        boardView.setcompActivity(this);
        game.setcompActivity(this);
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

    public int compMove(char currBoard[][][][], int lastMoveA, int lastMoveB){
        lastA = lastMoveA;
        lastB = lastMoveB;
        for (int i = 0; i< 3; i++){
            for (int j =0; j<3;j++){
                isOver[i][j] = game.getIsOver(i,j);
            }
        }
        int bestMove = findBestMove(currBoard);
        System.out.println("Move = " + bestMove);
        return bestMove;
    }

    public int minimax(char currBoard[][][][], boolean maximizing, char ogPlayer, int numOfMoves){
        if (gameOver) {
            if (winner == ogPlayer)
                return numOfMoves;
            else if (winner == 'T')
                return 0;
            else
                return -1;
        } else {
            if (maximizing){
                int bestEval = Integer.MIN_VALUE;
                for (int i = 0; i < 3; i++){
                    for (int j = 0; j < 3; j++) {
                        for (int a = 0; a < 3; a++) {
                            for ( int b = 0; b < 3; b++){
                                if (!getIsOver(i,j) && getPiece(currBoard, i, j, a, b) == ' ' && checkAllowed(i, j)) {
                                    currBoard[i][j][a][b] = 'O';
                                    checkEnded(i, j, currBoard);
                                    int result = minimax(currBoard, false, ogPlayer, numOfMoves+1);
                                    if (result > 0 && result < bestEval){
                                        bestEval = result;
                                    }
                                }
                            }
                        }
                    }
                }
                return bestEval;

            } else {
                int worstEval = Integer.MAX_VALUE;
                for (int i = 0; i < 3; i++){
                    for (int j = 0; j < 3; j++) {
                        for (int a = 0; a < 3; a++) {
                            for ( int b = 0; b < 3; b++){
                                if (!getIsOver(i,j) && getPiece(currBoard, i, j, a, b) == ' ' && checkAllowed(i, j)) {
                                    currBoard[i][j][a][b] = 'X';
                                    checkEnded(i, j, currBoard);
                                    int result = minimax(currBoard, true, ogPlayer, numOfMoves);
                                    worstEval = Math.max(result, worstEval);
                                }
                            }
                        }
                    }
                }
                return worstEval;
            }
        }
    }

    public int findBestMove(char currBoard[][][][]){
        int bestEval = Integer.MAX_VALUE;
        int bestMove = -1;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int a = 0; a < 3; a++) {
                    for (int b = 0; b < 3; b++) {
                        if (!getIsOver(i,j) && getPiece(currBoard, i, j, a, b) == ' ' && checkAllowed(i, j)) {
                            currBoard[i][j][a][b] = 'O';
                            checkEnded(i, j, currBoard);
                            int result = minimax(currBoard, false, 'O',1);
                            System.out.println("Move being checked: "+ i + ", " + j + ", "+ a+ ", "+b + "Result = " + result);
                            if (result > 0 && result < bestEval) {
                                bestEval = result;
                                bestMove = i * 1000 + j * 100 + a * 10 + b;
                                System.out.println("Best move is: " + bestMove +" and best eval: "+bestEval);
                            }
                        }
                    }
                }
            }
        }
        return bestMove;
    }

    public boolean checkAllowed(int i, int j){
        if ((lastA == i && lastB == j) || (lastA < 0 && lastA == lastB) || (getIsOver(lastA, lastB))){
            System.out.println(lastA +" = "+ i +" or "+ lastB +" = "+ j + " is it over: " + getIsOver(lastA, lastB));
            return true;
        } else {
            return false;
        }
    }

    public char getPiece(char currBoard[][][][], int i, int j, int a, int b){
        return currBoard[i][j][a][b];
    }

    public boolean getIsOver(int i, int j) { return isOver[i][j]; }

    public void checkEnded(int i, int j, char currBoard[][][][]){
        boolean isWon = false;
        for (int x = 0; x < 3; x++) {
            if(currBoard[i][j][x][0] != ' ' && currBoard[i][j][x][0] == currBoard[i][j][x][1] && currBoard[i][j][x][0] == currBoard[i][j][x][2]) {
                isOver[i][j] = true;
                isWon = true;
                won[i][j] = currBoard[i][j][x][0];
            }

            if(currBoard[i][j][0][x] != ' ' && currBoard[i][j][0][x] == currBoard[i][j][1][x] && currBoard[i][j][0][x] == currBoard[i][j][2][x]) {
                isOver[i][j] = true;
                isWon = true;
                won[i][j] = currBoard[i][j][0][x];
            }
        }

        if(currBoard[i][j][0][0] != ' ' && currBoard[i][j][0][0] == currBoard[i][j][1][1] && currBoard[i][j][0][0] == currBoard[i][j][2][2]) {
            isOver[i][j] = true;
            isWon = true;
            won[i][j] = currBoard[i][j][0][0];
        }

        if(currBoard[i][j][2][0] != ' ' && currBoard[i][j][2][0] == currBoard[i][j][1][1] && currBoard[i][j][2][0] == currBoard[i][j][0][2]) {
            isOver[i][j] = true;
            isWon = true;
            won[i][j] = currBoard[i][j][2][0];
        }


        if(isWon) {
            checkGameOver();
        }
    }

    public void checkGameOver(){
        for(int i = 0; i < 3; i++){
            if(won[i][0] != ' ' && won[i][0] == won [i][1] && won[i][1] == won[i][2]){
                gameOver = true;
                winner = won[i][0];
            }
            if(won[0][i] != ' ' && won[0][i] == won [1][i] && won[1][i] == won[2][i]){
                gameOver = true;
                winner = won[i][0];
            }
        }

        if(won[0][0] != ' ' && won[0][0] == won [1][1] && won[1][1] == won[2][2]){
            gameOver = true;
            winner = won[0][0];
        }

        if(won[0][2] != ' ' && won[0][2] == won [1][1] && won[1][1] == won[2][0]){
            gameOver = true;
            winner = won[0][2];
        }

        gameOver = true;

        for(int i = 0; i < 3;i++){
            for(int j = 0; j < 3; j++){
                if(won[i][j] == ' '){
                    gameOver = false;
                }
            }
        }

        if(gameOver && winner == ' '){
            winner = 'T';
        }
    }

    private void newGame() {
        game.newGame();
        boardView.invalidate();
    }

}
