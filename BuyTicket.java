import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class BuyTicket <T>
{
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

    public String checkMismatch(T info, String p)
            //method to check validation of the data about user
            //using <T> generic to check both String/Integer type info that user required to input
    {
        boolean flag = true; //indicates if input was valid
        do {
            info = (T) in.next();
            Pattern namePatt = Pattern.compile(p);
            //p = String of Regex with validation
            Matcher matcher = namePatt.matcher((CharSequence) info);
            flag = matcher.lookingAt();
            //flag = true, if input was valid;
            //flag = false otherwise
            if (!flag)
            {
                System.out.println("Invalid form! Try again.");
            }
        } while(!flag);
        //require to input valid info until it is invalid
        return (String) info;
    }

    public void showTicket()
    {
        try
        {
            System.out.println("You have bought a ticket for flight " + ticket.flight.getDepartFrom() + " - " + ticket.flight.getDepartTo() + "\n\nDetails:");
            System.out.println(this.ticket.toString());
        }
        catch (NullPointerException e)
        {
            return;
        }
    }

    public void buyTicket(int ticket_id) throws SQLException
            //method for buying one ticket with direct flight
    {
        int flight_id=0;

        String connectionUrl = "jdbc:postgresql://localhost:5432/AirlineSystem";
        String username = "postgres";
        String password = "postgres";

        con = DriverManager.getConnection(connectionUrl, username, password);
        statement = con.createStatement();

        ResultSet validTicket = statement.executeQuery("select * from ticket where ticket_id="+ticket_id);
        //if there is a valid ricket id was input then we buy it, otherwise show message
        if(validTicket.next()!=true)
        {
            System.out.println("This ticket does not exist.");
            return;
        } else
        {
            ResultSet flightId = statement.executeQuery("select ticket.flight_id from ticket where ticket_id=" + ticket_id);
            if (flightId.next())
            {
                flight_id = flightId.getInt("flight_id");
            }
            try
            {
                System.out.println("Enter your First Name: ");
                String firstName = "";
                String pName = "^[A-Za-z]+$"; //regex
                passenger.setFirstName(checkMismatch((T) firstName, pName)); //setting passengers info

                System.out.println("Enter your Second name:");
                String secondName = "";
                String pSName = "^[A-Za-z]+$"; //regex
                passenger.setSecondName(checkMismatch((T) secondName, pSName)); //setting passengers info

                System.out.println("Enter your age:");
                Integer age = 0;
                String pAge = "^[0-9]{1,2}$";
                checkMismatch((T) age, pAge);
                in.nextLine();
                passenger.setAge(age);

                System.out.println("Enter your gender: ");
                String gender = "";
                String pGender = "^[A-Za-z]{4,6}$";
                passenger.setGender(checkMismatch((T) gender, pGender));

                System.out.println("Enter your e-mail address");
                String email = "";
                String pEmail = "^[A-Za-z][A-Za-z0-9]+@((gmail.com)|(yandex.ru)|(mail.ru)|(yahoo.com))$";
                passenger.setEmail(checkMismatch((T) email, pEmail));

                System.out.println("Enter your phone number (+7):");
                String phoneNumber = "";
                String pPhone = "^\\+7\\(?([0-9]{3})\\)?([ .-]?)([0-9]{3})\\2([0-9]{4})$";
                passenger.setPhoneNumber(checkMismatch((T) phoneNumber, pPhone));

                System.out.println("Enter your passport number:");
                String passportNumber = "";
                String pPassp = "[0-9]{7}$";
                passenger.setPassport(checkMismatch((T) passportNumber, pPassp));

                System.out.println("Do you want to purchase?\n 1-YES 0-NO");
                int purch = in.nextInt();
                if (purch == 0)
                {
                    return;
                } else
                {
                    //setting flight parameters by executing query to select flight with appropriate id
                    ResultSet rs = statement.executeQuery("\n" +
                            "select * from flight, airplane where flight_id=" + flight_id + " and flight.airplane_id=airplane.airplane_id");
                    while (rs.next())
                    {
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
                    //setting ticket's parameters by executing query with appropriate tisket id
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
                        } else
                        {
                            flight.airplane.setEconomySitsNumber(flight.airplane.getEconomySitsNumber() - 1);
                        }
                        statement.executeUpdate("update ticket set status ='true' where ticket_id =" + ticket_id);
                    }

                    ResultSet price = statement.executeQuery("select * from ticket where ticket_id=" + ticket_id);
                    if (price.next())
                    {
                        ticket.setPrice(price.getInt("price"));
                    }
                    System.out.println("Your bill: " + ticket.getPrice() + "\n");

                    System.out.println("Enter your card number:");
                    String cardNumber = "";
                    String pCardNum = "[0-9]{16}$";
                    passenger.setCardNumber(checkMismatch((T) cardNumber, pCardNum));

                    System.out.println("Enter your security code:");
                    Integer securityCode = 0;
                    String pSecurity = "[0-9]{3}$";
                    checkMismatch((T) securityCode, pSecurity);
                    passenger.setSecurityCode(securityCode);

                }
            } catch (PatternSyntaxException patternException)
            {
                patternException.printStackTrace();
            }
        }
    }

    public void buyTicket(int ticket_id_first, int ticket_id_second) throws SQLException
            //method for buying two tickets with transfer flight

    {

        int flight_id_first = 0;
        int flight_id_second = 0;

        String connectionUrl = "jdbc:postgresql://localhost:5432/AirlineSystem";
        String username = "postgres";
        String password = "postgres";

        con = DriverManager.getConnection(connectionUrl, username, password);
        statement = con.createStatement();
        statement = con.createStatement();
        System.out.println(ticket_id_first + " " + ticket_id_second);
        ResultSet validTicket = statement.executeQuery("select * from ticket where ticket_id="+ticket_id_first);
        statement = con.createStatement();
        ResultSet validTicketSecond = statement.executeQuery("select * from ticket where ticket_id="+ticket_id_second);

        System.out.println("Processing...");
        if(!validTicket.next() || !validTicketSecond.next())
        {
            System.out.println("This ticket does not exist.");
            return;
        }

        else
        {
            statement = con.createStatement();
            ResultSet flightId = statement.executeQuery("select ticket.flight_id from ticket where ticket_id=" + ticket_id_first);
            statement = con.createStatement();
            ResultSet flightIdSecond = statement.executeQuery("select ticket.flight_id from ticket where ticket_id=" + ticket_id_second);
            if (flightId.next())
            {
                flight_id_first = flightId.getInt("flight_id");
            }
            if(flightIdSecond.next())
            {
                flight_id_second = flightIdSecond.getInt("flight_id");
            }
            try
            {
                System.out.println("Enter your First Name: ");
                String firstName = "";
                String pName = "^[A-Za-z]+$";

                checkMismatch((T) firstName, pName);
                passenger.setFirstName(firstName);

                System.out.println("Enter your Second name:");
                String secondName = "";
                String pSName = "^[A-Za-z]+$";
                checkMismatch((T) secondName, pSName);
                passenger.setSecondName(secondName);

                System.out.println("Enter your age:");
                Integer age = 0;
                String pAge = "^[0-9]{1,2}$";
                checkMismatch((T) age, pAge);
                in.nextLine();
                passenger.setAge(age);

                System.out.println("Enter your gender: ");
                String gender = "";
                String pGender = "^[A-Za-z]{4,6}$";
                checkMismatch((T) gender, pGender);
                passenger.setGender(gender);

                System.out.println("Enter your e-mail address");
                String email = "";
                String pEmail = "^[A-Za-z][A-Za-z0-9]+@((gmail.com)|(yandex.ru)|(mail.ru)|(yahoo.com))$";
                checkMismatch((T) email, pEmail);
                passenger.setEmail(email);

                System.out.println("Enter your phone number (+7):");
                String phoneNumber = "";
                String pPhone = "^\\+7\\(?([0-9]{3})\\)?([ .-]?)([0-9]{3})\\2([0-9]{4})$";
                checkMismatch((T) phoneNumber, pPhone);
                passenger.setPhoneNumber(phoneNumber);

                System.out.println("Enter your passport number:");
                String passportNumber = "";
                String pPassp = "[0-9]{7}$";
                checkMismatch((T) passportNumber, pPassp);
                passenger.setPassport(passportNumber);

                System.out.println("Do you want to purchase?\n 1-YES 0-NO");
                int purch = in.nextInt();
                if (purch == 0)
                {
                    return;
                } else
                {
                    statement = con.createStatement();
                    ResultSet rs = statement.executeQuery("\n" +
                            "select * from flight, airplane where flight_id=" + flight_id_first + " and flight.airplane_id=airplane.airplane_id");
                    statement = con.createStatement();
                    ResultSet rsSecond = statement.executeQuery("\n" +
                            "select * from flight, airplane where flight_id=" + flight_id_second + " and flight.airplane_id=airplane.airplane_id");

                    while (rs.next())
                    {
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
                    statement = con.createStatement();
                    ResultSet rsTicket = statement.executeQuery("select * from ticket where ticket_id = " + ticket_id_first);
                    statement = con.createStatement();
                    ResultSet rsTicket2 = statement.executeQuery("select * from ticket where ticket_id = " + ticket_id_second);

                    while (rsTicket.next() && rsTicket2.next())
                    {
                        ticket.setPassenger(passenger);
                        ticket.setTicket_id(rsTicket.getInt("ticket_id"));
                        ticket.setFlight(flight);
                        ticket.setPrice(rsTicket.getInt("price") + rsTicket2.getInt("price"));
                        ticket.setClassVip(rsTicket.getBoolean("class_vip"));
                        ticket.setTicketStatus(true);

                        if (ticket.getClassVip() == true)
                        {
                            flight.airplane.setBusinessSitsNumber(flight.airplane.getBusinessSitsNumber() - 1);
                        } else
                        {
                            flight.airplane.setEconomySitsNumber(flight.airplane.getEconomySitsNumber() - 1);
                        }
                        System.out.println("--*-*-");
                        statement.executeUpdate("update ticket set status ='true' where ticket_id =" + ticket_id_first + " or ticket_id = " + ticket_id_second);
                        System.out.println("--*-*-");
                    }
                    statement = con.createStatement();
                    ResultSet price = statement.executeQuery("select * from ticket where ticket_id=" + ticket_id_first);
                    statement = con.createStatement();
                    ResultSet price2 = statement.executeQuery("select * from ticket where ticket_id=" + ticket_id_second);

                    if (price.next() && price2.next())
                    {
                        ticket.setPrice(price.getInt("price") + price2.getInt("price"));
                    }
                    System.out.println("Your bill: " + ticket.getPrice() + "\n");

                    System.out.println("Enter your card number:");
                    String cardNumber = "";
                    String pCardNum = "[0-9]{16}$";
                    checkMismatch((T) cardNumber, pCardNum);
                    passenger.setCardNumber(cardNumber);

                    System.out.println("Enter your security code:");
                    Integer securityCode = 0;
                    String pSecurity = "[0-9]{3}$";
                    checkMismatch((T) securityCode, pSecurity);
                    passenger.setSecurityCode(securityCode);

                }
            } catch (PatternSyntaxException patternException)
            {
                patternException.printStackTrace();
            }
        }

    }

}
