package agents;

import jade.core.Agent;

public abstract class BaseAgent extends Agent {
    protected double x, y;  // Position of the agent
    protected double fuelLevel;  // Fuel level (range 0 to 100)
    protected double fuelConsumptionRate = 0.0001;  // Fuel consumed per tick

    @Override
    protected void setup() {
        System.out.println(getLocalName() + " is initialized.");
        initPosition();
        fuelLevel = 100.0;  // Initial fuel level
    }

    protected abstract void initPosition();

    // Abstract method for moving the agent (subclasses will implement)
    // protected abstract void move();

    public void consumeFuel(double speed ) {
        fuelLevel -= fuelConsumptionRate * speed;  // Consume fuel
        if (fuelLevel <= 0) {
            fuelLevel = 0;
            alertLowFuel();  // Alert if fuel is empty
        }
    }

    public boolean isLowFuel() {
        return fuelLevel < 20;  // Set a threshold for low fuel
    }

    public double getX() { return x; }
    public double getY() { return y; }

    // Handle low fuel scenario
    protected void alertLowFuel() {
        System.out.println(getLocalName() + " is low on fuel! Fuel level: " + fuelLevel);
    }

    public double getFuel() {
        return fuelLevel;
    }
}
