package ltu.fksyg.d0019n;

/**
 * Representerar en dörr mellan två rum i dungeon.
 * Varje dörr har en position (väderstreck) och kan vara låst eller olåst.
 */
public class Door {
    private char position;      // Väderstreck: 'n', 's', 'ö', 'v'
    private boolean locked;     // Om dörren är låst
    private Room leadsTo;       // Rummet som dörren leder till

    /**
     * Konstruktor för att skapa en ny dörr.
     * @param position Väderstrecket dörren är placerad i ('n', 's', 'ö', 'v')
     * @param locked Om dörren är låst eller inte
     * @param leadsTo Rummet som dörren leder till
     */
    public Door(char position, boolean locked, Room leadsTo) {
        this.position = position;
        this.locked = locked;
        this.leadsTo = leadsTo;
    }

    /**
     * Hämtar dörens position (väderstreck).
     * @return Väderstrecket som ett tecken
     */
    public char getPosition() {
        return position;
    }

    /**
     * Sätter dörrens position.
     * @param position Det nya väderstrecket
     */
    public void setPosition(char position) {
        this.position = position;
    }

    /**
     * Kontrollerar om dörren är låst.
     * @return true om dörren är låst, annars false
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Sätter dörrens låsstatus.
     * @param locked true för att låsa dörren, false för att låsa upp den
     */
    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    /**
     * Hämtar rummet som dörren leder till.
     * @return Rummet som dörren leder till
     */
    public Room getLeadsTo() {
        return leadsTo;
    }

    /**
     * Sätter vilket rum dörren leder till.
     * @param leadsTo Det nya rummet
     */
    public void setLeadsTo(Room leadsTo) {
        this.leadsTo = leadsTo;
    }

    /**
     * Returnerar en sträng med dörrens väderstreck i klartext.
     * @return Dörrens väderstreck som text
     */
    public String getDirectionName() {
        switch (position) {
            case 'n':
                return "norr";
            case 's':
                return "söder";
            case 'ö':
                return "öster";
            case 'v':
                return "väster";
            default:
                return "okänt";
        }
    }
}
