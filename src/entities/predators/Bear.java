package entities.predators;

import constants.TypesOfEntities;
import entities.Entities;

public class Bear extends Entities {

    public Bear() {
        super(TypesOfEntities.BEAR.getEntitySymbol(),
        500, 250, 250, 80, 2, 5);
    }

    @Override
    public Entities reproduce () {
        return new Bear();
    }

}
