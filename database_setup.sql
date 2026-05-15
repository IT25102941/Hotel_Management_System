-- Hotel Management Database Setup Script
-- Run this script on your MySQL server to create the hotel_management database

CREATE DATABASE IF NOT EXISTS hotel_management;
USE hotel_management;

-- Guests Table
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

-- Rooms Table
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

-- Bookings Table
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
    FOREIGN KEY (guest_id) REFERENCES guests(id),
    FOREIGN KEY (room_id) REFERENCES rooms(id),
    INDEX idx_guest (guest_id),
    INDEX idx_room (room_id),
    INDEX idx_status (status),
    INDEX idx_check_in (check_in_date),
    INDEX idx_check_out (check_out_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Invoices Table
CREATE TABLE IF NOT EXISTS invoices (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    booking_id BIGINT UNIQUE NOT NULL,
    guest_id BIGINT NOT NULL,
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
    FOREIGN KEY (booking_id) REFERENCES bookings(id),
    FOREIGN KEY (guest_id) REFERENCES guests(id),
    INDEX idx_status (status),
    INDEX idx_guest (guest_id),
    INDEX idx_booking (booking_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Staff Table
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

-- Services Table
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

