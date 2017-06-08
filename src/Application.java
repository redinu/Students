
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Application {
	
	public static String add(Scanner s){
		String something = "insert into MCStudents (FirstName,LastName,Address,City,State,ZIP,Phone, majorid, scheduleid) VALUES ('";
		// (FirstName,LastName,Address,City,State,ZIP,Phone) VALUES, majorid, scheduleid
		System.out.printf("What is the student's first name?\n");
		something += s.nextLine() + "', '";
		System.out.printf("What is the student's last name?\n");
		something += s.nextLine() + "', '";
		System.out.printf("What is the student's address?\n");
		something += s.nextLine() + "', '";
		System.out.printf("Which city is the student from?\n");
		something += s.nextLine() + "', '";
		System.out.printf("Which state is the student from?\n");
		something += s.nextLine() + "', '";
		System.out.printf("What is the student's ZIP code?\n");
		something += s.nextLine() + "', '";
		System.out.printf("What is the student's phone number?\n");
		something += s.nextLine() + "', '";
		System.out.printf("What is the student's major? ('1' for Comp. Sci., '2' for Civil Eng., '3' for Electrical Eng.)\n");
		something += String.valueOf(s.nextInt()) + "', ";
		System.out.printf("What courses is the student taking? ('Statics', 'Intro.toComp.Sci.', 'Dynamics')\n");
		s.nextLine();
		String[] classes = s.nextLine().split("\\s");
		if (classes.length == 3){
			something += "1);";
		}
		else if (classes.length == 2 && (classes[0].equals("Statics") || classes[0].equals("Intro.toComp.Sci.")) && 
				(classes[1].equals("Statics") || classes[1].equals("Intro.toComp.Sci."))){
			something += "2);";
		}
		else if (classes.length == 2 && (classes[0].equals("Dynamics") || classes[0].equals("Intro.toComp.Sci.")) && 
				(classes[1].equals("Dynamics") || classes[1].equals("Intro.toComp.Sci."))){
			something += "3);";
		}
		else if (classes.length == 2 && (classes[0].equals("Statics") || classes[0].equals("Dynamics")) && 
				(classes[1].equals("Statics") || classes[1].equals("Dynamics"))){
			something += "4);";
		}
		else if (classes[0].equals("Intro.toComp.Sci.")){
			something += "5);";
		}
		else if (classes[0].equals("Statics")){
			something += "6);";
		}
		else if (classes[0].equals("Dynamics")){
			something += "7);";
		}
		else{
			System.out.println("Bad input");
			return null;
		}
		return something;
	}
	
	public static String edit(Scanner s){
		String something = "update MCStudents set ";
		System.out.println("What is your student id?");
		int id = s.nextInt();
		System.out.println("What would you like to change? ('FirstName','LastName','Address','City','State','ZIP',"
				+ "'Phone', 'major')");
		s.nextLine();
		String val = s.nextLine();
		System.out.println("What would you like to change this to?");
		String newVal = s.nextLine();
		if(!val.equals("major")){
			something += val + " = '" + newVal + "' where MCStudents.studentid = " + String.valueOf(id) + ";";
		}
		else{
			something += "majorid = ";
			if(newVal.equals("Civil Engineering")){
				something += "2 where MCStudents.studentid = " + String.valueOf(id) + ";";
			}
			else if(newVal.equals("Electrical Engineering")){
				something += "3 where MCStudents.studentid = " + String.valueOf(id) + ";";
			}
			else{
				something += "1 where MCStudents.studentid = " + String.valueOf(id) + ";";
			}
		}
		return something;
	}
	
	public static String delete(Scanner s){
		String something = "delete from MCStudents where MCStudents.studentid = ";
		System.out.println("What is your student id?");
		int id = s.nextInt();
		s.nextLine();
		something += String.valueOf(id) + ";";
		return something;
	}
	
	public static String[] find(Scanner s){
		String something = "select ";
		System.out.println("What is your student id?");
		int id = s.nextInt();
		System.out.println("What would you like to search? ('FirstName','LastName','Address','City','State','ZIP','Phone')");
		s.nextLine();
		String val = s.nextLine();
		something += val + " from MCStudents where MCStudents.StudentID= " + String.valueOf(id)  + ";";
		String[] myval = {something, val};
		return myval;
	}
	
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		int r = 0;
		boolean flag = true;
		boolean flag2 = true;
		String sql = "select * from City";
		String tex = "";
		try{
			Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/MYNEWdATABASE?"
                                + "user=root&password=password");
			stmt = con.createStatement();
			while(flag){
				System.out.printf("What would you like to do? ('Add, Edit, Delete, Find, Exit')\n");
				String test = s.nextLine();
				if(test.equals("Add")){
					sql = add(s);
					//System.out.printf("%s\n",sql);
					flag2 = false;
					r = stmt.executeUpdate(sql);
				}
				else if (test.equals("Edit")){
					sql = edit(s);
					//System.out.printf("%s\n",sql);
					flag2 = false;
					r = stmt.executeUpdate(sql);
				}
				else if (test.equals("Delete")){
					sql = delete(s);
					flag2 = false;
					r = stmt.executeUpdate(sql);
				}
				else if (test.equals("Find")){
					String[] holder = find(s);
					
					sql = holder[0];
					//System.out.printf("%s\n",sql);
					tex = holder[1];
					//System.out.printf("%s\n",tex);
					flag2 = true;
					rs = stmt.executeQuery(sql);
				}
				else if (test.equals("Exit")){
					flag = false;
				}
				else{
					System.out.println("Your inputted something stupid, try again.");
				}
			
				
				if(flag2){
					
					while(rs.next()){
						System.out.print(rs.getString(tex) + "\t");
						/*if (tex.equals("FirstName")){
							System.out.print(rs.getString("CityId"));
						}*/
						
						System.out.println();
					}
				}
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				stmt.close();
				con.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		
		
		
		
		
	}
}