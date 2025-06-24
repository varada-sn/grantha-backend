package com.rvce.Grantha.book_rental_service.model;

import jakarta.persistence.*;


@Entity
@Table(name = "Customer_address")
public class Customer_address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long address_id;
    private String cust_address;

    // One-to-One relationship with Customer_details (this will have the foreign key)
    @OneToOne
    @JoinColumn(name = "cust_id", referencedColumnName = "id") // cust_id will be the foreign key in Customer_address
    private Customer_details customerDetails;


    public Customer_address()
    {
    }

    public String getCust_address() {
        return cust_address;
    }

    public void setCust_address(String cust_address) {
        this.cust_address = cust_address;
    }

    public Long getAddress_id() {
        return address_id;
    }

    public void setAddress_id(Long address_id) {
        this.address_id = address_id;
    }
}