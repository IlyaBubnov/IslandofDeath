package entities.predators;

import constants.TypesOfEntities;
import entities.Entities;

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
