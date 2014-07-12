package com.example.simcards;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

public class ToServer implements IOCallback {
    private SocketIO socket;
    SocketActivity socketActivity;
    int socketProvidedID;
    GameView gameview;

    public ToServer(SocketActivity s) throws Exception {
        socket = new SocketIO();
        socket.connect("http://sim_card_game_server.nodejitsu.com", this);
        this.socketActivity = s;
    }

    public SocketIO getSocket() {
        return socket;
    }

    public void setGameView(GameView gv) {
        gameview = gv;
    }

    @Override
    public void onDisconnect() {
        System.out.println("Connection terminated.");
    }

    @Override
    public void onConnect() {
        System.out.println("Connection established.");
    }

    @Override
    public void onMessage(String s, IOAcknowledge ioAcknowledge) {
        System.out.println("onMessageString unimp");
    }

    @Override
    public void onMessage(JSONObject jsonObject, IOAcknowledge ioAcknowledge) {
        System.out.println("onMessageJSON unimp");
    }

    @Override
    public void on(String s, IOAcknowledge ioAcknowledge, Object... objects) {
        try {
            int player_id = ((JSONObject)objects[0]).getInt("player_id");

            if (s.equals("connected")) {
                socketProvidedID = player_id;
            } else if (s.equals("gameFull")) {
                socketActivity.createGameView(socket, socketProvidedID);
            } else if (s.equals("gameStart")) {
                player.setStatus(true);
            } else if (s.equals("playerTurn")) {
                if (player.getId() == player_id) {
                    if (player.getStatus()) {
                        player.sendPlayerMove();
                    } else {
                        player.sendNoMove();
                    }
                }
            } else if (s.equals("movesPlayed")) {
                JSONObject card = ((JSONObject)objects[0]).getJSONObject("card");
                String rank = card.getString("RANK");
                String suit = card.getString("SUITS");

                String ranks[] = {
                        "King", "Queen", "Jack", "Ten", "Nine", "Eight",
                        "Seven", "Six", "Five", "Four", "Three", "Two", "Ace"
                };
                int values[] = {
                        13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1
                };

                int tempValue = 0;
                for(int i = 0; i < ranks.length; i++) {
                    if(rank.equals(ranks[i])) {
                        int tempValue = values[i];
                    }
                }
                Card newCard = new Card(rank, suit, tempValue, 0, 0, 0);
                // needs to let the game know that the card was played somehow
            } else if (s.equals("playerDied")) {
                player.setStatus(false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onError(SocketIOException e) {
        System.out.println("error happened.");
        socketIOException.printStackTrace();
    }
}
