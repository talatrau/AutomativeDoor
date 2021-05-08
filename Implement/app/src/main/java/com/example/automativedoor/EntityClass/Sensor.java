package com.example.automativedoor.EntityClass;

public class Sensor extends Component {
    private boolean state;

    public Sensor(String Id, String name, boolean state) {
        super(Id, name);
        this.state = state;
    }

    public boolean toggle(boolean signal) {
        if (this.state == signal) {
            return false;
        } else {
            this.state = signal;
            this.saveHistory();
            return true;
        }
    }

    public boolean getState() {
        return this.state;
    }

    @Override
    public void saveHistory() {

    }
}
