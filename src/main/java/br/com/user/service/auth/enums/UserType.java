package br.com.user.service.auth.enums;

/**
 * Supported user roles in the system as per project requirements.
 */
public enum UserType {
    /**
     * User who owns and manages a restaurant.
     */
    RESTAURANT_OWNER,

    /**
     * Standard customer who uses the system to find restaurants and place orders.
     */
    CLIENT
}