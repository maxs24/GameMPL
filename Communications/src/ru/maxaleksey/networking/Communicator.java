package ru.maxaleksey.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/*
Класс который непосредственно отправляет на сервер данные и принимает их.
*/
public class Communicator {
    private Socket s;
    private boolean active;
    private Thread communicationprocces;
    private ArrayList<Consumer<String>> dataReceivedListeners = new ArrayList<>();

    public Communicator(Socket socket){
        s = socket;
        active = true;
    }

    public boolean isAlive(){
        return active && s.isConnected();
    }

    public void addDataReceievListeners(Consumer<String> value){
        dataReceivedListeners.add(value);
    }

    public void removeDataReceivedListeners(Consumer<String> value){
        dataReceivedListeners.remove(value);
    }

    private void communicate(){
        while(isAlive()){
            try{
                String value = receivedData();
                if(value != null){
                    for(Consumer<String> it : dataReceivedListeners){
                        it.accept(value);
                    }
                }
            }catch (Exception e){
                for(StackTraceElement s:e.getStackTrace()){
                    System.out.println(s);
                }
                System.out.println(e.getMessage());
                active = false;
                try {
                    sendData("DISCONNECT {\"QUIT\":\"\"}");
                    if (!s.isClosed()) s.close();
                    System.out.println("Обмен данными неожиданно прекращен.");
                }catch (IOException exp) {
                    System.out.println("Ошибка");
                }
            }
        }
    }

    private String receivedData(){
        String data = null;
        if(isAlive()){
            try{
                BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                data = br.readLine();
            }catch (IOException e){
                active = false;
                System.out.println("Не удалось отправить данные в сеть");
                data = null;
            }
        }
        return data;
    }

    public void sendData(String data){
        try{
            if(isAlive()){
                PrintWriter pw = new PrintWriter(s.getOutputStream());
                pw.println(data);
                pw.flush();
            }
        }catch (IOException e){
            System.out.println("Не удалось получить данные из сети.");
            active = false;
        }
    }

    public void start(){
        Runnable task = () ->{
                if(communicationprocces != null &&communicationprocces.isAlive()){
                    stop();
                }
                active = true;
                Runnable task_two = () -> communicate();
                task_two.run();
                communicationprocces = new Thread(task_two);
                communicationprocces.start();
        };
        Thread thread = new Thread(task);
        thread.start();
    }

    public void stop(){
        active = false;
        if(communicationprocces != null){
            if(communicationprocces.isAlive()){
                communicationprocces.interrupt();
                try {
                    communicationprocces.join();
                }catch (InterruptedException e){
                    System.out.println("Ошибка");
                }
            }
            try {
                if (!s.isClosed()) s.close();
            }catch (IOException e){
                System.out.println("Ошибка");
            }
        }
    }
}
