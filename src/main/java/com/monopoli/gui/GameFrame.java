package com.monopoli.gui;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.monopoli.app.Contract;
import com.monopoli.app.Menager;
import com.monopoli.app.MoneyException;
import com.monopoli.app.Player;

public class GameFrame extends JFrame {
    public static int i = 0;

    private JPanel panel;
    private GameBoardPanel gameBoardPanel;

    private JTextField textField;
    private JTextArea textArea;

    private JButton turnOverButton;

    private JLabel timer;

    private List<Player> players;
    private List<Contract> contracts;
    private Menager menager;
    private static GameFrame frame;
    private int dice;
    private int time;

    Thread updateThread;
    Thread timerThread;

    public GameFrame(Menager menager) {
        GameFrame.frame = this;

        this.setSize(720,720);
        setResizable(false);
        this.setLocationRelativeTo(null);
        this.setTitle("Monopoli");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.players = menager.getPlayers();
        this.contracts = menager.getContracts();
        this.menager = menager;

        turnOverButton = new JButton("Termina turno");
        turnOverButton.setBounds(540, 560, 150, 30);

        textField = new JTextField();
        textArea = new JTextArea();

        timer = new JLabel();
        timer.setBounds(10, 10, 180, 30);
       
        textField.setBounds(530, 30, 170, 30);       
        textArea.setBounds(540, 70, 150, 300);

        this.add(textField);
        this.add(textArea);
        this.add(turnOverButton);
        this.add(timer);

        turnOverButton.addActionListener(
            e -> {
                turnOver();
            }
        );

        gameBoardPanel = new GameBoardPanel(players);
        
        addGameBoard();
        showPanel();
        
        if (!players.get(GameFrame.i).getStatus()) {
            updateThread();
            updateTimerThread();
            throwDice();
        }
    }

    private void showPanel() {
        if(players.get(GameFrame.i).getStatus() == true) {
            panel = new PrisonPanel(menager);
        } else {
            panel = new NormalGamePanel(menager);
        }

        this.add(panel);
        panel.setVisible(true);
    }

    public void updateThread() {
        updateThread = new Thread(() -> {

            while (true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {}
                
                textField.setText("Turno di " + players.get(GameFrame.i).getName() + ", soldi: " + players.get(GameFrame.i).getMoney());
                textArea.setText(setContracts());
            }
        });
        updateThread.start();
    }

    public void updateTimerThread() {
        timerThread = new Thread(() -> {
            time = 180;

            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {}

                timer.setText("Tempo rimasto: " + time);
                time--;
            }
        });
        timerThread.start();
    }

    private String setContracts() {
        String contractName = "";

        contracts = players.get(GameFrame.i).getContract();

        for (Contract contract : contracts) {
            contractName += contract.getName() + "\n";
        }
        return contractName;
    }

    public void throwDice() {
        List<Integer> numbers = new ArrayList<>();
        numbers.add(2);
        numbers.add(3);
        numbers.add(4);
        numbers.add(6);
        numbers.add(6);
        numbers.add(7);
        numbers.add(8);
        numbers.add(9);
        numbers.add(10);
        numbers.add(11);
        numbers.add(12);

        Collections.shuffle(numbers);

        dice = numbers.get(0);

        JOptionPane.showMessageDialog(
        null,
        dice,
        "Dadi",
        JOptionPane.INFORMATION_MESSAGE
        );

        checkAndMovePlayer();
        
    }

    public static GameFrame getInstance() {
        return frame;
    }

    private void addGameBoard() {
        gameBoardPanel.setBounds(0, 60, 512, 512);
        this.add(gameBoardPanel);
        gameBoardPanel.setVisible(true);
    }

    private void checkAndMovePlayer() {
        for (int i = 0; i < dice; i++) {
            gameBoardPanel.movePlayer();
            if (players.get(GameFrame.i).getBox() == 0) {
                players.get(GameFrame.i).addMoney(200);
            }
        }

        if(players.get(GameFrame.i).getBox() == 2 || players.get(GameFrame.i).getBox() == 17 || players.get(GameFrame.i).getBox() == 33) {
            JOptionPane.showMessageDialog(null,
                menager.getChanceCards().get(0).getDescription(), 
                menager.getChanceCards().get(0).getTitle(), 
                JOptionPane.INFORMATION_MESSAGE);

            doChanceCardAction(menager.getChanceCards().get(0).getId());
            
            Collections.shuffle(menager.getChanceCards());            
        }

        if(players.get(GameFrame.i).getBox() == 7 || players.get(GameFrame.i).getBox() == 22 || players.get(GameFrame.i).getBox() == 36) {
            JOptionPane.showMessageDialog(null,
             menager.getSuddenCards().get(0).getDescription(), 
             menager.getSuddenCards().get(0).getTitle(), 
             JOptionPane.INFORMATION_MESSAGE);

            doSuddenCardAction(menager.getChanceCards().get(0).getId());

            Collections.shuffle(menager.getSuddenCards());
        }

        if (players.get(GameFrame.i).getBox() == 30) {
            for (int i = 0; i < 20; i++) {
                gameBoardPanel.movePlayer();
            }

            players.get(GameFrame.i).setStatus(true);
            turnOver();
        }
    }

    private void turnOver() {
        updateThread.interrupt();
        timerThread.interrupt();

        if (players.get(GameFrame.i).getMoney() == 0) {
            JOptionPane.showMessageDialog(
                null,
                players.get(GameFrame.i).getName() + " e' stato eliminato",
                "Eliminazione",
                JOptionPane.INFORMATION_MESSAGE
            );

            players.remove(players.get(GameFrame.i));
            gameBoardPanel.removeNameLabel();
        }

        if(players.size() <= 1) {
            JOptionPane.showMessageDialog(
                null,
                players.get(GameFrame.i).getName() + " ha vinto",
                "Vittoria!",
                JOptionPane.INFORMATION_MESSAGE);

            this.dispose();
        } else {
            GameFrame.i++;

            if (i == players.size()) {
                i = 0;
            }

            try {
                menager.saveMenager();
            } catch (IOException e1) {
                JOptionPane.showMessageDialog(
                    null, 
                    "Errore salvataggio",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE);
                }
            this.remove(panel);

            showPanel();
            updateThread();

            if(!players.get(GameFrame.i).getStatus()){
                throwDice();
                updateTimerThread();
            }
        }
    }

    private void doChanceCardAction(int id) {
        switch (id) {
            case 1:
                while (players.get(GameFrame.i).getBox() != 1) {
                    gameBoardPanel.movePlayer();
                }
                break;
            case 2:
                players.get(GameFrame.i).addMoney(60);
                break;
            case 3:
                players.get(GameFrame.i).addMoney(50);
                break;
            case 4:
                players.get(GameFrame.i).addMoney(25);
                break;
            case 5:
                try {
                    players.get(GameFrame.i).subMoney(125);
                } catch (MoneyException e) {
                    JOptionPane.showMessageDialog(null, 
                     "Errore: " + e.getMessage(),
                     "Errore", 
                     JOptionPane.ERROR_MESSAGE);
                }
                break;
            case 6:
                players.get(GameFrame.i).addCard();
                break;
            case 7:
                players.get(GameFrame.i).addMoney(250);
                break;
            case 8:
                while (players.get(GameFrame.i).getBox() != 0) {
                    gameBoardPanel.movePlayer();
                    players.get(GameFrame.i).addMoney(200);
                }
                break;
            case 9:
                Object[] options = {"Paga", "Pesca"};
                int choice = JOptionPane.showOptionDialog(null,
                                                        "Scegli opzione",
                                                        "Probabilita'",
                                                        JOptionPane.YES_NO_OPTION,
                                                        JOptionPane.QUESTION_MESSAGE,
                                                        null,
                                                        options, 
                                                        options[0]);

                if (choice == JOptionPane.YES_OPTION) {
                    try {
                        players.get(GameFrame.i).subMoney(25);
                    } catch (MoneyException e) {
                        JOptionPane.showMessageDialog(null, 
                            "Errore: " + e.getMessage(),
                            "Errore", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                } else if (choice == JOptionPane.NO_OPTION) {
                    JOptionPane.showMessageDialog(null,
                        menager.getSuddenCards().get(0).getDescription(), 
                        menager.getSuddenCards().get(0).getTitle(), 
                        JOptionPane.INFORMATION_MESSAGE);

                    doSuddenCardAction(menager.getSuddenCards().get(0).getId());
                    Collections.shuffle(menager.getSuddenCards());
                }
            case 10:
                players.get(GameFrame.i).addMoney(500);
                break;
            case 11:
                try {
                    players.get(GameFrame.i).subMoney(250);
                } catch (MoneyException e) {
                    JOptionPane.showMessageDialog(null, 
                    "Errore: " + e.getMessage(),
                    "Errore", 
                    JOptionPane.ERROR_MESSAGE);
                }
                break;
            case 12:
                players.get(GameFrame.i).setStatus(true);
                turnOver();
                break;
            case 13:
                for (Player player : players) {
                    if (player != players.get(GameFrame.i)) {
                        try {
                            player.subMoney(25);
                        } catch (MoneyException e) {
                            JOptionPane.showMessageDialog(null, 
                                "Errore: " + e.getMessage(),
                                "Errore", 
                                JOptionPane.ERROR_MESSAGE);
                        }

                        players.get(GameFrame.i).addMoney(25);
                    }
                }
                break;
            case 14:
                players.get(GameFrame.i).addMoney(125);
                break;
            case 15:
                try {
                    players.get(GameFrame.i).subMoney(125);
                } catch (MoneyException e) {
                    JOptionPane.showMessageDialog(null, 
                    "Errore: " + e.getMessage(),
                    "Errore", 
                    JOptionPane.ERROR_MESSAGE);
                }
                break;
            default:
                break;
        }
    }

    private void doSuddenCardAction(int id) {
        switch (id) {
            case 1:
                while (players.get(GameFrame.i).getBox() != 24) {
                        gameBoardPanel.movePlayer();
                }
                break;
            case 2:
                players.get(GameFrame.i).setStatus(true);
                break;
            case 3:
                gameBoardPanel.goBackThreeBox();
                break;
            case 4:
                while (players.get(GameFrame.i).getBox() != 11) {
                    gameBoardPanel.movePlayer();
                }
                break;
            case 5:
                try {
                    players.get(GameFrame.i).subMoney(50);
                } catch (MoneyException e) {
                    JOptionPane.showMessageDialog(null, 
                    "Errore: " + e.getMessage(),
                    "Errore", 
                    JOptionPane.ERROR_MESSAGE);
                }
                break;
            case 6:
                while (players.get(GameFrame.i).getBox() != 25) {
                    gameBoardPanel.movePlayer();
                }
                break;
            case 7:
                try {
                    players.get(GameFrame.i).subMoney(40);
                } catch (MoneyException e) {
                    JOptionPane.showMessageDialog(null, 
                    "Errore: " + e.getMessage(),
                    "Errore", 
                    JOptionPane.ERROR_MESSAGE);
                }
                break;
            case 8:
                while (players.get(GameFrame.i).getBox() != 39) {
                    gameBoardPanel.movePlayer();
                }
                break;
            case 9:
                try {
                    players.get(GameFrame.i).subMoney(375);
                } catch (MoneyException e) {
                    JOptionPane.showMessageDialog(null, 
                    "Errore: " + e.getMessage(),
                    "Errore", 
                    JOptionPane.ERROR_MESSAGE);
                }
                break;
            case 10:
                players.get(GameFrame.i).addCard();
                break;
            case 11:
                players.get(GameFrame.i).addMoney(375);
                break;
            case 12:
                players.get(GameFrame.i).addMoney(125);
                break;
            case 13:
                while (players.get(GameFrame.i).getBox() != 0) {
                    gameBoardPanel.movePlayer();
                }
                break;
            case 14:
                players.get(GameFrame.i).addMoney(250);
                break;
            case 15:
                players.get(GameFrame.i).setStatus(true);
                break;
            default:
                break;
        }
    }
}
