import java.util.*;
import java.sql.*;

public class Application
{
    public static void main(String[] args)
    {
        //connection to our DBMS
        String connectionUrl = "jdbc:postgresql://localhost:5432/AirlineSystem";
        String username = "postgres";
        String password = "postgres";
        Connection con = null;
        ResultSet resultSet = null;
        Statement statement = null;

        Scanner in = new Scanner(System.in);

        Application user = new Application();
        ChooseTicket chooseTicket = new ChooseTicket();
        Passenger passenger = new Passenger();

        //start of our application
        System.out.println("* It is application for airline system *");

        boolean choice = false; //indicates if user has made a desicion or not
        int decision = 0; //desicion to buy ticket/quit program
        String city1 = null; //city user departs from
        String city2 = null; //city user departs to

        do
        {
            try
            {
                System.out.println("1. Buy ticket");
                System.out.println("2. Quit from program");
                decision = in.nextInt();
                in.nextLine();

                switch (decision)
                {
                    case 1:
                        System.out.println("Enter city you depart from: ");
                        city1 = in.nextLine();
                        System.out.println("Enter city you arrive to");
                        city2 = in.nextLine();
                        break;
                    case 2:
                        return;
                    default:
                        System.out.println("Incorrect number was inputted, write correct number (1/2)");

                }
                choice = true;
            }
            catch(InputMismatchException exception) //exception of wrong input
            {
                System.out.println("Please input an integer");
                System.out.println("Required int, instead of: " + in.nextLine());
            }
        } while (!choice || decision < 1 && decision > 3);

        try{
            Class.forName("org.postgresql.Driver"); //download driver for DBMS
            con = DriverManager.getConnection(connectionUrl, username, password);
            statement = con.createStatement();

            resultSet = statement.executeQuery("select flight.flight_id, depart_from,depart_to,ticket.ticket_id,class_vip,date_from,date_to from flight,ticket where ticket.flight_id=flight.flight_id and depart_from='"+city1+"' and depart_to = '"+city2+"'");
            //query to select ticket with appropriate city1 and city2
            System.out.println("Availible tickets with corresponding coordinates are:\nif(t) = business class, if(f)=econom class\n");
            //printing those ticket(s) and show them to user

            while (resultSet.next()){
                System.out.println( resultSet.getInt("ticket_id")+ " "
                        + resultSet.getString("depart_from")+" "+
                        resultSet.getString("depart_to")+ " "+
                        resultSet.getString("class_vip")+" "+
                        resultSet.getString("date_from")+ " "+
                        resultSet.getString("date_to"));
            }
            chooseTicket.chooseTicket(city1, city2);
            //go and choose ticket that user wants

        } catch(SQLException | ClassNotFoundException exception){
            exception.printStackTrace();
        } finally{
            try{
                resultSet.close();
                statement.close();
                con.close();
            } catch(SQLException exception){
                exception.printStackTrace();
            }
        }
            chooseTicket.buyTicket.showTicket();
            //printing ticket that user has chosen and bought

    }
}
