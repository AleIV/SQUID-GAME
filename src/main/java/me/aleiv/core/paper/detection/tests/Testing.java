package me.aleiv.core.paper.detection.tests;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Testing
 */
public class Testing {

    /**
     * InnerTesting
     */
    public static @Data @AllArgsConstructor class CVector {
        private double x;
        private double y;
        private double z;

        public boolean isInAABB(CVector min, CVector max) {
            var c1 = x >= min.x;
            var c2 = x <= max.x;
            var c3 = y >= min.y;
            var c4 = y <= max.y;
            var c5 = z >= min.z;
            var c6 = z <= max.z;
            return c1 && c2 && c3 && c4 && c5 && c6;
        }

        public boolean isAABB(CVector min, CVector max) {
            var maxX = min.x > max.x;

            return maxX;
        }

        public boolean isInCube(CVector min, CVector max) {

            double minX, minY, minZ, maxX, maxY, maxZ;

            if (min.getX() < max.getX()) {
                minX = min.getX();
                maxX = max.getX();
            } else {
                minX = max.getX();
                maxX = min.getX();
            }
            if (min.getY() < max.getY()) {
                minY = min.getY();
                maxY = max.getY();
            } else {
                minY = max.getY();
                maxY = min.getY();
            }
            if (min.getZ() < max.getZ()) {
                minZ = min.getZ();
                maxZ = max.getZ();
            } else {
                minZ = max.getZ();
                maxZ = min.getZ();
            }

            return !((this.getX() < minX || this.getY() < minY || this.getZ() < minZ)
                    || (this.getX() > maxX || this.getY() > maxY || this.getZ() > maxZ));
        }

    }

    public static void main(String[] args) {
        var v1 = new CVector(-202.5, 70.5, 97.5);
        var v2 = new CVector(-204.5, 74.5, 100.5);

        var v3 = new CVector(-203.5, 74.5, 100.5);
        if (v3.isInCube(v1, v2)) {
            System.out.println("In AABB");
        } else {
            System.out.println("Not in AABB");
        }
    }

    // A method that checks if a value is in between two bounds considering negative
    // inputs.
    public static boolean isInBetween(double value, double min, double max) {
        return value >= Math.min(min, max) && value <= Math.max(min, max);
    }

}