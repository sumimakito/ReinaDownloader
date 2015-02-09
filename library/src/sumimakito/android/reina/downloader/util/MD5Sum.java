package sumimakito.android.reina.downloader.util;

import java.security.*;

public class MD5Sum
{
	public static String get(String rawString)
	{
		try
		{
			MessageDigest bmd5 = MessageDigest.getInstance("MD5");
			bmd5.update(rawString.getBytes());
			int i;
			StringBuffer buf = new StringBuffer();
			byte[] b = bmd5.digest();
			for (int offset = 0; offset < b.length; offset++)
			{
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();
		}
		catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
