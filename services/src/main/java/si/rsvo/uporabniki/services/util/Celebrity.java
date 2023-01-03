package si.rsvo.uporabniki.services.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Celebrity {
    @JsonProperty("name")
    private String name;

    @JsonProperty("net_worth")
    private long netWorth;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("nationality")
    private String nationality;

    @JsonProperty("occupation")
    private String[] occupation;

    @JsonProperty("height")
    private float height;

    @JsonProperty("birthday")
    private String birthday;

    @JsonProperty("death")
    private String death;

    @JsonProperty("age")
    private int age;

    @JsonProperty("is_alive")
    private boolean isAlive;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
