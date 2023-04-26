package entities.herbivores;

import constants.TypesOfEntities;
import entities.Entities;

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
