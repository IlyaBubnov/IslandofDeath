package entities.herbivores;

import constants.TypesOfEntities;
import entities.Entities;

public class Deer extends Entities {

    public Deer ()  {
        super(TypesOfEntities.DEER.getEntitySymbol(),
        300, 150, 150, 50,4, 20);
    }

    @Override
    public Entities reproduce () {
        return new Deer();
    }

}
