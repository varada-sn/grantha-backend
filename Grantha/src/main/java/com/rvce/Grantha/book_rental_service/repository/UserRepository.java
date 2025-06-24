/*package com.rvce.Grantha.book_rental_service.repository;

import com.rvce.Grantha.book_rental_service.model.Customer_details;
import com.rvce.Grantha.book_rental_service.model.Supplier;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {
    @Autowired
    private EntityManager entityManager;

    // Find Customer by Email
    public Optional<Customer_details> findCustomerByEmail(String email) {
        TypedQuery<Customer_details> query = entityManager.createQuery(
                "SELECT c FROM Customer_details c WHERE c.email = :email", Customer_details.class);
        query.setParameter("email", email);
        return query.getResultStream().findFirst();
    }

    // Find Supplier by Email
    public Optional<Supplier> findSupplierByEmail(String email) {
        TypedQuery<Supplier> query = entityManager.createQuery(
                "SELECT s FROM Supplier s WHERE s.supplier_email = :email", Supplier.class);
        query.setParameter("email", email);
        return query.getResultStream().findFirst();
    }
}

 */

package com.rvce.Grantha.book_rental_service.repository;

import com.rvce.Grantha.book_rental_service.model.Customer_details;
import com.rvce.Grantha.book_rental_service.model.Supplier;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {

    @PersistenceContext
    private EntityManager entityManager;

    // Find Customer by Email
    public Optional<Customer_details> findCustomerByEmail(String email) {
        return entityManager.createQuery(
                        "SELECT c FROM Customer_details c WHERE c.email = :email", Customer_details.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }

    // Find Supplier by Email
    public Optional<Supplier> findSupplierByEmail(String email) {
        return entityManager.createQuery(
                        "SELECT s FROM Supplier s WHERE s.semail = :email", Supplier.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }

    // Get Supplier ID by Email (For Book Filtering)
    public Optional<Long> findSupplierIdByEmail(String email) {
        return entityManager.createQuery(
                        "SELECT s.id FROM Supplier s WHERE s.semail = :email", Long.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst();
    }

    // Save new Customer
    @Transactional
    public Customer_details saveCustomer(Customer_details customer) {
        entityManager.persist(customer);
        return customer;
    }

    // Save new Supplier
    @Transactional
    public Supplier saveSupplier(Supplier supplier) {
        entityManager.persist(supplier);
        return supplier;
    }
}