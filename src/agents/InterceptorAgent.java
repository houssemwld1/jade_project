package agents;

import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class InterceptorAgent extends BaseAgent {
    private BaseAgent target;

    public InterceptorAgent(BaseAgent target) {
        this.target = target;
    }

    @Override
    protected void initPosition() {
        x = 0;
        y = 0;
    }

    @Override
    protected void setup() {
        super.setup();
        addBehaviour(new TickerBehaviour(this, 1000) {
            @Override
            protected void onTick() {
                // Intercept logic based on position and fuel level
                double dx = target.getX() - x;
                double dy = target.getY() - y;
                double distance = Math.sqrt(dx * dx + dy * dy);

                if (distance > 5) {
                    x += dx / distance * 5;
                    y += dy / distance * 5;
                }
                System.out.println(getLocalName() + " is intercepting target at (" + x + ", " + y + ")");
            }
        });

        // Add a behavior to listen for other agents' information
        addBehaviour(new ReceiveInfoBehaviour());
    }

    // Behavior to receive messages
    private class ReceiveInfoBehaviour extends jade.core.behaviours.CyclicBehaviour {
        @Override
        public void action() {
            ACLMessage msg = receive();
            if (msg != null) {
                System.out.println(getLocalName() + " received info: " + msg.getContent());
                // Here you can add logic to process received information, like fuel warnings or target interception.
            } else {
                block();
            }
        }
    }
}
