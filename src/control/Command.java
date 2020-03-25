package control;

abstract class Command {
    Actor player;
    public Command(){
        this.player = null;
    }
    public Command(Actor player){
        this.player = player;
    }

    abstract void execute();
    //void undo ();
}
