/**
 * Representerar en spelare i Dragon Treasure spelet.
 * Spelaren har ett namn och navigerar genom dungeon.
 */
public class Player {
    private String name;

    /**
     * Konstruktor för att skapa en ny spelare.
     * @param name Spelarens namn
     */
    public Player(String name) {
        this.name = name;
    }

    /**
     * Hämtar spelarens namn.
     * @return Spelarens namn
     */
    public String getName() {
        return name;
    }

    /**
     * Sätter spelarens namn.
     * @param name Det nya namnet för spelaren
     */
    public void setName(String name) {
        this.name = name;
    }
}
