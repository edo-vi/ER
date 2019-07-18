package System.HP;

import State.StringCommand;

public class HPFactory {
    public String getHPInterface(String type) {
        if(type.isEmpty() || type == null)
            throw new IllegalArgumentException();
        else if(type.equals("search"))
            return new HPSearch<StringCommand>().getFile();
        else if(type.equals("default"))
            return new HPDismiss<StringCommand>().getFile();
        else if(type.equals("monitoring"))
            return new HPMonitoring<StringCommand>().getFile();

        return null;
    }
}
