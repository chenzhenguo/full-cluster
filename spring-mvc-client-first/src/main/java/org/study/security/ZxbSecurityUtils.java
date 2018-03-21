package org.study.security;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @Title: SecurityUtils
 * @Description: 密钥处理
 * @Author: gengwenfei
 * @Since:2018年03月01日 下午6:00:15
 * @Version:1.0
 */
public class ZxbSecurityUtils {
	public static final String KEY_ALGORITHM = "RSA";// 加密算法RSA
	public static final String SIGNATURE_ALGORITHM = "SHA1withRSA";// 签名算法

	private static final Logger logger = Logger.getLogger(ZxbSecurityUtils.class);
	static {
		try {
			java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("密钥初始化失败");
		}
	}

	/**
	 * 生成私钥 公钥
	 */
	public static Map<String, String> geration() throws Exception {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		secureRandom.setSeed("zhaotof".getBytes());// 盐粒
		keyPairGenerator.initialize(1024, secureRandom);
		KeyPair keyPair = keyPairGenerator.genKeyPair();
		byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
		String pubKey = Base64.encode(publicKeyBytes);
		System.out.println("公钥:" + pubKey);
		byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
		String priKey = Base64.encode(privateKeyBytes);
		System.out.println("私钥:" + priKey);
		Map<String, String> map = new HashMap<String, String>();
		map.put("pubKey", pubKey);
		map.put("priKey", priKey);
		return map;
	}

	/**
	 * 对传入字符串进行签名 ,私钥签名
	 * 
	 * @param inputStr
	 * @param prkeyKey
	 *            私钥
	 * @return @
	 */
	public static String sign(String inputStr, String prkeyKey) {
		String result = null;
		try {
			PrivateKey privateKey = getPrivateKey(prkeyKey);
			byte[] tByte;
			Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM, "BC");
			signature.initSign(privateKey);
			signature.update(inputStr.getBytes("UTF-8"));
			tByte = signature.sign();
			result = Base64.encode(tByte);
			logger.info("签名处理,明文:" + inputStr + ",密文:" + result);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("密钥初始化失败");
		}
		return result;
	}

	/**
	 * 对富友返回的数据进行验签,公钥验签
	 * 
	 * @param src
	 *            返回数据明文
	 * @param signValue
	 *            返回数据签名
	 * @param pbkKey
	 *            公钥
	 * @return
	 */
	public static boolean verifySign(String src, String signValue, String pbkKey) {
		boolean bool = false;
		try {
			PublicKey publicKey = getPublicKey(pbkKey);
			Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM, "BC");
			signature.initVerify(publicKey);
			signature.update(src.getBytes("UTF-8"));
			bool = signature.verify(Base64.decode(signValue));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("密钥初始化失败");
		}
		return bool;
	}

	private static PrivateKey getPrivateKey(String prkeyKey) {
		KeyFactory kf;
		PrivateKey privateKey = null;
		try {
			kf = KeyFactory.getInstance(KEY_ALGORITHM, "BC");
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decode(prkeyKey));
			privateKey = kf.generatePrivate(keySpec);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("密钥初始化失败");
		}
		return privateKey;
	}

	private static PublicKey getPublicKey(String pbkeyKey) {
		KeyFactory kf;
		PublicKey publickey = null;
		try {
			kf = KeyFactory.getInstance(KEY_ALGORITHM, "BC");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decode(pbkeyKey));
			publickey = kf.generatePublic(keySpec);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("密钥初始化失败");
		}
		return publickey;
	}

	/**
	 * 获取恒丰返回xml的plan域(明文验签部分)
	 * 
	 * @param xml
	 *            接口返回xml文本
	 * @return String
	 */
	public static String getPlanXml(String xml) {
		logger.info("接口返回报文全文:" + xml);
		String ckeckStr = StringUtils.substringBetween(xml, "<plain>", "</plain>");
		ckeckStr = "<plain>" + ckeckStr + "</plain>";
		logger.info("接口返回报文明文:" + ckeckStr);
		return ckeckStr;
	}

	/**
	 * 获取恒丰返回xml的plan域(密文部分)
	 * 
	 * @param xml
	 * @return String
	 */
	public static String getSignatureXml(String xml) {
		logger.info("恒丰接口返回报文全文:" + xml);
		String signature = ZxbUtil.xml2json(xml);
		logger.info("恒丰接口返回报文转换JSON文本:" + signature);
		JSONObject obj = JSONObject.parseObject(signature);
		signature = obj.getString("signature");
		logger.info("恒丰接口返回报文签名:" + signature);
		return signature;
	}

	/**
	 * 获取恒丰返回xml的JSON对象
	 * 
	 * @param xml
	 * @return String
	 */
	public static JSONObject getJSONXml(String xml) {
		logger.info("恒丰接口返回报文全文:" + xml);
		String signature = ZxbUtil.xml2json(xml);
		logger.info("恒丰接口返回报文转换JSON文本:" + signature);
		JSONObject obj = JSONObject.parseObject(signature);
		signature = obj.getString("signature");
		logger.info("恒丰接口返回报文签名:" + signature);
		return obj;
	}

}
