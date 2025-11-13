# Dragon Treasure - Del 1

Ett textbaserat äventyrsspel där spelaren navigerar genom en dungeon med hjälp av vädersträck.

## Projektbeskrivning

Dragon Treasure är ett äventyrsspel där spelaren utforskar olika rum i en dungeon. Spelet fokuserar på navigering mellan rum och interaktion med omgivningen genom textbaserade kommandon.

## Klassstruktur

Projektet består av följande klasser:

### Player.java
- Representerar spelaren
- Innehåller spelarens namn
- Använder privata instansvariabler med getters och setters

### Door.java
- Representerar en dörr mellan två rum
- Har position (väderstreck: n, s, ö, v)
- Kan vara låst eller olåst
- Innehåller referens till rummet dörren leder till

### Room.java
- Representerar ett rum i dungeon
- Innehåller rumsbeskrivning och en lista av dörrar
- Metoden `doNarrative()` skriver ut rumsbeskrivning och tillgängliga riktningar

### Dungeon.java
- Hanterar hela dungeon-strukturen
- Håller koll på alla rum och spelarens nuvarande position
- Innehåller huvudspellogiken i `playGame()` metoden

### DragonTreasure.java
- Huvudklass med main-metoden
- `setupGame()` skapar alla rum, dörrar och spelaren
- `playGame()` startar spellogiken

## Kompilering och Körning

### Windows (Enklast):
1. Dubbelklicka på **compile.bat** för att kompilera
2. Dubbelklicka på **run.bat** för att köra spelet

### Manuellt:
```bash
# Kompilera
javac -encoding UTF-8 *.java

# Köra
java DragonTreasure
```

## Spelmekanik

### Kommandon (flera alternativ fungerar):

**Navigation:**
- **Norr**: `n`, `norr`, eller `north`
- **Söder**: `s`, `söder`, `soder`, eller `south`
- **Öster**: `e`, `o`, `ö`, `öster`, `oster`, eller `east`
- **Väster**: `w`, `v`, `väster`, `vaster`, eller `west`

**Avsluta:**
- `exit`, `quit`, `avsluta`, eller `ö`

**OBS!** På grund av teckenuppsättningsproblem i Windows Command Prompt rekommenderas de engelska kommandona (`n`, `s`, `e`, `w`) för bäst kompatibilitet.

### Spelflöde:
1. Spelet visar en välkomstskärm med ASCII-art
2. Spelaren anger sitt namn
3. Spelaren börjar vid startpunkten utanför grottan
4. För varje rum visas:
   - Rumsbeskrivning
   - Tillgängliga riktningar med väderstreck
5. Spelaren navigerar genom att skriva väderstreck (n, s, ö, v)
6. Målet är att ta sig genom dungeon och hitta skatten

## Antaganden och Designval

### 1. Datastruktur för rum
Vi har valt att använda `ArrayList<Room>` för att lagra alla rum i dungeon. Detta ger flexibilitet att enkelt lägga till eller ta bort rum, samt underlättar framtida utökning av spelet.

### 2. Navigationssystem
Varje rum har sina egna dörrar som pekar direkt till andra rum. Detta skapar en flexibel struktur där rum kan kopplas ihop i olika konstellationer utan att behöva hårdkoda rumpositioner.

### 3. Dörrens koppling till rum
`Door`-klassen innehåller en referens till vilket `Room` den leder till. Detta förenklar navigeringen eftersom vi kan direkt hämta nästa rum från dörren.

### 4. Privata variabler
Alla instansvariabler i klasserna är privata enligt god programmeringssed. Åtkomst sker via getters och setters, vilket ger bättre inkapsling och kontroll över data.

### 5. Spellogik i Dungeon
Vi har placerat huvudspellogiken (`playGame()`) i `Dungeon`-klassen eftersom dungeon är den centrala komponenten som håller ihop spelet. Detta ger en tydlig ansvarsfördelning där `DragonTreasure` endast ansvarar för initiering.

### 6. Rumsbeskrivningar
Rumsbeskrivningarna är hårdkodade i `setupGame()`-metoden. Detta gör det enkelt att se hela spelstrukturen på ett ställe, men i en framtida version skulle dessa kunna läsas från en fil.

### 7. Input-hantering
Vi använder `Scanner` för att läsa användarinput. Input normaliseras till gemener (lowercase) för att göra kommandona case-insensitive, vilket ger bättre användarupplevelse.

### 8. Låsta dörrar
Systemet stöder låsta dörrar genom `locked`-attributet i `Door`-klassen, men i denna version av spelet är alla dörrar olåsta. Detta är en funktion som enkelt kan utökas i framtida versioner där spelaren kan hitta nycklar.

### 9. Avsluta spelet
Vi har valt att använda kommandot "ö" för att avsluta spelet när spelaren vill, vilket ger spelaren kontroll att avbryta när som helst. Spelet avslutas också automatiskt när spelaren når slutrummet.

### 10. ASCII-art
Den ursprungliga ASCII-arten för draken och skatten från den medföljande filen har integrerats i programmet - draken visas vid start och skatten visas när spelaren når slutet.

## Objektorienteringsprinciper

Projektet följer grundläggande objektorienterade principer:

- **Inkapsling**: Privata instansvariabler med getters/setters
- **Separation of Concerns**: Varje klass har ett tydligt ansvar
- **Composition**: Room innehåller Door-objekt, Dungeon innehåller Room-objekt
- **Single Responsibility**: Varje klass har ett specifikt syfte

## Kod-kvalitet

- Alla klasser och metoder är kommenterade med JavaDoc
- Variabelnamn är beskrivande och följer Java-konventioner
- Koden följer "Good programming practice" från kurslitteraturen
- Alla variabler är deklarerade så lokalt som möjligt

## Framtida utökningar (ej implementerade i Del 1)

- Monster och stridssystem
- Föremål och inventory
- Nycklar för att låsa upp dörrar
- Fler rum och mer komplex dungeonstruktur
- Spara och ladda spel
- Poängsystem

## Författare

Projektet är utvecklat som en del av kursen D0019N - Programutveckling med Java vid Luleå Tekniska Universitet.

## Version

Del 1 - Grundläggande navigeringssystem
