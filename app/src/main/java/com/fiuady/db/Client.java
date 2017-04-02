package com.fiuady.db;


public class Client {

    private int id;
    private String firstName;
    private String lastName;
    private String address;
    private String phone1;
    private String phone2;
    private String phone3;
    private String email;

    public Client(int id, String firstName, String lastName, String address,
                  String email, String phone1, String phone2, String phone3) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.phone1 = phone1;
        this.phone2 = phone2;
        this.phone3 = phone3;
    }

    public int getId() {return id;}

    public String getFirstName() {return firstName;}

    public String getLastName() {return lastName;}

    public String getAddress() {return address;}

    public String getEmail() {return email;}

    public String getPhone1() {return phone1;}

    public String getPhone2() {return phone2;}

    public String getPhone3() {return phone3;}


}
