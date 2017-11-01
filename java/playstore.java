import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import helpers.Base64;
import javax.crypto.Cipher;
class PlayStore
{
    public static void main(String[] args)
     {
        String encrypted = encryptString("leemail"+"\u0000"+"lepassword");
        System.out.println(encrypted);
     }

    public static PublicKey createKeyFromString(String str, byte[] bArr) {
      try {
          byte[] decode = Base64.decode(str, 0);
          int readInt = readInt(decode, 0);
          byte[] obj = new byte[readInt];          
          System.arraycopy(decode, 4, obj, 0, readInt);
          BigInteger bigInteger = new BigInteger(1, obj);
          int readInt2 = readInt(decode, readInt + 4);
          byte[] obj2 = new byte[readInt2];
          System.arraycopy(decode, readInt + 8, obj2, 0, readInt2);
          BigInteger bigInteger2 = new BigInteger(1, obj2);
          decode = MessageDigest.getInstance("SHA-1").digest(decode);
          bArr[0] = (byte) 0;
          System.arraycopy(decode, 0, bArr, 1, 4);
          return KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(bigInteger, bigInteger2));
      } catch (Throwable e) {
          throw new RuntimeException(e);
      }
    }

    public static String encryptString(String str) {
      int i = 0;
      String string = "AAAAgMom/1a/v0lblO2Ubrt60J2gcuXSljGFQXgcyZWveWLEwo6prwgi3iJIZdodyhKZQrNWp5nKJ3srRXcUW+F1BD3baEVGcmEgqaLZUNBjm057pKRI16kB0YppeGx5qIQ5QjKzsR8ETQbKLNWgRY0QRNVz34kMJR3P/LgHax/6rmf5AAAAAwEAAQ==";

      byte[] obj = new byte[5];
      Key createKeyFromString = createKeyFromString(string, obj);
      if (createKeyFromString == null) {
      	return null;
      }
      try {
      	Cipher instance = Cipher
      			.getInstance("RSA/ECB/OAEPWITHSHA1ANDMGF1PADDING");
      	byte[] bytes = str.getBytes("UTF-8");
      	int length = ((bytes.length - 1) / 86) + 1;
      	byte[] obj2 = new byte[(length * 133)];
      	while (i < length) {
      		instance.init(1, createKeyFromString);
      		byte[] doFinal = instance.doFinal(bytes, i * 86, i == length
      				+ -1 ? bytes.length - (i * 86) : 86);
      		System.arraycopy(obj, 0, obj2, i * 133, obj.length);
      		System.arraycopy(doFinal, 0, obj2, (i * 133) + obj.length,
      				doFinal.length);
      		i++;
      	}
      	return Base64.encodeToString(obj2, 10);
      } catch (Throwable e) {
      	throw new RuntimeException(e);
      }
    }

    public static void printObj(byte[] obj, String msgstart, String msgend) {
        System.out.println(msgstart);
        int i=0;
        while( i < obj.length ) {
          System.out.println(obj[i]);
          i++;
        }
        System.out.println(msgend);
    }

    private static int readInt(byte[] bArr, int i) {
  		return (((((bArr[i] & 255) << 24) | 0) | ((bArr[i + 1] & 255) << 16)) | ((bArr[i + 2] & 255) << 8))
  				| (bArr[i + 3] & 255);
	  }
}
