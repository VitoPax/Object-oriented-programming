package it.polito.extgol;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class CellSnapshot {
    @Column(name = "is_alive", nullable = false)
    private Boolean isAlive;

    @Column(name = "lifepoints", nullable = false)
    private Integer lifepoints;

    @Column(name = "cell_type", nullable = false)
    private CellType cellType;

    @Column(name = "cell_mood", nullable = false)
    private CellMood cellMood;
    
    public CellSnapshot() {}

    public CellSnapshot(Boolean isAlive, Integer lifepoints, CellType cellType, CellMood cellMood) {
        this.isAlive = isAlive;
        this.lifepoints = lifepoints;
        this.cellType = cellType;
        this.cellMood = cellMood;
    }

    public Boolean isAlive() {
        return this.isAlive;
    }

    public void isAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public Integer lifepoints() {
        return this.lifepoints;
    }

    public void lifepoints(int lifepoints) {
        this.lifepoints = lifepoints;
    }

    public CellType cellType() {
        return this.cellType;
    }

    public void cellType(CellType cellType) {
        this.cellType = cellType;
    }

    public CellMood cellMood() {
        return this.cellMood;
    }

    public void cellMood(CellMood cellMood) {
        this.cellMood = cellMood;
    }
}
