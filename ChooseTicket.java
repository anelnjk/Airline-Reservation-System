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
        //search for direct flight from city1 to city2
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
            //input id of ticket with suitable info for user
            ResultSet validTicket = statement.executeQuery("select * from ticket where ticket_id="+ticket_id);
            if(validTicket.next()!=true){
                //in case invalid id was input, print the message
                System.out.println("This ticket does not exist.");
                return;
            } else{
                buyTicket.buyTicket(ticket_id);
                //passing id of ticket and but it
            }
        }
        else
            //in case there is no direct ticket from city1 to city2
        {
            statement = con.createStatement();
            resultSet = statement.executeQuery("SELECT * from flight where depart_to = '" + city2 + "'");
            //find flight FROM city1

            String connectCity;
            int counter = 1;
            int idFirst = 0;
            int idSecond = 0;
            while(resultSet.next() )
            {
                connectCity = resultSet.getString("depart_from");
                //setting connector city as city2 of our flight
                //and search for city with depart_from as connector city

                Statement connectStatement = con.createStatement();
                ResultSet resultSetConnectingTwoCities = connectStatement.executeQuery("SELECT * from flight where depart_to = '" + connectCity + "' and depart_from = '" + city1+"'");

                if(resultSetConnectingTwoCities != null && resultSetConnectingTwoCities.next() == true)
                {
                    System.out.println("There is special way to go there. And it is transfer way, like above. Way №" + counter);

                    System.out.println( resultSet.getInt("flight_id")+ " "+
                            resultSet.getInt("airplane_id")+ " "+
                            resultSet.getString("code")+ " "+
                            resultSet.getString("depart_from")+ " "+
                            resultSet.getString("depart_to")+ " "+
                            resultSet.getString("company_name")+ " "+
                            resultSet.getString("date_from")+ " "+
                            resultSet.getString("date_to"));
                    idFirst = resultSet.getInt("flight_id");
                    System.out.println( resultSetConnectingTwoCities.getInt("flight_id")+ " "+
                            resultSetConnectingTwoCities.getInt("airplane_id")+ " "+
                            resultSetConnectingTwoCities.getString("code")+ " "+
                            resultSetConnectingTwoCities.getString("depart_from")+ " "+
                            resultSetConnectingTwoCities.getString("depart_to")+ " "+
                            resultSetConnectingTwoCities.getString("company_name")+ " "+
                            resultSetConnectingTwoCities.getString("date_from")+ " "+
                            resultSetConnectingTwoCities.getString("date_to"));
                    idSecond = resultSetConnectingTwoCities.getInt("flight_id");
                }
                counter++;
                buyTicket.buyTicket(idFirst, idSecond); //pass two tickets and buy them
            }
            if(counter == 1)
            {
                System.out.println("There is no possible variants.");
            }
            return;
        }

    }

}
