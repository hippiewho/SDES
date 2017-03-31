package MAIN;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import THREEDES.THREEDES;
import support.CASCII;
import sdes.SDES;

public class Driver {

	private final static String FILENAME = "./bin/support/msg1.txt";
	private static ArrayList<byte[]> byteList = new ArrayList<byte[]>();

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Press 1 to encode a word using SDES.\n" 
				+ "Press 2 to bruteforce a file encoded in SDED.\n"
				+ "Press 3 to bruteforce a file encoded in TripleDES.\n"
				+ "Press 4 to encrypt/decrypt a byte array using SDES.\n"
				+ "Press 5 to encrypt/decrypt a byte array using TripleDES.\n");
		int choice = scanner.nextInt();
		if (choice == 1) {
			encodeWordSDES(scanner);
		} else if (choice == 2) {

		} else if (choice == 3) {

		} else if (choice == 4) {
			// SDES
			SDES(scanner);
		} else if (choice == 5) {
			// TripleDES
			TDES(scanner);
		}
		scanner.close();
	}

	private static void encodeWordSDES(Scanner scanner) {
		String encryptedWord = "";
		System.out.println("Type in word to encode using key 0111001101. Q1 Part 3.");
		String word = scanner.next();
		word = word.toUpperCase();

		byte[] key = new byte[] { 0, 1, 1, 1, 0, 0, 1, 1, 0, 1 };
		SDES sdes = new SDES();
		byte[] wordByte = CASCII.Convert(word);

		for (int i = 0; i < wordByte.length; i += 8) {
			byte[] temp = new byte[8];
			temp[0] = wordByte[i];
			temp[1] = wordByte[i + 1];
			temp[2] = wordByte[i + 2];
			temp[3] = wordByte[i + 3];
			temp[4] = wordByte[i + 4];
			temp[4] = wordByte[i + 5];
			temp[4] = wordByte[i + 6];
			temp[4] = wordByte[i + 7];

			encryptedWord += BytetoString(sdes.encodeCharacter(key, temp));
			temp = null;
		}
		System.out.println(encryptedWord);

	}

	private void bruteSDES() {
		BufferedReader bufferedReader;
		FileReader fileReader;

		try {
			fileReader = new FileReader(FILENAME);
			bufferedReader = new BufferedReader(fileReader);
			String line;

			while ((line = bufferedReader.readLine()) != null) {
				int lngth = line.length();
				for (int i = 0; i < lngth; i += 5) {
					byte[] byteArray = new byte[8];
					byteArray[0] = toByte(line.charAt(i));
					byteArray[1] = toByte(line.charAt(i + 1));
					byteArray[2] = toByte(line.charAt(i + 2));
					byteArray[3] = toByte(line.charAt(i + 3));
					byteArray[4] = toByte(line.charAt(i + 4));
					byteArray[5] = 0;
					byteArray[6] = 0;
					byteArray[7] = 0;
					byteList.add(byteArray.clone());
					byteArray = null;
				}
			}

			fileReader.close();
			bufferedReader.close();
			Iterator<byte[]> byteListIt = byteList.iterator();

			while (byteListIt.hasNext()) {

			}
		} catch (Exception e) {
			System.out.println("Error Occured: " + e.toString());
		} finally {
			System.out.print("byteList size: " + byteList.size());

		}
	}

	private void bruteTDES() {

	}
	
	private static void SDES(Scanner scanner){
		System.out.println("Press 1 to encrypt.\n" + "Press 2 to decrypt.\n");
		int choice = scanner.nextInt();
		if (choice == 1) {
			byte[] key = new byte[10];
			byte[] plaintext = new byte[8];
			System.out.println("Enter Key: \n"
					+ "Enter either 0 or 1 followed by enter. The Key is 10 bytes Long.");
			for(int i = 0; i < 10; i++){
				int byteInt = scanner.nextInt();
				
				while(byteInt < 0 || byteInt > 1){
					System.out.println("Enter only 1 or 0.\n");
					byteInt = scanner.nextInt();
				}
				key[i] = (byte) byteInt;
			}
			System.out.println("Enter Plain Text byte Array: \n"
					+ "Enter either 0 or 1 followed by enter. The Text is 8 bytes Long.");
			for(int i = 0; i < 8; i++){
				int byteInt = scanner.nextInt();
				
				while(byteInt < 0 || byteInt > 1){
					System.out.println("Enter only 1 or 0.\n");
					byteInt = scanner.nextInt();
				}
				plaintext[i] = (byte) byteInt;
			}
			SDES sdes = new SDES();
			System.out.println("The CipherText is " + BytetoString(sdes.Encrypt(key, plaintext)));
		} else if (choice == 2) {
			byte[] key = new byte[10];
			byte[] ciphertext = new byte[8];
			System.out.println("Enter Key: \n"
					+ "Enter either 0 or 1 followed by enter. The Key is 10 bytes Long.");
			for(int i = 0; i < 10; i++){
				int byteInt = scanner.nextInt();
				
				while(byteInt < 0 || byteInt > 1){
					System.out.println("Enter only 1 or 0.\n");
					byteInt = scanner.nextInt();
				}
				key[i] = (byte) byteInt;
			}
			System.out.println("Enter Cipher Text byte Array: \n"
					+ "Enter either 0 or 1 followed by enter. The Text is 8 bytes Long.");
			for(int i = 0; i < 8; i++){
				int byteInt = scanner.nextInt();
				
				while(byteInt < 0 || byteInt > 1){
					System.out.println("Enter only 1 or 0.\n");
					byteInt = scanner.nextInt();
				}
				ciphertext[i] = (byte) byteInt;
			}
			SDES sdes = new SDES();
			System.out.println("The CipherText is " + BytetoString(sdes.Decrypt(key, ciphertext)));
		}
	}

	private static void TDES(Scanner scanner){ 
		System.out.println("Press 1 to encrypt.\n" + "Press 2 to decrypt.\n");
		int choice = scanner.nextInt();
		if (choice == 1) {
			byte[] key1 = new byte[10];
			byte[] key2 = new byte[10];
			byte[] plaintext = new byte[8];
			System.out.println("Enter Key1: \n"
					+ "Enter either 0 or 1 followed by enter. The Key is 10 bytes Long.");
			for(int i = 0; i < 10; i++){
				int byteInt = scanner.nextInt();
				
				while(byteInt < 0 || byteInt > 1){
					System.out.println("Enter only 1 or 0.\n");
					byteInt = scanner.nextInt();
				}
				key1[i] = (byte) byteInt;
			}
			System.out.println("Enter Key2: \n"
					+ "Enter either 0 or 1 followed by enter. The Key is 10 bytes Long.");
			for(int i = 0; i < 10; i++){
				int byteInt = scanner.nextInt();
				
				while(byteInt < 0 || byteInt > 1){
					System.out.println("Enter only 1 or 0.\n");
					byteInt = scanner.nextInt();
				}
				key2[i] = (byte) byteInt;
			}
			System.out.println("Enter Plain Text byte Array: \n"
					+ "Enter either 0 or 1 followed by enter. The Text is 8 bytes Long.");
			for(int i = 0; i < 8; i++){
				int byteInt = scanner.nextInt();
				
				while(byteInt < 0 || byteInt > 1){
					System.out.println("Enter only 1 or 0.\n");
					byteInt = scanner.nextInt();
				}
				plaintext[i] = (byte) byteInt;
			}
			THREEDES tdes = new THREEDES();
			System.out.println("The CipherText is " + BytetoString(tdes.Encrypt(key1, key2, plaintext)));
		} else if (choice == 2) {
			byte[] key1 = new byte[10];
			byte[] key2 = new byte[10];
			byte[] plaintext = new byte[8];
			System.out.println("Enter Key1: \n"
					+ "Enter either 0 or 1 followed by enter. The Key is 10 bytes Long.");
			for(int i = 0; i < 10; i++){
				int byteInt = scanner.nextInt();
				
				while(byteInt < 0 || byteInt > 1){
					System.out.println("Enter only 1 or 0.\n");
					byteInt = scanner.nextInt();
				}
				key1[i] = (byte) byteInt;
			}
			System.out.println("Enter Key2: \n"
					+ "Enter either 0 or 1 followed by enter. The Key is 10 bytes Long.");
			for(int i = 0; i < 10; i++){
				int byteInt = scanner.nextInt();
				
				while(byteInt < 0 || byteInt > 1){
					System.out.println("Enter only 1 or 0.\n");
					byteInt = scanner.nextInt();
				}
				key2[i] = (byte) byteInt;
			}
			System.out.println("Enter Cipher Text byte Array: \n"
					+ "Enter either 0 or 1 followed by enter. The Text is 8 bytes Long.");
			for(int i = 0; i < 8; i++){
				int byteInt = scanner.nextInt();
				
				while(byteInt < 0 || byteInt > 1){
					System.out.println("Enter only 1 or 0.\n");
					byteInt = scanner.nextInt();
				}
				plaintext[i] = (byte) byteInt;
			}
			THREEDES tdes = new THREEDES();
			System.out.println("The CipherText is " + BytetoString(tdes.Decrypt(key1, key2, plaintext)));
		}
	}

	public static byte toByte(char CHAR) {
		if (CHAR == '1') {
			return (byte) 1;
		} else {
			return (byte) 0;
		}
	}

	public static String BytetoString(byte[] array) {
		String string = "";
		for (int i = 0; i < array.length; i++) {
			string += array[i];
		}
		return string;
	}

}
