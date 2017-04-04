package MAIN;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class Driver {

	private final static String FILENAME_SDES = "./bin/support/msg1.txt";
	private final static String FILENAME_TDES = "./bin/support/msg2.txt";

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		while(true){
			System.out.println(
					  "Press 1 to Output answers to Project Part 1 and 2.\n"
					+ "Press 2 to encode a word using SDES. Project Part 3 - 1\n"
					+ "Press 3 to bruteforce a file encoded in SDES. Project Part 3 - 2\n"
					+ "Press 4 to bruteforce a file encoded in TripleDES. Project Part 3 - 3\n"
					+ "Press 5 to encrypt/decrypt a byte array using SDES.\n"
					+ "Press 6 to encrypt/decrypt a byte array using TripleDES.\n"
					+ "To exit press 0."
					);
			int choice = scanner.nextInt();
			if (choice == 1) {
				partOneAndTwo();
			} else if (choice == 2) {
				encodeWordSDES(scanner);
			} else if (choice == 3) {
				bruteSDES();
			} else if (choice == 4) { 
				bruteTDES();
			} else if (choice == 5) {
				// SDES
				SDES(scanner);
			} else if (choice == 6) {
				// TripleDES
				TDES(scanner);
			} else if (choice == 0){
				System.out.println("Program Ended.");
				break;
			}
		}
		scanner.close();

	}

	private static void encodeWordSDES(Scanner scanner) {
		String encryptedWord = "";
		System.out.println("Type in word to encode using key 0111001101. Q1 Part 3.\n"
				+ "Word made CASCII compliant. No Numbers.");
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
		System.out.println("Byte Array after Encryption: " + encryptedWord);

	}

	private static void bruteSDES() {
		BufferedReader bufferedReader;
		FileReader fileReader;

		try {
			fileReader = new FileReader(FILENAME_SDES);
			bufferedReader = new BufferedReader(fileReader);
			String line;

			byte[] byteList = null;
			
			while ((line = bufferedReader.readLine()) != null) {
				int lngth = line.length();
				byteList = new byte[lngth];
				for (int i = 0; i < lngth; i++) {
					byteList[i] = toByte(line.charAt(i));
				}
			}
			byte[][] keySet = generateKeys();

			fileReader.close();
			bufferedReader.close();
			String probablePhrase = "";
			String probableKey = "";
			int MaxNumOfE = 0;
			SDES sdes = new SDES();
			byte[] phraseByte = new byte[byteList.length];
			int phrasebyteindex = 0;
			for (int j = 0; j < keySet.length; j++){

				for(int i = 0; i < byteList.length; i += 8){
					byte[] temp = new byte[8];
					temp[0] = byteList[i];
					temp[1] = byteList[i+1];
					temp[2] = byteList[i+2];
					temp[3] = byteList[i+3];
					temp[4] = byteList[i+4];
					temp[5] = byteList[i+5];
					temp[6] = byteList[i+6];
					temp[7] = byteList[i+7];
					
					temp = sdes.Decrypt(keySet[j], temp);
					
					for(int k = 0; k < 8; k++){
						phraseByte[phrasebyteindex] = temp[k];
						phrasebyteindex++;
					}
					temp = null;
					
				}
				String phrase = CASCII.toString(phraseByte);
				int numofe = 0;
				for(int a = 0; a < phrase.length(); a++ ){
					if(phrase.charAt(a) == 'E'){
						numofe++;
					}
				}
				if(numofe > MaxNumOfE){
					MaxNumOfE = numofe;
					probablePhrase = phrase;
					probableKey = BytetoString(keySet[j]);
				}
				phrasebyteindex = 0;
			}
			System.out.println("Key :" + probableKey + "\nPhrase: " + probablePhrase);
		} catch (Exception e) {
			System.out.println("Error Occured: " + e.toString());
		} finally {
			
		}
	}

	private static void bruteTDES() {
		BufferedReader bufferedReader;
		FileReader fileReader;

		try {
			fileReader = new FileReader(FILENAME_TDES);
			bufferedReader = new BufferedReader(fileReader);
			String line;

			byte[] byteList = null;
			
			while ((line = bufferedReader.readLine()) != null) {
				int lngth = line.length();
				byteList = new byte[lngth];
				for (int i = 0; i < lngth; i++) {
					byteList[i] = toByte(line.charAt(i));
				}
			}
			byte[][] keySet1 = generateKeys();
			byte[][] keySet2 = keySet1.clone();

			fileReader.close();
			bufferedReader.close();

			String probablePhrase = "",
				   probableKey1 = "",
				   probableKey2 = "";
			int MaxNumOfE = 0;
			
			TripleDES tdes = new TripleDES();
			byte[] phraseByte = new byte[byteList.length];
			int phrasebyteindex = 0;
			for (int j = 0; j < keySet1.length; j++){
				for (int l = 0; l < keySet2.length; l++){

					for(int i = 0; i < byteList.length; i += 8){
						byte[] temp = new byte[8];
						temp[0] = byteList[i];
						temp[1] = byteList[i+1];
						temp[2] = byteList[i+2];
						temp[3] = byteList[i+3];
						temp[4] = byteList[i+4];
						temp[5] = byteList[i+5];
						temp[6] = byteList[i+6];
						temp[7] = byteList[i+7];

						temp = tdes.Decrypt(keySet1[j],keySet2[l], temp);

						for(int k = 0; k < 8; k++){
							phraseByte[phrasebyteindex] = temp[k];
							phrasebyteindex++;
						}
						temp = null;
					}
					String phrase = CASCII.toString(phraseByte);
					int numofe = 0;
					for(int a = 0; a < phrase.length(); a++ ){
						if(phrase.charAt(a) == 'E'){
							numofe++;
						}
					}
					if(numofe > MaxNumOfE){
						MaxNumOfE = numofe;
						probablePhrase = phrase;
						probableKey1 = BytetoString(keySet1[j]);
						probableKey2 = BytetoString(keySet2[l]);

					}
					phrasebyteindex = 0;
				}
			}
			System.out.println("Keys : " + probableKey1 + " " + probableKey2 + " Phrase: " + probablePhrase);
		} catch (Exception e) {
			System.out.println("Error Occured: " + e.toString());
		} finally {

		}
	}

	private static void SDES(Scanner scanner){
		System.out.println("Press 1 to encrypt.\n" + "Press 2 to decrypt.");
		int choice = scanner.nextInt();

		if (choice == 1) {
			byte[] key = new byte[10];
			byte[] plaintext = new byte[8];
			System.out.println("Enter Key: \n"
					+ "Enter either 0 or 1 followed by enter. The Key is 10 bytes Long.");
			String byteInt = scanner.next();

			while(byteInt.length() < 0 || byteInt.length() > 10 || !(isBinary(byteInt))){
				System.out.println("Keys are only 10 integers long and Must only be 0s or 1s.");
				byteInt = scanner.next();

			}
			key = StringToByte(byteInt);

			System.out.println("Enter Plain Text byte Array: \n"
					+ "Enter either 0 or 1 followed by enter. The Text is 8 bytes Long.");
			byteInt = scanner.next();

			while(byteInt.length() < 0 || byteInt.length() > 10 || !(isBinary(byteInt))){
				System.out.println("Enter only 1 or 0.\n");
				byteInt = scanner.next();
			}
			plaintext = StringToByte(byteInt);

			SDES sdes = new SDES();
			System.out.println("The CipherText is " + BytetoString(sdes.Encrypt(key, plaintext)));
		} else if (choice == 2) {
			byte[] key = new byte[10];
			byte[] ciphertext = new byte[8];
			System.out.println("Enter Key: \n"
					+ "Enter either 0 or 1 followed by enter. The Key is 10 bytes Long.");
			String byteInt = scanner.next();

			while(byteInt.length() < 0 || byteInt.length() > 10 || !(isBinary(byteInt))){
				System.out.println("Enter only 1 or 0.\n");
				byteInt = scanner.next();
			}
			key = StringToByte(byteInt);

			System.out.println("Enter Cipher Text byte Array: \n"
					+ "Enter either 0 or 1 followed by enter. The Text is 8 bytes Long.");
			for(int i = 0; i < 8; i++){
				byteInt = scanner.next();

				while(byteInt.length() < 0 || byteInt.length() > 10 || !(isBinary(byteInt))){
					System.out.println("Enter only 1 or 0.");
					byteInt = scanner.next();
				}
				ciphertext = StringToByte(byteInt);
			}
			SDES sdes = new SDES();
			System.out.println("The Plain Text is " + BytetoString(sdes.Decrypt(key, ciphertext)));
		}
	}

	private static void TDES(Scanner scanner){ 
		System.out.println("Press 1 to encrypt.\n" + "Press 2 to decrypt.");
		int choice = scanner.nextInt();
		if (choice == 1) {
			byte[] key1 = new byte[10];
			byte[] key2 = new byte[10];
			byte[] plaintext = new byte[8];
			System.out.println("Enter Key1: \n"
					+ "Enter either 0 or 1 followed by enter. The Key is 10 bytes Long.");

			String byteInt = scanner.next();

			while(byteInt.length() < 0 || byteInt.length() > 10 || !(isBinary(byteInt))){
				System.out.println("Enter only 1 or 0.");
				byteInt = scanner.next();
			}
			key1 = StringToByte(byteInt);

			System.out.println("Enter Key2: \n"
					+ "Enter either 0 or 1 followed by enter. The Key is 10 bytes Long.");
			byteInt = scanner.next();

			while(byteInt.length() < 0 || byteInt.length() > 10 || !(isBinary(byteInt))){
				System.out.println("Enter only 1 or 0.");
				byteInt = scanner.next();
			}
			key2 = StringToByte(byteInt);

			System.out.println("Enter Plain Text byte Array: \n"
					+ "Enter either 0 or 1 followed by enter. The Text is 8 bytes Long.");
			byteInt = scanner.next();

			while(byteInt.length() < 0 || byteInt.length() > 10 || !(isBinary(byteInt))){
				System.out.println("Enter only 1 or 0.");
				byteInt = scanner.next();
			}
			plaintext = StringToByte(byteInt);

			TripleDES tdes = new TripleDES();
			System.out.println("The CipherText is " + BytetoString(tdes.Encrypt(key1, key2, plaintext)));
		} else if (choice == 2) {
			byte[] key1 = new byte[10];
			byte[] key2 = new byte[10];
			byte[] plaintext = new byte[8];
			System.out.println("Enter Key1: \n"
					+ "Enter either 0 or 1 followed by enter. The Key is 10 bytes Long.");
			String byteInt = scanner.next();

			while(byteInt.length() < 0 || byteInt.length() > 10 || !(isBinary(byteInt))){
				System.out.println("Enter only 1 or 0.");
				byteInt = scanner.next();
			}
			key1 = StringToByte( byteInt);

			System.out.println("Enter Key2: \n"
					+ "Enter either 0 or 1 followed by enter. The Key is 10 bytes Long.");
			byteInt = scanner.next();

			while(byteInt.length() < 0 || byteInt.length() > 10 || !(isBinary(byteInt))){
				System.out.println("Enter only 1 or 0.");
				byteInt = scanner.next();
			}
			key2 = StringToByte(byteInt);

			System.out.println("Enter Cipher Text byte Array: \n"
					+ "Enter either 0 or 1 followed by enter. The Text is 8 bytes Long.");
			byteInt = scanner.next();

			while(byteInt.length() < 0 || byteInt.length() > 10 || !(isBinary(byteInt))){
				System.out.println("Enter only 1 or 0.");
				byteInt = scanner.next();
			}
			plaintext = StringToByte(byteInt);

			TripleDES tdes = new TripleDES();
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

	public static byte[][] generateKeys(){
		int size = (int) Math.pow(2.0, 10.0);
		byte[][] keys = new byte[size][10];
		for(int i = 0; i < size; i++){
			String binVal = IntegerToBinaryString(i);
			for(int j = 0; j < binVal.length(); j++){
				keys[i][j] = toByte(binVal.charAt(j));
			}
		}

		return keys;
	}

	public static boolean isBinary(String s){
		boolean isBinary = true;
		for(int i = 0; i < s.length(); i++){
			if(s.charAt(i) != '0' && s.charAt(i) != '1'){
				isBinary = false;
				break;
			}
		}

		return isBinary;
	}

	public static byte[] StringToByte(String s){
		byte[] byteArray = new byte[s.length()];

		for ( int i = 0; i < s.length(); i++ ){
			byteArray[i] = toByte(s.charAt(i));
		}

		return byteArray;
	}

	public static void partOneAndTwo(){
		SDES sdes = new SDES();
		TripleDES tdes = new TripleDES();
		byte[][] keysSDES = new byte[][]{
			{0,0,0,0,0,0,0,0,0,0},
			{1,1,1,1,1,1,1,1,1,1},
			{0,0,0,0,0,1,1,1,1,1},
			{0,0,0,0,0,1,1,1,1,1},
			{1,0,0,0,1,0,1,1,1,0},
			{1,0,0,0,1,0,1,1,1,0},
			{0,0,1,0,0,1,1,1,1,1},
			{0,0,1,0,0,1,1,1,1,1}
		},
				plaintextSDES = new byte[][]{
			{0,0,0,0,0,0,0,0},
			{1,1,1,1,1,1,1,1},
			{0,0,0,0,0,0,0,0},
			{1,1,1,1,1,1,1,1}
		},
				ciphertextSDES = new byte[][]{
			{0,0,0,1,1,1,0,0},
			{1,1,0,0,0,0,1,0},
			{1,0,0,1,1,1,0,1},
			{1,0,0,1,0,0,0,0}
		},
				keys1TDES = new byte[][]{
			{0,0,0,0,0,0,0,0,0,0},
			{1,0,0,0,1,0,1,1,1,0},
			{1,0,0,0,1,0,1,1,1,0},
			{1,1,1,1,1,1,1,1,1,1},
			{1,0,0,0,1,0,1,1,1,0},
			{1,0,1,1,1,0,1,1,1,1},
			{0,0,0,0,0,0,0,0,0,0},
			{1,1,1,1,1,1,1,1,1,1}
		}, 				
				keys2TDES = new byte[][]{
			{0,0,0,0,0,0,0,0,0,0},
			{0,1,1,0,1,0,1,1,1,0},
			{0,1,1,0,1,0,1,1,1,0},
			{1,1,1,1,1,1,1,1,1,1},
			{0,1,1,0,1,0,1,1,1,0},
			{0,1,1,0,1,0,1,1,1,0},
			{0,0,0,0,0,0,0,0,0,0},
			{1,1,1,1,1,1,1,1,1,1}
		}, 				
				plaintextTDES = new byte[][]{
			{0,0,0,0,0,0,0,0},
			{1,1,0,1,0,1,1,1},
			{1,0,1,0,1,0,1,0},
			{1,0,1,0,1,0,1,0}
		}, 				
				ciphertextTDES = new byte[][]{
			{1,1,1,0,0,1,1,0},
			{0,1,0,1,0,0,0,0},
			{1,0,0,0,0,0,0,0},
			{1,0,0,1,0,0,1,0}
		};
		
		//SDES
		System.out.println("SDES:");
		System.out.println("#  " + "KEY:    " + "    " + "Plaintext:" + " " + "CipherText:" ); 

		for(int i = 0; i < 4; i++){
			System.out.println((i+1) + ") " + BytetoString(keysSDES[i]) + "  " + BytetoString(plaintextSDES[i]) + "   " + BytetoString(sdes.Encrypt(keysSDES[i], plaintextSDES[i])) ); 
		}
		
		for(int i = 4; i < 8; i++){
			System.out.println((i+1) + ") " + BytetoString(keysSDES[i]) + "  " + BytetoString(sdes.Decrypt(keysSDES[i], ciphertextSDES[i-4])) + "   " +  BytetoString(ciphertextSDES[i-4]) ); 
		}
		
//		System.out.println("1) " + BytetoString(keysSDES[0]) + " " + BytetoString(plaintextSDES[0]) + " " + BytetoString(sdes.Encrypt(keysSDES[0], plaintextSDES[0])) ); 
//		System.out.println("2) " + BytetoString(keysSDES[1]) + " " + BytetoString(sdes.Encrypt(keysSDES[1], plaintextSDES[1])) ); 
//		System.out.println("3) " + BytetoString(keysSDES[2]) + " " + BytetoString(sdes.Encrypt(keysSDES[2], plaintextSDES[2])) ); 
//		System.out.println("4) " + BytetoString(keysSDES[3]) + " " + BytetoString(sdes.Encrypt(keysSDES[3], plaintextSDES[3])) ); 
//		System.out.println("5) " + BytetoString(keysSDES[4]) + " " + BytetoString(sdes.Encrypt(keysSDES[4], ciphertextSDES[0])) ); 
//		System.out.println("6) " + BytetoString(keysSDES[5]) + " " + BytetoString(sdes.Encrypt(keysSDES[5], ciphertextSDES[1])) ); 
//		System.out.println("7) " + BytetoString(keysSDES[6]) + " " + BytetoString(sdes.Encrypt(keysSDES[6], ciphertextSDES[2])) ); 
//		System.out.println("8) " + BytetoString(keysSDES[7]) + " " + BytetoString(sdes.Encrypt(keysSDES[7], ciphertextSDES[3])) ); 
	
		//TDES
		System.out.println("\nTDES:");
		System.out.println("#  " + "KEY1:    " + "   " + "Key2:" + "        " + "Plaintext:" + " " + "CipherText:" ); 

		for(int i = 0; i < 4; i++){
			System.out.println((i+1) + ") " + BytetoString(keys1TDES[0]) + "  " + BytetoString(keys2TDES[0]) + "   " + BytetoString(plaintextTDES[0]) + "   " +BytetoString(tdes.Encrypt(keys2TDES[0], keys1TDES[0], plaintextTDES[0])) ); 
		}
		
		for(int i = 4; i < 8; i++){
			System.out.println((i+1) + ") " + BytetoString(keys1TDES[0]) + "  " + BytetoString(keys2TDES[0]) + "   " + BytetoString(tdes.Decrypt(keys2TDES[0], keys1TDES[0], ciphertextTDES[0])) + "   " +  BytetoString(ciphertextTDES[0]) ); 
		}
		
//		System.out.println("1) " + BytetoString(keys1TDES[0]) + " " + BytetoString(keys2TDES[0]) + " " + BytetoString(tdes.Encrypt(keys2TDES[0], keys1TDES[0], plaintextTDES[0])) ); 
//		System.out.println("2) " + BytetoString(keys1TDES[1]) + " " + BytetoString(keys2TDES[1]) + " " + BytetoString(tdes.Encrypt(keys1TDES[1], keys1TDES[1], plaintextTDES[1])) ); 
//		System.out.println("3) " + BytetoString(keys1TDES[2]) + " " + BytetoString(keys2TDES[2]) + " " + BytetoString(tdes.Encrypt(keys1TDES[2], keys1TDES[2], plaintextTDES[2])) ); 
//		System.out.println("4) " + BytetoString(keys1TDES[3]) + " " + BytetoString(keys2TDES[3]) + " " + BytetoString(tdes.Encrypt(keys1TDES[3], keys1TDES[3], plaintextTDES[3])) ); 
//		System.out.println("5) " + BytetoString(keys1TDES[4]) + " " + BytetoString(keys2TDES[4]) + " " + BytetoString(tdes.Encrypt(keys1TDES[4], keys1TDES[4], ciphertextTDES[0])) ); 
//		System.out.println("6) " + BytetoString(keys1TDES[5]) + " " + BytetoString(keys2TDES[5]) + " " + BytetoString(tdes.Encrypt(keys1TDES[5], keys1TDES[5], ciphertextTDES[1])) ); 
//		System.out.println("7) " + BytetoString(keys1TDES[6]) + " " + BytetoString(keys2TDES[6]) + " " + BytetoString(tdes.Encrypt(keys1TDES[6], keys1TDES[6], ciphertextTDES[2])) ); 
//		System.out.println("8) " + BytetoString(keys1TDES[7]) + " " + BytetoString(keys2TDES[7]) + " " + BytetoString(tdes.Encrypt(keys1TDES[7], keys1TDES[7], ciphertextTDES[3])) );
		System.out.println("");
	}
	
	public static String IntegerToBinaryString(int i){
		String output = Integer.toBinaryString(i);
		
		while(output.length() < 10){
			output = "0" + output;
		}
		return output;
	}
	
	
}

