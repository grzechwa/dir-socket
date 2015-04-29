package serverbs;

import static java.nio.file.StandardWatchEventKinds.*;

// import java.net.*;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.FileTime;
// import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.UserPrincipal;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;


public class Listenbs extends Thread{

	ConnectSql c = new ConnectSql();

	@SuppressWarnings("unchecked")
	public void run() {
		
 		Path path = Paths.get("D:\\_programs\\EclipseJ\\Data\\workspace\\watch_folder_test");
 		WatchService watcher = null;
        String komunikat = null;	

        // komunikat o dzialaniu programu
        System.out.println("Obserwuje: " + path);
        
        // 1. Jesli jest TraySystemowy, utworz go i wype�niaj informacjami
        //    zwiazanymi z obserwacja folderu wskazanej w path
        if(SystemTray.isSupported()){

        	Image image = Toolkit.getDefaultToolkit().getImage("D:\\_programs\\EclipseJ\\Data\\workspace\\ECHO\\serverbs\\src\\ico.jpg");
            PopupMenu trayPopupMenu = new PopupMenu();
            final SystemTray tray = SystemTray.getSystemTray();			
            final TrayIcon trayIcon = new TrayIcon(image, komunikat, trayPopupMenu);
   
            MenuItem action = new MenuItem("Action");
            action.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null, "Action Clicked");          
                }
            });  
            trayPopupMenu.add(action);
            
            MenuItem close = new MenuItem("Close");
            close.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                        tray.remove(trayIcon);
                }
            });
            trayPopupMenu.add(close);
            
            	// 2. obserwuj folder path
            	try {
            		watcher = FileSystems.getDefault().newWatchService();
            		WatchKey key = path.register(watcher, ENTRY_CREATE, ENTRY_DELETE);	
            			for(;;){
            				// 2a
            				key = watcher.take();
                            	for(WatchEvent<?> event: key.pollEvents()){
                            		WatchEvent.Kind<?> kind = event.kind();
                            			// Warunek pozwala tworzyc tylko jedna ikonke w tray
                                        if(tray.getTrayIcons().length==0){
                                        	try {
                                        		tray.add(trayIcon); 
                                            } catch (Exception e){
                                            	System.err.println(e);
                                            }
                                        }
                                                
                                        if (kind == OVERFLOW) {
                                            continue;
                                        }
                                        // TODO: zmian znazwe path2 na cos konkretnego
                                        WatchEvent<Path> ev = (WatchEvent<Path>)event;
                                        Path filename = ev.context();
                                        Path path2 = Paths.get(path+"/"+filename);
                                        
                                        // 2b. Sprawdzenie rodzaju zdarzenia w folderze
                                        if(ev.kind().name().equals("ENTRY_CREATE")){
                                        	System.out.print("Utworzono ");
                                            komunikat = "W lokalizacji " + path + " utworzono ";
                                        } else {
                                            System.out.print("Usunieto ");
                                            komunikat = "W lokalizacji " + path + " usunieto ";
                                        }
                                        
                                        // 2c. Pobieram dane o folderze i plikach
                                        FileTime czas = Files.getLastModifiedTime(path2);
                                        BasicFileAttributes attr=Files.readAttributes(path2,BasicFileAttributes.class);
                                        FileOwnerAttributeView ownerAttributeView = Files.getFileAttributeView(path2, FileOwnerAttributeView.class);
                                        UserPrincipal owner = ownerAttributeView.getOwner();

                                        // Rozroznienie miedzy plikiem i katalogiem
                                        // oraz utworzenie komunikatow dla konsoli
                                        if(Files.isDirectory(path2)) {
                                        	// PosixFileAttributes posixAttr=Files.readAttributes(path,PosixFileAttributes.class);
                                            System.out.format("katalog %s%n", filename);
                                            System.out.format("modyfikacja %s%n", czas);
                                            System.out.format("rozmiar %s%n", attr.size());
                                            System.out.format("ostatni %s%n", attr.lastAccessTime());
                                            // System.out.format("wlasciciel %s%n", posixAttr.owner().getName());
                                            System.out.println("Wlasciciel: " + owner);
                                            c.connect(owner.toString(), (int)attr.size(), czas);
                                            komunikat+="katalog " + filename;
                                                    trayIcon.setToolTip(komunikat);
                                         } else {
                                            System.out.format("plik %s%n", filename);
                                            c.connect(owner.toString(), (int)attr.size(), czas);
                                            komunikat+="plik " + filename;
                                            trayIcon.setToolTip(komunikat);
                                        }
                                    }

                            // Reset the key -- this step is critical if you want to
                            // receive further watch events.  If the key is no longer valid,
                            // the directory is inaccessible so exit the loop.
                            boolean valid = key.reset();

                            if (!valid) {
                                break;
                            }
                        }
                    } catch (IOException x) {
                            System.err.println(x);
                    } catch (InterruptedException e) {   // przechwyt dodany dla key = watchkey
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                    }
         } else {
                 // disable tray option in your application or
                 // perform other actions
                 System.out.println("Jakis blad");
         }
	}
}
