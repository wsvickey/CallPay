package demo.app.adcharge.eu.sdkdemo.pojo;

import eu.adcharge.api.entities.Interest;

public class category {

    private boolean isSelected;
    private Interest animal;

    public Interest getAnimal() {
        return animal;
    }

    public void setAnimal(Interest animal) {
        this.animal = animal;
    }

    public boolean getSelected() {
        return isSelected;
    }
    public boolean getunSelected() {
        return false;
    }
    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}