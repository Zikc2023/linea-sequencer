package net.consensys.linea.zktracer.module.alu.ext;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class BigIntegerConverter {

  public static long[] toLongArray(BigInteger bigInteger) {
    // Convert the BigInteger to a byte array
    byte[] inputBytes = bigInteger.toByteArray();

    // If inputBytes.length is greater than 64 (512 bits), use only the least significant 64 bytes
    if (inputBytes.length > 64) {
      inputBytes = Arrays.copyOfRange(inputBytes, inputBytes.length - 64, inputBytes.length);
    }

    // Pad the inputBytes to 64 bytes (512 bits)
    byte[] byteArray = new byte[64];
    int start = byteArray.length - inputBytes.length;
    System.arraycopy(inputBytes, 0, byteArray, start, inputBytes.length);

    // Convert the byte array to an array of 8 longs
    long[] longArray = new long[8];
    ByteBuffer buffer = ByteBuffer.wrap(byteArray);
    for (int i = 0; i < 8; i++) {
      longArray[7 - i] = buffer.getLong(i * 8);
    }
    return longArray;
  }

  public static BigInteger fromLongArray(long[] longArray) {
    // Convert the array of 8 longs to a byte array
    ByteBuffer buffer = ByteBuffer.allocate(64);
    for (int i = 0; i < longArray.length; i++) {
      buffer.putLong(56 - (i * 8), longArray[i]);
    }
    byte[] byteArray = buffer.array();

    // Remove any leading zeros from the byte array
    int i = 0;
    while (i < byteArray.length && byteArray[i] == 0) {
      i++;
    }
    if (i == byteArray.length) {
      return BigInteger.ZERO;
    }
    byte[] trimmedByteArray = Arrays.copyOfRange(byteArray, i, byteArray.length);

    // Convert the byte array to a BigInteger
    return new BigInteger(trimmedByteArray);
  }
}
