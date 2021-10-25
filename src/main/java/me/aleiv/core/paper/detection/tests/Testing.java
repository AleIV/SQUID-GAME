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

    }

    public static void main(String[] args) {
        var v1 = new CVector(-202.5, 70.5, 97.5);
        var v2 = new CVector(-204.5, 74.5, 100.5);

        var v3 = new CVector(-203.5, 72.5, 99.5);
        if (v3.isInAABB(v1, v2)) {
            System.out.println("In AABB");
        } else {
            System.out.println("Not in AABB");
        }
    }
    
}