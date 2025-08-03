package it.polito.extgol;

/**
 * Defines the types of cells, each with distinct behaviors in the extended Game of Life.
 */
public enum CellType {
    
    /**
     * Standard Conway cell: follows default Game of Life rules.
     */
    BASIC('C', 2, 3, 0),
    
    /**
     * High-energy cell: it can withstand death-inducing conditions for three generations.
     */
    HIGHLANDER('H', 2, 3, 3),
    
    /**
     * Isolationist cell: survives with as few as one neighbor.
     */
    LONER('L', 1, 3, 0),
    
    /**
     * Crowd-loving cell: can survive even in highly populated situations,
     * up to eight neighbors.
     */
    SOCIAL('S', 2, 8, 0);

    public static final CellType[] VALUES = values();

    public final char symbol;
    public final int underpopulationBound;
    public final int overpopulationBound;
    public final int survivableDeaths;

    CellType(char symbol, int underpopulationBound, int overpopulationBound, int survivesDeath) {
        this.symbol = symbol;
        this.underpopulationBound = underpopulationBound;
        this.overpopulationBound = overpopulationBound;
        this.survivableDeaths = survivesDeath;
    }
}