package Entities.Herbivores;

import Constants.TypesOfEntities;
import Entities.Entities;

public class Duck extends Entities {

    public Duck() {
        super(TypesOfEntities.DUCK.getEntitySymbol(),
        1, 0.5, 0.5, 0.15,4, 200);
    }

    @Override
    public Entities reproduce () {
        return new Duck();
    }

}
