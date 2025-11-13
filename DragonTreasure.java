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

        // Visa välkomstskärm
        System.out.println("Välkommen till Dragon Treasure!");
        System.out.print("Skriv ditt namn och tryck på [Enter] för att starta ett nytt spel... ");
        String playerName = scanner.nextLine();

        // Skapa spelaren
        player = new Player(playerName);

        // Skapa dungeon med välkomstmeddelande
        String welcomeMsg = "Välkommen " + playerName + " till din skattjakt.";
        dungeon = new Dungeon(welcomeMsg);
        dungeon.setPlayer(player);

        // Skapa alla rum enligt körexemplet
        Room startRoom = new Room(
            "Du står utanför en grotta. Det luktar svavel från öppningen.\n" +
            "Grottsöppningen är österut. Skriv \"e\" och tryck på [Enter] för att komma in i grottan"
        );

        Room entranceRoom = new Room(
            "När du går in i grottan kollapsar ingången bakom dig.\n" +
            "Rummet är upplyst av några ljus som sitter på ett bord framför dig."
        );

        Room emptyRoom = new Room(
            "Du ser en död kropp på golvet."
        );

        Room torchRoom = new Room(
            "Du ser en brinnande fackla i rummets ena hörn och känner en motbjudande stank.\n" +
            "Du ser en utgång österut [e]"
        );

        Room wetRoom = new Room(
            "Du kommer in i ett fuktigt rum med vatten sipprandes längs den västra väggen.\n" +
            "Du ser en låst dörr i öster [e]"
        );

        Room hallRoom = new Room(
            "Du kommer in i ett rymligt bergrum med en ljusstrimma sipprandes genom en spricka i\n" +
            "den östra väggen."
        );

        Room exitRoom = new Room(
            "Du lämnar grottan med livet i behåll. Grattis, du förlorade inte!\n\n" +
            getTreasureArt()
        );

        // Koppla ihop rum med dörrar enligt körexemplet
        startRoom.addDoor(new Door('ö', false, entranceRoom));

        entranceRoom.addDoor(new Door('n', false, emptyRoom));
        entranceRoom.addDoor(new Door('s', false, torchRoom));

        emptyRoom.addDoor(new Door('s', false, torchRoom));
        emptyRoom.addDoor(new Door('v', false, wetRoom));

        torchRoom.addDoor(new Door('ö', false, exitRoom));
        torchRoom.addDoor(new Door('v', false, wetRoom));
        torchRoom.addDoor(new Door('s', false, hallRoom));

        wetRoom.addDoor(new Door('ö', true, wetRoom));  // Låst dörr med speciellt meddelande
        wetRoom.addDoor(new Door('n', false, torchRoom));
        wetRoom.addDoor(new Door('v', false, hallRoom));

        hallRoom.addDoor(new Door('n', false, wetRoom));
        hallRoom.addDoor(new Door('ö', false, wetRoom));

        // Lägg till alla rum i dungeon
        dungeon.addRoom(startRoom);
        dungeon.addRoom(entranceRoom);
        dungeon.addRoom(emptyRoom);
        dungeon.addRoom(torchRoom);
        dungeon.addRoom(wetRoom);
        dungeon.addRoom(hallRoom);
        dungeon.addRoom(exitRoom);

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
