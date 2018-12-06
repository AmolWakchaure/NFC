package sns.sn.systems.encryption;

import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.tech.Ndef;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.Security;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import sns.sn.systems.classes.M;

public class Encrypt
{
    public static String getStringFromDevice(Ndef ndef)
    {
        String dataString = null;
        byte MSG_DIRECTION_RESPONSE = 0x1;
        try
        {
            final byte MSG_ID_DEBUG = 0x59;
            byte[] command = new byte[]{MSG_ID_DEBUG, MSG_DIRECTION_RESPONSE};
            List<byte[]> response = writeReadNdef(command,ndef);
            dataString = M.bytesToHex(response.get(0));
        }
        catch (Exception e)
        {

        }
        return dataString;
    }

    public static List<byte[]> writeReadNdef(byte[] payload,Ndef ndef) {
        List<byte[]> responses = null;
        try {
            ndef.connect();
            try {
                writeNdef(payload,ndef);
                try { /* Give the NHS31xx some time to respond. */
                    //Thread.sleep(111);
                    Thread.sleep(250);
                } catch (InterruptedException e) {
                    Log.e("sleep", e.toString());
                }
                responses = readNdef(ndef);
            } finally {
                ndef.close();
            }
        } catch (NullPointerException | IOException | IllegalStateException e) {
            Log.e("writeReadNdef", e.toString());
        }
        return responses;
    }
    private static List<byte[]> readNdef(Ndef ndef) throws IOException {
        List<byte[]> responses = new ArrayList<>();
        try {
            NdefMessage ndefMessage = ndef.getNdefMessage();
            for (NdefRecord r : ndefMessage.getRecords()) {
                byte[] response = r.getPayload();
                if (r.getTnf() != 2) {
                    Log.d("readNdef", "Skipping a non-mime record");
                } else if ((response == null) || (response.length < 2)) {
                    Log.i("readNdef", "Empty record or record too short. Ignored.");
                } else if (response[1] != 1) {
                    Log.d("readNdef", "Tag byte not 1. Did we read back our own command?");
                } else {
                    responses.add(response);
                }
            }
        } catch (FormatException e) {
            Log.i("readNdef", e.toString());
        }
        return responses;
    }
    private static void writeNdef(byte[] payload,Ndef ndef) throws IOException {
        NdefRecord record = android.nfc.NdefRecord.createMime("n/p", payload);
        NdefMessage message = new NdefMessage(new NdefRecord[]{record});
        try {

            Log.d("writeNdef", "MESSAGE : "+message.toString());
            ndef.writeNdefMessage(message);
        } catch (FormatException e) {
            Log.e("writeNdef", e.toString());
        }
    }
    public static String processEncryption(String inputString)
    {
        String removalsString = null;
        try
        {
            Security.addProvider(new BouncyCastleProvider());

            byte[] input = hexStringToByteArray(inputString);

            byte[] keyBytes = new byte[] { (byte)0x2b, (byte)0x7e, (byte)0x15, (byte)0x16, (byte)0x28, (byte)0xae, (byte)0xd2, (byte)0xa6, (byte)0xab, (byte)0xf7, (byte)0x15, (byte)0x88, (byte)0x09, (byte)0xcf, (byte)0x4f, (byte)0x3c };
            byte[] ivBytes = new byte[] { (byte)0xf0, (byte)0xf1, (byte)0xf2, (byte)0xf3, (byte)0xf4, (byte)0xf5, (byte)0xf6, (byte)0xf7, (byte)0xf8, (byte)0xf9, (byte)0xfa, (byte)0xfb, (byte)0xfc, (byte)0xfd, (byte)0xfe, (byte)0xff };

            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding", "BC");
            //encryption pass
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            ByteArrayInputStream bIn = new ByteArrayInputStream(input);
            CipherInputStream cIn = new CipherInputStream(bIn, cipher);
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();

            int ch;
            while ((ch = cIn.read()) >= 0)
            {
                bOut.write(ch);
            }
            byte[] cipherText = bOut.toByteArray();

            String byteRemovalString = bytesToHex(cipherText).substring(0,12);
            String timefbyteA = byteRemovalString.charAt(6)+""+byteRemovalString.charAt(7)+""+byteRemovalString.charAt(4)+""+byteRemovalString.charAt(5)+""+byteRemovalString.charAt(2)+""+byteRemovalString.charAt(3)+""+byteRemovalString.charAt(0)+""+byteRemovalString.charAt(1);
            int timeSec = Integer.parseInt(timefbyteA,16);
            String removalsDateTime = getRealDateTimefromEpoch(timeSec);

            removalsString = removalsDateTime+"#"+byteRemovalString.charAt(8)+""+byteRemovalString.charAt(9)+"#"+byteRemovalString.charAt(10)+""+byteRemovalString.charAt(11);

        }
        catch(Exception e)
        {
            System.err.println("Exception : "+e);
        }
        return  removalsString;
    }
    public static byte[] hexStringToByteArray(String s)
    {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    public static String asciiToHex(String asciiValue)
    {
        char[] chars = asciiValue.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++)
        {
            hex.append(Integer.toHexString((int) chars[i]));
        }
        return hex.toString();
    }
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    public static String getRealDateTimefromEpoch(int timeSec) {

        String systemTime = null;

        try
        {
            Date date = new Date(timeSec * 1000L);
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            systemTime = format.format(date);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return systemTime;

    }
    public static ArrayList<String> devideStringIntoEqualPart(String encryptedString)
    {
        ArrayList<String> ENCRYPTED_STRINGS = new ArrayList<>();
        try
        {
            //Stores the length of the string
            int len = encryptedString.length();
            //n determines the variable that divide the string in 'n' equal parts
            int n = encryptedString.length() / 32;
            int temp = 0, chars = len/n;
            //Check whether a string can be divided into n equal parts
            if(len % n != 0)
            {
                System.out.println("Sorry this string cannot be divided into "+ n +" equal parts.");
            }
            else
            {
                for(int i = 0; i < len; i = i+chars)
                {
                    //Dividing string in n equal part using substring()
                    String part = encryptedString.substring(i, i+chars);
                    ENCRYPTED_STRINGS.add(part);
                    temp++;
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return ENCRYPTED_STRINGS;
    }
    public static byte[] encryptString(String inputString)
    {
        byte[] cipherText = null;
        try
        {
            Security.addProvider(new BouncyCastleProvider());

            byte[] input = inputString.getBytes();
            byte[] keyBytes = new byte[] { (byte)0x2b, (byte)0x7e, (byte)0x15, (byte)0x16, (byte)0x28, (byte)0xae, (byte)0xd2, (byte)0xa6, (byte)0xab, (byte)0xf7, (byte)0x15, (byte)0x88, (byte)0x09, (byte)0xcf, (byte)0x4f, (byte)0x3c };
            byte[] ivBytes = new byte[] { (byte)0xf0, (byte)0xf1, (byte)0xf2, (byte)0xf3, (byte)0xf4, (byte)0xf5, (byte)0xf6, (byte)0xf7, (byte)0xf8, (byte)0xf9, (byte)0xfa, (byte)0xfb, (byte)0xfc, (byte)0xfd, (byte)0xfe, (byte)0xff };

            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding", "BC");

            // encryption pass
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            ByteArrayInputStream bIn = new ByteArrayInputStream(input);
            CipherInputStream cIn = new CipherInputStream(bIn, cipher);
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();

            int ch;
            while ((ch = cIn.read()) >= 0)
            {
                bOut.write(ch);
            }
            cipherText = bOut.toByteArray();
        }
        catch(Exception e)
        {
            System.err.println("encryptString : "+e);
        }
        return cipherText;
    }

    public static String decryptString(String inputString)
    {
        String decryptedString = null;
        try
        {
            Security.addProvider(new BouncyCastleProvider());

            byte[] input = hexStringToByteArray(inputString);
            byte[] keyBytes = new byte[] { (byte)0x2b, (byte)0x7e, (byte)0x15, (byte)0x16, (byte)0x28, (byte)0xae, (byte)0xd2, (byte)0xa6, (byte)0xab, (byte)0xf7, (byte)0x15, (byte)0x88, (byte)0x09, (byte)0xcf, (byte)0x4f, (byte)0x3c };
            byte[] ivBytes = new byte[] { (byte)0xf0, (byte)0xf1, (byte)0xf2, (byte)0xf3, (byte)0xf4, (byte)0xf5, (byte)0xf6, (byte)0xf7, (byte)0xf8, (byte)0xf9, (byte)0xfa, (byte)0xfb, (byte)0xfc, (byte)0xfd, (byte)0xfe, (byte)0xff };
            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding", "BC");
            //encryption pass
            cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
            ByteArrayInputStream bIn = new ByteArrayInputStream(input);
            CipherInputStream cIn = new CipherInputStream(bIn, cipher);
            ByteArrayOutputStream bOut = new ByteArrayOutputStream();
            int ch;
            while ((ch = cIn.read()) >= 0)
            {
                bOut.write(ch);
            }
            byte[] cipherText = bOut.toByteArray();
            decryptedString = new String(cipherText);
        }
        catch(Exception e)
        {
            System.err.println("Exception : "+e);
        }
        return  decryptedString;
    }
}
