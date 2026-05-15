package com.stockmanagement.hotelmanagement.config;

import com.stockmanagement.hotelmanagement.auth.User;
import com.stockmanagement.hotelmanagement.auth.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Initializes default admin user on application startup
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        logger.info("========================================");
        logger.info("Starting Data Initialization...");
        logger.info("========================================");

        try {
            // Check if admin user already exists
            var existingAdmin = userRepository.findByEmail("admin@hotel.com");

            if (existingAdmin.isPresent()) {
                logger.info("✓ Admin user already exists in database");
                User admin = existingAdmin.get();
                logger.info("  Email: {}", admin.getEmail());
                logger.info("  Role: {}", admin.getRole());
                logger.info("  Status: {}", admin.getStatus());
            } else {
                logger.info("✗ Admin user not found, creating...");

                // Create new admin user
                User admin = new User();
                admin.setEmail("admin@hotel.com");
                admin.setPassword("admin123");
                admin.setFullName("Administrator");
                admin.setRole("ADMIN");
                admin.setPhoneNumber("+1-555-0000");
                admin.setStatus("Active");

                User savedAdmin = userRepository.save(admin);

                logger.info("✓ Admin user created successfully!");
                logger.info("  ID: {}", savedAdmin.getId());
                logger.info("  Email: {}", savedAdmin.getEmail());
                logger.info("  Role: {}", savedAdmin.getRole());
                logger.info("  Status: {}", savedAdmin.getStatus());
                logger.info("");
                logger.info("========================================");
                logger.info("✓ YOU CAN NOW LOGIN WITH:");
                logger.info("  Email: admin@hotel.com");
                logger.info("  Password: admin123");
                logger.info("  URL: http://localhost:8080/auth/admin-login");
                logger.info("========================================");
            }
        } catch (Exception e) {
            logger.error("Error during data initialization: ", e);
        }
    }
}

