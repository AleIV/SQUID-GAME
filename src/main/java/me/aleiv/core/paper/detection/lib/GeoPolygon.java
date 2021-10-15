package me.aleiv.core.paper.detection.lib;

import java.util.ArrayList;

import me.aleiv.core.paper.detection.objects.Position;

public class GeoPolygon {
    private GeoPolygonProc proc;

    // Vertices of the 3D polygon
    private ArrayList<GeoPoint> v;

    // Vertices Index
    private ArrayList<Integer> idx;

    // Number of vertices
    private int n;

    public ArrayList<GeoPoint> getV() {
        return this.v;
    }

    public ArrayList<Integer> getI() {
        return this.idx;
    }

    public int getN() {
        return this.n;
    }

    public GeoPolygon() {
    }

    /**
     * Create a polygon from a list of vertices of position objects.
     * 
     * @param pos List of vertices of position objects.
     */
    public GeoPolygon(Position... pos) {
        this.v = new ArrayList<GeoPoint>();
        this.idx = new ArrayList<Integer>();

        this.n = pos.length;

        for (int i = 0; i < pos.length; i++) {
            var position = pos[i];
            this.v.add(new GeoPoint(position.getX(), position.getY(), position.getZ()));
            this.idx.add(i);
        }

    }

    public GeoPolygon(ArrayList<GeoPoint> p) {
        this.v = new ArrayList<GeoPoint>();

        this.idx = new ArrayList<Integer>();

        this.n = p.size();

        for (int i = 0; i < n; i++) {
            this.v.add(p.get(i));
            this.idx.add(i);
        }
    }

    public boolean isInside(Position position) {
        if (proc == null)
            proc = new GeoPolygonProc(this);
        return proc.PointInside3DPolygon(position.getX(), position.getY(), position.getZ());
    }

}