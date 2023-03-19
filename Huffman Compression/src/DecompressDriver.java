
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class DecompressDriver {
	private String compressedName, fileName;
	private String decompressedName;
	private HashMap<String, Character> decodeMap;
	
	public DecompressDriver() {
		Scanner input = new Scanner(System.in);
		System.out.println("File to decompress? Do not include '_compressed'.");
		fileName = input.next();
		compressedName = fileName+"_compressed";
		decompressedName = fileName + "_decompressed";
		input.close();
		decodeMap = new HashMap<String,Character>();
		buildMap();
		decompress();
	}
	
	private void buildMap() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName + "_tree"));
			for (int i = reader.read(); i != -1; i = reader.read()) {
				char c = (char)i;
				String code = "";
				i = reader.read();
				while (i != -1 && i != 0) {
					code += (char)i;
					i = reader.read();
				}
				decodeMap.put(code,c);
			}
			reader.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void buildMap2() {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName + "_tree"));
			for (int i = reader.read(); i != -1; i = reader.read()) {
				char c = (char)i;
				String code = "";
				i = reader.read();
				while (i != -1 && i != 0) {
					code += (char)i;
					i = reader.read();
				}
				decodeMap.put(code,c);
			}
			reader.close();
			
			int max = 0;
			for (char c : decodeMap.values())
				if (c > max)
					max = c;
			System.out.print(max);
				
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	private void decompress() {
		
		try {
			BufferedBitReader reader = new BufferedBitReader(compressedName);
			BufferedWriter writer = new BufferedWriter(new FileWriter(decompressedName));

			String code = "";
			while (reader.hasNext()) {
				if (reader.readBit())
					code += '1';
				else
					code += '0';
				if (decodeMap.containsKey(code)) {
					char output = decodeMap.get(code);
					writer.write(output);
					code = "";
				}
			}
			reader.close();
			
			
			writer.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	public static void main(String[] args) {
		DecompressDriver driver = new DecompressDriver();
	}
}
