package com.rvce.Grantha.book_rental_service.service;

import com.rvce.Grantha.book_rental_service.dto.UserRegistrationDTO;
import com.rvce.Grantha.book_rental_service.model.Customer_details;
import com.rvce.Grantha.book_rental_service.model.Supplier;
import com.rvce.Grantha.book_rental_service.repository.UserRepository;
import com.rvce.Grantha.book_rental_service.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.Optional;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Authenticate user by email and password
    @Transactional(readOnly = true)
    public String authenticateUser(String email, String password, String requestedRole) {
        if ("CUSTOMER".equalsIgnoreCase(requestedRole)) {
            Optional<Customer_details> customer = userRepository.findCustomerByEmail(email);
            if (customer.isPresent() && passwordEncoder.matches(password, customer.get().getPassword())) {
                return jwtUtil.generateToken(email, "CUSTOMER");
            }
        } else if ("SUPPLIER".equalsIgnoreCase(requestedRole)) {
            Optional<Supplier> supplier = userRepository.findSupplierByEmail(email);
            if (supplier.isPresent() && passwordEncoder.matches(password, supplier.get().getSpassword())) {
                return jwtUtil.generateToken(email, "SUPPLIER");
            }
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials or role");
    }

    // Register a new user
    public String registerUser(UserRegistrationDTO userDTO) {
        Optional<Customer_details> existingCustomer = userRepository.findCustomerByEmail(userDTO.getEmail());
        Optional<Supplier> existingSupplier = userRepository.findSupplierByEmail(userDTO.getEmail());

        if (existingCustomer.isPresent() || existingSupplier.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists");
        }

        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());

        if ("CUSTOMER".equalsIgnoreCase(userDTO.getRole())) {
            Customer_details customer = new Customer_details();
            customer.setName(userDTO.getName());
            customer.setEmail(userDTO.getEmail());
            customer.setPhone(userDTO.getPhone());
            customer.setPassword(encodedPassword);
            customer.setRole("CUSTOMER");
            userRepository.saveCustomer(customer);
            return "CUSTOMER_REGISTERED";
        } else if ("SUPPLIER".equalsIgnoreCase(userDTO.getRole())) {
            Supplier supplier = new Supplier();
            supplier.setSname(userDTO.getName());
            supplier.setSemail(userDTO.getEmail());
            supplier.setSphone(userDTO.getPhone());
            supplier.setSpassword(encodedPassword);
            supplier.setSrole("SUPPLIER");
            userRepository.saveSupplier(supplier);
            return "SUPPLIER_REGISTERED";
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid role");
        }
    }
}