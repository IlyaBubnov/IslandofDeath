package Entities.Herbivores;

import Constants.TypesOfEntities;
import Entities.Entities;

public class Goat extends Entities {

    public Goat() {
        super(TypesOfEntities.GOAT.getEntitySymbol(),
        60, 30, 30, 10, 3, 140);
    }

    @Override
    public Entities reproduce () {
        return new Goat();
    }

}
