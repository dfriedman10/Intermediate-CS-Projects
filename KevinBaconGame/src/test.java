import java.util.function.Function;

public class test {
	
	public  String hello(String x) {
		return x + " hello";
	}

	
	public static void main(String[] args) {
		
		Function<String, String> h = new test()::hello;
		
		System.out.println(h.apply("h"));
		
		
	
	}
	}
