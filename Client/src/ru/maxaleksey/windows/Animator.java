package ru.maxaleksey.windows;

import ru.maxaleksey.logic.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Animator {
    private GameDataUI gameDataUI;
    private Graphics graphics;
    private Graphics screenGraphics;
    private BufferedImage img;
    private HashMap<Integer, BufferedImage> images = new HashMap<>();

    public Animator(Graphics screenGraphics, GameDataUI gamedata){
        gameDataUI = gamedata;
        this.screenGraphics = screenGraphics;
        this.img = new BufferedImage(getFieldWidth(), getFieldHeight(), BufferedImage.TYPE_3BYTE_BGR);
        this.graphics = img.getGraphics();
    }

    public int getFieldHeight() {
        return gameDataUI.getGameField().getSize().x * MainWindow.getCELL_SIZE();
    }

    public int getFieldWidth() {
        return gameDataUI.getGameField().getSize().y * MainWindow.getCELL_SIZE();
    }

    public void drawAll() throws IOException{
        clear();
        if (images.isEmpty()) {
            downloadImages();
        }
        if (!MainWindow.stopGame) {
            drawCells();
            drawObstacles();
        } else {
            drawImage();
        }
        if (gameDataUI.getIs_win().equals("win")) {
            graphics.drawImage(images.get(-4), 0, 0, getFieldWidth(), getFieldHeight(), null);
        }
        if (gameDataUI.getIs_win().equals("lose")) {
            graphics.drawImage(images.get(4), 0, 0, getFieldWidth(), getFieldHeight(), null);
        }
        drawToScreen();
    }

    private void drawImage() throws IOException {
        drawCells();
        drawObstacles();
    }

    private void downloadImages() {
        //images = new HashMap<>();
        try {
            String folder = "Client//src//resources";
            BufferedImage image1 = ImageIO.read(new File(folder + "//" + "-1.jpg"));
            images.put(-1, image1);
            BufferedImage image2 = ImageIO.read(new File(folder + "//" + "0.1.png"));
            images.put(0, image2);
            BufferedImage image3 = ImageIO.read(new File(folder + "//" + "1.jpg"));
            images.put(1, image3);
            BufferedImage imagewin1 = ImageIO.read(new File(folder + "//" + "win1.png"));
            images.put(-4, imagewin1);
            BufferedImage imagewin2 = ImageIO.read(new File(folder + "//" + "win2.png"));
            images.put(4, imagewin2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawCells() throws IOException {
        Point points = gameDataUI.getGameField().getSize();
        for (int i = 0; i < points.x; i++) {
            for (int j = 0; j < points.y; j++) {
                graphics.drawImage(images.get(0), i * MainWindow.getCELL_SIZE(), j * MainWindow.getCELL_SIZE(), MainWindow.getCELL_SIZE(), MainWindow.getCELL_SIZE(), null);
            }
        }
        graphics.setColor(Color.BLACK);
        for (int i = 0; i < points.x; i++) {
            for (int j = 0; j < points.y; j++) {
                graphics.drawLine(i * MainWindow.getCELL_SIZE(), j * MainWindow.getCELL_SIZE(), i * MainWindow.getCELL_SIZE() + MainWindow.getCELL_SIZE(), j * MainWindow.getCELL_SIZE());
                graphics.drawLine(i * MainWindow.getCELL_SIZE(), j * MainWindow.getCELL_SIZE(), i * MainWindow.getCELL_SIZE(), j * MainWindow.getCELL_SIZE() + MainWindow.getCELL_SIZE());
            }
        }
        graphics.drawImage(images.get(1), gameDataUI.getPositionPlayer().x * MainWindow.getCELL_SIZE(), gameDataUI.getPositionPlayer().y * MainWindow.getCELL_SIZE(), MainWindow.getCELL_SIZE(), MainWindow.getCELL_SIZE(), null);
        graphics.drawImage(images.get(-1), (gameDataUI.getOpponentPosition().x - 1) * MainWindow.getCELL_SIZE(), (gameDataUI.getOpponentPosition().y - 1) * MainWindow.getCELL_SIZE(), MainWindow.getCELL_SIZE(), MainWindow.getCELL_SIZE(), null);
    }

    private void drawObstacles() {
        graphics.setColor(Color.RED);
        for (Obstacle obstacle : gameDataUI.getBarriers()) {
            if (obstacle.getOne()[0].x > obstacle.getOne()[1].x) {
                graphics.drawLine(obstacle.getOne()[0].x * MainWindow.getCELL_SIZE(), obstacle.getOne()[0].y * MainWindow.getCELL_SIZE(),
                        obstacle.getOne()[0].x * MainWindow.getCELL_SIZE(), obstacle.getOne()[0].y * MainWindow.getCELL_SIZE() + MainWindow.getCELL_SIZE());
            }
            if (obstacle.getOne()[0].x < obstacle.getOne()[1].x) {
                graphics.drawLine(obstacle.getOne()[1].x * MainWindow.getCELL_SIZE(), obstacle.getOne()[1].y * MainWindow.getCELL_SIZE(),
                        obstacle.getOne()[1].x * MainWindow.getCELL_SIZE(), obstacle.getOne()[1].y * MainWindow.getCELL_SIZE() + MainWindow.getCELL_SIZE());
            }

            if (obstacle.getTwo()[0].x > obstacle.getTwo()[1].x) {
                graphics.drawLine(obstacle.getTwo()[0].x * MainWindow.getCELL_SIZE(), obstacle.getTwo()[0].y * MainWindow.getCELL_SIZE(),
                        obstacle.getTwo()[0].x * MainWindow.getCELL_SIZE(), obstacle.getTwo()[0].y * MainWindow.getCELL_SIZE() + MainWindow.getCELL_SIZE());
            }
            if (obstacle.getTwo()[0].x < obstacle.getTwo()[1].x) {
                graphics.drawLine(obstacle.getTwo()[1].x * MainWindow.getCELL_SIZE(), obstacle.getTwo()[1].y * MainWindow.getCELL_SIZE(),
                        obstacle.getTwo()[1].x * MainWindow.getCELL_SIZE(), obstacle.getTwo()[1].y * MainWindow.getCELL_SIZE() + MainWindow.getCELL_SIZE());
            }

            if (obstacle.getOne()[0].y < obstacle.getOne()[1].y) {
                graphics.drawLine(obstacle.getOne()[1].x * MainWindow.getCELL_SIZE(), obstacle.getOne()[1].y * MainWindow.getCELL_SIZE(),
                        obstacle.getOne()[1].x * MainWindow.getCELL_SIZE() + MainWindow.getCELL_SIZE(), obstacle.getOne()[1].y * MainWindow.getCELL_SIZE());
            }
            if (obstacle.getOne()[0].y > obstacle.getOne()[1].y) {
                graphics.drawLine(obstacle.getOne()[0].x * MainWindow.getCELL_SIZE(), obstacle.getOne()[0].y * MainWindow.getCELL_SIZE(),
                        obstacle.getOne()[0].x * MainWindow.getCELL_SIZE() + MainWindow.getCELL_SIZE(), obstacle.getOne()[0].y * MainWindow.getCELL_SIZE());
            }

            if (obstacle.getTwo()[0].y < obstacle.getTwo()[1].y) {
                graphics.drawLine(obstacle.getTwo()[1].x * MainWindow.getCELL_SIZE(), obstacle.getTwo()[1].y * MainWindow.getCELL_SIZE(),
                        obstacle.getTwo()[1].x * MainWindow.getCELL_SIZE() + MainWindow.getCELL_SIZE(), obstacle.getTwo()[1].y * MainWindow.getCELL_SIZE());
            }
            if (obstacle.getTwo()[0].y > obstacle.getTwo()[1].y) {
                graphics.drawLine(obstacle.getTwo()[0].x * MainWindow.getCELL_SIZE(), obstacle.getTwo()[0].y * MainWindow.getCELL_SIZE(),
                        obstacle.getTwo()[0].x * MainWindow.getCELL_SIZE() + MainWindow.getCELL_SIZE(), obstacle.getTwo()[0].y * MainWindow.getCELL_SIZE());
            }
        }
    }

    public void saveToFile(String data){
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\Users\\kseno\\OneDrive\\Рабочий стол\\GameZip\\Steps"));
            bw.write(data);
            bw.close();
        }catch (IOException e){
            System.out.println("Ошибка с файлом");
        }

    }

    private void drawToScreen() {
        screenGraphics.drawImage(img,
                0, 0,
                img.getWidth(), img.getHeight(),
                null);
    }

    private void clear() {
        graphics.setColor(Color.WHITE);
        graphics.drawRect(0, 0, img.getWidth(), img.getHeight());
    }

}
