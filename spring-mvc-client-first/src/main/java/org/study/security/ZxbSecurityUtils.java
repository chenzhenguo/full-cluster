package org.study.security;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * 
 * @Title: SecurityUtils
 * @Description: 密钥处理
 * @Author: gengwenfei
 * @Since:2018年03月01日 下午6:00:15
 * @Version:1.0
 */
public class ZxbSecurityUtils {
	public static final String CHARSET = "UTF-8";
	public static final String KEY_ALGORITHM = "RSA";// 加密算法RSA
	public static final String SIGNATURE_ALGORITHM = "SHA1withRSA";// 签名算法

	private static final Logger logger = Logger.getLogger(ZxbSecurityUtils.class);

//	/**
//	 * 得到RSA公钥
//	 * 
//	 * @param publicKey
//	 *            密钥字符串（经过base64编码）
//	 * @throws Exception
//	 */
//	public static RSAPublicKey getRSAPublicKey(String publicKey) throws Exception {
//		// 通过X509编码的Key指令获得公钥对象
//		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
//		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(
//				Base64.decodeBase64(publicKey));
//		RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
//		return key;
//	}
//
//	/**
//	 * 得到RSA私钥
//	 * 
//	 * @param privateKey
//	 *            密钥字符串（经过base64编码）
//	 * @throws Exception
//	 */
//	public static RSAPrivateKey getRSAPrivateKey(String privateKey) throws Exception {
//		// 通过PKCS#8编码的Key指令获得私钥对象
//		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
//		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(
//				org.apache.commons.codec.binary.Base64.decodeBase64(privateKey));
//		RSAPrivateKey key = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
//		return key;
//	}
//
//	/**
//	 * 公钥加密
//	 * 
//	 * @param data
//	 *            数据
//	 * @param pubKey
//	 *            公钥
//	 * @return
//	 * @throws Exception
//	 */
//	public static String publicEncrypt(String data, String pubKey) throws Exception {
//		try {
//			RSAPublicKey publicKey = getRSAPublicKey(pubKey);
//			Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
//			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//			return org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString(rsaSplitCodec(cipher,
//					Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), publicKey.getModulus().bitLength()));
//		} catch (Exception e) {
//			logger.error("公钥加密异常:" + data, e);
//			throw e;
//		}
//	}
//
//	/**
//	 * 私钥解密
//	 * 
//	 * @param data
//	 *            数据
//	 * @param priKey
//	 *            私钥
//	 * @return
//	 * @throws Exception
//	 */
//	public static String privateDecrypt(String data, String priKey) throws Exception {
//		try {
//			RSAPrivateKey privateKey = getRSAPrivateKey(priKey);
//			Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
//			cipher.init(Cipher.DECRYPT_MODE, privateKey);
//			return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE,
//					org.apache.commons.codec.binary.Base64.decodeBase64(data), privateKey.getModulus().bitLength()),
//					CHARSET);
//		} catch (Exception e) {
//			logger.error("私钥解密异常:" + data, e);
//			throw e;
//		}
//	}

	private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) throws Exception {
		int maxBlock = 0;
		ByteArrayOutputStream out = null;
		try {
			if (opmode == Cipher.DECRYPT_MODE) {
				maxBlock = keySize / 8;
			} else {
				maxBlock = keySize / 8 - 11;
			}
			int offSet = 0;
			byte[] buff;
			int i = 0;
			out = new ByteArrayOutputStream();
			while (datas.length > offSet) {
				if (datas.length - offSet > maxBlock) {
					buff = cipher.doFinal(datas, offSet, maxBlock);
				} else {
					buff = cipher.doFinal(datas, offSet, datas.length - offSet);
				}
				out.write(buff, 0, buff.length);
				i++;
				offSet = i * maxBlock;
			}
			byte[] resultDatas = out.toByteArray();
			IOUtils.closeQuietly(out);
			return resultDatas;
		} catch (Exception e) {
			logger.error("加密解密数据处理时异常:" + maxBlock + "," + datas.toString(), e);
			throw e;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					logger.error("加密解密数据处理-流关闭异常:", e);
				}
			}
		}
	}

	//
	// /**
	// * 生成私钥 公钥
	// */
	// public static Map<String, String> geration() throws Exception {
	// KeyPairGenerator keyPairGenerator =
	// KeyPairGenerator.getInstance(KEY_ALGORITHM);
	// SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
	// secureRandom.setSeed("zhaotof".getBytes());// 盐粒
	// keyPairGenerator.initialize(1024, secureRandom);
	// KeyPair keyPair = keyPairGenerator.genKeyPair();
	// byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
	// String pubKey = Base64.encode(publicKeyBytes);
	// System.out.println("公钥:" + pubKey);
	// byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
	// String priKey = Base64.encode(privateKeyBytes);
	// System.out.println("私钥:" + priKey);
	// Map<String, String> map = new HashMap<String, String>();
	// map.put("pubKey", pubKey);
	// map.put("priKey", priKey);
	// return map;
	// }
	//

}
