package com.example.automativedoor.EntityClass;

public class Servo extends Component {
    private boolean state;  // 0 means close - 1 means open

    public Servo(String devideID, String name, boolean state) {
        super(devideID, name);
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
        if (this.state) {

        } else {

        }
    }
}
