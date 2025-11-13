# Dragon Treasure Del 1 - Implementationsguide

En komplett genomgång av hur projektet utvecklades från start till mål.

## Innehållsförteckning

1. [Analys av uppgiften](#1-analys-av-uppgiften)
2. [Planering och design](#2-planering-och-design)
3. [Implementation steg-för-steg](#3-implementation-steg-för-steg)
4. [Testning och verifiering](#4-testning-och-verifiering)
5. [Reflektion och lärdomar](#5-reflektion-och-lärdomar)

---

## 1. Analys av uppgiften

### 1.1 Vad ska jag skapa?

Efter att ha läst uppgiftsbeskrivningen identifierade jag följande huvudkrav:

**Primära mål:**
- Ett textbaserat äventyrsspel där spelaren navigerar genom rum
- Navigering sker med vädersträck (n, s, ö, v)
- Varje rum ska ha beskrivningar och tillgängliga dörrar
- Del 1 fokuserar ENDAST på navigering (inte strid eller föremål)

**Tekniska krav:**
- Använd klasser och objektorienterad programmering
- Implementera enligt klassdiagrammet (Figur 2)
- Privata instansvariabler med getters/setters
- Använd datastrukturer (array eller ArrayList)
- Följa "Good programming practice"

**Leverabler:**
- 5 Java-filer (Player, Door, Room, Dungeon, DragonTreasure)
- README.md med antaganden och instruktioner
- Zip-fil för inlämning

### 1.2 Analys av klassdiagrammet

Från klassdiagrammet (Figur 2) kunde jag se följande struktur:

```
DragonTreasure
  ├─ setupGame(): void
  └─ playGame(): void
       │
       ├─── Dungeon (1)
       │      ├─ currentRoom: String
       │      └─ welcomeMessage: String
       │
       ├─── Room (1..*)
       │      ├─ roomDesc: String
       │      └─ doNarrative(): void
       │
       ├─── Door (1..4)
       │      ├─ position: char
       │      └─ locked: boolean
       │
       └─── Player (1)
              └─ name: String
```

**Viktiga observationer:**
- DragonTreasure är huvudklassen som skapar och startar spelet
- Dungeon håller koll på vilket rum spelaren är i
- Room innehåller beskrivning och kan ha flera dörrar
- Door kopplar ihop rum med varandra
- Player är en enkel klass som håller spelarens namn

### 1.3 Analys av körexemplet (Bilaga 1)

Från körexemplet lärde jag mig:

1. **Startsekvens:**
   - ASCII-art av drake visas först
   - Spelet ber om spelarens namn
   - Välkomstmeddelande med personligt hälsning

2. **Spelloop:**
   - Visa rumsbeskrivning
   - Visa tillgängliga riktningar i format: "Du kan gå norr [n]"
   - Vänta på användarinput
   - Validera input och flytta spelaren eller visa felmeddelande

3. **Avslut:**
   - När spelaren når slutet visas ASCII-art av skatt
   - Gratulationsmeddelande

---

## 2. Planering och design

### 2.1 Designbeslut innan kodning

Innan jag började koda gjorde jag följande överväganden:

#### A. Hur ska rum kopplas ihop?

**Alternativ 1: Grid-system (2D-array)**
```java
Room[][] dungeon = new Room[5][5];
```
❌ Problem: Begränsar dungeonens form, svårt med oregelbundna layouter

**Alternativ 2: Dörrar med rumreferenser** ✅
```java
class Door {
    Room leadsTo;  // Direkt referens till nästa rum
}
```
✅ Fördelar: Flexibelt, rum kan kopplas ihop hur som helst

**Valt beslut:** Alternativ 2 - Varje dörr pekar direkt på rummet den leder till.

#### B. Hur ska navigation fungera?

**Alternativ 1: Hårdkodad navigation i Dungeon**
```java
if (input.equals("n") && currentRoom == room1) {
    currentRoom = room2;
}
```
❌ Problem: Mycket kod, svårt att underhålla

**Alternativ 2: Dörrar hanterar navigation** ✅
```java
Door door = currentRoom.getDoor('n');
if (door != null) {
    currentRoom = door.getLeadsTo();
}
```
✅ Fördelar: Flexibelt, lätt att lägga till nya rum

**Valt beslut:** Alternativ 2 - Dörrar vet vilket rum de leder till.

#### C. Var ska spellogiken ligga?

**Överväganden:**
- DragonTreasure: Huvudklass, bör bara initiera
- Dungeon: Representerar spelvärlden ✅
- Room: Bör bara hantera rumsinformation

**Valt beslut:** Spellogiken (`playGame()`) ligger i `Dungeon` eftersom den hanterar spelvärlden.

### 2.2 Planerad implementationsordning

Jag planerade att implementera klasserna i denna ordning:

1. **Player** (enklast, inga dependencies)
2. **Door** (behöver Room, men vi kan forward-referera)
3. **Room** (behöver Door)
4. **Dungeon** (behöver Room och Player)
5. **DragonTreasure** (behöver alla andra)

Detta kallas "bottom-up" implementation - börja med de enklaste byggstenarna först.

---

## 3. Implementation steg-för-steg

### Steg 1: Player.java (Enklaste klassen först)

**Syfte:** Representera spelaren med ett namn.

**Tankeprocess:**
- Behöver bara ett attribut: `name`
- Enligt kravet ska variabler vara privata
- Behöver constructor för att sätta namn
- Behöver getter och setter

**Kod:**

```java
package ltu.fksyg.d0019n;

public class Player {
    private String name;  // Privat enligt krav

    // Constructor - så vi kan skapa spelare med namn direkt
    public Player(String name) {
        this.name = name;
    }

    // Getter - så andra klasser kan läsa namnet
    public String getName() {
        return name;
    }

    // Setter - så namnet kan ändras senare om behövs
    public void setName(String name) {
        this.name = name;
    }
}
```

**Lärdomar från detta steg:**
- Använd JavaDoc-kommentarer för att förklara vad klassen gör
- `this.name` används för att skilja på parameter och instansvariabel
- Även enkel kod ska vara välkommenterad

### Steg 2: Door.java (Kopplar ihop rum)

**Syfte:** Representera en dörr som kopplar två rum.

**Tankeprocess:**
- Behöver veta position (väderstreck): `char position`
- Behöver veta om den är låst: `boolean locked`
- Behöver veta vilket rum den leder till: `Room leadsTo`
- Extra: En metod för att få väderstrecket som text (t.ex. "norr" istället för 'n')

**Kod:**

```java
package ltu.fksyg.d0019n;

public class Door {
    private char position;      // 'n', 's', 'ö', 'v'
    private boolean locked;     // true om låst
    private Room leadsTo;       // Rummet dörren leder till

    public Door(char position, boolean locked, Room leadsTo) {
        this.position = position;
        this.locked = locked;
        this.leadsTo = leadsTo;
    }

    // Getters och setters för alla attribut
    public char getPosition() {
        return position;
    }

    public void setPosition(char position) {
        this.position = position;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public Room getLeadsTo() {
        return leadsTo;
    }

    public void setLeadsTo(Room leadsTo) {
        this.leadsTo = leadsTo;
    }

    // Hjälpmetod för att få väderstrecket som text
    public String getDirectionName() {
        switch (position) {
            case 'n': return "norr";
            case 's': return "söder";
            case 'ö': return "öster";
            case 'v': return "väster";
            default: return "okänt";
        }
    }
}
```

**Designval:**
- `isLocked()` istället för `getLocked()` - detta är Java-konvention för booleans
- `getDirectionName()` är en hjälpmetod som gör utskrifter enklare senare

### Steg 3: Room.java (Centrala klassen)

**Syfte:** Representera ett rum med beskrivning och dörrar.

**Tankeprocess:**
- Behöver rumsbeskrivning: `String roomDesc`
- Behöver lista av dörrar: `ArrayList<Door>` (dynamisk, kan växa)
- Behöver `doNarrative()` som visar ruminfo (enligt klassdiagram)
- Behöver metod för att lägga till dörrar
- Behöver metod för att hitta en specifik dörr baserat på väderstreck

**Kod - Del 1 (Attribut och constructor):**

```java
package ltu.fksyg.d0019n;

import java.util.ArrayList;

public class Room {
    private String roomDesc;
    private ArrayList<Door> doors;  // Dynamisk lista av dörrar

    public Room(String roomDesc) {
        this.roomDesc = roomDesc;
        this.doors = new ArrayList<>();  // Skapa tom lista
    }

    // Getter och setter
    public String getRoomDesc() {
        return roomDesc;
    }

    public void setRoomDesc(String roomDesc) {
        this.roomDesc = roomDesc;
    }
}
```

**Kod - Del 2 (Dörr-hantering):**

```java
    // Lägg till en dörr till rummet
    public void addDoor(Door door) {
        doors.add(door);
    }

    // Hitta en dörr baserat på väderstreck
    public Door getDoor(char direction) {
        for (Door door : doors) {
            if (door.getPosition() == direction) {
                return door;
            }
        }
        return null;  // Ingen dörr i den riktningen
    }

    public ArrayList<Door> getDoors() {
        return doors;
    }
```

**Kod - Del 3 (doNarrative - Den viktigaste metoden):**

```java
    // Visa rumsbeskrivning och tillgängliga dörrar
    public void doNarrative() {
        System.out.println(roomDesc);

        if (doors.isEmpty()) {
            System.out.println("Det finns inga dörrar i detta rum.");
        } else {
            System.out.print("Du kan gå ");
            ArrayList<String> directions = new ArrayList<>();

            // Samla alla olåsta dörrar
            for (Door door : doors) {
                if (!door.isLocked()) {
                    directions.add(door.getDirectionName() + " [" + door.getPosition() + "]");
                }
            }

            if (directions.isEmpty()) {
                System.out.println("ingenstans. Alla dörrar är låsta!");
            } else {
                // Skriv ut riktningar med kommatecken och "eller"
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
```

**Designval i doNarrative():**
- Separerar rumsbeskrivning från dörrinformation
- Filtrerar bort låsta dörrar från visningen
- Formaterar output snyggt med kommatecken: "Du kan gå norr [n], söder [s] eller öster [ö]"
- Hanterar kantfall: inga dörrar, alla dörrar låsta, en dörr, flera dörrar

### Steg 4: Dungeon.java (Spellogiken)

**Syfte:** Hålla ihop hela spelet - rum, spelare och spelloop.

**Tankeprocess:**
- Behöver lista av alla rum: `ArrayList<Room>`
- Behöver hålla koll på nuvarande rum: `Room currentRoom`
- Behöver välkomstmeddelande: `String welcomeMessage`
- Behöver referens till spelaren: `Player player`
- Behöver Scanner för input: `Scanner scanner`
- Huvudmetod `playGame()` med spelloop

**Kod - Del 1 (Struktur och setup):**

```java
package ltu.fksyg.d0019n;

import java.util.ArrayList;
import java.util.Scanner;

public class Dungeon {
    private String welcomeMessage;
    private Room currentRoom;
    private ArrayList<Room> rooms;
    private Player player;
    private Scanner scanner;

    public Dungeon(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
        this.rooms = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    // Getters och setters för alla attribut...
    // (utelämnat för korthet, se faktisk kod)
}
```

**Kod - Del 2 (Spellogiken - Den viktigaste delen):**

```java
    public void playGame() {
        boolean playing = true;

        while (playing) {
            // 1. Visa rummet
            System.out.println();
            currentRoom.doNarrative();
            System.out.println();

            // 2. Be om input
            System.out.print("Vad vill du göra? ");
            String input = scanner.nextLine().trim().toLowerCase();

            // 3. Hantera input
            if (input.equals("ö")) {
                // Avsluta spelet
                System.out.println("\nTack för att du spelade Dragon Treasure, "
                                   + player.getName() + "!");
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
                System.out.println("\nOgiltigt kommando! Använd väderstreck (n, s, ö, v)");
            }
        }

        scanner.close();
    }
```

**Flödesschema för playGame():**

```
START
  │
  ├─→ Visa rumsbeskrivning (currentRoom.doNarrative())
  │
  ├─→ Be om input från spelare
  │
  ├─→ Är input "ö"?
  │     JA → Avsluta spelet → SLUT
  │     NEJ → Fortsätt
  │
  ├─→ Är input ett enda tecken?
  │     NEJ → Visa felmeddelande → Tillbaka till början
  │     JA → Fortsätt
  │
  ├─→ Finns det en dörr i den riktningen?
  │     NEJ → Visa "Kan inte gå dit" → Tillbaka till början
  │     JA → Fortsätt
  │
  ├─→ Är dörren låst?
  │     JA → Visa "Dörren är låst" → Tillbaka till början
  │     NEJ → Byt currentRoom → Tillbaka till början
  │
  └─→ Upprepa loop
```

**Designval:**
- `.trim()` tar bort mellanslag före/efter input
- `.toLowerCase()` gör input case-insensitive
- Loop fortsätter tills `playing = false`
- Separation mellan att hitta dörr och kontrollera om den är låst

### Steg 5: DragonTreasure.java (Huvudklassen)

**Syfte:** Initiera och starta spelet.

**Tankeprocess:**
- `main()` är ingångspunkten
- `setupGame()` skapar alla rum och kopplar ihop dem
- `playGame()` startar Dungeon's spelloop
- Här skapas hela spelvärlden

**Kod - Del 1 (Huvudstruktur):**

```java
package ltu.fksyg.d0019n;

import java.util.Scanner;

public class DragonTreasure {
    private static Dungeon dungeon;
    private static Player player;

    public static void main(String[] args) {
        setupGame();
        playGame();
    }

    private static void playGame() {
        dungeon.showWelcome();
        dungeon.playGame();
    }
}
```

**Kod - Del 2 (setupGame - Kritisk del):**

```java
    private static void setupGame() {
        Scanner scanner = new Scanner(System.in);

        // 1. Visa ASCII-art och få spelarnamn
        System.out.println(/* ASCII-art drake */);
        System.out.println("\nVälkommen till Dragon Treasure!");
        System.out.print("Skriv ditt namn och tryck på [Enter]... ");
        String playerName = scanner.nextLine();

        // 2. Skapa spelaren
        player = new Player(playerName);

        // 3. Skapa dungeon
        String welcomeMsg = "Välkommen " + playerName + " till din skattjakt.\n" +
                           "Du står utanför en grotta...";
        dungeon = new Dungeon(welcomeMsg);
        dungeon.setPlayer(player);

        // 4. Skapa alla rum
        Room startRoom = new Room("Du står utanför en grotta...");
        Room entranceRoom = new Room("När du går in i grottan...");
        Room corridorRoom = new Room("Du är i en korridor...");
        // ... fler rum

        // 5. Koppla ihop rum med dörrar
        startRoom.addDoor(new Door('ö', false, entranceRoom));
        entranceRoom.addDoor(new Door('v', false, corridorRoom));
        // ... fler kopplingar

        // 6. Lägg till rum i dungeon
        dungeon.addRoom(startRoom);
        dungeon.addRoom(entranceRoom);
        // ... fler rum

        // 7. Sätt startrummet
        dungeon.setCurrentRoom(startRoom);
    }
```

**Viktiga steg i setupGame():**

1. **Visuell presentation** - ASCII-art skapar atmosfär
2. **Personalisering** - Få spelarens namn
3. **Objektskapande** - Skapa alla rum först
4. **Koppling** - Lägg till dörrar som kopplar rum
5. **Registrering** - Lägg till rum i dungeon
6. **Initiering** - Sätt startrummet

**Exempel på rumkoppling:**

```
     [Grotta utsida]
            │
            │ ö (öster)
            ↓
     [Ingång]
            │
            │ v (väster)
            ↓
     [Korridor]
       ↙     ↘
   n /         \ s
    ↙           ↘
[Norr rum]  [Söder rum]
```

Koden för detta:

```java
Room outside = new Room("Grotta utsida");
Room entrance = new Room("Ingång");
Room corridor = new Room("Korridor");
Room north = new Room("Norr rum");
Room south = new Room("Söder rum");

// Koppla ihop
outside.addDoor(new Door('ö', false, entrance));
entrance.addDoor(new Door('v', false, corridor));
corridor.addDoor(new Door('n', false, north));
corridor.addDoor(new Door('s', false, south));
north.addDoor(new Door('s', false, corridor));
south.addDoor(new Door('n', false, corridor));
```

**Observera:**
- Dörrar är enkelriktade! Om du kan gå norr från A till B, behöver du en separat dörr för att gå söder från B till A.
- Detta ger flexibilitet: Du kan ha en hemlig passage som bara fungerar åt ett håll!

---

## 4. Testning och verifiering

### 4.1 Kompilering

**Första steget - Säkerställ att koden kompilerar:**

```bash
cd "C:\Users\johnn\LTU\Java övningar\Dragon AI"
javac DragonTreasure.java Player.java Door.java Room.java Dungeon.java
```

**Vanliga kompileringsfel och lösningar:**

| Fel | Orsak | Lösning |
|-----|-------|---------|
| `cannot find symbol` | Felstavat variabel/metodnamn | Kontrollera stavning |
| `class X is public, should be declared in X.java` | Filnamn matchar inte klassnamn | Döp om fil |
| `';' expected` | Glömt semikolon | Lägg till ; på rätt rad |
| `package ltu.fksyg.d0019n does not exist` | Fel package-deklaration | Kontrollera package-namnet |

### 4.2 Manuell testning

**Testplan:**

1. **Test 1: Grundläggande navigering**
   - Starta spelet
   - Ange namn
   - Försök gå åt olika håll
   - Förväntat: Kan navigera mellan rum

2. **Test 2: Felhantering**
   - Testa ogiltiga kommandon (t.ex. "abc", "123")
   - Testa att gå åt håll där det inte finns dörr
   - Förväntat: Tydliga felmeddelanden

3. **Test 3: Avsluta spelet**
   - Skriv "ö" när spelet frågar
   - Förväntat: Spelet avslutas snyggt

4. **Test 4: Hela genomspelningen**
   - Spela från start till slut
   - Förväntat: ASCII-art visas, navigering fungerar, skatten hittas

### 4.3 Code Review Checklist

Innan inlämning, kontrollera:

- [ ] Alla klasser har package-deklaration `package ltu.fksyg.d0019n;`
- [ ] Alla instansvariabler är privata
- [ ] Alla metoder har JavaDoc-kommentarer
- [ ] Variabelnamn är beskrivande (inte `x`, `temp`, `data`)
- [ ] Ingen hårdkodad logik i loopar (använd dörrar för navigation)
- [ ] Scanner stängs i `playGame()` efter loop
- [ ] Ingen duplicerad kod
- [ ] Kod är välindenterad (4 mellanslag eller tab)

---

## 5. Reflektion och lärdomar

### 5.1 Vad fungerade bra?

**1. Bottom-up implementation**
Att börja med de enklaste klasserna (Player, Door) och bygga uppåt gjorde det enklare att förstå hur delarna passar ihop.

**2. Separation of Concerns**
Varje klass har ett tydligt ansvar:
- Player: Håller spelarinformation
- Door: Kopplar rum
- Room: Hanterar rumsinformation
- Dungeon: Spellogik
- DragonTreasure: Initiering

**3. Flexibel arkitektur**
Genom att använda dörrar med rumreferenser blev det lätt att ändra dungeonens struktur utan att ändra annan kod.

### 5.2 Utmaningar och lösningar

**Utmaning 1: Cirkulära beroenden**
Problem: Room behöver Door, Door behöver Room.
Lösning: Java tillåter forward-referenser inom samma package.

**Utmaning 2: Formatering av dörrlistor**
Problem: Hur skriva "Du kan gå norr [n], söder [s] eller öster [ö]" snyggt?
Lösning: Samla directions i ArrayList först, loopa sen med index för att veta när man ska skriva "eller".

**Utmaning 3: Varifrån ska Scanner komma?**
Problem: Behöver Scanner i både setupGame() och playGame().
Lösning: Skapa en Scanner i setupGame() för spelarnamn, en annan i Dungeon för spelloop.

### 5.3 Vad kunde förbättras?

**1. Externalisera rumdata**
Nuvarande lösning: Alla rum hårdkodas i setupGame().
Bättre: Läs rum och dörrar från en fil (JSON/XML).

```json
{
  "rooms": [
    {
      "id": "start",
      "description": "Du står utanför...",
      "doors": [
        {"direction": "ö", "leadsTo": "entrance", "locked": false}
      ]
    }
  ]
}
```

**2. Felhantering**
Nuvarande: Antar att input är korrekt.
Bättre: Try-catch för Scanner exceptions.

**3. Enhetstester**
Nuvarande: Manuell testning.
Bättre: JUnit-tester för varje klass.

```java
@Test
public void testPlayerCreation() {
    Player player = new Player("Alice");
    assertEquals("Alice", player.getName());
}
```

### 5.4 Objektorienterade principer som tillämpades

**Encapsulation (Inkapsling)**
```java
private String name;  // Privat variabel
public String getName() { return name; }  // Publik åtkomst
```

**Composition (Sammansättning)**
```java
class Room {
    private ArrayList<Door> doors;  // Room "har" Doors
}
```

**Single Responsibility Principle**
- Player: Ansvarar ENDAST för spelarinformation
- Room: Ansvarar ENDAST för rumsinformation
- Dungeon: Ansvarar ENDAST för spellogik

**Don't Repeat Yourself (DRY)**
```java
// Bra: En metod för att hitta dörr
public Door getDoor(char direction) { ... }

// Dåligt: Upprepa samma logik överallt
```

### 5.5 Viktigaste lärdomen

**Planering är 50% av arbetet!**

Genom att:
1. Läsa uppgiften noggrant
2. Analysera klassdiagrammet
3. Planera implementationsordning
4. Tänka igenom designval INNAN kodning

...sparade jag tid och undvek många omskrivningar.

**Som student, gör detta innan du kodar:**
1. Rita klassdiagrammet på papper
2. Skriv pseudokod för de svåra delarna
3. Fråga dig: "Hur ska klasserna prata med varandra?"
4. Starta med den enklaste klassen först

---

## 6. Tips för studenten som ska replikera

### 6.1 Steg-för-steg guide

**Vecka 1: Analys och planering**
1. Läs uppgiftsbeskrivningen 3 gånger
2. Rita klassdiagrammet för hand
3. Skriv ner alla frågor du har
4. Identifiera vilka klasser som beror på varandra

**Vecka 2: Implementation**
1. Dag 1: Player.java (30 min)
2. Dag 2: Door.java (1 timme)
3. Dag 3: Room.java (2 timmar - svåraste delen)
4. Dag 4: Dungeon.java (2 timmar)
5. Dag 5: DragonTreasure.java (2 timmar)

**Vecka 3: Testning och dokumentation**
1. Dag 1-2: Testa spelet, fixa buggar
2. Dag 3: Skriv README.md
3. Dag 4: Code review, refaktorera
4. Dag 5: Skapa zip-fil, lämna in

### 6.2 Vanliga misstag att undvika

❌ **Misstag 1: Börja koda direkt**
✅ Rätt: Spendera tid på att förstå uppgiften först

❌ **Misstag 2: Göra allt i en klass**
✅ Rätt: Följ klassdiagrammet, separera ansvar

❌ **Misstag 3: Hårdkoda allting**
✅ Rätt: Använd dörrar för navigation, lägg rum i lista

❌ **Misstag 4: Ingen kommentarer**
✅ Rätt: JavaDoc för alla klasser och metoder

❌ **Misstag 5: Testa för sent**
✅ Rätt: Kompilera och testa efter varje klass

### 6.3 Användbara Java-koncept

**ArrayList vs Array**
```java
// Array - fast storlek
Room[] rooms = new Room[10];  // Måste veta storleken

// ArrayList - dynamisk storlek
ArrayList<Room> rooms = new ArrayList<>();  // Kan växa
rooms.add(newRoom);
```
👉 Använd ArrayList när du inte vet hur många element du får.

**Enhanced for-loop**
```java
// Iterera genom alla dörrar
for (Door door : doors) {
    System.out.println(door.getPosition());
}
```
👉 Enklare än `for (int i = 0; i < doors.size(); i++)`

**Switch-statement**
```java
switch (position) {
    case 'n':
        return "norr";
    case 's':
        return "söder";
    default:
        return "okänt";
}
```
👉 Bra för flera if-else baserat på samma variabel.

**String metoder**
```java
String input = "  NORR  ";
input = input.trim();         // "NORR"
input = input.toLowerCase();  // "norr"
char first = input.charAt(0); // 'n'
```

### 6.4 Felsökning tips

**Problem: NullPointerException**
```java
Door door = currentRoom.getDoor('n');
Room nextRoom = door.getLeadsTo();  // NPE om door är null!
```
Lösning: Kontrollera först!
```java
if (door != null) {
    Room nextRoom = door.getLeadsTo();
}
```

**Problem: Scanner fungerar inte**
```java
Scanner scanner = new Scanner(System.in);
scanner.close();
scanner.nextLine();  // Fel! Scanner är stängd
```
Lösning: Stäng Scanner först när programmet är klart.

**Problem: Rum kopplar inte ihop**
```java
room1.addDoor(new Door('n', false, room2));
// Glömde: room2 -> room1 koppling!
```
Lösning: Kom ihåg att dörrar är enkelriktade.

---

## 7. Sammanfattning

### Nyckelpunkter att komma ihåg:

1. **Planera före kodning** - Spara tid genom att tänka igenom designen
2. **En klass i taget** - Börja med enklaste, bygga uppåt
3. **Följ OOP-principer** - Privata variabler, tydliga ansvar
4. **Testa kontinuerligt** - Kompilera efter varje klass
5. **Dokumentera allt** - Din framtida jag tackar dig

### Checklista för inlämning:

- [ ] Alla 5 Java-filer fungerar och kompilerar
- [ ] README.md med antaganden finns
- [ ] All kod är kommenterad
- [ ] Spelet går att spela från start till slut
- [ ] Zip-fil skapad med rätt innehåll
- [ ] Deadline: Kolla Canvas!

---

## 8. Resurser för vidare studier

**Officiell Java-dokumentation:**
- Scanner: https://docs.oracle.com/javase/8/docs/api/java/util/Scanner.html
- ArrayList: https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html

**Rekommenderade böcker:**
- "Clean Code" av Robert C. Martin - Kod-kvalitet
- "Head First Design Patterns" - OOP-mönster

**Öva mer:**
- Lägg till fler rum
- Implementera låsta dörrar med nyckelsystem
- Lägg till monster (Del 2 av uppgiften)
- Skapa ett inventory-system

---

**Lycka till med din implementation!** 🚀

Om du följer denna guide steg för steg kommer du att:
- Förstå varför varje designval gjordes
- Kunna förklara din kod för andra
- Ha en solid grund för Del 2 av uppgiften

**Kom ihåg:** Programmering är som att lösa ett pussel. Ta det lugnt, en bit i taget! 🧩
