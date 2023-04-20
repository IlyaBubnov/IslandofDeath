package Constants;

//Варианты действий для юнитов. Каждому действию задаю вероятность.
public enum Action {
    EAT (90),
    MOVE (70),
    REPRODUCE (50),
    DO_NOTHING (30);

    //Пер-ая и конструктор для создания и хранения вероятности действия юнита.
    final int actionChanceInPercent;

    Action(int actionChanceInPercent) {
        this.actionChanceInPercent = actionChanceInPercent;
    }

    //Метод (геттер) возвращает значение вероятности действия.
    public int getActionChanceInPercent() {
        return this.actionChanceInPercent;
    }
}
