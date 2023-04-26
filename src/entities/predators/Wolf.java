package entities.predators;

import constants.TypesOfEntities;
import entities.Entities;

public class Wolf extends Entities {

    public Wolf() {
        super(TypesOfEntities.WOLF.getEntitySymbol(),
        50, 25, 25, 8, 3, 30);
    }

    @Override
    public Entities reproduce () {
        return new Wolf();
    }

}
