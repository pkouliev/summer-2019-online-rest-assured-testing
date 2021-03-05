package com.automation.pojos.school;

/**
 * Composition - is one of the fundamental concepts of OOP
 * When class, is referenced to one or more objects of another class
 * in instance variable, it calls composition.
 * <p>
 * company object will contain address object
 * <p>
 * The main advantage of Composition is that we can reuse the code
 * without is-a (without inheritance) relationship
 */
public class Company {
    private Address address;
    private int companyId;
    private String companyName;
    private String startDate;
    private String title;

    public Company() {

    }

    public Company(Address address, String companyName, String startDate, String title) {
        this.address = address;
        this.companyName = companyName;
        this.startDate = startDate;
        this.title = title;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int getCompanyId() {
        return companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Company{" +
                "address=" + address +
                ", companyId=" + companyId +
                ", companyName='" + companyName + '\'' +
                ", startDate='" + startDate + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
