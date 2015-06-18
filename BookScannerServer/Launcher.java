
public class Launcher
{

    public static void main(String args[])
    {
        if (args.length != 2)
        {
            System.out.println("Usage:  java Launcher prefix extension");
            System.out.println("  Example:  java Launcher test .jpeg");
            System.out.println("  Will create test0000.jpeg, test0001.jpeg, and so on...");
            return;
        }
        
        BookScannerServer bss = new BookScannerServer(9876);
        
        FileWriterThread fw = new FileWriterThread(args[0], args[1]);
        bss.registerDataSink(fw);        
        
        // Start the file writer thread
        fw.start();
        
        
        while(true)
            bss.handleClients();
    }
}

