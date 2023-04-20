package Entities.Herbivores;

import Constants.TypesOfEntities;
import Entities.Entities;

public class Buffalo extends Entities {

    public Buffalo() {
        super(TypesOfEntities.BUFFALO.getEntitySymbol(),
        700, 350, 350, 100, 3, 10);
    }

    @Override
    public Entities reproduce () {
        return new Buffalo();
    }

}
