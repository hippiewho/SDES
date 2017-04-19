/**
 * 
 */
package MAIN;

/**
 * @author Frank Navarrrete
 *
 */
public class TripleDES extends SDES {

	byte[] KEY1 = new byte[10], 
		   KEY2 = new byte[10], 
		   PLAIN_TEXT = new byte[8], 
		   CIPHER_TEXT = new byte[8];

	public byte[] encodeCharacter(byte[] k1, byte[] k2, byte[] pt) {
		this.KEY1 = k1.clone();
		this.KEY2 = k2.clone();
		this.PLAIN_TEXT = pt.clone();
		this.CIPHER_TEXT = Decrypt(KEY1, KEY2, PLAIN_TEXT);
		return CIPHER_TEXT;
	}

	public byte[] decodeCharacter(byte[] k1, byte[] k2, byte[] ct) {
		this.KEY1 = k1.clone();
		this.KEY2 = k2.clone();
		this.CIPHER_TEXT = ct.clone();
		this.PLAIN_TEXT = Decrypt(KEY1, KEY2, CIPHER_TEXT);
		return PLAIN_TEXT;
	}

	public byte[] Encrypt(byte[] rawkey1, byte[] rawkey2, byte[] plaintext) {
		// E3DES(p) = EDES(k1,DDES(k2,EDES(k1, p)))
		return EncryptHelper(rawkey1, DecryptHelper(rawkey2, EncryptHelper(rawkey1, plaintext)));
	}

	public byte[] Decrypt(byte[] rawkey1, byte[] rawkey2, byte[] ciphertext) {
		// D3DES(c) = DDES(k1,EDES(k2,DDES(k1, c)))
		return DecryptHelper(rawkey1, EncryptHelper(rawkey2, DecryptHelper(rawkey1, ciphertext)));
	}

	public byte[] EncryptHelper(byte[] rawkey, byte[] plaintext) {
		final byte[][] KEYS = generateKeys(rawkey);
		byte[] cipherText = encryptionRound(initialPermutation(plaintext), KEYS, 1, 2);

		return inversePermutation(cipherText);
	}

	public byte[] DecryptHelper(byte[] rawkey, byte[] ciphertext) {
		byte[][] KEYS = generateKeys(rawkey);
		byte[] key1 = KEYS[1];
		byte[] key2 = KEYS[0];
		KEYS[0] = key1;
		KEYS[1] = key2;
		byte[] plaintext = encryptionRound(initialPermutation(ciphertext), KEYS, 1, 2);

		return inversePermutation(plaintext);
	}
}
