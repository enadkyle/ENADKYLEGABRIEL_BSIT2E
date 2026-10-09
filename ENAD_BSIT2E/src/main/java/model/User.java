package model;

/**
 * Immutable data model for one row of the "users" table.
 * Roles: ADMIN, STAFF, DRIVER   |   Status: PENDING, ACTIVE, INACTIVE
 */
public record User(int id, String username, String password, String roles, String status) {
}
