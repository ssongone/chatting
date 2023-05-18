package org.example;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ChatClient {
    public static void main(String[] args) {
        try(Socket socket = new Socket("localhost", 12345)) {

            System.out.println("채팅 서버에 접속되었습니다.");

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            new Thread(() -> {
                try {
                    String message;
                    while ((message = reader.readLine()) != null) {
                        System.out.println("수신: " + message);
                    }
                } catch (IOException e) {
                    System.out.println("Connection has been closed.");
                }
            }).start();

            // 사용자 입력을 받아 서버에 메시지 전송
            Scanner scanner = new Scanner(System.in);
            String message;
            while (true) {
                message = scanner.nextLine();
                writer.write(message + "\n");
                writer.flush();
            }
        } catch (IOException e) {
            System.out.println("Connection has been closed.");
        }

    }
}


