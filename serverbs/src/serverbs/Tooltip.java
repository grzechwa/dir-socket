package serverbs;

import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

public class Tooltip {
	String komunikat = null;
	Image image = Toolkit.getDefaultToolkit().getImage("/home/greg/Pulpit/cloudy19.png");
    PopupMenu trayPopupMenu = new PopupMenu();
    final SystemTray tray = SystemTray.getSystemTray();			
    final TrayIcon trayIcon = new TrayIcon(image, komunikat, trayPopupMenu);

	public Tooltip(){
            MenuItem action = new MenuItem("Action");
            action.addActionListener(new ActionListener() {
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
            
            if(tray.getTrayIcons().length==0){
    			try {
    				tray.add(trayIcon); 
    			} catch (Exception e){
       	 			System.err.println(e);
    			}
    		}
	}
}
