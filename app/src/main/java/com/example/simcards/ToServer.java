package com.example.simcards;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIOException;

/**
 * Created by J on 7/12/14.
 */
public class ToServer implements IOCallback {
    private SocketIO socket;
    Player player;

    public ToServer(Player p) throws Exception {
        socket = new SocketIO();
        this.player = p;
        socket.connect("http://sim_card_game_server.nodejitsu.com", this);
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
                player.setId(player_id);
            } else if (s.equals("gameFull")) {
                if (player.getId() == 0) {
                    player.sendDealtCards();
                }
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
                JSONObject card = ((JSONObject)objects[0].getObject())
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