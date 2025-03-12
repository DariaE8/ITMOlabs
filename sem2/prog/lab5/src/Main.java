import cli.CLI;

public class Main
{
    public static void main(String[] args) {
        CLI cli = new CLI();
        if (!cli.init(args)){
            System.exit(1);
        }
        cli.run();
    }
}
