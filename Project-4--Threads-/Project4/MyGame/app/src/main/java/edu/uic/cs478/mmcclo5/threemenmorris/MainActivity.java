package edu.uic.cs478.mmcclo5.threemenmorris;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    Button newGame;
    Thread player1, player2,p1,p2;
    Handler mHandler,wHandler,wHandler2;
    Message main_msg;
    static TextView text;
    static GridView grid;
    static buttonAdapter gridAdapter;
    String[] default_button_text = {"-", "-", "-", "-", "-", "-", "-", "-", "-"};
    int pieces_placed = 0;
    long x = 2000;
    int gameOver;
    boolean duringGame = false;
    boolean play = true;
    int[] move;
    Random rand = new Random();
    int first;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newGame = (Button) findViewById(R.id.newGame);
        text = (TextView)findViewById(R.id.text);
        grid = (GridView) findViewById(R.id.morris_grid);
        gridAdapter = new buttonAdapter(this, default_button_text);
        grid.setAdapter(gridAdapter);

        player1 = new workerThread();
        player2 = new workerThread2();
        player1.start();
        player2.start();

        mHandler = new Handler(getMainLooper()) {
            public void handleMessage(Message msg)
            {
                //Log.i("worker thread", msg.toString());
                String s;

                switch(msg.obj.toString())
                {
                    case "place":
                        s = Integer.toString(msg.arg1);
                        ((Button)grid.getChildAt(msg.what)).setText(s);
                        pieces_placed++;

                        gameOver = isGameOver();
                        if(gameOver == 1 || gameOver == 2)
                        {
                            //Log.i("GAME OVER!!!", "PLAYER " + isGameOver() + " WINS!!!");
                            main_msg = Message.obtain();
                            main_msg.obj = "end";
                            wHandler.getLooper().quit();
                            //wHandler.sendMessage(main_msg);

                            main_msg = Message.obtain();
                            main_msg.obj = "end";
                            wHandler2.sendMessage(main_msg);
                            text.setText("Player " + gameOver + " Wins !!!");
                        }

                        main_msg = Message.obtain();

                        if(pieces_placed < 6)
                            main_msg.obj = "place";
                        else
                            main_msg.obj = "move";

                        if (msg.arg1 == 1)
                           wHandler2.sendMessage(main_msg);
                        else
                            wHandler.sendMessage(main_msg);

                        break;

                    case "move1":
                        move = msg.getData().getIntArray("movement");
                        //s = Integer.toString(msg.arg1);
                        Log.i("making move", move[0] + " to " + move[1]);
                        ((Button)grid.getChildAt(move[1])).setText("1");
                        ((Button)grid.getChildAt(move[0])).setText("-");

                        gameOver = isGameOver();
                        if(gameOver == 1 || gameOver == 2)
                        {
                            Log.i("GAME OVER!!!", "PLAYER " + isGameOver() + " WINS!!!");
                            main_msg = Message.obtain();
                            main_msg.obj = "end";

                            wHandler.sendMessage(main_msg);

                            main_msg = Message.obtain();
                            main_msg.obj = "end";
                            wHandler2.sendMessage(main_msg);
                            text.setText("Player " + gameOver + " Wins !!!");
                        }

                        main_msg = Message.obtain();

                        main_msg.obj = "move";

                        if (msg.arg1 == 1)
                            wHandler2.sendMessage(main_msg);
                        else
                            wHandler.sendMessage(main_msg);

                        break;

                    case "move2":
                        move = msg.getData().getIntArray("movement");
                        //s = Integer.toString(msg.arg1);
                        ((Button)grid.getChildAt(move[1])).setText("2");
                        ((Button)grid.getChildAt(move[0])).setText("-");

                        gameOver = isGameOver();
                        if(gameOver == 1 || gameOver == 2)
                        {
                            Log.i("GAME OVER!!!", "PLAYER " + isGameOver() + " WINS!!!");
                            main_msg = Message.obtain();
                            main_msg.obj = "end";

                            wHandler.sendMessage(main_msg);

                            main_msg = Message.obtain();
                            main_msg.obj = "end";
                            wHandler2.sendMessage(main_msg);
                            text.setText("Player " + gameOver + " Wins !!!");
                        }

                        main_msg = Message.obtain();

                        main_msg.obj = "move";

                        if (msg.arg1 == 1)
                            wHandler2.sendMessage(main_msg);
                        else
                            wHandler.sendMessage(main_msg);

                        break;

                }

            }

        };


        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if(duringGame)
                {
                    for (int i = 0; i < 9; i++)
                        ((Button) grid.getChildAt(i)).setText("-");

                    pieces_placed = 0;
                    wHandler.getLooper().quit();
                    wHandler2.getLooper().quit();
                    wHandler.removeCallbacksAndMessages(player1); // null to player1
                    wHandler2.removeCallbacksAndMessages(player2); // null to player2
                    play = false;
                    main_msg = Message.obtain();
                    main_msg.obj = "place";
                    duringGame = true;


                    player1.interrupt();
                    player2.interrupt();
                    player1 = null;
                    player2 = null;
                    player1.start();
                    player2.start();



                    //the player who didn't go first in the previous game now goes first
                    if(first == 1)
                    {
                        wHandler2.sendMessage(main_msg);
                    }
                    else
                    {
                        wHandler.sendMessage(main_msg);
                    }

                }
                else
                {
                    first = rand.nextInt(2);
                    main_msg = Message.obtain();
                    main_msg.obj = "place";
                    duringGame = true;

                    if(first == 1)
                        wHandler.sendMessage(main_msg);
                    else
                        wHandler2.sendMessage(main_msg);

                }
            }
        });

    }// end on create


    //
    // Thread class for 1st player
    //
    class workerThread extends Thread implements Runnable
    {
        int pos;
        int[] piece_movement;
        Message m;
        ArrayList<Integer> player1_piece_positions = new ArrayList<>();
        private Handler h;

        @Override
        public void run()
        {
            if(Looper.myLooper() == null)
                Looper.prepare();

            while(play) {
                wHandler = new Handler() {
                    public void handleMessage(Message msg) {
                        try {
                            Thread.sleep(x);
                        } catch (InterruptedException ie) {
                            Log.i("in worker1", "Thread interrupted...");
                            return;
                        }

                        switch (msg.obj.toString()) {
                            case "place":
                                //Log.i("in worker1", "player 1 placing...");
                                pos = place_piece_on_board(1);
                                m = Message.obtain();
                                m.obj = "place";
                                m.arg1 = 1;
                                m.what = pos;
                                mHandler.sendMessage(m);
                                break;

                            case "move":
                                Log.i("in worker1", "player 1 moving piece...");
                                player1_piece_positions = getPiecePositions("1");
                                piece_movement = player1_makeMove(player1_piece_positions);
                                Bundle bundle = new Bundle();
                                bundle.putIntArray("movement", piece_movement);
                                m = Message.obtain();
                                m.obj = "move1";
                                m.arg1 = 1;
                                m.setData(bundle);
                                mHandler.sendMessage(m);
                                break;

                            case "end":
                                wHandler.removeMessages(0);

                                break;
                        }
                    }

                };

                Looper.loop();
            }
        }

    }//end workerThread


    //
    //thread class for 2nd player
    //
    class workerThread2 extends Thread implements Runnable
    {
        int pos;
        int[] piece_movement;
        Message m;
        ArrayList<Integer> player2_piece_positions = new ArrayList<>();

        @Override
        public void run()
        {
            if(Looper.myLooper() == null)
                Looper.prepare();

            while(play)
            {
                wHandler2 = new Handler() {
                    public void handleMessage(Message msg) {
                        try {
                            Thread.sleep(x);
                        } catch (InterruptedException ie) {
                            Log.i("in worker2", "Thread interrupted...");
                        }

                        switch (msg.obj.toString()) {
                            case "place":
                                //Log.i("in worker2", "player 2 placing...");
                                pos = place_piece_on_board(2);
                                m = Message.obtain();
                                m.obj = "place";
                                m.arg1 = 2;
                                m.what = pos;
                                mHandler.sendMessage(m);
                                break;

                            case "move":
                                Log.i("in worker2", "player 2 moving piece...");
                                player2_piece_positions = getPiecePositions("2");
                                piece_movement = player2_makeMove(player2_piece_positions);
                                Bundle bundle = new Bundle();
                                bundle.putIntArray("movement", piece_movement);
                                m = Message.obtain();
                                m.obj = "move2";
                                m.arg1 = 2;
                                m.setData(bundle);
                                mHandler.sendMessage(m);
                                break;

                            case "end":
                                wHandler2.removeMessages(0);
                                break;
                        }
                    }

                };

                Looper.loop();
            }
        }

    }//end workerThread2


    //
    //places a piece on the board with parameter player with value of either 1 or 2
    //
    public int place_piece_on_board(int player)
    {
        Random r = new Random();
        int num;

        switch(player)
        {
            case 1:
                for(int i = 0; i < 9; i++)
                {
                    //Log.i("grid.child = " + i, Integer.toString(grid.getChildCount()) );
                    Button b = ((Button)grid.getChildAt(i));
                    if( !b.getText().equals("1") && !b.getText().equals("2") )
                    {
                        return i;
                    }
                }

                break;

            case 2:
                //Log.i("player2_place", "player 2 placing piece");
                num = r.nextInt(9);
                while( ((Button)grid.getChildAt(num)).getText().equals("1")  ||
                            ((Button)grid.getChildAt(num)).getText().equals("2") )
                {
                    num = r.nextInt(9);
                }
                return num;

        }
        return 0;

    }//end place_piece_on_board


    //
    // checks if a player has won the game
    //
    public int isGameOver()
    {
        ArrayList<Button> buttons = new ArrayList<>();

        for(int i = 0; i < 9; i++)
            buttons.add( (Button)grid.getChildAt(i) );

        if( (buttons.get(0).getText().equals("1") && buttons.get(1).getText().equals("1") && buttons.get(2).getText().equals("1")) ||
            (buttons.get(3).getText().equals("1") && buttons.get(4).getText().equals("1") && buttons.get(5).getText().equals("1")) ||
            (buttons.get(6).getText().equals("1") && buttons.get(7).getText().equals("1") && buttons.get(8).getText().equals("1")) ||
            (buttons.get(0).getText().equals("1") && buttons.get(3).getText().equals("1") && buttons.get(6).getText().equals("1")) ||
            (buttons.get(1).getText().equals("1") && buttons.get(4).getText().equals("1") && buttons.get(7).getText().equals("1")) ||
            (buttons.get(2).getText().equals("1") && buttons.get(5).getText().equals("1") && buttons.get(8).getText().equals("1")) )
        {
            return 1; //player 1 won
        }
        else if((buttons.get(0).getText().equals("2") && buttons.get(1).getText().equals("2") && buttons.get(2).getText().equals("2")) ||
                (buttons.get(3).getText().equals("2") && buttons.get(4).getText().equals("2") && buttons.get(5).getText().equals("2")) ||
                (buttons.get(6).getText().equals("2") && buttons.get(7).getText().equals("2") && buttons.get(8).getText().equals("2")) ||
                (buttons.get(0).getText().equals("2") && buttons.get(3).getText().equals("2") && buttons.get(6).getText().equals("2")) ||
                (buttons.get(1).getText().equals("2") && buttons.get(4).getText().equals("2") && buttons.get(7).getText().equals("2")) ||
                (buttons.get(2).getText().equals("2") && buttons.get(5).getText().equals("2") && buttons.get(8).getText().equals("2")) )
        {
            return 2; //player 2 won
        }


        return 0; // nobody won yet
    }


    //
    // function for player 1's movement; returns int for pos to move to
    //
    public int[] player1_makeMove(ArrayList<Integer> positions)
    {
        Random r = new Random();
        int move_piece = 0;
        int target = 0;
        int[] source_target = new int[2];

        int[] y = canWin("1");
        int x = 0;

        if(y[0] != -1)
        {
            x = find_piece_to_move(y,positions);
            source_target = new int[]{x,y[0]};
        }

        if(y[0] == -1 || x == -1)
        {
            //get random int between 0 and 2 to pick a random piece from the player to move
            move_piece = r.nextInt(2);

            for (int i = 0; i < 9; i++) {
                if (((Button) grid.getChildAt(i)).getText().equals("-")) {
                    target = i;
                    break;
                }
            }

            source_target = new int[]{positions.get(move_piece), target};
        }

        return source_target;

    }// end player1_makeMove


    //
    // function for player 2's movement; returns int for pos to move to
    //
    public int[] player2_makeMove(ArrayList<Integer> positions)
    {
        Random r = new Random();
        int move_piece;
        int target;
        int[] source_target;

        move_piece = r.nextInt(2);

        target = r.nextInt(8);

        while(!((Button)grid.getChildAt(target)).getText().equals("-"))
        {
            target = r.nextInt(8);
        }

        source_target = new int[] {positions.get(move_piece),target};

        return source_target;

    }// end player2_makeMove


    //
    // gets the current positions of a players pieces
    //
    public ArrayList<Integer> getPiecePositions(String player)
    {
        ArrayList<Integer> positions = new ArrayList<>();

        for(int i = 0; i < 9; i++)
        {
            Button b = ((Button)grid.getChildAt(i));
            if(b.getText().equals(player))
                positions.add(i);
        }

        return positions;
    }


    //
    // methods that returns the position that would put 3 pieces in a row or column, returns -1 otherwise
    //
    public int[] canWin(String player)
    {
        ArrayList<Button> buttons = new ArrayList<>();

        for(int i = 0; i < 9; i++)
        {
            buttons.add(((Button) grid.getChildAt(i)));
        }

        if( (buttons.get(1).getText().equals(player) && buttons.get(2).getText().equals(player)) &&
                                                                buttons.get(0).getText().equals("-"))
        {
            return new int[]{0,1,2};
        }
        else if((buttons.get(3).getText().equals(player) && buttons.get(6).getText().equals(player)) &&
                                                                        buttons.get(0).getText().equals("-"))
        {
            return new int[]{0,3,6};
        }
        else if(buttons.get(0).getText().equals(player) && buttons.get(2).getText().equals(player)
                                                                && buttons.get(1).getText().equals("-"))
        {
            return new int[]{1,0,2};
        }
        else if((buttons.get(4).getText().equals(player) && buttons.get(7).getText().equals(player))
                                                                && buttons.get(1).getText().equals("-"))
        {
            return new int[]{1,4,7};
        }
        else if(buttons.get(0).getText().equals(player) && buttons.get(1).getText().equals(player)
                                                                && buttons.get(2).getText().equals("-"))
        {
            return new int[]{2,0,1};
        }
        else if((buttons.get(5).getText().equals(player) && buttons.get(8).getText().equals(player))
                                                                && buttons.get(2).getText().equals("-"))
        {
            return new int[]{2,5,8};
        }
        else if(buttons.get(4).getText().equals(player) && buttons.get(5).getText().equals(player)
                                                                && buttons.get(3).getText().equals("-"))
        {
            return new int[]{3,4,5};
        }
        else if((buttons.get(0).getText().equals(player) && buttons.get(6).getText().equals(player))
                                                                && buttons.get(3).getText().equals("-"))
        {
            return new int[]{3,0,6};
        }
        else if(buttons.get(3).getText().equals(player) && buttons.get(5).getText().equals(player)
                                                                && buttons.get(4).getText().equals("-"))
        {
            return new int[]{4,3,5};
        }
        else if((buttons.get(1).getText().equals(player) && buttons.get(7).getText().equals(player))
                                                                && buttons.get(4).getText().equals("-"))
        {
            return new int[]{4,1,7};
        }
        else if(buttons.get(3).getText().equals(player) && buttons.get(4).getText().equals(player)
                                                                && buttons.get(5).getText().equals("-"))
        {
            return new int[]{5,3,4};
        }
        else if(buttons.get(2).getText().equals(player) && buttons.get(8).getText().equals(player)
                                                                && buttons.get(5).getText().equals("-"))
        {
            return new int[]{5,2,8};
        }
        else if(buttons.get(0).getText().equals(player) && buttons.get(3).getText().equals(player)
                                                                && buttons.get(6).getText().equals("-"))
        {
            return new int[]{6,0,3};
        }
        else if(buttons.get(7).getText().equals(player) && buttons.get(8).getText().equals(player)
                                                                && buttons.get(6).getText().equals("-"))
        {
            return new int[]{6,7,8};
        }
        else if(buttons.get(1).getText().equals(player) && buttons.get(4).getText().equals(player)
                                                                && buttons.get(7).getText().equals("-"))
        {
            return new int[]{7,1,4};
        }
        else if(buttons.get(6).getText().equals(player) && buttons.get(8).getText().equals(player)
                                                                && buttons.get(7).getText().equals("-"))
        {
            return new int[]{7,6,8};
        }
        else if(buttons.get(2).getText().equals(player) && buttons.get(5).getText().equals(player)
                                                                && buttons.get(8).getText().equals("-"))
        {
            return new int[]{8,2,5};
        }
        else if(buttons.get(6).getText().equals(player) && buttons.get(7).getText().equals(player)
                                                                && buttons.get(8).getText().equals("-"))
        {
            return new int[]{8, 6, 7};

        }

        return new int[]{-1,-1,-1};

    }//end canWin


    public int find_piece_to_move(int[] x, ArrayList<Integer> pos)
    {
        for(int p: pos)
            if(p != x[1] && p != x[2])
                return p;

        return -1;
    }

}