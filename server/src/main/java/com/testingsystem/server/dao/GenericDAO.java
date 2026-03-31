package com.testingsystem.server.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Generic Data Access Object interface.
 * Defines standard CRUD operations for all entities.
 * Implements the Factory Method pattern through concrete implementations.
 *
 * @param <T> the entity type
 * @param <ID> the ID type
 */
public interface GenericDAO<T, ID> {

    /**
     * Saves an entity to the database.
     *
     * @param entity the entity to save
     * @return the saved entity with generated ID
     * @throws SQLException if a database error occurs
     */
    T save(T entity) throws SQLException;

    /**
     * Updates an existing entity in the database.
     *
     * @param entity the entity to update
     * @return the updated entity
     * @throws SQLException if a database error occurs
     */
    T update(T entity) throws SQLException;

    /**
     * Deletes an entity by ID.
     *
     * @param id the entity ID
     * @return true if the entity was deleted
     * @throws SQLException if a database error occurs
     */
    boolean delete(ID id) throws SQLException;

    /**
     * Finds an entity by ID.
     *
     * @param id the entity ID
     * @return an Optional containing the entity if found
     * @throws SQLException if a database error occurs
     */
    Optional<T> findById(ID id) throws SQLException;

    /**
     * Finds all entities.
     *
     * @return list of all entities
     * @throws SQLException if a database error occurs
     */
    List<T> findAll() throws SQLException;

    /**
     * Counts all entities.
     *
     * @return the total count
     * @throws SQLException if a database error occurs
     */
    long count() throws SQLException;

    /**
     * Checks if an entity exists by ID.
     *
     * @param id the entity ID
     * @return true if the entity exists
     * @throws SQLException if a database error occurs
     */
    boolean exists(ID id) throws SQLException;
}
