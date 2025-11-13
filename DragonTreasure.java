import java.util.Scanner;

/**
 * Huvudklass för Dragon Treasure spelet.
 * Skapar och initierar spelet, samt startar spellogiken.
 */
public class DragonTreasure {
    private static Dungeon dungeon;
    private static Player player;

    /**
     * Huvudmetod som startar spelet.
     */
    public static void main(String[] args) {
        setupGame();
        playGame();
    }

    /**
     * Sätter upp spelet genom att skapa alla rum, dörrar och spelaren.
     * Denna metod initierar hela dungeonens struktur.
     */
    private static void setupGame() {
        Scanner scanner = new Scanner(System.in);

        // Visa välkomstskärm med ASCII art
        System.out.println(
            "                  _.--.\n"+
            "              _.-'_:-'||\n"+
            "          _.-'_.-::::'||\n"+
            "     _.-:'_.-::::::'  ||\n"+
            "   .'`-.-:::::::'     ||\n"+
            "  /.'`;|:::::::'      ||_\n"+
            " ||   ||::::::'      _.;._'-._\n"+
            " ||   ||:::::'   _.-!oo @.!-._'-.\n"+
            " \'.  ||:::::.-!() oo @!()@.-'_.||\n"+
            "   '.'-;|:.-'.&$@.& ()$%-'o.'\\U||\n"+
            "     `>'-.!@%()@'@_%-'_.-o _.|'||\n"+
            "      ||-._'-.@.-'_.-' _.-o  |'||\n"+
            "      ||=[ '-._.-\\U/.-'    o |'||\n"+
            "      || '-.]=|| |'|      o  |'||\n"+
            "      ||      || |'|        _| ';\n"+
            "      ||      || |'|    _.-'_.-'\n"+
            "      |'-._   || |'|_.-'_.-'\n"+
            "      '-._'-.|| |' `_.-'\n"+
            "           '-.||_/.-'\n");

        System.out.println("\nVälkommen till Dragon Treasure!");
        System.out.print("Skriv ditt namn och tryck på [Enter] för att starta ett nytt spel... ");
        String playerName = scanner.nextLine();

        // Skapa spelaren
        player = new Player(playerName);

        // Skapa dungeon med välkomstmeddelande
        String welcomeMsg = "Välkommen " + playerName + " till din skattjakt.\n" +
                           "Du står utanför en grotta. Det luktar svavel från öppningen.\n" +
                           "Grottsöppningen är österut.";
        dungeon = new Dungeon(welcomeMsg);
        dungeon.setPlayer(player);

        // Skapa alla rum
        Room startRoom = new Room(
            "Du står utanför en grotta. Det luktar svavel från öppningen.\n" +
            "Grottsöppningen är österut."
        );

        Room entranceRoom = new Room(
            "När du går in i grottan kollapsar ingången bakom dig.\n" +
            "Rummet är upplyst av några ljus som sitter på ett bord framför dig."
        );

        Room corridorRoom = new Room(
            "Du står i en korridor. Det luktar unket här.\n" +
            "Du ser en låst dörr i öster."
        );

        Room emptyRoomNorth = new Room(
            "Du ser en död kropp på golvet."
        );

        Room emptyRoomSouth = new Room(
            "Ett tomt rum. Du hör ett mullrande ljud i fjärran."
        );

        Room darkRoom = new Room(
            "Det är mycket mörkt här. Du hör något andas tungt någonstans."
        );

        Room treasureRoom = new Room(
            "Du kommer in i ett fuktigt rum med vatten sipprandes längs den västra väggen."
        );

        Room deepCaveRoom = new Room(
            "Du ser en brinnande fackla i rummets ena hörn och känner en motbjudande stank."
        );

        Room hallRoom = new Room(
            "Du kommer in i ett rymligt bergrum med ljusstrimma sipprandes genom en spricka i\n" +
            "den östra väggen."
        );

        Room finalRoom = new Room(
            "Du lämnar grottan med livet i behåll. Grattis, du förlorade inte!\n\n" +
            getTreasureArt()
        );

        // Koppla ihop rum med dörrar
        startRoom.addDoor(new Door('ö', false, entranceRoom));

        entranceRoom.addDoor(new Door('v', false, corridorRoom));

        corridorRoom.addDoor(new Door('n', false, emptyRoomNorth));
        corridorRoom.addDoor(new Door('s', false, emptyRoomSouth));
        corridorRoom.addDoor(new Door('ö', true, treasureRoom));  // Låst dörr!

        emptyRoomNorth.addDoor(new Door('s', false, corridorRoom));
        emptyRoomNorth.addDoor(new Door('v', false, darkRoom));

        emptyRoomSouth.addDoor(new Door('n', false, corridorRoom));
        emptyRoomSouth.addDoor(new Door('v', false, treasureRoom));

        darkRoom.addDoor(new Door('ö', false, emptyRoomNorth));

        treasureRoom.addDoor(new Door('ö', false, deepCaveRoom));

        deepCaveRoom.addDoor(new Door('ö', false, emptyRoomSouth));
        deepCaveRoom.addDoor(new Door('s', false, hallRoom));

        hallRoom.addDoor(new Door('v', false, finalRoom));

        // Lägg till alla rum i dungeon
        dungeon.addRoom(startRoom);
        dungeon.addRoom(entranceRoom);
        dungeon.addRoom(corridorRoom);
        dungeon.addRoom(emptyRoomNorth);
        dungeon.addRoom(emptyRoomSouth);
        dungeon.addRoom(darkRoom);
        dungeon.addRoom(treasureRoom);
        dungeon.addRoom(deepCaveRoom);
        dungeon.addRoom(hallRoom);
        dungeon.addRoom(finalRoom);

        // Sätt startrummet
        dungeon.setCurrentRoom(startRoom);
    }

    /**
     * Startar spellogiken genom att visa välkomstmeddelandet och
     * starta Dungeon's playGame() metod.
     */
    private static void playGame() {
        dungeon.showWelcome();
        dungeon.playGame();
    }

    /**
     * Returnerar ASCII art för skatten.
     * @return Skatten som ASCII art
     */
    private static String getTreasureArt() {
        return "                                                  .~))>>\n"+
               "                                                 .~)>>\n"+
               "                                               .~))))>>>\n"+
               "                                             .~))>>             ___\n"+
               "                                           .~))>>)))>>      .-~))>>\n"+
               "                                         .~)))))>>       .-~))>>)>\n"+
               "                                       .~)))>>))))>>  .-~)>>)>\n"+
               "                   )                 .~))>>))))>>  .-~)))))>>)>\n"+
               "                ( )@@*)             //)>))))))  .-~))))>>)>\n"+
               "              ).@(@@               //))>>))) .-~))>>)))))>>)>\n"+
               "            (( @.@).              //))))) .-~)>>)))))>>)>\n"+
               "          ))  )@@*.@@ )          //)>))) //))))))>>))))>>)>\n"+
               "       ((  ((@@@.@@             |/))))) //)))))>>)))>>)>\n"+
               "      )) @@*. )@@ )   (\\_(\\-\\b  |))>)) //)))>>)))))))>>)>\n"+
               "    (( @@@(.@(@ .    _/`-`  ~|b |>))) //)>>)))))))>>)>\n"+
               "     )* @@@ )@*     (@)  (@) /\\b|))) //))))))>>))))>>\n"+
               "   (( @. )@( @ .   _/  /    /  \\b)) //))>>)))))>>>_._\n"+
               "    )@@ (@@*)@@.  (6///6)- / ^  \\b)//))))))>>)))>>   ~~-.\n"+
               " ( @jgs@@. @@@.*@_ VvvvvV//  ^  \\b/)>>))))>>      _.     `bb\n"+
               " ((@@ @@@*.(@@ . - | o |' \\ (  ^   \\b)))>>        .'       b`,\n"+
               "   ((@@).*@@ )@ )   \\^^^/  ((   ^  ~)_        \\  /           b `,\n"+
               "     (@@. (@@ ).     `-'   (((   ^    `\\ \\ \\ \\ \\|             b  `.\n"+
               "       (*.@*              / ((((        \\| | |  \\       .       b `.\n"+
               "                         / / (((((  \\    \\ /  _.-~\\     Y,      b  ;\n"+
               "                        / / / (((((( \\    \\.-~   _.`\" _.-~`,    b  ;\n"+
               "                       /   /   `(((((()    )    (((((~      `,  b  ;\n"+
               "                     _/  _/      `\"\"\"/   /'                  ; b   ;\n"+
               "                 _.-~_.-~           /  /'                _.'~bb _.'\n"+
               "               ((((~~              / /'              _.'~bb.--~\n"+
               "                                  ((((          __.-~bb.-~\n"+
               "                                              .'  b .~~\n"+
               "                                              :bb ,' \n"+
               "                                              ~~~~\n";
    }
}
