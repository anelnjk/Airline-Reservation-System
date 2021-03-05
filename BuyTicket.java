import java.sql.*;
import java.util.Scanner;
import java.util.*;
public class BuyTicket{

    Connection con = null;
    Statement statement = null;

    Passenger passenger = new Passenger();
    Ticket ticket = new Ticket();
    Flight flight = new Flight();

    Scanner in = new Scanner(System.in);

    public BuyTicket()
    {
        Passenger passenger = new Passenger();
        Ticket ticket = new Ticket();
        Flight flight = new Flight();
    }

    public void showTicket()
    {
        System.out.println("You have bought a ticket for flight "+ticket.flight.getDepartFrom()+" - "+ticket.flight.getDepartTo()+"\n\nDetails:");
        System.out.println(this.ticket.toString());
    }

    public void buyTicket(int ticket_id) throws SQLException {
        int flight_id=0;

        String connectionUrl = "jdbc:postgresql://localhost:5432/AirlineSystem";
        String username = "postgres";
        String password = "postgres";
        con = DriverManager.getConnection(connectionUrl, username, password);
        statement = con.createStatement();

        ResultSet flightId = statement.executeQuery("select ticket.flight_id from ticket where ticket_id="+ticket_id);
        if(flightId.next()){
            flight_id=flightId.getInt("flight_id");
        }

        System.out.println("Enter your First Name: ");
        String firstName = in.next();
        passenger.setFirstName(firstName);

        System.out.println("Enter your Second name:");
        String secondName = in.next();
        passenger.setSecondName(secondName);

        System.out.println("Enter your age:");
        int age = in.nextInt();
        in.nextLine();
        passenger.setAge(age);

        System.out.println("Enter your gender: ");
        String gender = in.next();
        passenger.setGender(gender);

        System.out.println("Enter your e-mail address");
        String email = in.next();
        passenger.setEmail(email);

        System.out.println("Enter your phone number:");
        String phoneNumber = in.next();
        passenger.setPhoneNumber(phoneNumber);

        System.out.println("Enter your passport number:");
        String passportNumber = in.next();
        in.nextLine();
        passenger.setPassport(passportNumber);

        System.out.println("Do you want to purchase?\n 1-YES 0-NO");
        int purch = in.nextInt();
        if(purch==0){
            return;
        } else {

            ResultSet rs = statement.executeQuery("\n" +
                    "select * from flight, airplane where flight_id="+flight_id+" and flight.airplane_id=airplane.airplane_id");
            while (rs.next()) {
                Airplane airplane = new Airplane(rs.getInt("airplane_id"), rs.getString("airplane_model"), rs.getInt("business_sits"), rs.getInt("economy_sits"), rs.getInt("crew_sits"));
                flight.setFlightID(rs.getInt("flight_id"));
                flight.setAirplane(airplane);
                flight.setCode(rs.getString("code"));
                flight.setDepartFrom(rs.getString("depart_from"));
                flight.setDepartTo(rs.getString("depart_to"));
                flight.setCompany(rs.getString("company_name"));
                flight.setDateTo(rs.getTimestamp("date_to"));
                flight.setDateFrom(rs.getTimestamp("date_from"));
            }
            ResultSet rs2 = statement.executeQuery("select * from ticket where ticket_id =" + ticket_id);

            while (rs2.next())
            {
                ticket.setPassenger(passenger);
                ticket.setTicket_id(rs2.getInt("ticket_id"));
                ticket.setFlight(flight);
                ticket.setPrice(rs2.getInt("price"));
                ticket.setClassVip(rs2.getBoolean("class_vip"));
                ticket.setTicketStatus(true);
                if (ticket.getClassVip() == true)
                {
                    flight.airplane.setBusinessSitsNumber(flight.airplane.getBusinessSitsNumber() - 1);
                } else {
                    flight.airplane.setEconomySitsNumber(flight.airplane.getEconomySitsNumber() - 1);
                }
                statement.executeUpdate("update ticket set status ='true' where ticket_id =" + ticket_id);
            }

            ResultSet price = statement.executeQuery("select * from ticket where ticket_id=" + ticket_id);
            if(price.next()){
                ticket.setPrice(price.getInt("price"));
            }
            System.out.println("Your bill: "+ticket.getPrice()+"\n");

            System.out.println("Enter your card number:");
            String cardNumber = in.next();
            passenger.setCardNumber(cardNumber);

            System.out.println("Enter your security code:");
            int securityCode = in.nextInt();
            passenger.setSecurityCode(securityCode);

        }

    }

}
