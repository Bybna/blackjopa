package org.example;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Game {
    private List<Player> players;
    private Player humanPlayer;
    private LogWriter logWriter;

    public Game() {
        this.players = new ArrayList<>();
        this.logWriter = new LogWriter("game_log.json");
    }

    public void startGame() {
        System.out.println("Добро пожаловать в игру Перудо!");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите 1, чтобы начать игру, 2 для просмотра правил, 3 для выхода.");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1 -> initPlayers(scanner);
            case 2 -> showRules();
            case 3 -> System.exit(0);
            default -> System.out.println("Неверный выбор. Пожалуйста, выберите снова.");
        }
    }

    private void initPlayers(Scanner scanner) {
        System.out.println("Введите ваше имя или введите 'auto' для генерации имени:");
        String nameInput = scanner.next();
        this.humanPlayer = nameInput.equalsIgnoreCase("auto") ? generatePlayer("Player") : new Player(nameInput);
        players.add(humanPlayer);

        for (int i = 0; i < 3; i++) {
            players.add(generatePlayer("Computer" + (i + 1)));
        }

        logWriter.logGameStart(players);
        playGame();
    }

    private Player generatePlayer(String baseName) {
        Random rand = new Random();
        String name = baseName + rand.nextInt(100000);
        return new Player(name);
    }

    private void playGame() {
        System.out.println("Игра начинается!");
        boolean gameInProgress = true;

        while (gameInProgress) {
            System.out.println("Новый тур начинается!");
            for (Player player : players) {
                player.rollDice();
                System.out.println(player.getName() + " бросил кости: " + player.getDice());
            }

            boolean roundInProgress = true;
            int currentBidCount = 0;
            int currentBidValue = 0;
            Player currentPlayer = players.get(0);

            while (roundInProgress) {
                System.out.println("Ход игрока: " + currentPlayer.getName());

                if (currentPlayer == humanPlayer) {
                    System.out.println("Введите ставку (формат: количество номинал), 'не верю' или 'выйти' для выхода из игры:");
                    Scanner scanner = new Scanner(System.in);
                    String input = scanner.nextLine();

                    if (input.equals("выйти")) {
                        System.out.println("Вы вышли из игры.");
                        System.exit(0);
                    }

                    if (input.equalsIgnoreCase("не верю")) {
                        if (checkBid(currentBidCount, currentBidValue)) {
                            System.out.println("Ставка подтверждена! " + currentPlayer.getName() + " проиграл раунд.");
                        } else {
                            System.out.println("Ставка неверна. " + currentPlayer.getName() + " выигрывает раунд.");
                        }
                        roundInProgress = false;
                    } else {
                        try {
                            String[] bid = input.split(" ");
                            currentBidCount = Integer.parseInt(bid[0]);
                            currentBidValue = Integer.parseInt(bid[1]);
                        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                            System.out.println("Неверный формат ввода. Попробуйте снова.");
                            continue;
                        }
                    }
                } else {
                    if (Math.random() < 0.6) {
                        currentBidCount += 1;
                        System.out.println(currentPlayer.getName() + " повышает ставку: " + currentBidCount + " " + currentBidValue);
                    } else {
                        System.out.println(currentPlayer.getName() + " не верит!");
                        if (checkBid(currentBidCount, currentBidValue)) {
                            System.out.println("Ставка подтверждена! " + currentPlayer.getName() + " проиграл раунд.");
                        } else {
                            System.out.println("Ставка неверна. " + currentPlayer.getName() + " выигрывает раунд.");
                        }
                        roundInProgress = false;
                    }
                }

                int nextPlayerIndex = (players.indexOf(currentPlayer) + 1) % players.size();
                currentPlayer = players.get(nextPlayerIndex);
            }

            if (players.size() == 1) {
                gameInProgress = false;
                System.out.println("Победитель тура: " + players.get(0).getName());
            }
        }
    }



    private boolean checkBid(int bidCount, int bidValue) {
        int totalCount = 0;
        for (Player player : players) {
            for (int die : player.getDice()) {
                if (die == bidValue || die == 1) {
                    totalCount++;
                }
            }
        }
        return totalCount >= bidCount;
    }


    private void showRules() {
        System.out.println("Правила игры:");
        System.out.println("1. Каждый игрок бросает кости.");
        System.out.println("2. Игроки ставят ставку.");
        System.out.println("3. Если ставка верна, то игрок проигрывает раунд.");
        System.out.println("4. Если ставка неверна, то игрок выигрывает раунд.");
    }

}
