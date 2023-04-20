package Entities.Herbivores;

import Constants.TypesOfEntities;
import Entities.Entities;

public class Horse extends Entities {

    public Horse() {
        super(TypesOfEntities.HORSE.getEntitySymbol(),
        400, 200, 200, 60, 4, 20);
    }

    @Override
    public Entities reproduce () {
        return new Horse();
    }

}
