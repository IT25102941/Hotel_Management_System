-- =====================================================
-- Hotel Management System - Complete Database Schema
-- =====================================================
-- Run this script on your MySQL server to set up the complete database

CREATE DATABASE IF NOT EXISTS hotel_management;
USE hotel_management;

-- =====================================================
-- 1. USERS TABLE (Authentication & Authorization)
-- =====================================================
CREATE TABLE IF NOT EXISTS users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  full_name VARCHAR(255) NOT NULL,
  role VARCHAR(50) NOT NULL,
  phone_number VARCHAR(20),
  status VARCHAR(50) NOT NULL DEFAULT 'Active',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  last_login TIMESTAMP NULL,
  CONSTRAINT unique_email UNIQUE (email),
  INDEX idx_email (email),
  INDEX idx_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 2. GUESTS TABLE
-- =====================================================
CREATE TABLE IF NOT EXISTS guests (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    address VARCHAR(255),
    city VARCHAR(100),
    country VARCHAR(100),
    postal_code VARCHAR(20),
    id_number VARCHAR(50) UNIQUE NOT NULL,
    id_type VARCHAR(50),
    registration_date DATETIME,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_phone (phone_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 3. ROOMS TABLE
-- =====================================================
CREATE TABLE IF NOT EXISTS rooms (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    room_number VARCHAR(20) UNIQUE NOT NULL,
    room_type VARCHAR(100) NOT NULL,
    price_per_night DECIMAL(10, 2) NOT NULL,
    capacity INT NOT NULL,
    description TEXT,
    amenities TEXT,
    status VARCHAR(50) NOT NULL,
    bed_type VARCHAR(50),
    floor INT,
    created_at DATETIME,
    updated_at DATETIME,
    INDEX idx_status (status),
    INDEX idx_room_type (room_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 4. BOOKINGS TABLE
-- =====================================================
CREATE TABLE IF NOT EXISTS bookings (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    guest_id BIGINT NOT NULL,
    room_id BIGINT NOT NULL,
    check_in_date DATE NOT NULL,
    check_out_date DATE NOT NULL,
    number_of_guests INT NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    special_requests TEXT,
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (guest_id) REFERENCES guests(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_guest (guest_id),
    INDEX idx_room (room_id),
    INDEX idx_status (status),
    INDEX idx_check_in (check_in_date),
    INDEX idx_check_out (check_out_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 5. INVOICES TABLE
-- =====================================================
CREATE TABLE IF NOT EXISTS invoices (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    booking_id BIGINT UNIQUE NOT NULL,
    guest_id BIGINT NOT NULL,
    invoice_number VARCHAR(50) UNIQUE,
    invoice_date DATE NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    tax_amount DECIMAL(10, 2),
    total_amount DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    due_date DATE,
    payment_date DATE,
    payment_method VARCHAR(50),
    description TEXT,
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (booking_id) REFERENCES bookings(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (guest_id) REFERENCES guests(id) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_status (status),
    INDEX idx_guest (guest_id),
    INDEX idx_booking (booking_id),
    INDEX idx_invoice_number (invoice_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 6. STAFF TABLE
-- =====================================================
CREATE TABLE IF NOT EXISTS staff (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone_number VARCHAR(20) NOT NULL,
    position VARCHAR(100) NOT NULL,
    department VARCHAR(100),
    salary DECIMAL(10, 2) NOT NULL,
    join_date DATE NOT NULL,
    address VARCHAR(255),
    city VARCHAR(100),
    country VARCHAR(100),
    status VARCHAR(50) NOT NULL,
    emergency_contact VARCHAR(100),
    emergency_contact_phone VARCHAR(20),
    created_at DATETIME,
    updated_at DATETIME,
    INDEX idx_status (status),
    INDEX idx_position (position),
    INDEX idx_department (department),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- 7. SERVICES TABLE
-- =====================================================
CREATE TABLE IF NOT EXISTS services (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    service_name VARCHAR(100) UNIQUE NOT NULL,
    description TEXT NOT NULL,
    category VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    status VARCHAR(50) NOT NULL,
    availability VARCHAR(100),
    provider VARCHAR(100),
    requires_advance_booking BOOLEAN DEFAULT FALSE,
    max_capacity INT,
    contact_info VARCHAR(255),
    created_at DATETIME,
    updated_at DATETIME,
    INDEX idx_status (status),
    INDEX idx_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- INSERT SAMPLE DATA
-- =====================================================

-- Insert Admin User
INSERT IGNORE INTO users (email, password, full_name, role, phone_number, status)
VALUES ('admin@hotel.com', 'admin123', 'Administrator', 'ADMIN', '+1-555-0000', 'Active');

-- Insert Sample Guests
INSERT IGNORE INTO guests (first_name, last_name, email, phone_number, address, city, country, postal_code, id_number, id_type, registration_date)
VALUES
('John', 'Doe', 'john.doe@example.com', '+1-555-0001', '123 Main St', 'New York', 'USA', '10001', 'DL123456', 'Driver License', NOW()),
('Jane', 'Smith', 'jane.smith@example.com', '+1-555-0002', '456 Oak Ave', 'Los Angeles', 'USA', '90001', 'PP987654', 'Passport', NOW());

-- Insert Sample Rooms
INSERT IGNORE INTO rooms (room_number, room_type, price_per_night, capacity, description, amenities, status, bed_type, floor)
VALUES
('101', 'Single', 100.00, 1, 'Cozy single room', 'WiFi, AC, TV', 'Available', 'Single', 1),
('102', 'Double', 150.00, 2, 'Comfortable double room', 'WiFi, AC, TV, Bathtub', 'Available', 'Double', 1),
('201', 'Suite', 250.00, 4, 'Luxurious suite', 'WiFi, AC, TV, Bathtub, Kitchenette', 'Available', 'King', 2);

-- Insert Sample Services
INSERT IGNORE INTO services (service_name, description, category, price, status, availability, provider, requires_advance_booking, max_capacity)
VALUES
('Room Service', '24/7 room service delivery', 'Food & Beverage', 10.00, 'Available', '24/7', 'Hotel Kitchen', FALSE, NULL),
('Spa Treatment', 'Professional spa services', 'Wellness', 100.00, 'Available', '9AM-8PM', 'In-house Spa', TRUE, 4),
('Airport Transfer', 'Transportation to/from airport', 'Transportation', 50.00, 'Available', '24/7', 'External', TRUE, 6);

-- =====================================================
-- END OF SCHEMA
-- =====================================================

