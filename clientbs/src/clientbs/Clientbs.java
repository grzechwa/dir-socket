package clientbs;
import java.io.*;
import java.net.*;

public class Clientbs {
    public static void main(String[] args) throws IOException {

    	// Nie dziala w pracy
        String serverHostname = new String("127.0.0.1");

        // String serverHostname = new String("172.16.5.16");
        // String serverHostname = new String("0.0.0.0");
        /*
        if (args.length > 0)
           serverHostname = args[0];
        */
        System.out.println ("Attemping to connect to host " +
                serverHostname + " on port 10008.");

        Socket echoSocket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            echoSocket = new Socket(serverHostname, 10008);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(
                                        echoSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: " + serverHostname);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                               + "the connection to: " + serverHostname);
            System.exit(1);
        }

	BufferedReader stdIn = new BufferedReader(
                                   new InputStreamReader(System.in));
	String userInput;

        System.out.println ("Type Message (\"Bye.\" to quit)");
	while ((userInput = stdIn.readLine()) != null) 
           {
	    out.println(userInput);

            // end loop
            if (userInput.equals("Bye."))
                break;

	    System.out.println("echo: " + in.readLine());
	   }

	out.close();
	in.close();
	stdIn.close();
	echoSocket.close();
    }
}

