package InterfaceController.NURControllerFactory;

import System.Sistema;

public class NURControllerFactory {
    private Sistema sys = Sistema.getInstance();

    public NURController getController(String type) {
        if(type.isEmpty())
            throw new IllegalArgumentException();
        else if(type.equals("search"))
            return new NURDController(sys.getStore(), sys.getStore().getEventStream());
        else if(type.equals("searchResult"))
            return new NURDController(sys.getStore(), sys.getStore().getEventStream());
        else if(type.equals("default"))
            return new NURDController(sys.getStore(), sys.getStore().getEventStream());
        else if(type.equals("monitoring"))
            return new NURDController(sys.getStore(), sys.getStore().getEventStream());

        return null;
    }
}
