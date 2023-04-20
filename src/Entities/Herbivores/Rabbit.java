package Entities.Herbivores;

import Constants.TypesOfEntities;
import Entities.Entities;

public class Rabbit extends Entities {

    public Rabbit() {
        super(TypesOfEntities.RABBIT.getEntitySymbol(),
        2, 1, 1, 0.45, 2, 150);
    }

    @Override
    public Entities reproduce () {
        return new Rabbit();
    }

}
