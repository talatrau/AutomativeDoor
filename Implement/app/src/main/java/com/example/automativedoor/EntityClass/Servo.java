package com.example.automativedoor.EntityClass;

public class Servo extends Component {

    public Servo(String devideID, String name, boolean state) {
        super(devideID, name, state);
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

    @Override
    public void saveHistory() {
        if (this.state) {

        } else {

        }
    }
}
