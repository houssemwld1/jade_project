package agents;

import jade.core.AID; 
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class ScoutAgent extends BaseAgent {
    private double radius = 200, angle = 0;
    private double avgSpeed = 5; 
    @Override
    protected void initPosition() {
        x = 600;
        y = 500;
    }

    @Override
    protected void setup() {
        super.setup();
        addBehaviour(new TickerBehaviour(this, 1000) {
            @Override
            protected void onTick() {
                consumeFuel(avgSpeed);
                angle += Math.toRadians(10);
                x = 600 + radius * Math.cos(angle);
                y = 500 + radius * Math.sin(angle);

                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.addReceiver(new AID("Patroller", AID.ISLOCALNAME)); // Send to PatrollingAgent
                msg.setContent("Position: (" + x + ", " + y + "), Fuel Level: " + fuelLevel);
                send(msg);

                System.out.println(getLocalName() + " is scouting at (" + x + ", " + y + ") with fuel level: " + fuelLevel);
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
                // Here you can add logic to process received information
            } else {
                block();
            }
        }
    }
}
