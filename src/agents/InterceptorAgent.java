package agents;

import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class InterceptorAgent extends BaseAgent {
    private double targetX = 100.0; // Example target coordinates
    private double targetY = 200.0; // Example target coordinates

    @Override
    protected void initPosition() {
        x = 0;
        y = 0;
    }

    @Override
    protected void setup() {
        super.setup();

        System.out.println(getLocalName() + " targeting coordinates: (" + targetX + ", " + targetY + ")");

        // Add interception behavior
        addBehaviour(new TickerBehaviour(this, 1000) {
            @Override
            protected void onTick() {
                // Calculate distance to the target
                double distance = Math.sqrt(Math.pow(x - targetX, 2) + Math.pow(y - targetY, 2));

                if (distance < 10) { // Define a threshold distance for interception
                    System.out.println(getLocalName() + " has intercepted the target at coordinates (" + targetX + ", " + targetY + ")");
                    consumeFuel(); // Simulate fuel consumption
                } else {
                    System.out.println(getLocalName() + " is moving towards target coordinates (" + targetX + ", " + targetY + ")");
                }

                // Send an update message to inform the target coordinates and fuel level
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.setContent("Interceptor position: (" + x + ", " + y + "), Fuel Level: " + fuelLevel);
                send(msg);
            }
        });

        // Add behavior to receive messages
        addBehaviour(new ReceiveInfoBehaviour());
    }

    // Behavior to receive messages
    private class ReceiveInfoBehaviour extends jade.core.behaviours.CyclicBehaviour {
        @Override
        public void action() {
            ACLMessage msg = receive();
            if (msg != null) {
                System.out.println(getLocalName() + " received info: " + msg.getContent());
            } else {
                block();
            }
        }
    }
}
