C195 Java Project Submitted by Michael Citi
--------------------------------
Languages used for localization on Login page:
* English		
* Spanish(es_MX)

Login attempts recorded on "user_event_log.txt" that is created and held in the local project folder.

List of available users to log in to the app:

*Username*		*Password*
   test			   test
   c195			 password

User page added to main landing screen if you prefer to test new users.
--------------------------------
Approved schedules defined as follows:

* Sunday - Saturday
* 8:00AM - 5:00PM

Application is detecting local time zone based on UTC.
--------------------------------
The main Schedule Screen (src/View/ScheduleScreen.fxml) will show 
both Weekly and Monthly schedules depending on radio button selected.

The reports required for this project are attached to the Main landing screen
and can be reached by activating the "Appointment Reports" button.
There will be 3 tabs to view each report:
* Appointments by Month
* Appointments by Consultant
* Appointments by Customer (3rd report of my choice).
--------------------------------
**UPDATES** (Revision Attempt 1)

B. Customer Records:
	Corrected city selection when modifying a customer. Added logic to line 280 in /src/Controller/ModCustomerController.java
	Delete error corrected. Was not properly utilizing the foreign key structure in the database to perform delete.
		See /src/Controller/CustomerListController.java 'removeCustomer()' method on line 116 for changes.
	Cannot replicate issue with creating extra customers. 
	
C. Appointments:
	Added scene change method 'loadScene' to line 102 in /src/Controller/ModAppointmentController.java. Saving changes
	will now automatically return to main schedule screen.

E. Time Zones:
	No changes made. Code already adjusts for local time zones and daylight savings time.
	Unsure of changes needed to reach higher Competency levels.

F. Exception Control:
	Added more logic to 'validateData()' method in ScheduleScreenController and ModApppointmentController.java files.
	Added 'checkConflict()' methods to both files as well to clean up and better control the appointment overlap conflict check.
	In addition, more error control was put in place to prevent appointments from being scheduled backwards
	(end time is earlier than start time), or from scheduling appointments with the same start and end times.
	
H. Alerts:
	Removed duplicate alert controls from /src/Controller/MainScreenController.java and 
	corrected multiples of the same appointments showing on the Login Page after successful login.

