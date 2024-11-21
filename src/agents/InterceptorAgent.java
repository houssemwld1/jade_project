package agents;

import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.core.AID;

public class InterceptorAgent extends BaseAgent {
    private double targetX = 0, targetY = 0;
    private double avgSpeed = 20; // Average speed of the agent
    @Override
    protected void initPosition() {
        x = 0;
        y = 0;
    }

    @Override
    protected void setup() {
        super.setup();

        // Add behavior to receive coordinates from PatrollingAgent
        addBehaviour(new ReceiveTargetCoordinatesBehaviour());

        // Add behavior for moving towards the received target coordinates
        addBehaviour(new TickerBehaviour(this, 1000) {
            @Override
            protected void onTick() {
                consumeFuel(avgSpeed);
                double distance = Math.sqrt(Math.pow(x - targetX, 2) + Math.pow(y - targetY, 2));

                if (distance > 1) { // If still not at target, move towards it
                                    // Send a confirmation that it's moving towards the coordinates
                    ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                    msg.addReceiver(new AID("Patroller", AID.ISLOCALNAME));
                    msg.setContent("Interceptor  current position: (" + x + ", " + y + ")");
                    send(msg);
                    // Move towards the target
                    x += (targetX - x) * 0.9; // Simple move step towards target
                    y += (targetY - y) * 0.9;
                    System.out.println(getLocalName() + " is moving towards the new target coordinates: (" + targetX
                            + ", " + targetY + ")");
                } else {
                    System.out.println(getLocalName() + " has intercepted the target at coordinates: (" + targetX + ", "
                            + targetY + ")");
                    // come home to refuel
                    x = 0;
                    y = 0;

                    // Send a confirmation that it has intercepted the target
                    ACLMessage msg1 = new ACLMessage(ACLMessage.INFORM);
                    msg1.addReceiver(new AID("Patroller", AID.ISLOCALNAME));
                    msg1.setContent("Interceptor has intercepted the target");
                    send(msg1);

                    // Send a confirmation that it's coming home to refuel
                    ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
                    msg2.addReceiver(new AID("Patroller", AID.ISLOCALNAME));
                    msg2.setContent("Interceptor is coming home to refuel");
                    send(msg2);

                }

            }
        });
    }

    private class ReceiveTargetCoordinatesBehaviour extends jade.core.behaviours.CyclicBehaviour {
        @Override
        public void action() {
            ACLMessage msg = receive();
            if (msg != null && msg.getContent().startsWith("New Target Coordinates")) {
                String content = msg.getContent();
                String[] parts = content.replace("New Target Coordinates: ", "").replace("(", "").replace(")", "")
                        .split(",");
                targetX = Double.parseDouble(parts[0].trim());
                targetY = Double.parseDouble(parts[1].trim());

                System.out.println(
                        getLocalName() + " received new target coordinates: (" + targetX + ", " + targetY + ")");
            } else {
                block();
            }
        }
    }
}
