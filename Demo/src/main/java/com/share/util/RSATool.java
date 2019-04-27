/**  
* @Title: RSATool.java
* @Package com.share.util
* @Description: TODO(用一句话描述该文件做什么)
* @author johnny
* @date 2019年4月24日 下午7:25:48
* @version V1.0  
*/ 
package com.share.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;


public class RSATool {
	public static final String KEY_ALGORITHM = "RSA";
	/**
	 * RSA最大加密明文大小
	 */
	private static final int MAX_ENCRYPT_BLOCK = 245;

	/**
	 * RSA最大解密密文大小
	 */
	private static final int MAX_DECRYPT_BLOCK = 256;

	/**
	 * @Title: getPublicKey
	 * @Description: 将字符串型公钥转化为PublicKey Java
	 * @param publicKey
	 * @return PublicKey
	 */
	public static PublicKey getPublicKey() {
		try {
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(
					Base64.decodeBase64(ConfigUtil.getKey("share.terminal.publicKey")));
			KeyFactory factory;
			factory = KeyFactory.getInstance(KEY_ALGORITHM);
			return factory.generatePublic(x509EncodedKeySpec);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @Title: getPrivateKey
	 * @Description: 将字符串型私钥转化为 PrivateKey Java
	 * @param privateKey
	 * @return PrivateKey
	 */
	public static PrivateKey getPrivateKey() {
		try {
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(
					Base64.decodeBase64(ConfigUtil.getKey("share.terminal.privateKey")));
			KeyFactory factory = KeyFactory.getInstance(KEY_ALGORITHM);
			return factory.generatePrivate(pkcs8EncodedKeySpec);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
    public static String RSAEncode( String plainText) {  
        byte[] b = plainText.getBytes();  
        try {  
            int inputLen = b.length;  
            ByteArrayOutputStream out = new ByteArrayOutputStream();  
            int offSet = 0;  
            byte[] cache;  
            int i = 0;  
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);  
            cipher.init(Cipher.ENCRYPT_MODE, getPublicKey());  
            // 对数据分段解密  
            while (inputLen - offSet > 0) {  
                if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {  
                    cache = cipher.doFinal(b, offSet, MAX_ENCRYPT_BLOCK);  
                } else {  
                    cache = cipher.doFinal(b, offSet, inputLen - offSet);  
                }  
                out.write(cache, 0, cache.length);  
                i++;  
                offSet = i * MAX_ENCRYPT_BLOCK;  
            }  
            byte[] decryptedData = out.toByteArray();  
            out.close();  
            return Base64.encodeBase64String(decryptedData);  
        } catch (InvalidKeyException e) {  
            e.printStackTrace();  
        } catch (NoSuchAlgorithmException e) {
        	 e.printStackTrace();
		}catch (BadPaddingException e) {
			 e.printStackTrace();
		}catch (IOException e) {
			 e.printStackTrace();
		} catch(NoSuchPaddingException e){
			 e.printStackTrace();
		}catch (IllegalBlockSizeException e) {
			 e.printStackTrace();
		}
        return null;  
    }  
    
    /** 
     * @Title: RSADecode 
     * @Description: 将字符串解密 
     * @param key 
     * @param encodedText 
     * @return String 
     */  
    public static String RSADecode(String encodedText) {  
        try {  
            byte[] b = Base64.decodeBase64(encodedText);  
            int inputLen = b.length;  
            ByteArrayOutputStream out = new ByteArrayOutputStream();  
            int offSet = 0;  
            byte[] cache;  
            int i = 0;  
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);  
            cipher.init(Cipher.DECRYPT_MODE, getPrivateKey());  
            // 对数据分段解密  
            while (inputLen - offSet > 0) {  
                if (inputLen - offSet > MAX_DECRYPT_BLOCK) {  
                    cache = cipher.doFinal(b, offSet, MAX_DECRYPT_BLOCK);  
                } else {  
                    cache = cipher.doFinal(b, offSet, inputLen - offSet);  
                }  
                out.write(cache, 0, cache.length);  
                i++;  
                offSet = i * MAX_DECRYPT_BLOCK;  
            }  
            byte[] decryptedData = out.toByteArray();  
            out.close();  
            return new String(decryptedData);  
        }  catch (InvalidKeyException e) {  
            e.printStackTrace();  
        } catch (NoSuchAlgorithmException e) {
        	 e.printStackTrace();
		}catch (BadPaddingException e) {
			 e.printStackTrace();
		}catch (IOException e) {
			 e.printStackTrace();
		} catch(NoSuchPaddingException e){
			 e.printStackTrace();
		}catch (IllegalBlockSizeException e) {
			 e.printStackTrace();
		}
        return null;  
    }  



}
