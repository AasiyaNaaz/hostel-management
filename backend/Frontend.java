package backend;

import java.awt.*;															
import java.util.HashMap;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.text.TableView;
import backend.User.userTypes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Frontend extends JFrame {
	User user = new User();
    final private Font mainfont = new Font(" Arial", Font.BOLD, 18);    // Fixed font name
    final private Font smallfont = new Font("Arial", Font.PLAIN, 14);  
    JTextField userField;
    JPasswordField passField;
    JButton loginButton,changePassButton,signUpButton ;
   // JButton cancelButton;
    static HashMap<String, String> users = new HashMap<>();

    public void initialize() {

        users.put("admin", "admin");
        users.put("student", "student");

        // Form Panel

        JLabel titleLabel = new JLabel("                 MY HOSTEL   ");
        titleLabel.setFont(new Font("Arial",Font.BOLD,30));

        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(mainfont);

        userField = new JTextField();
        userField.setFont(mainfont);

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(mainfont);

        passField = new JPasswordField();
        passField.setFont(mainfont);  // Set font for password field

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(5, 1, 5, 5));
        formPanel.add(titleLabel);
        formPanel.add(userLabel);
        formPanel.add(userField);
        formPanel.add(passLabel);
        formPanel.add(passField);

        loginButton = new JButton("Login");
        loginButton.setFont(mainfont);
        loginButton.addActionListener(e -> authenticate());
        
        signUpButton =  new JButton("Sign Up?");
        signUpButton.setFont(mainfont);
        signUpButton.addActionListener(e ->  new SignUpDialog(this));


        changePassButton = new JButton("    Forgot Password??");
        changePassButton.setFont(smallfont);
        changePassButton.setBackground(Color.WHITE);
        changePassButton.setBorderPainted(false);
        changePassButton.setFocusPainted(false);
        changePassButton.setContentAreaFilled(false);
        changePassButton.setForeground(Color.BLACK);
        

        changePassButton.addActionListener(e -> new ForgotPasswordDialog(this));

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(3, 2, 5, 5));
        buttonsPanel.add(loginButton);
        buttonsPanel.add(signUpButton);
        buttonsPanel.add(changePassButton);
    
        

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(255, 255, 255));
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);
    
        add(mainPanel);

        setTitle("HOSTEL MANAGEMENT SYSTEM... ");
        setSize(500, 600);
        setMinimumSize(new Dimension(300, 400));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
    	dbCommands.connectDatabase();
    	Frontend myFrame = new Frontend();
        myFrame.initialize();
    }

    private void authenticate() 
    {
        String username = userField.getText();
        String password = new String(passField.getPassword());

        int res = user.login(username, password);
        if (res == 1)
        {
        	String ut = user.checkUserType(username);
        	//System.out.println(ut);
        	if (ut.equals("Admin") || ut.equals("SuperAdmin")) {
                new AdminDashboard1();
            } 
            else {
            	new StudentDashboard1(username);
            } 
            
        } 
        if (res == -1)
        {
        	JOptionPane.showMessageDialog(this, "User does not exists!\nTry Signing up.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        if (res == -2)
        {
        	JOptionPane.showMessageDialog(this, "Wrong Password!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

class ForgotPasswordDialog extends JDialog {
	User user = new User();
    private JTextField userField;
    private JPasswordField newPassField, confirmPassField;
    private JButton submitButton;

    ForgotPasswordDialog(JFrame parent) {
        super(parent, "Forgot Password", true);
        setSize(500, 600);
        setLayout(new GridLayout(8, 1, 5, 5));
        setLocationRelativeTo(parent);

        JLabel userLabel = new JLabel("Username:");
        userField = new JTextField();

        JLabel newPassLabel = new JLabel("New Password:");
        newPassField = new JPasswordField();

        JLabel confirmPassLabel = new JLabel("Confirm New Password:");
        confirmPassField = new JPasswordField();

        submitButton = new JButton("Reset Password");
        submitButton.addActionListener(e -> resetPassword());

        add(userLabel);
        add(userField);
        add(newPassLabel);
        add(newPassField);
        add(confirmPassLabel);
        add(confirmPassField);
        add(new JLabel()); // Empty label for spacing
        add(submitButton);

        setVisible(true);
    }

    private void resetPassword() {
        String username = userField.getText();
        String newPass = new String(newPassField.getPassword());
        String confirmPass = new String(confirmPassField.getPassword());
        
        if (newPass.equals(confirmPass))
        {
        	int n = user.forgetPassword(username,newPass);
        	if (n == 1)
        	{
        		JOptionPane.showMessageDialog(this, "Password reset successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose();
        	} else {
        		JOptionPane.showMessageDialog(this, "Invalid username!", "Error", JOptionPane.ERROR_MESSAGE);
        	}
        } else {
            JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}






class AdminDashboard1 extends JFrame {
    AdminDashboard1() {
        setTitle("Admin Dashboard");
        setSize(800, 600);
      setDefaultCloseOperation(EXIT_ON_CLOSE);
       // setLayout(new BorderLayout());

        // Left navigation panel
        JPanel navPanel = new JPanel();
        navPanel.setBackground(new Color(30, 60, 90));
        navPanel.setLayout(new GridLayout(12, 1, 5, 5));
        navPanel.setPreferredSize(new Dimension(250, 0));

        JButton dashboard = new JButton("DashBoard");
        dashboard.addActionListener(e -> dashBoard());
        
        JButton studentManagement = new JButton("Student Management");
        JButton roomAllocation = new JButton("Room Allocation");
        JButton feesManagement = new JButton("Fees & Payment");
        JButton complaintManagement = new JButton("Complaints & Maintenance Management");
        JButton adminManagement = new JButton("Admin Management");
        JButton notifications = new JButton("Notices");
        JButton announcement = new JButton("Announcement");
        JButton reports = new JButton("Reports");
        JButton logout = new JButton("Logout");
        logout.addActionListener(e -> logout());

        JButton[] btns = {
            dashboard ,studentManagement, roomAllocation, feesManagement, complaintManagement, adminManagement,
            notifications, announcement, reports, logout
        };

        for (JButton btn : btns) {
            btn.setFocusPainted(false);
            btn.setBackground(new Color(30, 60, 90));
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btn.setForeground(Color.WHITE);
            navPanel.add(btn);
        }

        // Wrapper panel for layout control
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.add(navPanel, BorderLayout.WEST);

        // Center panel for main content
        JPanel centerPanel = new JPanel();
        JLabel welcomeLabel = new JLabel("WELCOME ,ADMIN!!", SwingConstants.CENTER);	
        welcomeLabel.setForeground(new Color(0, 102, 204));
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 26));

        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(welcomeLabel, BorderLayout.CENTER);

        // Add panels to the main frame
        add(wrapperPanel, BorderLayout.WEST);  // Navigation panel
        add(centerPanel, BorderLayout.CENTER);  // Content area

        setVisible(true);
    }
    
    public void logout() {
    	int x = JOptionPane.showConfirmDialog(this,"Are  you sure you want to Logout?","Confirm",JOptionPane.YES_NO_OPTION);
    	if(x == JOptionPane.YES_OPTION) {
    		setVisible(false);
    		JOptionPane.showMessageDialog(this,"Successfully Loged out","Success",JOptionPane.INFORMATION_MESSAGE);
    	}else {
    		setVisible(true);
    	}
    	
    }
    
   /* public void dashBoard() {
    	
    	JFrame dashBoardFrame = new JFrame("DashBoard");  
    	dashBoardFrame.setSize(800,500);
    	
		dashBoardFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		dashBoardFrame.setLayout(new BorderLayout());
		
		
		JLabel titleLabel = new JLabel("DASHBOARD");
		titleLabel.setFont(new Font("Arial",Font.BOLD,24));
		
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(1 ,2,3,3));
		
		JButton changePass = new JButton("Change Password");
		JButton viewProfile = new JButton("View Profile");
		changePass.setFocusPainted(false);
		changePass.setBackground(new Color(135,206,250));
		
		viewProfile.setFocusPainted(false);
		viewProfile.setBackground(new Color(135,206,250));
		
		topPanel.add(changePass,BorderLayout.CENTER);
		topPanel.add(viewProfile,BorderLayout.CENTER);
		
		dashBoardFrame.add(topPanel,BorderLayout.CENTER);
		
		JPanel bottomLabel = new JPanel();
		bottomLabel.setLayout(new GridLayout(5,2,5,5));
		 
		JLabel  heading = new JLabel("Status:");
		heading.setFont(new Font("Arial",Font.BOLD,24));
		heading.setForeground(new Color(0,102,204));
		
		JButton vac = new JButton();
		
		JLabel  h1 = new JLabel("Number of Students :");
		JButton a1 = new JButton(" ");
		JLabel  h2 = new JLabel("Vacant rooms :");
		JButton a2 = new JButton(" ");
		JLabel  h3= new JLabel("Reading Requests :");
		JButton a3 = new JButton(" ");
		JLabel  h4 = new JLabel("Creating Announcement :");
		JButton a4 = new JButton(" ");
		
		bottomLabel.add(heading);
		bottomLabel.add(vac);
		bottomLabel.add(h1);
		bottomLabel.add(a1);
		bottomLabel.add(h2);
		bottomLabel.add(a2);
		bottomLabel.add(a3);
		bottomLabel.add(h3);
		bottomLabel.add(a4);
		bottomLabel.add(h4);
		
		dashBoardFrame.add(bottomLabel,BorderLayout.SOUTH);
		dashBoardFrame.setVisible(true);	
    }
}*/

 public void dashBoard() {
    	
    	JFrame dashBoardFrame = new JFrame("DashBoard");  
    	dashBoardFrame.setSize(800,500);
    	
		dashBoardFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		dashBoardFrame.setLayout(new BorderLayout());
		
		
		JLabel titleLabel = new JLabel("DASHBOARD");
		titleLabel.setFont(new Font("Arial",Font.BOLD,24));
		
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout( 6,2,3,3));
		
		JButton changePass = new JButton("Change Password");
		JButton viewProfile = new JButton("View Profile");
		changePass.setFocusPainted(false);
		changePass.setBackground(new Color(135,206,250));
		
		viewProfile.setFocusPainted(false);
		viewProfile.setBackground(new Color(135,206,250));
		
		topPanel.add(changePass,BorderLayout.CENTER);
		topPanel.add(viewProfile,BorderLayout.CENTER);
		
	//	dashBoardFrame.add(topPanel,BorderLayout.CENTER);
		
		//JPanel bottomLabel = new JPanel();
		//bottomLabel.setLayout(new GridLayout(5,2,5,5));
		 
		JLabel  heading = new JLabel("Status:");
		heading.setFont(new Font("Arial",Font.BOLD,24));
		heading.setForeground(new Color(0,102,204));
		
		JButton vac = new JButton();
		vac.setBackground(new Color(255,255,255));
		
		JLabel  h1 = new JLabel("Number of Students :");
		JButton a1 = new JButton(" ");
		JLabel  h2 = new JLabel("Vacant rooms :");
		JButton a2 = new JButton(" ");
		JLabel  h3= new JLabel("Reading Requests :");
		JButton a3 = new JButton(" ");
		JLabel  h4 = new JLabel("Creating Announcement :");
		JButton a4 = new JButton(" ");
		
		topPanel.add(heading);
		topPanel.add(vac);
		topPanel.add(h1);
		topPanel.add(a1);
		topPanel.add(h2);
		topPanel.add(a2);
		topPanel.add(a3);
		topPanel.add(h3);
		topPanel.add(a4);
		topPanel.add(h4);
		
		dashBoardFrame.add(topPanel,BorderLayout.CENTER);	
		dashBoardFrame.setVisible(true);
    	
    }
}



class StudentDashboard1 extends JFrame {
	Student student = new Student();
	String username;
	private JPanel centerPanel;
	private JPanel sidePanelWrapper;
    private JPanel navPanel;
    private JPanel moreOptionsPanel;	    
    private JFrame moreOptionsWindow;
	 
	 
    StudentDashboard1(String username1) {
        setTitle("Student Dashboard");
        username = username1;
        
        setSize(1000, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        sidePanelWrapper = new JPanel(new BorderLayout());

        
        // Left Navigation Panel
        JPanel navPanel = new JPanel();
        navPanel.setLayout(new GridLayout(6, 1, 10, 10));
        navPanel.setBackground(new Color(30,60, 90));
        navPanel.setPreferredSize(new Dimension(100, 0));

       JButton iconButton = new JButton("≡");
      iconButton.addActionListener(e -> toggleMoreOptions());
       
      

       
       
        ImageIcon originalIcon = new ImageIcon("src/home.png");
        Image scaledImg = originalIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImg);

        // Create the JButton with text and the resized icon
        JButton homeButton = new JButton(" Home", scaledIcon);
        homeButton.addActionListener(e -> Home());
        homeButton.setFocusPainted(false);
        homeButton.setBackground(new Color(30, 60, 90));
        homeButton.setForeground(Color.WHITE);
        homeButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        homeButton.setHorizontalAlignment(SwingConstants.LEFT);
      //  homeButton.setIconTextGap(10);
        
        JButton profileButton = new JButton("Profile");
        profileButton.addActionListener(e -> showProfile());
        JButton requestsButton = new JButton("Requests");
        JButton feedbackButton = new JButton("Feedback");
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        JButton changePassword = new JButton("Change Password");
        changePassword.addActionListener(e ->  new ChangePassword(this));

        JButton[] buttons = {iconButton, homeButton, profileButton, requestsButton, feedbackButton,logoutButton};
        for (JButton btn : buttons) {
            btn.setFocusPainted(false);
            btn.setBackground(new Color(30, 60, 90));
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            navPanel.add(btn);
        }

        sidePanelWrapper.add(navPanel, BorderLayout.WEST);

        // Add wrapper to the frame
        add(sidePanelWrapper, BorderLayout.WEST);
     
        
        // Center Panel (Welcome Message)
         centerPanel = new JPanel(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome, " + username1 + "!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 26));


        centerPanel.add(welcomeLabel, BorderLayout.CENTER);

        // Right Panel (Notices and Announcements)
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new GridLayout(2, 1, 10, 10));
        rightPanel.setPreferredSize(new Dimension(250, 0));

        JTextArea noticesArea = new JTextArea("Notice 1...\nNotice 2...");
        JTextArea announcementsArea = new JTextArea("Announcement 1...\nAnnouncement 2...");
        noticesArea.setLineWrap(true);
        announcementsArea.setLineWrap(true);

        JScrollPane noticeScroll = new JScrollPane(noticesArea);
        JScrollPane announceScroll = new JScrollPane(announcementsArea);

        JPanel noticePanel = new JPanel(new BorderLayout());
        noticePanel.setBorder(BorderFactory.createTitledBorder("Notices"));
        noticePanel.add(noticeScroll);

        JPanel announcePanel = new JPanel(new BorderLayout());
        announcePanel.setBorder(BorderFactory.createTitledBorder("Announcements"));
        announcePanel.add(announceScroll);

        rightPanel.add(noticePanel);
        rightPanel.add(announcePanel);

        // Add all to Frame
        add(navPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.EAST);

       

   

      setVisible(true);
        
        //create more option panel initially hidden;
        
        
    }
    
    public static class ChangePassword extends JDialog {

        private JTextField userField;
        private JPasswordField cpassField;
        private JPasswordField npassField;
        private JPasswordField newpasstext;
        private JButton submitButton;
        User user = new User();

        ChangePassword(JFrame parent) {
            super(parent, "Change Password", true);
            setSize(400, 500);
            setLayout(new GridLayout(9, 1, 5, 5)); // Updated to 9 rows
            setLocationRelativeTo(parent);  

            JLabel userLabel = new JLabel("Username");
            userField = new JTextField(20);
            userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            userField.setFont(new Font("Arial", Font.PLAIN, 14));

            JLabel cpassLabel = new JLabel("Current Password");
            cpassField = new JPasswordField();
            cpassLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            JLabel npassLabel = new JLabel("New Password");
            npassField = new JPasswordField();
            npassLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            JLabel cnpassLabel = new JLabel("Confirm New Password");
            newpasstext = new JPasswordField();
            cnpassLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            submitButton = new JButton("Submit"); // Use class-level button
            submitButton.addActionListener(e -> changepass());

            // Add components to dialog
            add(userLabel);
            add(userField);
            add(cpassLabel);
            add(cpassField);
            add(npassLabel);
            add(npassField);
            add(cnpassLabel);
            add(newpasstext);
            add(submitButton);

            setVisible(true);
        }

        private void changepass() {
            String username = userField.getText();
            String currentpass = new String(cpassField.getPassword());
            String newpass = new String(npassField.getPassword());
            String confirmpass = new String(newpasstext.getPassword());
            if(username.isEmpty() || (currentpass.isEmpty() || newpass.isEmpty() || confirmpass.isEmpty() ))
            {
            	JOptionPane.showMessageDialog(this,"Please Fill all the credentials!!","Error",JOptionPane.INFORMATION_MESSAGE);
	
            }else {
            	if (newpass.equals(confirmpass))
                {
            		int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to change your password?", "Confirm", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) 
                    {
                    	int n = user.forgetPassword(username,newpass);
                    	if (n == 1)
                    	{
                    		JOptionPane.showMessageDialog(this, "Password reset successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            dispose();
                    	} else {
                    		JOptionPane.showMessageDialog(this, "Invalid username!", "Error", JOptionPane.ERROR_MESSAGE);
                    	}
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                }
        }
}
		private boolean isempty(String username) {
			// TODO Auto-generated method stub
			return false;
		}
    }
    
    private void Home() {  
    	if(moreOptionsWindow != null) {
    		moreOptionsWindow.dispose();
    		moreOptionsWindow = null ;
    		
    	}
    	centerPanel.removeAll();
    	
    	 JLabel welcomeLabel = new JLabel("Welcome, " + username +" !", SwingConstants.CENTER);
         welcomeLabel.setFont(new Font("Arial", Font.BOLD, 26));
         welcomeLabel.setForeground(new Color(0, 102, 204));
         centerPanel.add(welcomeLabel, BorderLayout.CENTER);

         revalidate(); // Revalidate the layout
         repaint(); 
    	
    }
    public void logout() {
    	int x = JOptionPane.showConfirmDialog(this,"Are  you sure you want to Logout?","Confirm",JOptionPane.YES_NO_OPTION);
    	if(x == JOptionPane.YES_OPTION) {
    		setVisible(false);
    		JOptionPane.showMessageDialog(this,"Successfully Loged out","Success",JOptionPane.INFORMATION_MESSAGE);
    	}else {
    		setVisible(true);
    	}
    }
    
    private void toggleMoreOptions()
    {
    	
    
        moreOptionsWindow = new JFrame("More Options");
        moreOptionsWindow.setSize(400, 300);
        moreOptionsWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Close the window when done
        moreOptionsWindow.setLayout(new GridLayout(4, 1, 10, 10)); // Using GridLayout to add buttons vertically


    	      
    	JButton StudentManagement = new JButton("Student Management");
    	StudentManagement.addActionListener(e -> {
            openStudentManagement();
            moreOptionsWindow.dispose(); // Close the window
        });
    	
    	JButton FeeManagement = new JButton("Fee Management");
    	FeeManagement.addActionListener(e -> {
            openFeeManagement();
            moreOptionsWindow.dispose(); // Close the window
        });
    	
    	JButton facilityManagement = new JButton("Facility Management");
    	facilityManagement.addActionListener(e -> {
            openFacilityManagement();
            moreOptionsWindow.dispose(); // Close the window
        });
    	
    	JButton contactNumber = new JButton("Contact Numbers");
    	contactNumber.addActionListener(e -> {
            contactNo();
            moreOptionsWindow.dispose(); // Close the window
        });  	
    	
    	JButton[] buttons = {StudentManagement,FeeManagement,facilityManagement,contactNumber} ;
    	for(JButton btn : buttons ) {
    		
    		btn.setFont(new Font("Arial", Font.BOLD, 14));
            btn.setBackground(new Color(255, 255, 255));
            moreOptionsWindow.add(btn,BorderLayout.NORTH);
    	}
    	
    	moreOptionsWindow.setVisible(true); 	
    }
    
    private void contactNo() {
    	JPanel contactPanel = new JPanel();
    	contactPanel.setLayout(new BoxLayout(contactPanel,BoxLayout.Y_AXIS));  
    	
    	JLabel title = new JLabel("CONTACT NUMBERS",SwingConstants.CENTER); 
    	  title.setFont(new Font("Arial", Font.BOLD, 40));
    	  JLabel space = new JLabel("");
    	  JLabel no1 = new JLabel("Hostel Manager : 94210xxxxx");
    	  JLabel no2 = new JLabel("Super Admin : 81498xxxxx");
    	  JLabel no3 = new JLabel("Student Admin 1: 65901xxxxx");
    	  JLabel no4 = new JLabel("Student Admin 2: 94210xxxxx");
    	  JLabel no5 = new JLabel("Fee management Admin : 94210xxxxx");
    	  JLabel no6 = new JLabel("Facility Admin: 45139xxxxx");
    	  JLabel no7 = new JLabel("General Admin: 95610xxxxx");
    	  JLabel no8 = new JLabel("Emergency Contact : 82310xxxxx");
    	 // JLabel no6 = new JLabel(" Admin (Hostel 4): 94210xxxxx");
    	  
    	  JLabel[] labels = {no1,no2,no3,no4,no5,no6,no7,no8};
    	  for(JLabel btn: labels) {
    	    btn.setFont(new Font("Arial",Font.BOLD,20));
    			  
    	  }
    	 
    	 contactPanel.add(title);
    	 contactPanel.add(space);
    	 contactPanel.add(no1);
    	 contactPanel.add(no2);
    	 contactPanel.add(no3);
    	 contactPanel.add(no4);
    	 contactPanel.add(no5);
    	 contactPanel.add(no6);
    	 contactPanel.add(no7);
    	 contactPanel.add(no8);
    	 
    	 centerPanel.removeAll();
    	 centerPanel.add(contactPanel,BorderLayout.CENTER);
    	 
    	 
    	   revalidate();
           repaint();	
    }
    
    private void openStudentManagement() {
    	
    	 centerPanel.removeAll();//clear current center panel
    	 JPanel studentManagementPanel = new JPanel();
    	 
    	 studentManagementPanel.setLayout(new GridLayout(3,2,5,5));
    	 
    	 JButton roomDetails = new JButton("*  Get Room Details");
    	 roomDetails.addActionListener(e -> getRoomDetails());
    	 //JButton requestRoomAllotment = new JButton("Request Room Allotment");
    	 //JButton requestDocuments = new JButton("Request Documents");
    	 JButton viewRoommates = new JButton("*  View Roommates");
    	 viewRoommates.addActionListener(e -> viewRommates());
    	 JButton checkIn = new JButton("*  Check-In");
    	 checkIn.addActionListener(e -> checkIn());
    	 JButton checkOut= new JButton("*  CheckOut");
    	 
    	 
    	 JButton[] buttons = {roomDetails, viewRoommates , checkIn ,checkOut};
    			 
    	 for(JButton btn : buttons) {
    		 btn.setFocusPainted(false);
             btn.setBackground(new Color(255, 255, 255));
             btn.setForeground(Color.BLACK);
             btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
             studentManagementPanel.add(btn);
    	 }
    	 
    	centerPanel.add(studentManagementPanel,BorderLayout.WEST);
    	revalidate();
    	repaint();
    	
    }
    
    private void checkIn()
    {
    	
    }
    
    private void viewRommates()
    {
    	ResultSet resultset = student.viewRoommates(username);
    	
    	try {
    		JPanel profilePanel = new JPanel();
			profilePanel.setLayout(new BoxLayout(profilePanel,BoxLayout.Y_AXIS));
			
			if(resultset.next()) {
				try {
				
					do {
						JLabel studentID = new JLabel("Student ID: " + resultset.getString("ID"));
						JLabel name = new JLabel("Name: " + resultset.getString("firstName") + " " + resultset.getString("lastName"));
						JLabel contactNumber = new JLabel("Contact Number: " + resultset.getString("contactNumber"));
						JLabel newLine = new JLabel("\n\n");

						studentID.setFont(new Font("Arial",Font.PLAIN,16));
						name.setFont(new Font("Arial",Font.PLAIN,16));
						contactNumber.setFont(new Font("Arial",Font.PLAIN,16));
						newLine.setFont(new Font("Arial",Font.PLAIN,16));
						
						profilePanel.add(studentID);
						profilePanel.add(name);
						profilePanel.add(contactNumber);
						profilePanel.add(newLine);
					} while (resultset.next());
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else	{
				JLabel text = new JLabel("Student is not yet assigned to a room.");
				text.setFont(new Font("Arial",Font.PLAIN,20));
				profilePanel.add(text);
			}
			centerPanel.removeAll();
		    centerPanel.add(profilePanel, BorderLayout.CENTER);
		    centerPanel.revalidate();
		    centerPanel.repaint();
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    
    private void getRoomDetails()
    {
    	ResultSet resultset = student.getRoomDetails(username);
    	
    	try {
    		JPanel profilePanel = new JPanel();
			profilePanel.setLayout(new BoxLayout(profilePanel,BoxLayout.Y_AXIS));
			
			if(resultset.next()) {
				try {
					JLabel roomNumber = new JLabel("Room Number: " + resultset.getInt("roomNumber"));
					JLabel floorNumber = new JLabel("Floor Number: " + resultset.getInt("floorNumber"));
					JLabel buildingName = new JLabel("Building Name: " + resultset.getString("buildingName"));

					roomNumber.setFont(new Font("Arial",Font.PLAIN,16));
					floorNumber.setFont(new Font("Arial",Font.PLAIN,16));
					buildingName.setFont(new Font("Arial",Font.PLAIN,16));
					
					profilePanel.add(roomNumber);
					profilePanel.add(floorNumber);
					profilePanel.add(buildingName);
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}else	{
				//JOptionPane.showMessageDialog(this,"Student details not found","Error",JOptionPane.ERROR_MESSAGE);
				JLabel text = new JLabel("Student is not yet assigned to a room.");
				text.setFont(new Font("Arial",Font.PLAIN,20));
				profilePanel.add(text);
			}
			centerPanel.removeAll();
		    centerPanel.add(profilePanel, BorderLayout.CENTER);
		    centerPanel.revalidate();
		    centerPanel.repaint();
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    
    private void openFeeManagement() {
        // Clear current center panel
        centerPanel.removeAll();

        // Show options related to fee management
      
        
        JPanel feeManagementPanel = new JPanel();
   	 
   	 feeManagementPanel.setLayout(new GridLayout(3,1,5,5));
   	 
   	 JButton viewRecords = new JButton("*  View FeeRecords");
   	 viewRecords.addActionListener(e -> viewFeeRecords());
   	 JButton payFees = new JButton("*  Pay Fees");
   	 payFees.addActionListener(e -> payFees());
   	 JButton viewReceipts = new JButton("*  View Receipts");
   	 viewReceipts.addActionListener(e -> viewReceipts());
   	 JButton  viewDues= new JButton("*  View Dues");
   	 viewDues.addActionListener(e -> viewDues());
   	 
   	 
   	 JButton[] buttons = {viewRecords , payFees ,viewReceipts, viewDues};
   			 
   	 for(JButton btn : buttons) {
   		 btn.setFocusPainted(false);
            btn.setBackground(new Color(255, 255, 255));
            btn.setForeground(Color.BLACK);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            feeManagementPanel.add(btn);
   	 }


        centerPanel.add(feeManagementPanel, BorderLayout.WEST);
        revalidate();
        repaint();
    }
    
    private void viewDues()
    {
    	ResultSet resultSet = student.viewDues(username);
    	
    	try {
    		try {

				// Create frame
		        JFrame frame = new JFrame("Student Info");
		        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		        frame.setSize(700, 400);

		        // Sample data and column names
		        String[][] data = new String[50][6];
		        int index = 0;
		        while (resultSet.next())
		        {
		        	data[index][0] = resultSet.getString("feeID");
		        	data[index][1] = username;
		        	data[index][2] = resultSet.getString("firstName") + " " + resultSet.getString("lastName");
		        	data[index][3] = resultSet.getString("academicYear");
		        	data[index][4] = resultSet.getString("totalFee");
		        	data[index][5] = resultSet.getString("dueDate");
		        	index++;
		        }
		        String[] columnNames = {"Fee ID", "Student ID", "Name","Academic Year","Total Fee","Due Date"};

		        // Create JTable
		        JTable table = new JTable(data, columnNames) {
		            @Override
		            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		                Component c = super.prepareRenderer(renderer, row, column);
		                if (!isRowSelected(row)) {
		                    if (row % 2 == 0) {
		                        c.setBackground(Color.WHITE);
		                    } else {
		                        c.setBackground(new Color(224, 240, 255)); // Light blue
		                    }
		                } else {
		                    c.setBackground(getSelectionBackground());
		                }
		                return c;
		            }
		        }; 

		        // Put the table inside a scroll pane
		        JScrollPane scrollPane = new JScrollPane(table);

		        // Add scroll pane to frame
		        frame.add(scrollPane, BorderLayout.CENTER);

		        // Set frame visible
		        frame.setVisible(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void viewReceipts()
    {
    	ResultSet resultSet = student.viewReceipts(username);
    	
    	try {
    		try {

				// Create frame
		        JFrame frame = new JFrame("Student Info");
		        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		        frame.setSize(700, 400);

		        // Sample data and column names
		        String[][] data = new String[50][6];
		        int index = 0;
		        while (resultSet.next())
		        {
		        	data[index][0] = resultSet.getString("receiptNumber");
		        	data[index][1] = username;
		        	data[index][2] = resultSet.getString("firstName") + " " + resultSet.getString("lastName");
		        	data[index][3] = resultSet.getString("feeID");
		        	data[index][4] = resultSet.getString("totalFee");
		        	data[index][5] = resultSet.getString("amountPaid");
		        	index++;
		        }
		        String[] columnNames = {"Receipt Number", "Student ID", "Name","Fee ID","Total Fee","Amount Paid"};

		        // Create JTable
		        JTable table = new JTable(data, columnNames) {
		            @Override
		            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		                Component c = super.prepareRenderer(renderer, row, column);
		                if (!isRowSelected(row)) {
		                    if (row % 2 == 0) {
		                        c.setBackground(Color.WHITE);
		                    } else {
		                        c.setBackground(new Color(224, 240, 255)); // Light blue
		                    }
		                } else {
		                    c.setBackground(getSelectionBackground());
		                }
		                return c;
		            }
		        }; 

		        // Put the table inside a scroll pane
		        JScrollPane scrollPane = new JScrollPane(table);

		        // Add scroll pane to frame
		        frame.add(scrollPane, BorderLayout.CENTER);

		        // Set frame visible
		        frame.setVisible(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void payFees()
    {
    	//int result = student.payFees(username,acadyr);
    }
    
    private void viewFeeRecords()
    {
    	ResultSet resultSet = student.viewFeeRecords(username);
    	
    	try {
    		try {

				// Create frame
		        JFrame frame = new JFrame("Student Info");
		        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		        frame.setSize(700, 400);

		        // Sample data and column names
		        String[][] data = new String[50][7];
		        int index = 0;
		        while (resultSet.next())
		        {
		        	data[index][0] = resultSet.getString("feeID");
		        	data[index][1] = username;
		        	data[index][2] = resultSet.getString("firstName") + " " + resultSet.getString("lastName");
		        	data[index][3] = resultSet.getString("academicYear");
		        	data[index][4] = resultSet.getString("totalFee");
		        	data[index][5] = resultSet.getDate("dueDate").toString();
		        	data[index][6] = resultSet.getInt("paymentStatus") == 1 ? "Paid" : "Not Paid";
		        	index++;
		        }
		        String[] columnNames = {"Fee ID","Student ID", "Name","Academic Year","Total Fee","Due Date","Payment Status"};

		        // Create JTable
		        JTable table = new JTable(data, columnNames) {
		            @Override
		            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		                Component c = super.prepareRenderer(renderer, row, column);
		                if (!isRowSelected(row)) {
		                    if (row % 2 == 0) {
		                        c.setBackground(Color.WHITE);
		                    } else {
		                        c.setBackground(new Color(224, 240, 255)); // Light blue
		                    }
		                } else {
		                    c.setBackground(getSelectionBackground());
		                }
		                return c;
		            }
		        }; 

		        // Put the table inside a scroll pane
		        JScrollPane scrollPane = new JScrollPane(table);

		        // Add scroll pane to frame
		        frame.add(scrollPane, BorderLayout.CENTER);

		        // Set frame visible
		        frame.setVisible(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }


    private void openFacilityManagement() {
        // Clear current center panel
        centerPanel.removeAll();  
        JPanel facilityManagementPanel = new JPanel();
   	 facilityManagementPanel.setLayout(new GridLayout(3,2,5,5));
   	  	 
   	 JButton viewFacility = new JButton("*  Search a Facility");
   	 JButton viewFacilityinBuilding = new JButton("*  View all Facilities in a Building");
   	 JButton viewAllFacility = new JButton("*  View all Facilities");
   	 viewAllFacility.addActionListener(e -> viewAllFacility());
   	 JButton reserveFacility = new JButton("* Reserve Facility");
   	 JButton cancelReservation = new JButton("*  Cancel Reservation");
   	 JButton viewReservations = new JButton("*  View Reservations");
   	 viewReservations.addActionListener(e -> viewReservations());
   	 
   	 
   	 JButton[] buttons = {viewFacility,viewFacilityinBuilding,viewAllFacility ,reserveFacility, cancelReservation ,viewReservations };
   			 
   	 for(JButton btn : buttons) {
   		 btn.setFocusPainted(false);
            btn.setBackground(new Color(255, 255, 255));
            btn.setForeground(Color.BLACK);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            facilityManagementPanel.add(btn);
   	 }        

        centerPanel.add(facilityManagementPanel, BorderLayout.WEST);
        revalidate();
        repaint();
    }
    
    private void viewReservations()
    {
    	ResultSet resultSet = student.viewReservations(username);
    	
    	try {
    		try {

				// Create frame
		        JFrame frame = new JFrame("Student Info");
		        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		        frame.setSize(700, 400);

		        // Sample data and column names
		        String[][] data = new String[50][5];
		        int index = 0;
		        while (resultSet.next())
		        {
		        	data[index][0] = resultSet.getString("facilityName");
		        	data[index][1] = resultSet.getString("buildingName");
		        	data[index][2] = resultSet.getString("dateForReservation");
		        	data[index][3] = resultSet.getTime("startTime").toString();
		        	data[index][4] = resultSet.getTime("endTime").toString();
		        	index++;
		        }
		        String[] columnNames = {"Facility Name", "Building Name","Date","Start Time","End Time"};

		        // Create JTable
		        JTable table = new JTable(data, columnNames) {
		            @Override
		            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		                Component c = super.prepareRenderer(renderer, row, column);
		                if (!isRowSelected(row)) {
		                    if (row % 2 == 0) {
		                        c.setBackground(Color.WHITE);
		                    } else {
		                        c.setBackground(new Color(224, 240, 255)); // Light blue
		                    }
		                } else {
		                    c.setBackground(getSelectionBackground());
		                }
		                return c;
		            }
		        }; 
		        //JTable table = new JTable(data, columnNames);

		        // Put the table inside a scroll pane
		        JScrollPane scrollPane = new JScrollPane(table);

		        // Add scroll pane to frame
		        frame.add(scrollPane, BorderLayout.CENTER);

		        // Set frame visible
		        frame.setVisible(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void viewAllFacility()
    {
    	ResultSet resultSet = student.viewAllFacilities() ;
    	
    	try {
    		try {

				// Create frame
		        JFrame frame = new JFrame("Student Info");
		        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		        frame.setSize(700, 400);

		        // Sample data and column names
		        String[][] data = new String[50][5];
		        int index = 0;
		        while (resultSet.next())
		        {
		        	data[index][0] = resultSet.getString("facilityID");
		        	data[index][1] = resultSet.getString("facilityName");
		        	data[index][2] = resultSet.getString("buildingName");
		        	data[index][3] = resultSet.getString("Capacity"); 
		        	data[index][4] = resultSet.getInt("status") == 1 ? "Available" : "Blocked/UnAvailable";
		        	index++;
		        }
		        String[] columnNames = {"Facility ID","Facility Name", "Building Name","Capacity","Status"};

		        // Create JTable
		        JTable table = new JTable(data, columnNames) {
		            @Override
		            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		                Component c = super.prepareRenderer(renderer, row, column);
		                if (!isRowSelected(row)) {
		                    if (row % 2 == 0) {
		                        c.setBackground(Color.WHITE);
		                    } else {
		                        c.setBackground(new Color(224, 240, 255)); // Light blue
		                    }
		                } else {
		                    c.setBackground(getSelectionBackground());
		                }
		                return c;
		            }
		        }; 

		        // Put the table inside a scroll pane
		        JScrollPane scrollPane = new JScrollPane(table);

		        // Add scroll pane to frame
		        frame.add(scrollPane, BorderLayout.CENTER);

		        // Set frame visible
		        frame.setVisible(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void showProfile() {
    	
    	ResultSet resultset = student.getProfile(username);
    	
    	try {
			if(resultset.next()) {
				JPanel profilePanel = new JPanel();
				profilePanel.setLayout(new BoxLayout(profilePanel,BoxLayout.Y_AXIS));
				
				
				JLabel nameLabel;
				try {
					nameLabel = new JLabel("Name :  " + resultset.getString("firstName") + " " + resultset.getString("lastName"));
					JLabel rollNumberLabel = new JLabel("Roll Number: " + resultset.getString("ID"));
					//JLabel roomNumberLabel = new JLabel("Room Number: " + resultset.getString(""));
					JLabel emailLabel = new JLabel("email ID: " + resultset.getString("email"));
					JLabel dob = new JLabel("Date of Birth: " + resultset.getString("dob"));
					JLabel contactNumber = new JLabel("Contact Number: " + resultset.getString("contactNumber"));
					JLabel city = new JLabel("City: " + resultset.getString("city"));
					JLabel courseName = new JLabel("Course Name: " + resultset.getString("courseName"));
					JLabel admissionDate = new JLabel("Admission Date: " + resultset.getString("admissionDate").toString());
					JLabel nationality = new JLabel("Nationality: " + resultset.getString("nationality"));
					JLabel collegeName = new JLabel("College Name: " + resultset.getString("collegeName"));
					JLabel guardianName = new JLabel("Guardian Name: " + resultset.getString("guardianName"));
					JLabel guardianContact = new JLabel("Guardian Contact: " + resultset.getString("guardianContact"));
					
					nameLabel.setFont(new Font("Arial",Font.PLAIN,16));
					rollNumberLabel.setFont(new Font("Arial",Font.PLAIN,16));
					dob.setFont(new Font("Arial",Font.PLAIN,16));
					emailLabel.setFont(new Font("Arial",Font.PLAIN,16));
					city.setFont(new Font("Arial",Font.PLAIN,16));
					contactNumber.setFont(new Font("Arial",Font.PLAIN,16));
					courseName.setFont(new Font("Arial",Font.PLAIN,16));
					admissionDate.setFont(new Font("Arial",Font.PLAIN,16));
					nationality.setFont(new Font("Arial",Font.PLAIN,16));
					collegeName.setFont(new Font("Arial",Font.PLAIN,16));
					guardianName.setFont(new Font("Arial",Font.PLAIN,16));
					guardianContact.setFont(new Font("Arial",Font.PLAIN,16));
					
					profilePanel.add(nameLabel);
					profilePanel.add(rollNumberLabel);
					profilePanel.add(dob);
					profilePanel.add(emailLabel); 
					profilePanel.add(city);
					profilePanel.add(contactNumber);
					profilePanel.add(courseName);
					profilePanel.add(admissionDate); 
					profilePanel.add(nationality);
					profilePanel.add(collegeName);
					profilePanel.add(guardianName);
					profilePanel.add(guardianContact); 
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				   centerPanel.removeAll();
			       centerPanel.add(profilePanel, BorderLayout.CENTER);
			       centerPanel.revalidate();
			       centerPanel.repaint();
				
			}else	{
				JOptionPane.showMessageDialog(this,"Student details not found","Error",JOptionPane.ERROR_MESSAGE);
			}
		} catch (HeadlessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    
}
          
class SignUpDialog extends JDialog {
	User user = new User();
    private JTextField userField;  
    private JPasswordField passField, confirmPassField;
    private JComboBox<String> userTypeComboBox;
    private JTextField emailField;
    private JButton submitButton;

    SignUpDialog(JFrame parent) {
        super(parent, "Sign Up", true); // Changed title to 'Sign Up'
        setSize(400, 500);
        setLocationRelativeTo(parent);
        
        // Set layout for better control of components' placement
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 5, 5); // Add some padding between components
        gbc.anchor = GridBagConstraints.WEST;  // Align components to the left

        // Username Field
        JLabel userLabel = new JLabel("Username:");
        userField = new JTextField(20);  // Set preferred width
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userField.setFont(new Font("Arial", Font.PLAIN, 14));

        // Password Field
        JLabel passLabel = new JLabel("Password:");
        passField = new JPasswordField(20); // Set preferred width
        passLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passField.setFont(new Font("Arial", Font.PLAIN, 14));

        // Re-enter Password Field
        JLabel confirmPassLabel = new JLabel("Confirm Password:");
        confirmPassField = new JPasswordField(20); // Set preferred width
        confirmPassLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        confirmPassField.setFont(new Font("Arial", Font.PLAIN, 14));
        
        //email field
        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField(20); // Set preferred width
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));

        // User Type ComboBox
        JLabel userTypeLabel = new JLabel("User Type:");
        userTypeComboBox = new JComboBox<>(new String[] {"Student", "Admin"});
        userTypeLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userTypeComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Submit Button
        submitButton = new JButton("Sign Up");
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.setBackground(new Color(0, 123, 255));  // Bootstrap primary color
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.addActionListener(e -> signUp());

        // Add components to the dialog using GridBagLayout
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(userLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        add(userField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(passLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        add(passField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(confirmPassLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        add(confirmPassField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(emailLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        add(userTypeLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        add(userTypeComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;  // Button spans both columns
        add(submitButton, gbc);

        setVisible(true);
    }

    private void signUp() {
        String username = userField.getText();
        String password = new String(passField.getPassword());
        String reEnteredPassword = new String(confirmPassField.getPassword());
        String email = new String(emailField.getText());
        String userType = (String) userTypeComboBox.getSelectedItem();

        
        
        
        // Validate input
        if(  "Admin".equals(userType)) {
        JOptionPane.showMessageDialog(this, "Admin account could not be created.","Acess Limited",JOptionPane.INFORMATION_MESSAGE);
        
        }
        else {
        if (username.isEmpty() || password.isEmpty() || reEnteredPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if the password and re-entered password match
        if (!password.equals(reEnteredPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if the username already exists
        User user = new User();
        int n = user.createUser(username,password, User.userTypes.Student, email);
        if (n == -2) {
        	JOptionPane.showMessageDialog(this, "Username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (n == -1) { 
        	JOptionPane.showMessageDialog(this, "Student data is not available for sign up!.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (n == 1) {
        	JOptionPane.showMessageDialog(this, "Account created successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        }
        // Add new user to the users map

        // Optionally, store user type (you can modify how to store user type)
        // In this example, we are not storing user type directly in the map,
        // but if you wanted to, you could use a Map to store both username and user type.

          // Close the sign-up dialog after successful registration
    }

        // Add new user to the users map
        //Framework.users.put(username, password);

        // Optionally, store user type (you can modify how to store user type)
        // In this example, we are not storing user type directly in the map,
        // but if you wanted to, you could use a Map to store both username and user type.
    } 
}