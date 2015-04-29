package serverbs;
import java.net.*; 
import java.io.*; 

public class Serverbs extends Thread { 

	protected Socket clientSocket;
	private static ServerSocket serverSocket 	= null; 
	protected static boolean watchAddress 		= false;
	private final static String PORT			="10008";
 
	Listenbs listen 		= new Listenbs();
	String clientIP 	= "127.0.0.1";  	// do testowania
	// String clientIP 	= "172.16.5.16";  	// do testowania

	public static void main(String[] args) throws IOException {
		
		// 1. Nawiazanie po�aczenia z portem PORT
		//   Jesli nie: konunikat i zadania z zakresu finally
		//   proba zamkniecia polaczenia
		try { 
			serverSocket = new ServerSocket(Integer.parseInt(PORT)); 
			System.out.println ("Utworzone po��czenie przezz socket");
			
			// 1a. Oczekiwanie na po�aczenie, w razie po�aczenia 
			//     uruchomienie nowego w�tku w metodzie Serverbs (konstruktor)
			//     i powr�t do oczekiwaina w p�tli while (P�tla while jest zawieszana do momentu otrzymania po�aczenia?)
			//     przerwij try'a, jesli co� sie nie powiedzie
			//     powrot do oczekiwania?
			try {
				while(true) {
					System.out.println ("Oczejuje na zgloszenie");
					new Serverbs (serverSocket.accept());
				}
			} catch (IOException e) {
				System.err.println("Accept failed."); 
				System.exit(1); 
			}
		} catch (IOException e) {
			System.err.println("Nie moge uzywac portu: " + PORT); 
			System.exit(1); 
        	} 
		finally {
			try {
				serverSocket.close(); 
			} catch (IOException e) {
				System.err.println("Nie moglem zamknac portu: " + PORT); 
				System.exit(1); 
			}
		}
	} 

	// ! 
	private Serverbs (Socket clientSoc) {
		clientSocket = clientSoc;
		String s = clientSocket.getInetAddress().getHostAddress();
		if( s.equals("clientIP")) watchAddress = true;
		// System.out.println("watchAddress: " + watchAddress + " ip: " + clientSocket.getInetAddress());
		//       if(watchAddress == true && listen.isAlive() == false){
		//             listen.start();
		//        }
		listen.start();
		start();
   }

	public void run() {
		System.out.println ("New Communication Thread Started");
		try { 
			// Do laczenia przez OutputStream
			// Mozna wysylac komunikat
			// TODO: wysylanie informacji o trayu
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader( new InputStreamReader( clientSocket.getInputStream())); 
			String inputLine; 
			while ((inputLine = in.readLine()) != null) {
				System.out.println ("Server: " + inputLine); 
				out.println(inputLine); 
				
				if (inputLine.equals("Bye.")) 
					break; 
             	} 
			out.close(); 
			in.close(); 
			clientSocket.close(); 
		} catch (IOException e) {
			System.err.println("Problem with Communication Server");
			System.exit(1); 
        } 
    }
} 
