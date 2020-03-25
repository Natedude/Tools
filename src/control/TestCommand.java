package control;

public class TestCommand extends Command {

    @Override
    public void execute() {

        System.out.println("COMMAND TEST: executed -- at: " + System.currentTimeMillis() );
    }
}
