package com.example.schoolmanagement.models;

public class TransportRoute {
    private int id;
    private String routeName;
    private String vehicleNumber;
    private String driverName;
    private double cost;

    public TransportRoute(int id, String routeName, String vehicleNumber, String driverName, double cost) {
        this.id = id;
        this.routeName = routeName;
        this.vehicleNumber = vehicleNumber;
        this.driverName = driverName;
        this.cost = cost;
    }

    public TransportRoute(String routeName, String vehicleNumber, String driverName, double cost) {
        this.routeName = routeName;
        this.vehicleNumber = vehicleNumber;
        this.driverName = driverName;
        this.cost = cost;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getRouteName() { return routeName; }
    public void setRouteName(String routeName) { this.routeName = routeName; }

    public String getVehicleNumber() { return vehicleNumber; }
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }

    public String getDriverName() { return driverName; }
    public void setDriverName(String driverName) { this.driverName = driverName; }

    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }
    
    @Override
    public String toString() { return routeName + " (" + cost + ")"; }
}
