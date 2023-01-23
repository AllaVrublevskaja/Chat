package com.example.chat;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try(Socket client = new Socket("localhost", 8080);
            Scanner scanner = new Scanner(System.in)) {
            String nickName;
            System.out.println("Введите nickName :");
            nickName = scanner.nextLine();
            ObjectOutputStream outputStream = new ObjectOutputStream(client.getOutputStream());

            new MessageReader(client).start();

            while(true) {
                String text = scanner.nextLine();
                Message message = new Message(text,nickName);
                outputStream.writeObject(message);
                outputStream.flush();
            }
        } catch(IOException ex) {
            ex.printStackTrace();
        }
    }

    static class MessageReader extends Thread {
        private final Socket client;

        public MessageReader(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            try(ObjectInputStream reader = new ObjectInputStream(client.getInputStream())) {
                while (true) {
                    Message message = (Message) reader.readObject();
                    System.out.println("Anonymous: " + message.toString());
                }
            } catch(IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
