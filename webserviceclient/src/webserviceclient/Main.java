package webserviceclient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import com.Mlh.CallWebServiceImpService;

public class Main {

	public static void main(String[] args) {

		try {
			File fi = new File("C:\\Users\\Admin\\Desktop\\fotograf.jpg");
			byte[] fileContent = Files.readAllBytes(fi.toPath());
			System.out.println(fi.getName());

			CallWebServiceImpService call = new CallWebServiceImpService();
			if (call.getCallWebServiceImpPort().checkUserImage("12345678904", fileContent) == 1)
				System.out.println("System successful");
			else
				System.out.println("System failed");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
