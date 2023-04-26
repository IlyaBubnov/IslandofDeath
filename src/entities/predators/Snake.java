package entities.predators;

import constants.TypesOfEntities;
import entities.Entities;

public class Snake extends Entities {

    public Snake() {
        super(TypesOfEntities.SNAKE.getEntitySymbol(),
        15, 7.5, 7.5, 3, 1, 30);
    }

    @Override
    public Entities reproduce () {
        return new Snake();
    }

}
