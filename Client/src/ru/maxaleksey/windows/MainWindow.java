package ru.maxaleksey.windows;

import ru.maxaleksey.logic.GameDataUI;
import ru.maxaleksey.networking.Client;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

// главное окно в которой проходит игра
public class MainWindow extends JFrame {
    private static GameDataUI gameDataUI = GameDataUI.getInstance();
    private Client client;
    private static final int CELL_SIZE = (int) (70/Math.pow(1.1,gameDataUI.getSize().x)-5);
    private static final int WINDOW_WIDTH_ONECELL = 70;
    private static final int WINDOW_HEIGHT_ONECELL = 70;
    public static boolean stopGame = false;
    private Animator animator;

    public static int getCELL_SIZE() { return CELL_SIZE; }

    public MainWindow(){
        this.setBounds(0, 0, gameDataUI.getSize().x * WINDOW_WIDTH_ONECELL + 100, gameDataUI.getSize().y * WINDOW_HEIGHT_ONECELL + 100);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(MainWindow.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        this.add(panel);

        panel.setBounds(0, 0, gameDataUI.getSize().x * getCELL_SIZE(), gameDataUI.getSize().y * getCELL_SIZE());
        panel.setBackground(Color.WHITE);

        gameDataUI.addSendDataListeners(this::draw);
        client  = new Client();
        animator = new Animator(panel.getGraphics());
    }

    private void draw(String data){
        try {
            animator.drawAll();
            animator.saveToFile(data);
        }catch (IOException e){
            System.out.println("Ошибка");
        }
    }

    public static void main(String[] args) {
        MainWindow window = new MainWindow();
    }

}
