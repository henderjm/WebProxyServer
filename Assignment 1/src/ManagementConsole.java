import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import java.awt.BorderLayout;
import javax.swing.UIManager;
import java.awt.TextArea;
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JTable;

public class ManagementConsole extends JFrame {
	JPanel mainPanel = new JPanel();
	JTextArea blockedIP = new JTextArea();
	JScrollPane scrollpane = new JScrollPane();
	DefaultListModel ipList, cacheList;
	JList list_ip, list_cache;
	UrlHandling urlhandling = new UrlHandling();
	public ManagementConsole() {
		try {
			mcInit();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	private void mcInit() {
		
		this.setTitle("Marks Management Console");
		final JPanel CachePane = new JPanel();
		CachePane.setVisible(false);
		mainPanel.add(scrollpane);
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu optionList = new JMenu("Admin");
		menuBar.add(optionList);
		
		JMenuItem cachedIP = new JMenuItem("View Cache");
		cachedIP.setBackground(UIManager.getColor("MenuBar.selectionBackground"));
		optionList.add(cachedIP);
		JMenuItem blockedList = new JMenuItem("View Blocklist\n");
		blockedList.setBackground(UIManager.getColor("MenuBar.selectionBackground"));

		optionList.add(blockedList);
		
		JMenuItem Log = new JMenuItem("View Log");
		Log.setBackground(UIManager.getColor("MenuBar.selectionBackground"));
		optionList.add(Log);
//		this.getContentPane().add(blocked);
		this.setVisible(true);
		this.setSize(400, 500);
		
		final JPanel BlockedIpPane = new JPanel();
		BlockedIpPane.setForeground(UIManager.getColor("PasswordField.selectionBackground"));
		getContentPane().add(BlockedIpPane, BorderLayout.CENTER);
		
		ipList = new DefaultListModel();
		cacheList = new DefaultListModel();
		
		BlockedIpPane.setLayout(null);
		
		
		Button AddIP = new Button("ADD IP");
		AddIP.setBounds(0, 369, 196, 77);
		BlockedIpPane.add(AddIP);
		
		JScrollPane listScrollPane = new JScrollPane();
		listScrollPane.setBounds(6, 0, 388, 305);
		BlockedIpPane.add(listScrollPane);
		list_ip = new JList(ipList);
		list_ip.setBounds(6, 20, 388, 305);
		list_ip.setBackground(new Color(192, 192, 192));
		list_ip.setForeground(new Color(255, 0, 0));
		listScrollPane.add(list_ip);
		
		list_cache = new JList(cacheList);
		list_cache.setBounds(6, 20, 388, 305);
		list_cache.setBackground(new Color(192, 192, 192));
		list_cache.setForeground(new Color(255, 0, 0));
		CachePane.add(list_cache);
		BlockedIpPane.add(listScrollPane);
		
		Button removeButton = new Button("REMOVE IP");
		removeButton.setBounds(202, 369, 192, 77);
		BlockedIpPane.add(removeButton);
		
		removeButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int index = list_ip.getSelectedIndex();
				String name = list_ip.getSelectedValue().toString();
				System.out.println("REMOVING IP ADDRESS: "+name);
				ipList.removeElementAt(index);
				urlhandling.removeUrl(name);
			}
			
		});
		AddIP.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String name = JOptionPane.showInputDialog(
						ManagementConsole.this, "Enter IP Address");
				ipList.addElement(name);
				urlhandling.addUrl(name);
				
			}
			
		});
		/*
		 * Adding functionality to AddIP button
		 */
		
		getContentPane().add(CachePane, BorderLayout.NORTH);
		blockedList.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				BlockedIpPane.setVisible(true);
				CachePane.setVisible(false);
				
			}
			
		});
		cachedIP.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BlockedIpPane.setVisible(false);
				CachePane.setVisible(true);	
			}
			
		});
		this.validate();
	}
	public void print_message_window(JTextArea ta, String message){
		ta.append(message);
	}
	public void addIp(String ip){
		urlhandling.addUrl(ip);
	}
	public boolean blockedIP(String ip){
		return urlhandling.isBlocked(ip);
	}

	public void print_to_cache_screen(String s) {
		cacheList.addElement(s);
		
	}
}
