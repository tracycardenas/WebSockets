package ec.ups.edu.server;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

public class TestPrinter {
    public static void main(String[] args) {
        PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
        System.out.println("Number of print services: " + printServices.length);

        System.out.println("Available printer: ");
        PrintService mPrintService = null;        
        for (PrintService printer : printServices) {
            System.out.println(printer.getName());
        }

        PrintService defaultPrinter = PrintServiceLookup.lookupDefaultPrintService();
        if (defaultPrinter != null) {
            System.out.println("Default printer: " + defaultPrinter.getName());        
        }
    }   
}