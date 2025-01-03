import jade.core.Runtime;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;

public class Main {
    public static void main(String[] args) {
        try {
            Runtime runtime = Runtime.instance();
            Profile profile = new ProfileImpl();
            profile.setParameter(Profile.MAIN_HOST, "localhost");
            AgentContainer container = runtime.createMainContainer(profile);

            AgentController patrollingAgent = container.createNewAgent("Patroller", "agents.PatrollingAgent", null);
            AgentController scoutAgent = container.createNewAgent("Scout", "agents.ScoutAgent", null);
            AgentController interceptorAgent = container.createNewAgent("Interceptor", "agents.InterceptorAgent", null); // Pass Scout as the target

            System.out.println("Passing argument to InterceptorAgent: Scout");

            patrollingAgent.start();
            scoutAgent.start();
            interceptorAgent.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
