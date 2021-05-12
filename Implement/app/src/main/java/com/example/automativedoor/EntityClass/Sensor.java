package com.example.automativedoor.EntityClass;

public class Sensor extends Component {

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

    }
}
