package com.example.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try(Socket client = new Socket("localhost", 8080);
            PrintWriter writer = new PrintWriter(client.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in)) {

            new MessageReader(client).start();

            while(true) {
                String message = scanner.nextLine();
                writer.println(message);
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
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()))) {
                while (true) {
                    String message = reader.readLine();
                    System.out.println("Anonymous: " + message);
                }
            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
