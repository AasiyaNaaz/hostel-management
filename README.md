

Hostel Management System
A Java-based GUI application that manages hostel operations for both administrators and students. It provides user authentication, facility management, student information handling, fee processing, and more. Built using Java Swing and JDBC.

Features
🔐 Authentication
Secure login system for Admin and Student users.

Password change and recovery options.

Sign-up interface for new students.

👨‍💼 Admin Dashboard
Facility management: Add, view, block/unblock, and remove facilities.

Building management: Create, delete, and inspect buildings and their room structures.

Student services: View and manage student data, room allocations, and check-in/out status.

Fee tracking: View payments and pending dues.

Announcement broadcasting and notice management.

🎓 Student Dashboard
View personal room information and roommate details.
Submit check-in and check-out forms.
Access announcements and notices.
Reserve or cancel facilities.
View receipts, fee records, and due payments.
Contact directory for emergency and admin numbers.

TECHNOLOGIES USED:
Java SE
Swing (Java GUI Toolkit)
JDBC (Java Database Connectivity)
MySQL or another compatible RDBMS (assumed from JDBC usage)



Notes
Exception handling is in place but could be improved with logging.
The UI is built entirely with Swing – modularity and MVC pattern adoption is recommended for scalability.
Facility/room capacity is statically viewed and not dynamically updated within this snippet.
Multi-threading could improve responsiveness on heavy database operations.

Author
Tanuja Jamadar(CS24B015) and Aasiya Naaz(CS24B002)


