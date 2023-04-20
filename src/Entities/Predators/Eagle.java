package Entities.Predators;

import Constants.TypesOfEntities;
import Entities.Entities;

public class Eagle extends Entities {

    public Eagle() {
        super(TypesOfEntities.EAGLE.getEntitySymbol(),
        6, 3, 3, 1, 3, 20);
    }

    @Override
    public Entities reproduce () {
        return new Eagle();
    }

}
