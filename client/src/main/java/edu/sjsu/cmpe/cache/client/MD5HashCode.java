package edu.sjsu.cmpe.cache.client;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

public class MD5HashCode implements HashFunction {

	@Override
	public Integer hash(String string) {
		
		
		com.google.common.hash.HashFunction hf = Hashing.md5();
		HashCode hc = hf.newHasher()
		       .putString(string, Charsets.UTF_8)
		       .hash();
		return hc.asInt();
//		string = "dfe2r3f3$%$5dfdf34^&234!@" + string;
//		MessageDigest md;
//		try {
//			md = MessageDigest.getInstance("MD5");
//		} catch (NoSuchAlgorithmException e) {
//			throw new RuntimeException(e);
//		}
//		String md5 = new String(md.digest(string.getBytes()));
//		return md5.hashCode();
	}
}
