/**
 * 
 */
package sdes;


/**
 * @author Frank Navarrrete
 *
 */
public class SDES {

	public static void main(String[] args) {
		final byte[] KEY		= {1,1,1,0,0,0,1,1,1,0},
				     PLAIN_TEXT	= {0,1,0,1,0,1,0,1};
		final byte[] CIPHER_TEXT = inversePermutation(Encrypt(KEY, PLAIN_TEXT).clone());
		System.out.println("Key    Text: " + toString(KEY));
		System.out.println("Plain  Text: " + toString(PLAIN_TEXT));
		System.out.println("Cipher Text: " + toString(CIPHER_TEXT));
		
//		final byte[] testKEY = {1,0,1,0,0,0,0,0,1,0};
//		final byte[] testPLAIN_TEXT = {};
//		final byte[] testCIPHER_TEXT = inversePermutation(Encrypt(testKEY, testPLAIN_TEXT).clone()).clone();

	}
	public static byte[] Encrypt(byte[] rawkey, byte[] plaintext){
		final byte[][] KEYS = generateKeys(rawkey);
		byte[] cipherText   = encryptionRound(initialPermutation(plaintext), KEYS, 1, 2);
		
		return cipherText;
	}
	public static byte[] encryptionRound(byte[] text, byte[][]keys, int round, int rounds) {
		byte[] ciphertextLeft  = new byte[4],
			   ciphertextRight = new byte[4],
			   P4  = new byte[4],
			   Output = new byte[4];
		
		for(int i = 0; i < (text.length/2); i++){
			ciphertextLeft[i] = text[i];
		}
		for(int i = 4; i < text.length; i++){
			ciphertextRight[i - 4] = text[i];
		}

		P4 = functionX(ciphertextRight.clone(), keys[round - 1].clone());
		
		System.out.println("This is P4 " + round + ": " + toString(P4));
		System.out.println("This is ciphertextLeft " + round + ": " + toString(ciphertextLeft));

		
		for(int i = 0; i < 4; i++){
			Output[i] = (byte) (ciphertextLeft[i]^P4[i]);
		}		

		if(round == rounds){
			return combine(Output.clone(), ciphertextRight.clone()).clone();
		} else {
			byte[] ciphertext = combine(ciphertextRight.clone(),Output.clone());
			return encryptionRound(ciphertext.clone(), keys, ++round, rounds).clone();
		}
	}
	public static byte[] combine(byte[] ciphertextLeft, byte[] ciphertextRight) {
		byte[] combinedOutput = new byte[8];
		for (int i = 0; i < 4; i++){
			combinedOutput[i] = ciphertextLeft[i];
		}
		for (int i = 4; i < 8; i++){
			combinedOutput[i] = ciphertextRight[i - 4];	
		}
		return combinedOutput.clone();
	}
	public static byte[] functionX(byte[] ciphertextRight, byte[] key) {
		final byte[] EP = {4,1,2,3,2,3,4,1};
		byte[] expandedCipher = new byte[8],
			   xorLeft		  = new byte[4],
			   xorRight 	  = new byte[4],
			   P4       	  = new byte[4];

		for(int i = 0; i < EP.length; i++){
			expandedCipher[i] = ciphertextRight[EP[i] - 1];
		}

		for(int i = 0; i < expandedCipher.length/2; i++){
			xorLeft[i] = (byte) (expandedCipher[i]^key[i]);
		}
		
		for(int i = 4; i < expandedCipher.length; i++){
			xorRight[i - 4] = (byte) (expandedCipher[i]^key[i]);
		}

		P4 = sbox(xorLeft, xorRight);

		return P4.clone();
	}
	public static byte[] sbox(byte[] xorLeft, byte[] xorRight) {
		int[][] s1 = {{1,0,3,2}, {3,2,1,0}, {0,2,1,3}, {3,1,3,2}},
				s2 = {{0,1,2,3}, {2,0,1,3}, {3,0,1,0}, {2,1,0,3}};
		int s1L = Integer.parseUnsignedInt("" +  xorLeft[0] + xorLeft[1], 2),
			s1R = Integer.parseUnsignedInt("" +  xorLeft[2] + xorLeft[3], 2),
			s2L = Integer.parseUnsignedInt("" +  xorRight[0] + xorRight[1], 2),
			s2R = Integer.parseUnsignedInt("" +  xorRight[2] + xorRight[3], 2);	
		byte[] sbox1 = new byte[2];
		byte[] sbox2 = new byte[2];
		byte[] P4    = new byte[4];
		
		String sBox1 = Integer.toBinaryString(s1[s1L][s1R]),
			   sBox2 = Integer.toBinaryString(s2[s2L][s2R]);
	 	
		while (sBox1.length() < 2){
			sBox1 = "0" + sBox1;
		}
		while (sBox2.length() < 2){
			sBox2 = "0" + sBox2;
		}
		
		sbox1[0] = toByte(sBox1.charAt(0));
		sbox1[1] = toByte(sBox1.charAt(1));
		sbox2[0] = toByte(sBox2.charAt(0));
		sbox2[1] = toByte(sBox2.charAt(1));
		
		for(int i = 0; i < 2; i++){
			P4[i] = sbox1[i];
		}
		for(int i = 2; i < 4; i++){
			P4[i] = sbox2[i - 2];
		}
		return p4Permutation(P4);
	}
	public static byte   toByte(char charAt) {
		if(charAt == '1'){
			return (byte) 1;
		} else {
			return (byte) 0;
		}
	}
	public static byte[] Decrypt(byte[] rawkey, byte[] ciphertext){
		
		return null;
	}
	public static byte[][] generateKeys(byte[] rawkey){
		final byte[] P10 = {3,5,2,7,4,10, 1,9,8,6},
     				 P8	 = {6,3,7,4,8,5 ,10,9};
		byte[] permutation10 = new byte[10],
			   permutation8	 = new byte[8];
		byte[][] keys = new byte[2][10];
		/*
		 * Initial Permution of key.
		 */
		for(int i = 0; i < permutation10.length; i++){
			permutation10[i] = rawkey[P10[i] - 1];
		}
		/*
		 * Shift Left one position
		 */
		byte temp = permutation10[0];
		for(int i = 0; i < (rawkey.length)/2 - 1; i++){
			permutation10[i] = permutation10[i+1];
		}
		permutation10[4] = temp;
		
		temp = permutation10[5];
		for(int i = 5; i < rawkey.length - 1; i++){
			permutation10[i] = permutation10[i+1];
		}
		permutation10[9] = temp;
		/*
		 * Permution and compression
		 */
		for(int i = 0; i < permutation8.length; i++){
			permutation8[i] = permutation10[P8[i] - 1];
		}
		/*
		 * Add Key 1 to array of keys
		 */
		keys[0] = permutation8.clone();
		
		/*
		 * Shift Left two positions
		 */
		temp  = permutation10[0];
		byte temp2 = permutation10[1];
		for(int i = 0; i < ((rawkey.length)/2) - 2; i++){
			permutation10[i] = permutation10[i+2];
		}
		permutation10[3] = temp;
		permutation10[4] = temp2;
		
		
		temp = permutation10[5];
		temp2 = permutation10[6];

		for(int i = 5; i < rawkey.length - 2; i++){
			permutation10[i] = permutation10[i+2];
		}
		permutation10[8] = temp;
		permutation10[9] = temp2;
		/*
		 * Permution and compression
		 */
		for(int i = 0; i < permutation8.length; i++){
			permutation8[i] = permutation10[P8[i] - 1];
		}
		keys[1] = permutation8.clone();
		return keys.clone();
	}
	@SuppressWarnings("unused")
	public static String toString(byte[] array){
		String string = "";
		for(int i = 0; i < array.length; i++){
			string += array[i];
		}
		return string;
	}
	public static byte[] initialPermutation(byte[] plaintext){
		final byte[] IP = {2,6,3,1,4,8,5,7};
		byte[] permutation = new byte[plaintext.length];
		for(int i = 0; i < plaintext.length; i++){
			permutation[i] = plaintext[IP[i] - 1];
		}
		return permutation.clone();
	}
	public static byte[] inversePermutation(byte[] text) {
		final byte[] IPinverse = {4,1,3,5,7,2,8,6};
		byte[] permutation = new byte[text.length];
		for(int i = 0; i < text.length; i++){
			permutation[i] = text[IPinverse[i] - 1];
		}
		return permutation.clone();
	}	
	public static byte[] p4Permutation(byte[] text) {
		final byte[] IPinverse = {2,4,3,1};
		byte[] permutation = new byte[text.length];
		for(int i = 0; i < text.length; i++){
			permutation[i] = text[IPinverse[i] - 1];
		}
		return permutation.clone();
	}
	
}
