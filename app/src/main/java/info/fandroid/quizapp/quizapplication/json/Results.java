package info.fandroid.quizapp.quizapplication.json;

import java.io.Serializable;

public class Results implements Serializable {

    public String AType;
    private int id;
    private String type;
    private String description;
    private long id_attempt;
    private String data_attempt;

    public Results() {
    }
    public Results(long id_attempt, String data_attempt, int id, String type, String description, String AType) {
        this.id_attempt = id_attempt;
        this.data_attempt = data_attempt;
        this.id = id;
        this.type = type;
        this.description = description;
        this.AType = AType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId_attempt() {
        return id_attempt;
    }

    public void setId_attempt(long id_attempt) {
        this.id_attempt = id_attempt;
    }

    public String getData_attempt() { return data_attempt; }

    public void setData_attempt(String data_attempt) {
        this.data_attempt = data_attempt;
    }

    public String getAType() {
        return AType;
    }

    public void setAType(String AType) {
        this.AType = AType;
    }
}
