package ltu.fksyg.d0019n;

import java.util.ArrayList;

/**
 * Representerar ett rum i dungeon.
 * Varje rum har en beskrivning och dörrar som leder till andra rum.
 */
public class Room {
    private String roomDesc;
    private ArrayList<Door> doors;

    /**
     * Konstruktor för att skapa ett nytt rum.
     * @param roomDesc Beskrivning av rummet
     */
    public Room(String roomDesc) {
        this.roomDesc = roomDesc;
        this.doors = new ArrayList<>();
    }

    /**
     * Hämtar rummets beskrivning.
     * @return Rummets beskrivning
     */
    public String getRoomDesc() {
        return roomDesc;
    }

    /**
     * Sätter rummets beskrivning.
     * @param roomDesc Den nya beskrivningen
     */
    public void setRoomDesc(String roomDesc) {
        this.roomDesc = roomDesc;
    }

    /**
     * Lägger till en dörr till rummet.
     * @param door Dörren som ska läggas till
     */
    public void addDoor(Door door) {
        doors.add(door);
    }

    /**
     * Hittar en dörr baserat på väderstreck.
     * @param direction Väderstrecket ('n', 's', 'ö', 'v')
     * @return Dörren om den finns, annars null
     */
    public Door getDoor(char direction) {
        for (Door door : doors) {
            if (door.getPosition() == direction) {
                return door;
            }
        }
        return null;
    }

    /**
     * Hämtar alla dörrar i rummet.
     * @return Lista med alla dörrar
     */
    public ArrayList<Door> getDoors() {
        return doors;
    }

    /**
     * Skriver ut rummets beskrivning och visar tillgängliga dörrar.
     * Detta är spelets huvudsakliga interaktionsmetod för varje rum.
     */
    public void doNarrative() {
        System.out.println(roomDesc);

        if (doors.isEmpty()) {
            System.out.println("Det finns inga dörrar i detta rum.");
        } else {
            System.out.print("Du kan gå ");
            ArrayList<String> directions = new ArrayList<>();

            for (Door door : doors) {
                if (!door.isLocked()) {
                    directions.add(door.getDirectionName() + " [" + door.getPosition() + "]");
                }
            }

            if (directions.isEmpty()) {
                System.out.println("ingenstans. Alla dörrar är låsta!");
            } else {
                for (int i = 0; i < directions.size(); i++) {
                    if (i == directions.size() - 1 && directions.size() > 1) {
                        System.out.print(" eller " + directions.get(i));
                    } else if (i > 0) {
                        System.out.print(", " + directions.get(i));
                    } else {
                        System.out.print(directions.get(i));
                    }
                }
                System.out.println();
            }
        }
    }
}
