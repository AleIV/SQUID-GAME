package us.jcedeno.cookie.objects;

import org.bukkit.Location;

import lombok.Data;

/**
 * A matrix generic object to easily manage matrices of data.
 * 
 * @author jcedeno
 */
public @Data class Matrix3d<T> {
    volatile T[][][] matrix;

    @SuppressWarnings("unchecked")
    public Matrix3d(int rows, int columns, int depth) {
        this.matrix = (T[][][]) new Object[rows][columns][depth];
    }

    /**
     * Returns the object at the specified position.
     * 
     * @param row    The row of the object.
     * @param column The column of the object.
     * @return The object at the specified position.
     */
    public T getAt(int row, int column, int depth) {
        return matrix[row][column][depth];
    }

    /**
     * Sets the object at the specified position.
     * 
     * @param row    The row of the object.
     * @param column The column of the object.
     * @param object The object to set.
     * @return The object at the specified position.
     */
    public T setAt(int row, int column, int depth, T object) {
        return matrix[row][column][depth] = object;
    }

    public T getAt(Location loc) {
        return getAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

}
