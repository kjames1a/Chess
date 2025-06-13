package ui;


import java.util.Scanner;
import websocket.NotificationHandler;
import websocket.messages.NotificationMessage;

import static ui.EscapeSequences.*;

public class Repl implements NotificationHandler {
    private final ChessClient client;

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl, this);
    }

    public void run() {
        System.out.printf("%sWelcome to Chess%s. Sign in to start.\n", BLACK_KNIGHT, BLACK_QUEEN);
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    public void notify(NotificationMessage notification) {
        if (notification.message() != null) {
            System.out.println(SET_TEXT_COLOR_BLUE + notification.message());
            printPrompt();
        }
    }

    private void printPrompt() {
        System.out.print("\n" + ">>> ");
    }

}
