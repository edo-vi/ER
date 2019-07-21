package InterfaceController.DOCControllerFactory;

import System.Sistema;

public class DOCControllerFactory {
    private Sistema sys = Sistema.getInstance();

    public DOCController getController(String type) {
        if(type.isEmpty())
            throw new IllegalArgumentException();
        else if(type.equals("search"))
            return new DOCSController(sys.getStore(), sys.getStore().getEventStream());
        else if(type.equals("searchResult"))
            return new DOCSRController(sys.getStore(), sys.getStore().getEventStream());
        else if(type.equals("default"))
            return new DOCDController(sys.getStore(), sys.getStore().getEventStream());
        else if(type.equals("monitoring"))
            return new DOCMController(sys.getStore(), sys.getStore().getEventStream());
        else if(type.equals("addPrescription"))
            return new DOCAPController(sys.getStore(), sys.getStore().getEventStream());
        return null;
    }
}