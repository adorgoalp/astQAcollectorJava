import java.util.regex.Pattern;

public class Main {
	public static void main(String[] args) {
		Thread t = new Thread(new CollectorAST());
		t.start();
//		System.out.println(Pattern.matches("\\(+\\d*+\\)", "(100)"));
	}
}
