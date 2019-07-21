package InterfaceController.NURControllerFactory;

import System.Sistema;

public class NURControllerFactory {
    private Sistema sys = Sistema.getInstance();

    public NURController getController(String type) {
        if(type.isEmpty())
            throw new IllegalArgumentException();
        else if(type.equals("search"))
            return null;
        else if(type.equals("searchResult"))
            return null;
        else if(type.equals("default"))
            return new NURDController(sys.getStore(), sys.getStore().getEventStream());
        else if(type.equals("monitoring"))
            return null;
        else if(type.equals("addPatient"))
            return new NURAPController(sys.getStore(), sys.getStore().getEventStream());

        return null;
    }
}
