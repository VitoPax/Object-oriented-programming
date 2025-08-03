package it.polito.extgol;

/**
 * Defines the interaction style or “mood” of a cell, influencing how it
 * exchanges lifePoints with other cells during the interaction phase.
 */
public enum CellMood {

    /**
     * Default state with no special interaction effects.
     * Does not grant or drain lifePoints when interacting.
     */
    NAIVE {
        @Override
        public void interactWith(Cell me, Cell other) {}
    },

    /**
     * Drains lifePoints from other cells upon interaction.
     */
    VAMPIRE {
        @Override
        public void interactWith(Cell me, Cell other) {
            if (other.getMood() == VAMPIRE || other.getLifePoints() < 0) return;
            other.addLifePoints(-1);
            me.addLifePoints(1);

            if (other.getMood() == NAIVE || me.canBiteHealers()) {
                other.setNextMood(VAMPIRE);
            }
        }
    },

    /**
     * Grants lifePoints to other cells upon interaction.
     */
    HEALER {
        @Override
        public void interactWith(Cell me, Cell other) {
            if (other.getMood() != NAIVE) return;
            other.addLifePoints(1);
        }
    };

    public abstract void interactWith(Cell me, Cell other);
}