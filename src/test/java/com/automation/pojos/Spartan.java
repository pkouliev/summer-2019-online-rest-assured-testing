package com.automation.pojos;

public class Spartan {
    /*
    {
        "id": 3,
        "name": "Fidole",
        "gender": "Male",
        "phone": 6105035233
    }
     */
    private int id;
    private String name;
    private String gender;
    private long phone;

    public Spartan() {

    }

    public Spartan(String name, String gender, long phone) {
        this.name = name;
        this.gender = gender;
        this.phone = phone;
    }

    public Spartan(int id, String name, String gender, long phone) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Spartan{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", phone=" + phone +
                '}';
    }

    public Spartan withId(int id) {
        this.id = id;
        return this;
    }

    public Spartan withName(String name) {
        this.name = name;
        return this;
    }

    public Spartan withGender(String gender) {
        this.gender = gender;
        return this;
    }

    public Spartan withPhone(long phone) {
        this.phone = phone;
        return this;
    }


}
