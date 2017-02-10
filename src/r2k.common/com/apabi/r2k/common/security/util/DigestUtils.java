/**
 * Copyright (c) 2005-2009 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * 
 * $Id: DigestUtils.java 799 2009-12-31 15:34:10Z calvinxiu $
 */
package com.apabi.r2k.common.security.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;

import com.apabi.r2k.common.utils.EncodeUtils;

/**
 * 支持SHA-1/MD5消息摘要的工具类.
 * 
 * 支持Hex与Base64两种编码方式.
 * 
 * @author calvin
 */
public abstract class DigestUtils {
	private static final int BUFFER_LENGTH = 1024;

	private DigestUtils() {
		
	}

	private static final String SHA1 = "SHA-1";
	private static final String MD5 = "MD5";

	// -- String Hash function --//
	/**
	 * 对输入字符串进行sha1散列, 返回Hex编码的结果.
	 */
	public static String sha1ToHex(final String input) {
		byte[] digestResult = digest(input, SHA1);
		return EncodeUtils.hexEncode(digestResult);
	}

	/**
	 * 对输入字符串进行md5散列, 返回Hex编码的结果.
	 */
	public static String md5ToHex(final String input) {
		byte[] digestResult = digest(input, MD5);
		return EncodeUtils.hexEncode(digestResult);
	}

	/**
	 * 对输入字符串进行md5散列, 返回Hex编码的结果，用指定的编码从输入字符串获取字节数组.
	 */
	public static String md5ToHex(final String input, final String charset) {
		byte[] digestResult;
		try {
			digestResult = digest(input.getBytes(charset), MD5);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Security exception", e);
		}
		return EncodeUtils.hexEncode(digestResult);
	}
	
	/**
	 * 对指定字符集charset的字符串input进行md5编码.
	 * 
	 * @param input	需要进行md5编码的字符串
	 * 
	 * @param charset 
	 * @return
	 */
	public static byte[] encryptByMd5(String input, String charset) {
		try {
			return digest(input.getBytes(charset), MD5);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("Security exception", e);
		}
	}

	/**
	 * 对输入字符串进行sha1散列, 返回Base64编码的结果.
	 */
	public static String sha1ToBase64(final String input) {
		byte[] digestResult = digest(input, SHA1);
		return EncodeUtils.base64Encode(digestResult);
	}

	/**
	 * 对输入字符串进行sha1散列, 返回Base64编码的URL安全的结果.
	 */
	public static String sha1ToBase64UrlSafe(final String input) {
		byte[] digestResult = digest(input, SHA1);
		return EncodeUtils.base64UrlSafeEncode(digestResult);
	}

	/**
	 * 对字符串进行散列, 支持md5与sha1算法，使用默认编码获取字节数组.
	 */
	private static byte[] digest(final String input, final String algorithm) {
		return digest(input.getBytes(), algorithm);
	}

	/**
	 * 对字符串进行散列, 支持md5与sha1算法.
	 */
	private static byte[] digest(final byte[] data, final String algorithm) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			return messageDigest.digest(data);
		} catch (GeneralSecurityException e) {
			throw new IllegalStateException("Security exception", e);
		}
	}

	// -- File Hash function --//
	/**
	 * 对文件进行md5散列,返回Hex编码结果.
	 */
	public static String md5ToHex(final InputStream input) throws IOException {
		return digest(input, MD5);
	}

	/**
	 * 对文件进行sha1散列,返回Hex编码结果.
	 */
	public static String sha1ToHex(final InputStream input) throws IOException {
		return digest(input, SHA1);
	}

	/**
	 * 对文件进行散列, 支持md5与sha1算法.
	 */
	private static String digest(final InputStream input, final String algorithm) throws IOException {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);
			int bufferLength = BUFFER_LENGTH;
			byte[] buffer = new byte[bufferLength];
			int read = input.read(buffer, 0, bufferLength);

			while (read > -1) {
				messageDigest.update(buffer, 0, read);
				read = input.read(buffer, 0, bufferLength);
			}

			return EncodeUtils.hexEncode(messageDigest.digest());

		} catch (GeneralSecurityException e) {
			throw new IllegalStateException("Security exception", e);
		}
	}

	public static void main(final String[] as) {
		//I1+qP18tmDrGtMAvuGgQjQ==
		String str = "founder";
		byte[] bytes  = DigestUtils.encryptByMd5(str, "UTF-16LE");
		System.out.println(Base64.encodeBase64String(DigestUtils.encryptByMd5(str, "UTF-16LE")));
	}
}
