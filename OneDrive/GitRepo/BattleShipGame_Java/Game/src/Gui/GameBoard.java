package Gui;

import Element.Grid;
import Element.ShipContainer;
import Element.ShipPutDel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Random;


public class GameBoard {

    Grid person1_grid;
    Grid person2_grid;
    ShipContainer shipContainer;
    ShipContainer shipContainer2;
    ShipPutDel person1ShipPutDel;
    ShipPutDel person2ShipPutDel;

    JFrame mainFrame;
    JPanel mainPanel;

    JPanel personPanel1;
    JPanel personPanel2;

    JPanel chatPanel;
    JPanel sendPanel;

    JButton buttonSend;
    JTextField chatTextField;
    TextArea chatTextArea;
    TextArea localTextArea;
    TextArea comTextArea;
    Socket socket;

    public GameBoard(){
        mainFrame = new JFrame();

        person1_grid = new Grid();
        person2_grid = new Grid();

        mainPanel = new JPanel();
        chatPanel = new JPanel();
        sendPanel = new JPanel();
        personPanel1 = new JPanel();
        personPanel2 = new JPanel();

        buttonSend = new JButton("Send");
        chatTextField = new JTextField();
        chatTextArea = new TextArea(10, 30);
        localTextArea = new TextArea(5, 50);
        comTextArea = new TextArea(5,50);

        shipContainer = new ShipContainer();
        shipContainer2 = new ShipContainer();
        person1ShipPutDel = new ShipPutDel(person1_grid,localTextArea,shipContainer);
        person2ShipPutDel = new ShipPutDel(person2_grid,chatTextArea,shipContainer2);


        buttonSend.setBackground(Color.GRAY);
        buttonSend.setForeground(Color.WHITE);
        buttonSend.addActionListener( new ActionListener() {
            @Override
            public void actionPerformed( ActionEvent actionEvent ) {
                sendMessage();
            }
        });

        chatTextField.setColumns(30);
        chatTextField.addKeyListener( new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                char keyCode = e.getKeyChar();
                if (keyCode == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });

        sendPanel.setBackground(Color.LIGHT_GRAY);
        localTextArea.setEditable(false);
        sendPanel.add(localTextArea);
        sendPanel.add(chatTextField);
        sendPanel.add(buttonSend);
        chatTextArea.setEditable(false);

        chatPanel.add(BorderLayout.SOUTH, sendPanel);
        chatPanel.add(BorderLayout.CENTER, chatTextArea);

        BorderLayout fl = new BorderLayout();
        mainPanel.setLayout(fl);

        mainPanel.add(person1_grid.getJGrid(),BorderLayout.WEST);
        mainPanel.add(person2_grid.getJGrid(),BorderLayout.EAST);
        mainPanel.add(chatPanel,BorderLayout.SOUTH);
        mainFrame.add(mainPanel);

        mainFrame.pack();
        Dimension dim = new Dimension(1000,700);
        mainFrame.setPreferredSize(dim);
        mainFrame.setVisible(true);

        setComShip();
    }

    private void sendMessage() {
        try {
            PrintWriter pw;
            String message = chatTextField.getText();
            if(message.indexOf("%") != -1){
                int start = message.indexOf("%");
                int end = message.indexOf("%", start +1);
                String point = message.substring(start+1,end);
                String[] tokens = point.split("&");
                String pointA = (tokens[0]);
                int xVec = Integer.parseInt(tokens[1]);
                int yVec = Integer.parseInt(tokens[2]);
                int shipIndex = Integer.parseInt(tokens[3]);
                person1ShipPutDel.put(pointA,xVec,yVec,shipIndex);

                chatTextField.setText("");
                chatTextField.requestFocus();
            }else if(message.indexOf("?") != -1){
                for(int i =0; i < shipContainer.getShipArray().size(); i++){
                    localTextArea.append("Index : " + i +"Ship Name :" + shipContainer.getShipArray().get(i).getShipName() +
                                          " Ship Size : " +shipContainer.getShipArray().get(i).getSize() +
                                         "Ship Num : " + shipContainer.getShipArray().get(i).getNum()+"\n");
                }
                chatTextField.setText("");
                chatTextField.requestFocus();
            }
            else if(message.indexOf("|") != -1){
                if(shipContainer.getShipArray().size() ==0){
                    int start = message.indexOf("|");
                    int end = message.indexOf("|", start +1);
                    String point = message.substring(start+1,end);
                    person2ShipPutDel.ShipDrop(point);
                    chatTextArea.append("\nPlayer :"+point+"\n");
                    chatTextField.setText("");
                    chatTextField.requestFocus();
                    comRDrop();

                }else{
                    chatTextArea.append("------------------\n");
                    chatTextArea.append("We have not yet placed all of your ships.\n");
                    chatTextArea.append("------------------\n");
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setComShip(){
        while (shipContainer2.getShipArray().size() != 0){
            Random r = new Random();
            int x = r.nextInt(24);
            char y = (char) (r.nextInt(24) + 65);

            int xVec = r.nextInt(1) - 1;
            int yVec = r.nextInt(1) - 1;
            int shipIndex = r.nextInt(shipContainer2.getShipArray().size());
            String point = y + "," + x;

            System.out.println("point : " + point + " xVec :" + xVec + " yVec : " + yVec + " shipIndex : " + shipIndex);
            person2ShipPutDel.put(point, xVec, yVec, shipIndex);
            if(!comGridCK()){
                setComShip();
            }
        }
    }

    private boolean comGridCK(){
        boolean result =true;
        for(int i = 0; i < 25; i++){
           if(person2_grid.getGrid().get(0)[i].getState() == 2 || person2_grid.getGrid().get(i)[1].getState() == 2){
               person2_grid = new Grid();
               shipContainer2 = new ShipContainer();
               person2ShipPutDel.setGrid(person2_grid);
               person2ShipPutDel.setShipList(shipContainer2);
               result = false;
           }
        }
        return result;
    }

    private void comRDrop(){
        Random r = new Random();
        int x = r.nextInt(24);
        char y = (char) (r.nextInt(24) + 65);
        String point = y + "," + x;
        person1ShipPutDel.ShipDrop(point);
        chatTextArea.append("\nCom :"+point+"\n");
    }


}
