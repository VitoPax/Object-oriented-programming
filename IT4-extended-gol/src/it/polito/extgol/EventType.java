package it.polito.extgol;

import java.util.List;

/**
 * Enumeration of global event types that can be applied to the entire board
 * during a generation, altering cells’ lifePoints and moods.
 */
public enum EventType {

    /** 
     * Sets every cell’s lifePoints to zero, simulating a catastrophic reset.
     */
    CATACLYSM {
        @Override
        public void apply(List<Tile> tiles) {
            tiles.stream().map(Tile::getCell).forEach(c -> c.setLifePoints(0));
        }
    },

    /** 
     * Absorbs 1 lifePoint from each cell, representing a period of scarcity.
     */
    FAMINE {
        @Override
        public void apply(List<Tile> tiles) {
            tiles.stream().map(Tile::getCell).forEach(c -> c.addLifePoints(-1));
        }
    },

    /** 
     * Grants 2 lifePoints to each cell, causing a burst of growth.
     */
    BLOOM {
        @Override
        public void apply(List<Tile> tiles) {
            tiles.stream().map(Tile::getCell).filter(Cell::isAlive).forEach(c -> c.addLifePoints(2));
        }
    },

    /** 
     * Each Vampire cell steals 1 lifePoint from each adjacent Naive or Healer 
     * and converts them into Vampires.
     */
    BLOOD_MOON {
        @Override
        public void apply(List<Tile> tiles) {
            tiles.stream().map(Tile::getCell).forEach(c -> {
                if (c.getMood() == CellMood.VAMPIRE) {
                    c.canBiteHealers(true);
                }
            });
        }
    },

    /** 
     * All Healer cells gain 1 lifePoint, and all Vampire cells revert to Naive.
     */
    SANCTUARY {
        @Override
        public void apply(List<Tile> tiles) {
            tiles.stream().map(Tile::getCell).forEach(c -> {
                if (c.getMood() == CellMood.HEALER) {
                    c.addLifePoints(1);
                } else if (c.getMood() == CellMood.VAMPIRE) {
                    c.setMood(CellMood.NAIVE);
                }
            });
        }
    };

    public abstract void apply(List<Tile> tiles);
}