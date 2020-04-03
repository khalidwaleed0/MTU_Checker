
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class checker {
	private static TrayIcon trayIcon;
	public static void main(String[] args) throws IOException {
		
		int mtu = 1492;
	    String efficiency;
	    boolean finished = false;
	    JFrame frame = new JFrame();
	    JPanel contentPane;
	    frame.setTitle("MTU Checker");
	    frame.setIconImage(Toolkit.getDefaultToolkit().getImage(checker.class.getResource("/appIcon.png")));
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setResizable(false);
		frame.setSize(450, 150);
		frame.setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.setLayout(null);
		frame.setContentPane(contentPane);
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnAbout = new JMenu("about");
		menuBar.add(mnAbout);
		
		JMenuItem mntmGithubLink = new JMenuItem("Github Link");
		mnAbout.add(mntmGithubLink);
		
		JMenuItem mntmContact = new JMenuItem("contact me");
		mntmContact.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Feel free to contact me at :\nkhalidwaleed0@outlook.com");
			}
		});
		mntmGithubLink.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().browse(new URI("https://github.com/khalidwaleed0/MTU_Checker/"));
				} catch (IOException | URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		});
		mnAbout.add(mntmContact);
		JLabel lblItWillTake = new JLabel("It will take a few minutes to try all mtu values ,please be patient");
		lblItWillTake.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblItWillTake.setBounds(10, 60, 400, 14);
		contentPane.add(lblItWillTake);
		
		JLabel lblTryingMtu = new JLabel();
		lblTryingMtu.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTryingMtu.setBounds(10, 23, 128, 14);
		contentPane.add(lblTryingMtu);
		frame.setVisible(true);
		showTrayIcon();
	    while(!finished)
	    {
	    	lblTryingMtu.setVisible(false);
	    	lblTryingMtu.setText("Trying mtu : "+mtu);
	    	lblTryingMtu.setVisible(true);
	    	efficiency = cmd(mtu);
	    	if(efficiency.contains("Lost = 0"))
			{
	    		lblTryingMtu.setVisible(false);
				lblTryingMtu.setText("The best mtu is : "+mtu);
				notifier("MTU Chooser Finished","The best MTU is : "+mtu);
				lblTryingMtu.setVisible(true);
				finished = true;
			}
			else
				mtu = mtu -5;
	    }
	}
	
	private static String cmd(int mtu) throws IOException
	{
		Process proc = Runtime.getRuntime().exec("ping www.google.com "+"-l "+mtu+" -f");
	    java.io.InputStream is = proc.getInputStream();
	    Scanner s = new Scanner(is).useDelimiter("\\A");
	    String result = "";
	    if (s.hasNext()) {
	        result = s.next();
	    }
	    else {
	        result = "";
	    }
	    s.close();
	    return result;
	}
	
	private static void showTrayIcon()
	{
		SystemTray tray = SystemTray.getSystemTray();
		Image image = Toolkit.getDefaultToolkit().createImage(checker.class.getResource("/appIcon.png"));
		trayIcon = new TrayIcon(image, "Java AWT Tray Demo");
		trayIcon.setImageAutoSize(true);
		trayIcon.setToolTip("MTU Checker");
		try {
			tray.add(trayIcon);
			}catch (AWTException e) {
				e.printStackTrace();
			}
	}
	
	private static void notifier(String downloadState ,String epName)
	{
		try{
		    trayIcon.displayMessage(downloadState,epName, MessageType.NONE);
		}catch(Exception ex){
		    ex.printStackTrace();
		}
	}
}
