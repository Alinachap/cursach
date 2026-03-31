package com.testingsystem.common.utils;

import java.io.*;

/**
 * Utility class for object serialization and deserialization.
 * Provides methods for converting objects to byte arrays and vice versa.
 */
public final class SerializationUtils {

    /**
     * Private constructor to prevent instantiation.
     */
    private SerializationUtils() {
        throw new UnsupportedOperationException("SerializationUtils class cannot be instantiated");
    }

    /**
     * Serializes an object to a byte array.
     *
     * @param object the object to serialize
     * @return byte array containing the serialized object
     * @throws SerializationException if serialization fails
     */
    public static byte[] serialize(Object object) {
        if (object == null) {
            return new byte[0];
        }
        
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(object);
            oos.flush();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new SerializationException("Failed to serialize object", e);
        }
    }

    /**
     * Deserializes an object from a byte array.
     *
     * @param data the byte array containing serialized data
     * @return the deserialized object
     * @throws SerializationException if deserialization fails
     */
    @SuppressWarnings("unchecked")
    public static <T> T deserialize(byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new SerializationException("Failed to deserialize object", e);
        }
    }

    /**
     * Serializes an object to a file.
     *
     * @param object the object to serialize
     * @param filePath the path to the file
     * @throws SerializationException if serialization fails
     */
    public static void serializeToFile(Object object, String filePath) {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(object);
        } catch (IOException e) {
            throw new SerializationException("Failed to serialize object to file: " + filePath, e);
        }
    }

    /**
     * Deserializes an object from a file.
     *
     * @param filePath the path to the file
     * @return the deserialized object
     * @throws SerializationException if deserialization fails
     */
    @SuppressWarnings("unchecked")
    public static <T> T deserializeFromFile(String filePath) {
        try (FileInputStream fis = new FileInputStream(filePath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new SerializationException("Failed to deserialize object from file: " + filePath, e);
        }
    }

    /**
     * Custom exception for serialization/deserialization errors.
     */
    public static class SerializationException extends RuntimeException {
        /**
         * Constructs a new SerializationException with the specified detail message.
         *
         * @param message the detail message
         */
        public SerializationException(String message) {
            super(message);
        }

        /**
         * Constructs a new SerializationException with the specified detail message and cause.
         *
         * @param message the detail message
         * @param cause the cause of the exception
         */
        public SerializationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
