package com.example.automativedoor.EntityClass;

public class Servo extends Component{
    private boolean State;  // 0 means close - 1 means open

    public Servo(String devideID, String name, boolean state) {
        super(devideID, name);
        this.State = state;
    }

    public boolean Toggle(boolean signal) {
        try {
            this.State = !State;
            this.saveHistory();
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean GetState() {
        return this.State;
    }

    @Override
    public void saveHistory() {
        if (this.State) {

        } else {

        }
    }
}
