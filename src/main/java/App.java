import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.GregorianCalendar;

public class App {
    public static void main(String[] args){
        LocalDate test = LocalDate.now();
        LocalDate test2 = LocalDate.of(1971,1,1);
        System.out.println(test.until(test2).getYears());
        System.out.println(test.toString());
        System.out.println(test2.toString());
    }
}
