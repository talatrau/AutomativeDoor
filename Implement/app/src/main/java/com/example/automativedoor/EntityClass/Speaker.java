package com.example.automativedoor.EntityClass;

public class Speaker extends Component {
    private int volume;  // 0 means close - 1 means open

    public Speaker(String devideID, String name, int volume) {
        super(devideID, name, true);
        this.volume = volume;
    }

    public int getVolume() {
        return this.volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public void alarm(int second) {
        this.saveHistory();
    }

    @Override
    public void saveHistory() {

    }
}
