package ec.ups.edu.server;

import java.io.*;
import java.nio.ByteBuffer;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.event.PrintJobAdapter;
import javax.print.event.PrintJobEvent;
import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@SuppressWarnings("ALL")
@ServerEndpoint("/receive/fileserver")
public class FileServerEndpoint {

    @OnOpen
    public void open(Session session, EndpointConfig conf) {
        System.out.println("Server Web Socket is open");
    }


    @OnMessage
    public void message(Session session, String msg) {
        System.out.println("Nombre de archivo: " + msg);
       
            try {
                print(msg);  
            } catch (IOException e) {
                e.printStackTrace();
            } catch (PrintException e) {
                e.printStackTrace();
            }
    }
    

    private void print(String filename) throws PrintException, IOException {
    	
    	String defaultPrinter = PrintServiceLookup.lookupDefaultPrintService().getName();
    		    System.out.println("Default printer: " + defaultPrinter);
    		    PrintService service = PrintServiceLookup.lookupDefaultPrintService();
    		    FileInputStream is2 = new FileInputStream("/home/tracy/Downloads/imprimir.pdf");

    		    PrintRequestAttributeSet  pras = new HashPrintRequestAttributeSet();
    		    pras.add(new Copies(1));

    		    DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
    		    Doc doc = new SimpleDoc(is2, flavor, null);
    		    DocPrintJob job = service.createPrintJob();

    		    PrintJobWatcher pjw = new PrintJobWatcher(job);
    		    job.print(doc, pras);
    		    pjw.waitForDone();
    		    is2.close();
    }

    @OnClose
    public void close(Session session, CloseReason reason) {
        System.out.println("WebSocket closed: "+ reason.getReasonPhrase());
    }

    @OnError
    public void error(Session session, Throwable t) {
        t.printStackTrace();

    }
}

class PrintJobWatcher {
	  boolean done = false;

	  PrintJobWatcher(DocPrintJob job) {
	    job.addPrintJobListener(new PrintJobAdapter() {
	      public void printJobCanceled(PrintJobEvent pje) {
	        allDone();
	      }
	      public void printJobCompleted(PrintJobEvent pje) {
	        allDone();
	      }
	      public void printJobFailed(PrintJobEvent pje) {
	        allDone();
	      }
	      public void printJobNoMoreEvents(PrintJobEvent pje) {
	        allDone();
	      }
	      void allDone() {
	        synchronized (PrintJobWatcher.this) {
	          done = true;
	          System.out.println("Printing done ...");
	          PrintJobWatcher.this.notify();
	        }
	      }
	    });
	  }
	  public synchronized void waitForDone() {
	    try {
	      while (!done) {
	        wait();
	      }
	    } catch (InterruptedException e) {
	    }
	  }
	}
