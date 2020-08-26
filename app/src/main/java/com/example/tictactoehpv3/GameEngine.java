package com.example.tictactoehpv3;

public class GameEngine {

    public char[][][][] bBoard;
    private char currentPlayer;
    private boolean gameOver;
    private char winner;
    private boolean[][] isOver;
    private char[][] won;
    public int lastA, lastB;
    private twoPlayerActivity activity;
    private compActivity cActivity;
    private BoardView boardView;
    private hiddenGameActivity hActivity;

    public GameEngine() {
        bBoard = new char[3][3][3][3];
        isOver = new boolean [3][3];
        won = new char [3][3];
        newGame();
    }

    public void setBoardView(BoardView b) {
        boardView = b;
    }

    public void setcompActivity(compActivity a) { cActivity = a; }

    public void setHiddenGameActivity(hiddenGameActivity a) { hActivity = a; }

    public void setTwoPlayerActivity(twoPlayerActivity a) {
        activity = a;
    }

    public boolean getIsOver(int i, int j){
        return isOver[i][j];
    }

    public char getIsWon(int i, int j) { return won[i][j]; }

    public char play(int i, int j, int a, int b){
        if(activity != null) { activity.currentPlayer(currentPlayer); }
        if(hActivity != null) { hActivity.currentPlayer(currentPlayer); }
        if(!getIsOver(i,j) && getPiece(i, j, a, b) == ' ' && checkAllowed(i, j)){
            bBoard[i][j][a][b] = currentPlayer;
            lastA = a;
            lastB = b;
        } else { // If invalid move, do nothing
            return ' ';
        }

        if( hActivity!= null) { hActivity.setButtonVisible(); }

        return checkEnded(i, j);
    }

    public boolean isGameOver(){
        return gameOver;
    }

    public void changePlayer(){
        currentPlayer = (currentPlayer == 'X'? 'O' : 'X');
    }

    public char getPiece(int i, int j, int a, int b){
        return bBoard[i][j][a][b];
    }

    public void newGame(){
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                for (int a = 0; a < 3; a++){
                    for(int b = 0; b < 3; b++){
                        bBoard[i][j][a][b] = ' ';
                    }
                }
                isOver[i][j] = false;
                won[i][j] = ' ';
            }
        }

        currentPlayer = 'X';
        gameOver = false;
        lastA = -1;
        lastB = -1;
    }

    public boolean checkAllowed(int i, int j){
        if ((lastA == i && lastB == j) || (lastA < 0 && lastA == lastB) || (getIsOver(lastA, lastB))){
            return true;
        } else {
            return false;
        }
    }

    public char checkEnded(int i, int j){
        boolean isWon = false;
        for (int x = 0; x < 3; x++) {
            if(bBoard[i][j][x][0] != ' ' && bBoard[i][j][x][0] == bBoard[i][j][x][1] && bBoard[i][j][x][0] == bBoard[i][j][x][2]) {
                isOver[i][j] = true;
                isWon = true;
                won[i][j] = currentPlayer;
            }

            if(bBoard[i][j][0][x] != ' ' && bBoard[i][j][0][x] == bBoard[i][j][1][x] && bBoard[i][j][0][x] == bBoard[i][j][2][x]) {
                isOver[i][j] = true;
                isWon = true;
                won[i][j] = currentPlayer;
            }
        }

        if(bBoard[i][j][0][0] != ' ' && bBoard[i][j][0][0] == bBoard[i][j][1][1] && bBoard[i][j][0][0] == bBoard[i][j][2][2]) {
            isOver[i][j] = true;
            isWon = true;
            won[i][j] = currentPlayer;
        }

        if(bBoard[i][j][2][0] != ' ' && bBoard[i][j][2][0] == bBoard[i][j][1][1] && bBoard[i][j][2][0] == bBoard[i][j][0][2]) {
            isOver[i][j] = true;
            isWon = true;
            won[i][j] = currentPlayer;
        }

        changePlayer();

        if(isWon) {
            if (hActivity != null) { hActivity.bonus(getIsWon(i,j)); }
            return checkGameOver();
        }

        return ' ';
    }

    public char checkGameOver(){
        for(int i = 0; i < 3; i++){
            if(won[i][0] != ' ' && won[i][0] == won [i][1] && won[i][1] == won[i][2]){
                gameOver = true;
                return won[i][0];
            }
            if(won[0][i] != ' ' && won[0][i] == won [1][i] && won[1][i] == won[2][i]){
                gameOver = true;
                return won[i][0];
            }
        }

        if(won[0][0] != ' ' && won[0][0] == won [1][1] && won[1][1] == won[2][2]){
            gameOver = true;
            return won[0][0];
        }

        if(won[0][2] != ' ' && won[0][2] == won [1][1] && won[1][1] == won[2][0]){
            gameOver = true;
            return won[0][2];
        }

        for(int i = 0; i < 3;i++){
            for(int j = 0; j < 3; j++){
                if(won[i][j] == ' '){
                    gameOver = false;
                    return ' ';
                }
            }
        }

        return 'T'; //for a tie
    }
}
