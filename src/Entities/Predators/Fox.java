package Entities.Predators;

import Constants.TypesOfEntities;
import Entities.Entities;

public class Fox extends Entities {

    public Fox() {
        super(TypesOfEntities.FOX.getEntitySymbol(),
        8, 4, 4, 2, 2, 30);
    }

    @Override
    public Entities reproduce () {
        return new Fox();
    }

}
