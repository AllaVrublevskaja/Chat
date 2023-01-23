package com.example.chat;

import java.io.*;
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
    private static final List<ObjectOutputStream> writers = new ArrayList<>();

    public static void main(String[] args) {
        try(ServerSocket server = new ServerSocket(8080)) {
            //Ожидаем новые подключения и принимаем их
            while(true) {
                Socket client = server.accept();

                System.out.printf("Подключился %d-й клиент!\n", clients.size() + 1);
                clients.add(client);
                writers.add(new ObjectOutputStream(client.getOutputStream()));
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
            try(ObjectInputStream reader = new ObjectInputStream(client.getInputStream())) {
                while(true) {
                    Message message = (Message) reader.readObject();
                    System.out.println(Thread.currentThread().getName() + " : " + message.toString());
                    for(ObjectOutputStream writer : writers) {
                        writer.writeObject(message);
                        writer.flush();
                    }
                }
            } catch(IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
