import java.sql.*;
import java.util.Scanner;

public class ChooseTicket{
    Connection con = null;
    ResultSet resultSet = null;
    Statement statement = null;
    BuyTicket buyTicket = new BuyTicket();
    Scanner in = new Scanner(System.in);

    public void chooseTicket(String city1, String city2) throws SQLException
    {
        Flight flight = new Flight();
        String connectionUrl = "jdbc:postgresql://localhost:5432/AirlineSystem";
        String username = "postgres";
        String password = "postgres";
        con = DriverManager.getConnection(connectionUrl, username, password);
        statement = con.createStatement();
        resultSet = statement.executeQuery("select * from flight where depart_from='"+city1+"' and depart_to = '"+city2+"'");

        if(resultSet!=null && resultSet.next() == true)
        {
            while (resultSet.next())
            {
                System.out.println( resultSet.getInt("flight_id")+ " "+
                        resultSet.getInt("airplane_id")+ " "+
                        resultSet.getString("code")+ " "+
                        resultSet.getString("depart_from")+ " "+
                        resultSet.getString("depart_to")+ " "+
                        resultSet.getString("company_name")+ " "+
                        resultSet.getString("date_from")+ " "+
                        resultSet.getString("date_to"));

            }
            System.out.println("\nEnter ID of ticket you want to choose:");
            int ticket_id = in.nextInt();
            buyTicket.buyTicket(ticket_id);
        }
        else
        {
            System.out.println("This race is not available");
        }

    }


}
