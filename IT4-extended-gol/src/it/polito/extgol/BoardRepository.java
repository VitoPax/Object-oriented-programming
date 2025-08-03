package it.polito.extgol;

import java.util.List;

public class BoardRepository extends GenericExtGOLRepository<Board, Long> {
    public BoardRepository() {
        super(Board.class);
    }

    public List<Board> load() {
        return findAll();
    }
}
