package com.rvce.Grantha.book_rental_service.service;

import com.rvce.Grantha.book_rental_service.model.Customer_details;
import com.rvce.Grantha.book_rental_service.model.Supplier;
import com.rvce.Grantha.book_rental_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Customer_details> customer = userRepository.findCustomerByEmail(email);
        if (customer.isPresent()) {
            return new User(customer.get().getEmail(), customer.get().getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("CUSTOMER")));
        }

        Optional<Supplier> supplier = userRepository.findSupplierByEmail(email);
        if (supplier.isPresent()) {
            return new User(supplier.get().getSemail(), supplier.get().getSpassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("SUPPLIER")));
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}