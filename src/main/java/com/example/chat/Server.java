package com.example.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*
1. Добавить переменную nickname.
2. Написать класс Message, передавать его по сети.
3. Добавить время отправки сообщения
4. Всю эту информацию отображать в консоли
 */
public class Server {
    private static final List<Socket> clients = new ArrayList<>();
    private static final List<PrintWriter> writers = new ArrayList<>();

    public static void main(String[] args) {
        try(ServerSocket server = new ServerSocket(8080)) {
            //Ожидаем новые подключения и принимаем их
            while(true) {
                Socket client = server.accept();

                System.out.printf("Подключился %d-й клиент!\n", clients.size() + 1);
                clients.add(client);
                writers.add(new PrintWriter(client.getOutputStream(), true));
                //Запустить новый поток, связанный с клиентом.
                new MessageReciever(client).start();
            }
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    static class MessageReciever extends Thread {
        private final Socket client;

        public MessageReciever(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            //Считываем сообщения от клиента и рассылаем его другим клиентам
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
                while(true) {
                    String message = reader.readLine();
                    System.out.println(Thread.currentThread().getName() + " : " + message);
                    for(PrintWriter writer : writers) {
                        writer.println(message);
                    }
                }

            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
