package Entities.Herbivores;

import Constants.TypesOfEntities;
import Entities.Entities;

public class Caterpillar extends Entities {

    public Caterpillar() {
        super(TypesOfEntities.CATERPILLAR.getEntitySymbol(),
        0.01, 0.005, 0.005, 0,0, 1000);
    }

    @Override
    public Entities reproduce () {
        return new Caterpillar();
    }

}
