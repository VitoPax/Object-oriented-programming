package it.polito.extgol;

import java.util.List;

public class CellRepository extends GenericExtGOLRepository<Cell, Long> {
    public CellRepository() {
        super(Cell.class);
    }

    public List<Cell> load() {
        return findAll();
    }
}
