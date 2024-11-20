package agents;

import jade.core.Agent;

public abstract class BaseAgent extends Agent {
    protected double x, y;  // Position of the agent
    protected double fuelLevel;  // Fuel level of the agent

    @Override
    protected void setup() {
        System.out.println(getLocalName() + " is initialized.");
        initPosition();
        initFuelLevel();
    }

    // Initialize position of the agent
    protected abstract void initPosition();

    // Initialize fuel level (for simplicity, let's assume it's 100 at start)
    protected void initFuelLevel() {
        fuelLevel = 100;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getFuelLevel() { return fuelLevel; }
}
