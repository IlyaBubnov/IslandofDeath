package constants;

//Список доступных сущностей.
public enum TypesOfEntities {
    WOLF("\ud83d\udc3a"),
    SNAKE("\ud83d\udc0d"),
    FOX("\ud83e\udd8a"),
    BEAR("\ud83d\udc3b"),
    EAGLE("\ud83e\udd85"),
    HORSE("\ud83d\udc0e"),
    DEER("\ud83e\udd8c"),
    RABBIT("\ud83d\udc07"),
    MOUSE("\ud83d\udc01"),
    GOAT("\ud83d\udc10"),
    SHEEP("\ud83d\udc11"),
    BOAR("\ud83d\udc17"),
    BUFFALO("\ud83d\udc03"),
    DUCK("\ud83e\udd86"),
    CATERPILLAR("\ud83d\udc1b"),
    PLANTS ("\ud83c\udf31");

    //Пер-ая и конструктор для создания и хранения символа юнита.
    final String symbol;
    TypesOfEntities(String symbol) {
        this.symbol = symbol;
    }

    //Метод (геттер) возвращает символ юнита.
    public String getEntitySymbol() {
        return this.symbol;
    }
}
