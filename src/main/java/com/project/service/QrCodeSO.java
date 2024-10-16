package com.project.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.openssl.EncryptionException;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class QrCodeSO {
	@Value("${encrypt.key}")
	private String encryptionKey;

	@Value("${encrypt.iv}")
	private String encryptionIv;

	public ResponseEntity<byte[]> generateQRCodeImage(String text) throws WriterException, IOException {
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 300, 300);

		ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
		MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
		byte[] pngData = pngOutputStream.toByteArray();

		return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(pngData);
	}

	private String encryptString(String plainText, byte[] key, byte[] iv)
			throws EncryptionException, IllegalStateException, InvalidCipherTextException {
		AESEngine engine = new AESEngine();
		GCMBlockCipher cipher = new GCMBlockCipher(engine);
		cipher.init(true, new AEADParameters(new KeyParameter(key), 128, iv));

		byte[] plainTextBytes = plainText.getBytes(StandardCharsets.UTF_8);
		byte[] encryptedBytes = new byte[cipher.getOutputSize(plainTextBytes.length)];
		int outputLength = cipher.processBytes(plainTextBytes, 0, plainTextBytes.length, encryptedBytes, 0);
		cipher.doFinal(encryptedBytes, outputLength);

		return Hex.toHexString(encryptedBytes);
	}

	private String decryptString(String encryptedText, byte[] key, byte[] iv) throws Exception {
		AESEngine engine = new AESEngine();
		GCMBlockCipher cipher = new GCMBlockCipher(engine);
		cipher.init(false, new AEADParameters(new KeyParameter(key), 128, iv));

		byte[] encryptedBytes = Hex.decode(encryptedText);
		byte[] plainTextBytes = new byte[cipher.getOutputSize(encryptedBytes.length)];

		int outputLength = cipher.processBytes(encryptedBytes, 0, encryptedBytes.length, plainTextBytes, 0);
		cipher.doFinal(plainTextBytes, outputLength);

		return new String(plainTextBytes, StandardCharsets.UTF_8);
	}

	public String getEncryptedText(String plainText)
			throws EncryptionException, IllegalStateException, InvalidCipherTextException {
		byte[] key = Hex.decode(encryptionKey);
		byte[] iv = Hex.decode(encryptionIv);

		return this.encryptString(plainText, key, iv);
	}

	public String getDecryptedText(String encryptedText) throws Exception {
		byte[] key = Hex.decode(encryptionKey);
		byte[] iv = Hex.decode(encryptionIv);

		return this.decryptString(encryptedText, key, iv);
	}
}