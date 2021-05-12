package ru.levkharitonov.spbstu.oop.model;

public class Cargo {
    private CargoType type;
    private int quantity;

    public Cargo() {
        this.type = null;
        this.quantity = 0;
    }

    public Cargo(CargoType type, int quantity) {
        this.type = type;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Cargo{" +
                "type=" + type +
                ", quantity=" + quantity +
                '}';
    }

    public CargoType getType() {
        return type;
    }

    public void setType(CargoType type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
