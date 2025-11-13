package ltu.fksyg.d0019n;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Representerar dungeon där spelet äger rum.
 * Hanterar alla rum, spelarens position och spellogiken.
 */
public class Dungeon {
    private String welcomeMessage;
    private Room currentRoom;
    private ArrayList<Room> rooms;
    private Player player;
    private Scanner scanner;

    /**
     * Konstruktor för att skapa en dungeon.
     * @param welcomeMessage Välkomstmeddelandet som visas när spelet startar
     */
    public Dungeon(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
        this.rooms = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    /**
     * Hämtar välkomstmeddelandet.
     * @return Välkomstmeddelandet
     */
    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    /**
     * Sätter välkomstmeddelandet.
     * @param welcomeMessage Det nya välkomstmeddelandet
     */
    public void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    /**
     * Hämtar det aktuella rummet spelaren är i.
     * @return Det aktuella rummet
     */
    public Room getCurrentRoom() {
        return currentRoom;
    }

    /**
     * Sätter det aktuella rummet.
     * @param currentRoom Det nya aktuella rummet
     */
    public void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    /**
     * Lägger till ett rum till dungeon.
     * @param room Rummet som ska läggas till
     */
    public void addRoom(Room room) {
        rooms.add(room);
    }

    /**
     * Hämtar alla rum i dungeon.
     * @return Lista med alla rum
     */
    public ArrayList<Room> getRooms() {
        return rooms;
    }

    /**
     * Sätter spelaren som navigerar genom dungeon.
     * @param player Spelaren
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Hämtar spelaren.
     * @return Spelaren
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Visar välkomstmeddelandet.
     */
    public void showWelcome() {
        System.out.println(welcomeMessage);
    }

    /**
     * Huvudspellogiken. Kör spelet tills spelaren väljer att avsluta.
     */
    public void playGame() {
        boolean playing = true;

        while (playing) {
            // Visa rumsbeskrivning och tillgängliga dörrar
            System.out.println();
            currentRoom.doNarrative();
            System.out.println();

            // Be spelaren om input
            System.out.print("Vad vill du göra? ");
            String input = scanner.nextLine().trim().toLowerCase();

            // Hantera spelarens kommando
            if (input.equals("ö")) {
                // Avsluta spelet
                System.out.println("\nTack för att du spelade Dragon Treasure, " + player.getName() + "!");
                playing = false;
            } else if (input.length() == 1) {
                char direction = input.charAt(0);
                Door door = currentRoom.getDoor(direction);

                if (door != null) {
                    if (door.isLocked()) {
                        System.out.println("\nDörren är låst!");
                    } else {
                        // Flytta till nästa rum
                        currentRoom = door.getLeadsTo();
                    }
                } else {
                    System.out.println("\nDu kan inte gå åt det hållet!");
                }
            } else {
                System.out.println("\nOgiltigt kommando! Använd väderstreck (n, s, ö, v) för att navigera eller 'ö' för att avsluta.");
            }
        }

        scanner.close();
    }
}
