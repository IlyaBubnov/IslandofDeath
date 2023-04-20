package Entities.Herbivores;

import Constants.TypesOfEntities;
import Entities.Entities;

public class Mouse extends Entities {

    public Mouse() {
        super(TypesOfEntities.MOUSE.getEntitySymbol(),
        0.05, 0.025, 0.025, 0.01, 1, 500);
    }

    @Override
    public Entities reproduce () {
        return new Mouse();
    }

}
