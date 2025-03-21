package cli.commands;

import cli.DumpManager;
import models.Command;

public class Save extends Command {
    private DumpManager dm;

    public Save() {
        super("save", "сохранить коллекцию в файл");
    }
        @Override
        public void run(String[] args){
            //dump to csv
            dm.save(tm.dumpCSV());
        }
}
