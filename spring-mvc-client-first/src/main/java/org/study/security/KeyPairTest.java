package org.study.security;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

import javax.crypto.Cipher;

import org.junit.Test;

/**
 * 
 * @Title: KeyPairTest.java
 * @Description: TODO
 * @author zhaotf
 * @date 2018年2月10日 下午2:19:19
 * @see {@linkplain https://www.2cto.com/kf/201505/403640.html}
 */
public class KeyPairTest {
	private static final String ALGOGRITHM = "RSA";
	private static final String PUBLIC_KEY_PATH = "public.key";
	private static final String PRIVATE_KEY_PATH = "private.key";

	/**
	 * 生成公钥、私钥
	 */
	@Test
	public void testGenerate() throws Exception {
		// KeyPairGenerator引擎类用于产生密钥对，JDK(7)默认支持的算法有，DiffieHellman、DSA、RSA、EC
		KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGOGRITHM);
		// 产生密钥对
		KeyPair keyPair = generator.generateKeyPair();
		// 获取公钥
		PublicKey publicKey = keyPair.getPublic();
		// 获取私钥
		PrivateKey privateKey = keyPair.getPrivate();

		// 将公钥与私钥写入文件，以备后用
		writeKey(PUBLIC_KEY_PATH, publicKey);
		writeKey(PRIVATE_KEY_PATH, privateKey);
	}

	/**
	 * 加密、解密
	 */
	@Test
	public void testEncryptAndDecrypt() throws Exception {
		Cipher cipher = Cipher.getInstance(ALGOGRITHM);
		// 读取私钥，进行加密
		PrivateKey privateKey = (PrivateKey) readKey(PRIVATE_KEY_PATH);
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		// 加密
		String sendInfo = "我的明文|||hello world|||";
		byte[] results = cipher.doFinal(sendInfo.getBytes());
		String str1 = Base64.getEncoder().encodeToString(results);
		System.out.println("加密后:" + str1);

		// 读取公钥，进行解密
		PublicKey publicKey = (PublicKey) readKey(PUBLIC_KEY_PATH);
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		// 解密
		byte[] deciphered = cipher.doFinal(Base64.getDecoder().decode(str1));
		// 得到明文
		String recvInfo = new String(deciphered);
		System.out.println("解密后:" + recvInfo);
	}

	public void writeKey(String path, Key key) throws Exception {
		FileOutputStream fos = new FileOutputStream(path);
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		System.out.println("中:" + key.getAlgorithm());
		System.out.println("中:" + key.getFormat());
		System.out.println("中:" + Base64.getEncoder().encodeToString(key.getEncoded()));
		oos.writeObject(key);
		oos.close();
	}

	public Key readKey(String path) throws Exception {
		FileInputStream fis = new FileInputStream(path);
		ObjectInputStream bis = new ObjectInputStream(fis);
		Object object = bis.readObject();
		bis.close();
		return (Key) object;
	}

}
