/**
 * 
 */
package sdes;

/**
 * @author Frank Navarrrete
 *
 */
public class SDES {

	/**
	 * @param args - No command line arguments need to be passed in. 
	 */
	public static void main(String[] args) {
		final byte[] KEY        = {0,0,0,0,0,0,0,0,0,0};
		final byte[] PLAIN_TEXT = {1,0,1,0,1,0,1,0};
		
		final byte[] CIPHER_TEXT = Encrypt(KEY, PLAIN_TEXT);
		System.out.println(CIPHER_TEXT);
	}
	/**
	 * 
	 * @param rawkey - Initial Key
	 * @param plaintext - Text/Data to be encrypted
	 * @return byte[]
	 */
	public static byte[] Encrypt(byte[] rawkey, byte[] plaintext){
		
		return null;
	}
	/**
	 * 
	 * @param rawkey - Initial Key
	 * @param ciphertext - Text/Data to be decrypted
	 * @return byte[]
	 */
	public static byte[] Decrypt(byte[] rawkey, byte[] ciphertext){
		
		return null;
	}

}
