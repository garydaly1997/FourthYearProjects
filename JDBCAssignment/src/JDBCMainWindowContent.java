import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.swing.*;
import javax.swing.border.*;
import java.sql.*;

@SuppressWarnings("serial")
public class JDBCMainWindowContent extends JInternalFrame implements ActionListener
{
	String cmd = null;

	
	private Connection con = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	private PreparedStatement pst = null;

	private Container content;

	private JPanel detailsPanel;
	private JPanel exportButtonPanel;
	
	private JScrollPane dbContentsPanel;

	private Border lineBorder;

	private JLabel IDLabel=new JLabel("ID:                 ");
	private JLabel FirstNameLabel=new JLabel("FirstName:               ");
	private JLabel LastNameLabel=new JLabel("LastName:      ");
	private JLabel DobLabel=new JLabel("Date of Birth:        ");
	private JLabel GenderLabel=new JLabel("Gender:                 ");
	private JLabel HeightLabel=new JLabel("Height:               ");
	private JLabel WeightLabel=new JLabel("Weight:      ");
	private JLabel BloodLabel=new JLabel("Blood Type:      ");
	

	private JTextField IDTF= new JTextField(10);
	private JTextField FirstNameTF=new JTextField(10);
	private JTextField LastNameTF=new JTextField(10);
	private JTextField DobTF=new JTextField(10);
	private JTextField GenderTF=new JTextField(10);
	private JTextField HeightTF=new JTextField(10);
	private JTextField WeightTF=new JTextField(10);
	private JTextField BloodTF=new JTextField(10);
	


	private static QueryTableModel TableModel = new QueryTableModel();
	
	private JTable TableofDBContents=new JTable(TableModel);
	
	private JButton updateButton = new JButton("Update");
	private JButton insertButton = new JButton("Insert");
	private JButton exportButton  = new JButton("Export");
	private JButton deleteButton  = new JButton("Delete");
	private JButton clearButton  = new JButton("Clear");

	private JButton  NumBloods = new JButton("NumBloods:");
	private JTextField NumBloodsTF  = new JTextField(12);
	private JButton NumGenders  = new JButton("NumGenders:");
	private JTextField NumGendersTF  = new JTextField(12);
	private JButton ListAllHeights  = new JButton("ListAllHeights");
	private JButton ListAllWeights  = new JButton("ListAllWeights");
	



	public JDBCMainWindowContent( String aTitle)
	{	
		
		super(aTitle, false,false,false,false);
		setEnabled(true);

		initiate_db_conn();
		
		content=getContentPane();
		content.setLayout(null);
		content.setBackground(Color.lightGray);
		lineBorder = BorderFactory.createEtchedBorder(15, Color.red, Color.black);

		
		detailsPanel=new JPanel();
		detailsPanel.setLayout(new GridLayout(11,2));
		detailsPanel.setBackground(Color.lightGray);
		detailsPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "CRUD Actions"));

		detailsPanel.add(IDLabel);			
		detailsPanel.add(IDTF);
		detailsPanel.add(FirstNameLabel);		
		detailsPanel.add(FirstNameTF);
		detailsPanel.add(LastNameLabel);		
		detailsPanel.add(LastNameTF);
		detailsPanel.add(DobLabel);	
		detailsPanel.add(DobTF);
		detailsPanel.add(GenderLabel);		
		detailsPanel.add(GenderTF);
		detailsPanel.add(HeightLabel);
		detailsPanel.add(HeightTF);
		detailsPanel.add(WeightLabel);
		detailsPanel.add(WeightTF);
		detailsPanel.add(BloodLabel);
		detailsPanel.add(BloodTF);
		

		
		exportButtonPanel=new JPanel();
		exportButtonPanel.setLayout(new GridLayout(3,2));
		exportButtonPanel.setBackground(Color.lightGray);
		exportButtonPanel.setBorder(BorderFactory.createTitledBorder(lineBorder, "Export Data"));
		exportButtonPanel.add(NumBloods);
		exportButtonPanel.add(NumBloodsTF);
		exportButtonPanel.add(NumGenders);
		exportButtonPanel.add(NumGendersTF);
		exportButtonPanel.add(ListAllHeights);
		exportButtonPanel.add(ListAllWeights);
		exportButtonPanel.setSize(500, 200);
		exportButtonPanel.setLocation(3, 300);
		content.add(exportButtonPanel);

		insertButton.setSize(100, 30);
		updateButton.setSize(100, 30);
		exportButton.setSize (100, 30);
		deleteButton.setSize (100, 30);
		clearButton.setSize (100, 30);

		insertButton.setLocation(370, 10);
		updateButton.setLocation(370, 110);
		exportButton.setLocation (370, 160);
		deleteButton.setLocation (370, 60);
		clearButton.setLocation (370, 210);

		insertButton.addActionListener(this);
		updateButton.addActionListener(this);
		exportButton.addActionListener(this);
		deleteButton.addActionListener(this);
		clearButton.addActionListener(this);

		this.ListAllHeights.addActionListener(this);
		this.ListAllWeights.addActionListener(this);
		this.NumBloods.addActionListener(this);
		this.NumGenders.addActionListener(this);


		content.add(insertButton);
		content.add(updateButton);
		content.add(exportButton);
		content.add(deleteButton);
		content.add(clearButton);


		TableofDBContents.setPreferredScrollableViewportSize(new Dimension(900, 300));

		dbContentsPanel=new JScrollPane(TableofDBContents,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		dbContentsPanel.setBackground(Color.lightGray);
		dbContentsPanel.setBorder(BorderFactory.createTitledBorder(lineBorder,"Database Content"));

		detailsPanel.setSize(360, 300);
		detailsPanel.setLocation(3,0);
		dbContentsPanel.setSize(700, 300);
		dbContentsPanel.setLocation(477, 0);

		content.add(detailsPanel);
		content.add(dbContentsPanel);

		setSize(982,645);
		setVisible(true);

		TableModel.refreshFromDB(stmt);
	}

	public void initiate_db_conn()
	{
		try
		{
			
			Class.forName("com.mysql.jdbc.Driver");
			
			String url="jdbc:mysql://localhost:3306/patients";
			
			con = DriverManager.getConnection(url, "root", "admin");
			
			stmt = con.createStatement();
		}
		catch(Exception e)
		{
			System.out.println("Error: Failed to connect to database\n"+e.getMessage());
		}
	}

	
	public void actionPerformed(ActionEvent e)
	{
		Object target=e.getSource();
		if (target == clearButton)
		{
			IDTF.setText("");
			FirstNameTF.setText("");
			LastNameTF.setText("");
			DobTF.setText("");
			GenderTF.setText("");
			HeightTF.setText("");
			WeightTF.setText("");
			BloodTF.setText("");
			

		}

		if (target == insertButton)
		{		 
			try
			{
				String sql = "INSERT INTO details"
		                +"(firstName, Lastname, dob, gender, height, weight, blood)"
		                +"VALUES (?,?,?,?,?,?,?)";
		        con = DriverManager.getConnection("jdbc:mysql://localhost/patients","root","admin");
		        pst = con.prepareStatement(sql);
		        pst.setString(1, FirstNameTF.getText());
		        pst.setString(2, LastNameTF.getText());
		        pst.setString(3, DobTF.getText());
		        pst.setString(4, GenderTF.getText());
		        pst.setString(5, HeightTF.getText());
		        pst.setString(6, WeightTF.getText());
		        pst.setString(7, BloodTF.getText());
		        pst.executeUpdate();

			}
			catch (SQLException sqle)
			{
				System.err.println("Error with  insert:\n"+sqle.toString());
			}
			finally
			{
				TableModel.refreshFromDB(stmt);
			}
		}
		if (target == deleteButton)
		{

			try
			{
				String sql ="DELETE FROM details WHERE id =?"; 
				con = DriverManager.getConnection("jdbc:mysql://localhost/patients","root","admin");
		        pst = con.prepareStatement(sql);
		        pst.setString(1, IDTF.getText());
		        pst.executeUpdate();

			}
			catch (SQLException sqle)
			{
				System.err.println("Error with delete:\n"+sqle.toString());
			}
			finally
			{
				TableModel.refreshFromDB(stmt);
			}
		}
		if (target == updateButton)
		{	 	
			try
			{ 			
				String sql ="UPDATE details SET firstName=?,LastName=?,dob=?,gender=?,height=?,weight=?,blood=? WHERE id=?";
				con = DriverManager.getConnection("jdbc:mysql://localhost/patients","root","admin");
		        pst = con.prepareStatement(sql);
		        pst.setString(8, IDTF.getText());
		        pst.setString(1, FirstNameTF.getText());
		        pst.setString(2, LastNameTF.getText());
		        pst.setString(3, DobTF.getText());
		        pst.setString(4, GenderTF.getText());
		        pst.setString(5, HeightTF.getText());
		        pst.setString(6, WeightTF.getText());
		        pst.setString(7, BloodTF.getText());
		        pst.executeUpdate();


				
			
				rs = stmt.executeQuery("SELECT * from details ");
				rs.next();
				rs.close();	
			}
			catch (SQLException sqle){
				System.err.println("Error with  update:\n"+sqle.toString());
			}
			finally{
				TableModel.refreshFromDB(stmt);
			}
		}
		
		if (target == exportButton)
		{		 
			cmd = "select * from details;";

			try{					
				rs= stmt.executeQuery(cmd); 	
				writeToFile(rs);
			}
			catch(Exception e1){e1.printStackTrace();}

		}
		

		

		if(target == this.ListAllHeights){

			cmd = "select distinct height from details;";

			try{					
				rs= stmt.executeQuery(cmd); 	
				writeToFile(rs);
			}
			catch(Exception e1){e1.printStackTrace();}

		}
		
		if(target == this.ListAllWeights){

			cmd = "select distinct weight from details;";

			try{					
				rs= stmt.executeQuery(cmd); 	
				writeToFile(rs);
			}
			catch(Exception e1){e1.printStackTrace();}

		}
	
		
		if(target == this.NumBloods){
			String bloodName = this.NumBloodsTF.getText();

			cmd = "select blood, count(*) "+  "from details " + "where blood = '"  +bloodName+"';";

			System.out.println(cmd);
			try{					
				rs= stmt.executeQuery(cmd); 	
				writeToFile(rs);
			}
			catch(Exception e1){e1.printStackTrace();}

		}
		
		if(target == this.NumGenders){
			String genderName = this.NumGendersTF.getText();

			cmd = "select gender, count(*) "+  "from details " + "where gender = '"  +genderName+"';";

			System.out.println(cmd);
			try{					
				rs= stmt.executeQuery(cmd); 	
				writeToFile(rs);
			}
			catch(Exception e1){e1.printStackTrace();}

		}

	}
	///////////////////////////////////////////////////////////////////////////

	private void writeToFile(ResultSet rs){
		try{
			System.out.println("In writeToFile");
			FileWriter outputFile = new FileWriter("Patients.csv");
			PrintWriter printWriter = new PrintWriter(outputFile);
			ResultSetMetaData rsmd = rs.getMetaData();
			int numColumns = rsmd.getColumnCount();

			for(int i=0;i<numColumns;i++){
				printWriter.print(rsmd.getColumnLabel(i+1)+",");
			}
			printWriter.print("\n");
			while(rs.next()){
				for(int i=0;i<numColumns;i++){
					printWriter.print(rs.getString(i+1)+",");
				}
				printWriter.print("\n");
				printWriter.flush();
			}
			printWriter.close();
		}
		catch(Exception e){e.printStackTrace();}
	}
}


