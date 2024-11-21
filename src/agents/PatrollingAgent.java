package agents;

import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.core.AID;

public class PatrollingAgent extends BaseAgent {
    private double radius = 100, angle = 0;
    private double targetIntercepted = 0; // Flag to check if target has been intercepted
    private double avgSpeed = 10; // Average speed of the agent
    @Override
    protected void initPosition() {
        x = 400;
        y = 300;
    }

    @Override
    protected void setup() {
        super.setup();
        addBehaviour(new TickerBehaviour(this, 1000) {
            @Override
            protected void onTick() {
                consumeFuel(avgSpeed);
                if (isLowFuel()) {
                    System.out.println(getLocalName() + " is stopping due to low fuel.");
                    return;  // Stop movement if fuel is low
                }
                angle += Math.toRadians(10);
                x = 400 + radius * Math.cos(angle);
                y = 300 + radius * Math.sin(angle);
                
                // Sending vital information (position and fuel level)
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.addReceiver(new AID("Scout", AID.ISLOCALNAME)); // Send to ScoutAgent
                msg.addReceiver(new AID("Interceptor", AID.ISLOCALNAME)); // Send to InterceptorAgent
                msg.setContent("Position: (" + x + ", " + y + "), Fuel Level: " + fuelLevel);
                send(msg);
                // SEND MESSAGE TO interceptor about a target position
            // Send target coordinates to Interceptor if not intercepted
            if (targetIntercepted == 0) {
                ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
                msg2.addReceiver(new AID("Interceptor", AID.ISLOCALNAME)); // Send to InterceptorAgent
                msg2.setContent("New Target Coordinates: (" + 100 + ", " + 200 + ")");
                send(msg2);
            }

    
                System.out.println(getLocalName() + " is patrolling at (" + x + ", " + y + ") with fuel level: " + fuelLevel);
               }
        });

        addBehaviour(new ReceiveInfoBehaviour());
    }

    private class ReceiveInfoBehaviour extends jade.core.behaviours.CyclicBehaviour {
        @Override
        public void action() {
            ACLMessage msg = receive();
            if (msg != null) {
                System.out.println(getLocalName() + " received info: " + msg.getContent());
    
                if (msg.getContent().equals("Interceptor has intercepted the target")) {
                    targetIntercepted  += 1;
                    System.out.println(getLocalName() + " received interception confirmation. Stopping target updates.");
                }
    
                ACLMessage reply = msg.createReply();
                reply.setPerformative(ACLMessage.CONFIRM);
                reply.setContent("Received your position update.");
                send(reply);
            } else {
                block();
            }
        }
    }
    

}
