// This file has been automatically generated, DO NOT EDIT

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.lucene.test;

import java.io.IOException;

import jdk.incubator.vector.LongVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;

// Inspired from https://fulmicoton.com/posts/bitpacking/
// Encodes multiple integers in a long to get SIMD-like speedups.
// If bitsPerValue <= 8 then we pack 8 ints per long
// else if bitsPerValue <= 16 we pack 4 ints per long
// else we pack 2 ints per long
final class PanamaForUtil90SameFormat {

    static final int BLOCK_SIZE = 128;
    private static final int BLOCK_SIZE_LOG2 = 7;

    private static long expandMask32(long mask32) {
        return mask32 | (mask32 << 32);
    }

    private static long expandMask16(long mask16) {
        return expandMask32(mask16 | (mask16 << 16));
    }

    private static long expandMask8(long mask8) {
        return expandMask16(mask8 | (mask8 << 8));
    }

    private static long mask32(int bitsPerValue) {
        return expandMask32((1L << bitsPerValue) - 1);
    }

    private static long mask16(int bitsPerValue) {
        return expandMask16((1L << bitsPerValue) - 1);
    }

    private static long mask8(int bitsPerValue) {
        return expandMask8((1L << bitsPerValue) - 1);
    }

    static final LongVector FF = LongVector.broadcast(LongVector.SPECIES_512, 0xFFL);

    public static void expand8(long[] arr) {
        VectorSpecies<Long> SPECIES = LongVector.SPECIES_512; // use preferred?
        int loopBound = 16 / SPECIES.length();
        for (int i = 0; i < loopBound; i++) {
            final int offset = i * 8;
            LongVector l = LongVector.fromArray(LongVector.SPECIES_512, arr, offset);

            LongVector shifted0 = l.and(FF);
            LongVector shifted1 = l.lanewise(VectorOperators.LSHR, 8).and(FF);
            LongVector shifted2 = l.lanewise(VectorOperators.LSHR, 16).and(FF);
            LongVector shifted3 = l.lanewise(VectorOperators.LSHR, 24).and(FF);
            LongVector shifted4 = l.lanewise(VectorOperators.LSHR, 32).and(FF);
            LongVector shifted5 = l.lanewise(VectorOperators.LSHR, 40).and(FF);
            LongVector shifted6 = l.lanewise(VectorOperators.LSHR, 48).and(FF);
            LongVector shifted7 = l.lanewise(VectorOperators.LSHR, 56).and(FF);

            shifted0.intoArray(arr, 112 + offset);
            shifted1.intoArray(arr, 96 + offset);
            shifted2.intoArray(arr, 80 + offset);
            shifted3.intoArray(arr, 64 + offset);
            shifted4.intoArray(arr, 48 + offset);
            shifted5.intoArray(arr, 32 + offset);
            shifted6.intoArray(arr, 16 + offset);
            shifted7.intoArray(arr, offset);
        }
    }

//    public static void expand8(long[] arr) {
//        VectorSpecies<Long> SPECIES = LongVector.SPECIES_256; // use preferred?
//        int loopBound = 16 / SPECIES.length();
//        for (int i = 0; i < loopBound; i++) {
//            LongVector l = LongVector.fromArray(SPECIES, arr, i * SPECIES.length());
//
//            LongVector shifted0 = l.and(FF);
//            LongVector shifted1 = l.lanewise(VectorOperators.LSHR, 8).and(FF);
//            LongVector shifted2 = l.lanewise(VectorOperators.LSHR, 16).and(FF);
//            LongVector shifted3 = l.lanewise(VectorOperators.LSHR, 24).and(FF);
//            LongVector shifted4 = l.lanewise(VectorOperators.LSHR, 32).and(FF);
//            LongVector shifted5 = l.lanewise(VectorOperators.LSHR, 40).and(FF);
//            LongVector shifted6 = l.lanewise(VectorOperators.LSHR, 48).and(FF);
//            LongVector shifted7 = l.lanewise(VectorOperators.LSHR, 56).and(FF);
//
//            shifted0.intoArray(arr, 112 + i * SPECIES.length());
//            shifted1.intoArray(arr, 96 + i * SPECIES.length());
//            shifted2.intoArray(arr, 80 + i * SPECIES.length());
//            shifted3.intoArray(arr, 64 + i * SPECIES.length());
//            shifted4.intoArray(arr, 48 + i * SPECIES.length());
//            shifted5.intoArray(arr, 32 + i * SPECIES.length());
//            shifted6.intoArray(arr, 16 + i * SPECIES.length());
//            shifted7.intoArray(arr, i * SPECIES.length());
//        }
//    }

//    private static void expand8(long[] arr) {
//        for (int i = 0; i < 16; ++i) {
//            long l = arr[i];
//            arr[i] = (l >>> 56) & 0xFFL;
//            arr[16 + i] = (l >>> 48) & 0xFFL;
//            arr[32 + i] = (l >>> 40) & 0xFFL;
//            arr[48 + i] = (l >>> 32) & 0xFFL;
//            arr[64 + i] = (l >>> 24) & 0xFFL;
//            arr[80 + i] = (l >>> 16) & 0xFFL;
//            arr[96 + i] = (l >>> 8) & 0xFFL;
//            arr[112 + i] = l & 0xFFL;
//        }
//    }

//deode

    private static void expand8To32(long[] arr) {
        for (int i = 0; i < 16; ++i) {
            long l = arr[i];
            arr[i] = (l >>> 24) & 0x000000FF000000FFL;
            arr[16 + i] = (l >>> 16) & 0x000000FF000000FFL;
            arr[32 + i] = (l >>> 8) & 0x000000FF000000FFL;
            arr[48 + i] = l & 0x000000FF000000FFL;
        }
    }

    private static void collapse8(long[] arr) {
        for (int i = 0; i < 16; ++i) {
            arr[i] =
                    (arr[i] << 56)
                            | (arr[16 + i] << 48)
                            | (arr[32 + i] << 40)
                            | (arr[48 + i] << 32)
                            | (arr[64 + i] << 24)
                            | (arr[80 + i] << 16)
                            | (arr[96 + i] << 8)
                            | arr[112 + i];
        }
    }

    private static void expand16(long[] arr) {
        for (int i = 0; i < 32; ++i) {
            long l = arr[i];
            arr[i] = (l >>> 48) & 0xFFFFL;
            arr[32 + i] = (l >>> 32) & 0xFFFFL;
            arr[64 + i] = (l >>> 16) & 0xFFFFL;
            arr[96 + i] = l & 0xFFFFL;
        }
    }

    private static void expand16To32(long[] arr) {
        for (int i = 0; i < 32; ++i) {
            long l = arr[i];
            arr[i] = (l >>> 16) & 0x0000FFFF0000FFFFL;
            arr[32 + i] = l & 0x0000FFFF0000FFFFL;
        }
    }

    private static void collapse16(long[] arr) {
        for (int i = 0; i < 32; ++i) {
            arr[i] = (arr[i] << 48) | (arr[32 + i] << 32) | (arr[64 + i] << 16) | arr[96 + i];
        }
    }

    private static void expand32(long[] arr) {
        for (int i = 0; i < 64; ++i) {
            long l = arr[i];
            arr[i] = l >>> 32;
            arr[64 + i] = l & 0xFFFFFFFFL;
        }
    }

    private static void collapse32(long[] arr) {
        for (int i = 0; i < 64; ++i) {
            arr[i] = (arr[i] << 32) | arr[64 + i];
        }
    }

    private final long[] tmp = new long[BLOCK_SIZE / 2];

    /** Encode 128 integers from {@code longs} into {@code out}. */
    // @Override
    public void encode(long[] longs, int bitsPerValue, long[] out) throws IOException {
        final int nextPrimitive;
        final int numLongs;
        if (bitsPerValue <= 8) {
            nextPrimitive = 8;
            numLongs = BLOCK_SIZE / 8;
            collapse8(longs);
        } else if (bitsPerValue <= 16) {
            nextPrimitive = 16;
            numLongs = BLOCK_SIZE / 4;
            collapse16(longs);
        } else {
            nextPrimitive = 32;
            numLongs = BLOCK_SIZE / 2;
            collapse32(longs);
        }

        final int numLongsPerShift = bitsPerValue * 2;
        int idx = 0;
        int shift = nextPrimitive - bitsPerValue;
        for (int i = 0; i < numLongsPerShift; ++i) {
            tmp[i] = longs[idx++] << shift;
        }
        for (shift = shift - bitsPerValue; shift >= 0; shift -= bitsPerValue) {
            for (int i = 0; i < numLongsPerShift; ++i) {
                tmp[i] |= longs[idx++] << shift;
            }
        }

        final int remainingBitsPerLong = shift + bitsPerValue;
        final long maskRemainingBitsPerLong;
        if (nextPrimitive == 8) {
            maskRemainingBitsPerLong = MASKS8[remainingBitsPerLong];
        } else if (nextPrimitive == 16) {
            maskRemainingBitsPerLong = MASKS16[remainingBitsPerLong];
        } else {
            maskRemainingBitsPerLong = MASKS32[remainingBitsPerLong];
        }

        int tmpIdx = 0;
        int remainingBitsPerValue = bitsPerValue;
        while (idx < numLongs) {
            if (remainingBitsPerValue >= remainingBitsPerLong) {
                remainingBitsPerValue -= remainingBitsPerLong;
                tmp[tmpIdx++] |= (longs[idx] >>> remainingBitsPerValue) & maskRemainingBitsPerLong;
                if (remainingBitsPerValue == 0) {
                    idx++;
                    remainingBitsPerValue = bitsPerValue;
                }
            } else {
                final long mask1, mask2;
                if (nextPrimitive == 8) {
                    mask1 = MASKS8[remainingBitsPerValue];
                    mask2 = MASKS8[remainingBitsPerLong - remainingBitsPerValue];
                } else if (nextPrimitive == 16) {
                    mask1 = MASKS16[remainingBitsPerValue];
                    mask2 = MASKS16[remainingBitsPerLong - remainingBitsPerValue];
                } else {
                    mask1 = MASKS32[remainingBitsPerValue];
                    mask2 = MASKS32[remainingBitsPerLong - remainingBitsPerValue];
                }
                tmp[tmpIdx] |= (longs[idx++] & mask1) << (remainingBitsPerLong - remainingBitsPerValue);
                remainingBitsPerValue = bitsPerValue - remainingBitsPerLong + remainingBitsPerValue;
                tmp[tmpIdx++] |= (longs[idx] >>> remainingBitsPerValue) & mask2;
            }
        }

        System.arraycopy(tmp, 0, out, 0, 4 * bitsPerValue);

//        for (int i = 0; i < numLongsPerShift; ++i) {
//            out.writeLong(tmp[i]);
//        }
    }

    /** Number of bytes required to encode 128 integers of {@code bitsPerValue} bits per value. */
    //  @Override
    public int numBytes(int bitsPerValue) {
        return bitsPerValue << (BLOCK_SIZE_LOG2 - 3);
    }

    private static void decodeSlow(int bitsPerValue, long[] in, long[] tmp, long[] longs)
            throws IOException {
        final int numLongs = bitsPerValue << 1;
        //in.readLongs(tmp, 0, numLongs);
        System.arraycopy(in, 0, tmp, 0, 4 * bitsPerValue);
        final long mask = MASKS32[bitsPerValue];
        int longsIdx = 0;
        int shift = 32 - bitsPerValue;
        for (; shift >= 0; shift -= bitsPerValue) {
            shiftLongs(tmp, numLongs, longs, longsIdx, shift, mask);
            longsIdx += numLongs;
        }
        final int remainingBitsPerLong = shift + bitsPerValue;
        final long mask32RemainingBitsPerLong = MASKS32[remainingBitsPerLong];
        int tmpIdx = 0;
        int remainingBits = remainingBitsPerLong;
        for (; longsIdx < BLOCK_SIZE / 2; ++longsIdx) {
            int b = bitsPerValue - remainingBits;
            long l = (tmp[tmpIdx++] & MASKS32[remainingBits]) << b;
            while (b >= remainingBitsPerLong) {
                b -= remainingBitsPerLong;
                l |= (tmp[tmpIdx++] & mask32RemainingBitsPerLong) << b;
            }
            if (b > 0) {
                l |= (tmp[tmpIdx] >>> (remainingBitsPerLong - b)) & MASKS32[b];
                remainingBits = remainingBitsPerLong - b;
            } else {
                remainingBits = remainingBitsPerLong;
            }
            longs[longsIdx] = l;
        }
    }

    /**
     * The pattern that this shiftLongs method applies is recognized by the C2 compiler, which
     * generates SIMD instructions for it in order to shift multiple longs at once.
     */
    private static void shiftLongs(long[] a, int count, long[] b, int bi, int shift, long mask) {
        for (int i = 0; i < count; ++i) {
            b[bi + i] = (a[i] >>> shift) & mask;
        }
    }

    private static final long[] MASKS8 = new long[8];
    private static final long[] MASKS16 = new long[16];
    private static final long[] MASKS32 = new long[32];

    static {
        for (int i = 0; i < 8; ++i) {
            MASKS8[i] = mask8(i);
        }
        for (int i = 0; i < 16; ++i) {
            MASKS16[i] = mask16(i);
        }
        for (int i = 0; i < 32; ++i) {
            MASKS32[i] = mask32(i);
        }
    }
    // mark values in array as final longs to avoid the cost of reading array, arrays should only be
    // used when the idx is a variable
    private static final long MASK8_1 = MASKS8[1];
    private static final long MASK8_2 = MASKS8[2];
    private static final long MASK8_3 = MASKS8[3];
    private static final long MASK8_4 = MASKS8[4];
    private static final long MASK8_5 = MASKS8[5];
    private static final long MASK8_6 = MASKS8[6];
    private static final long MASK8_7 = MASKS8[7];
    private static final long MASK16_1 = MASKS16[1];
    private static final long MASK16_2 = MASKS16[2];
    private static final long MASK16_3 = MASKS16[3];
    private static final long MASK16_4 = MASKS16[4];
    private static final long MASK16_5 = MASKS16[5];
    private static final long MASK16_6 = MASKS16[6];
    private static final long MASK16_7 = MASKS16[7];
    private static final long MASK16_9 = MASKS16[9];
    private static final long MASK16_10 = MASKS16[10];
    private static final long MASK16_11 = MASKS16[11];
    private static final long MASK16_12 = MASKS16[12];
    private static final long MASK16_13 = MASKS16[13];
    private static final long MASK16_14 = MASKS16[14];
    private static final long MASK16_15 = MASKS16[15];
    private static final long MASK32_1 = MASKS32[1];
    private static final long MASK32_2 = MASKS32[2];
    private static final long MASK32_3 = MASKS32[3];
    private static final long MASK32_4 = MASKS32[4];
    private static final long MASK32_5 = MASKS32[5];
    private static final long MASK32_6 = MASKS32[6];
    private static final long MASK32_7 = MASKS32[7];
    private static final long MASK32_8 = MASKS32[8];
    private static final long MASK32_9 = MASKS32[9];
    private static final long MASK32_10 = MASKS32[10];
    private static final long MASK32_11 = MASKS32[11];
    private static final long MASK32_12 = MASKS32[12];
    private static final long MASK32_13 = MASKS32[13];
    private static final long MASK32_14 = MASKS32[14];
    private static final long MASK32_15 = MASKS32[15];
    private static final long MASK32_17 = MASKS32[17];
    private static final long MASK32_18 = MASKS32[18];
    private static final long MASK32_19 = MASKS32[19];
    private static final long MASK32_20 = MASKS32[20];
    private static final long MASK32_21 = MASKS32[21];
    private static final long MASK32_22 = MASKS32[22];
    private static final long MASK32_23 = MASKS32[23];
    private static final long MASK32_24 = MASKS32[24];

    /** Decode 128 integers into {@code longs}. */
    //@Override
    public void decode(int bitsPerValue, long[] in, long[] longs) throws IOException {
        switch (bitsPerValue) {
            case 5:
                decode5(in, tmp, longs);
                expand8(longs);
                break;
            case 6:
                decode6(in, tmp, longs);
                expand8(longs);
                break;
            default:
                decodeSlow(bitsPerValue, in, tmp, longs);
                expand32(longs);
                break;
        }
    }

    /**
     * Decodes 128 integers into 64 {@code longs} such that each long contains two values, each
     * represented with 32 bits. Values [0..63] are encoded in the high-order bits of {@code longs}
     * [0..63], and values [64..127] are encoded in the low-order bits of {@code longs} [0..63]. This
     * representation may allow subsequent operations to be performed on two values at a time.
     */
    //@Override
    public void decodeTo32(int bitsPerValue, long[] in, long[] longs) throws IOException {
        switch (bitsPerValue) {
            case 5:
                decode5(in, tmp, longs);
                expand8To32(longs);
                break;
            case 6:
                decode6(in, tmp, longs);
                expand8To32(longs);
                break;
            default:
                decodeSlow(bitsPerValue, in, tmp, longs);
                break;
        }
    }


    private static void decode5(long[] in, long[] tmp, long[] longs) throws IOException {
        //in.readLongs(tmp, 0, 10);
        System.arraycopy(in, 0, tmp, 0, 10);

        LongVector input = LongVector.fromArray(LongVector.SPECIES_256, tmp, 0);
        input.lanewise(VectorOperators.LSHR, 3).lanewise(VectorOperators.AND, MASK8_5).intoArray(longs, 0);
        //LongVector tail1 = input.lanewise(VectorOperators.AND, MASK8_3);

        input = LongVector.fromArray(LongVector.SPECIES_256, tmp, 4);
        input.lanewise(VectorOperators.LSHR, 3).lanewise(VectorOperators.AND, MASK8_5).intoArray(longs, 4);
        //LongVector tail1 = input.lanewise(VectorOperators.AND, MASK8_3).lanewise(VectorOperators.LSHL, 2);

        LongVector input128 = LongVector.fromArray(LongVector.SPECIES_128, tmp, 8);
        input128.lanewise(VectorOperators.LSHR, 3).lanewise(VectorOperators.AND, MASK8_5).intoArray(longs, 8);
        //LongVector tail2 = input.lanewise(VectorOperators.AND, MASK8_3).lanewise(VectorOperators.LSHL, 2);

        // some bs with masks etc  / /there isn't really a uniform mask that we can apply to each
//        tmpTail = input.lanewise(VectorOperators.AND, MASK8_3);
//        tail.lanewise(VectorOperators.OR, tmpTail);
//        tail.intoArray(longs, 12);

        //shiftLongs(tmp, 10, longs, 0, 3, MASK8_5);
        // remainder
        for (int iter = 0, tmpIdx = 0, longsIdx = 10; iter < 2; ++iter, tmpIdx += 5, longsIdx += 3) {
            long l0 = (tmp[tmpIdx + 0] & MASK8_3) << 2;
            l0 |= (tmp[tmpIdx + 1] >>> 1) & MASK8_2;
            longs[longsIdx + 0] = l0;
            long l1 = (tmp[tmpIdx + 1] & MASK8_1) << 4;
            l1 |= (tmp[tmpIdx + 2] & MASK8_3) << 1;
            l1 |= (tmp[tmpIdx + 3] >>> 2) & MASK8_1;
            longs[longsIdx + 1] = l1;
            long l2 = (tmp[tmpIdx + 3] & MASK8_2) << 3;
            l2 |= (tmp[tmpIdx + 4] & MASK8_3) << 0;
            longs[longsIdx + 2] = l2;
        }
    }

    static final LongVector MASK_6 = LongVector.broadcast(LongVector.SPECIES_256, MASK8_6);
    static final LongVector MASK_2 = LongVector.broadcast(LongVector.SPECIES_256, MASK8_2);

    static void decode6(long[] in, long[] tmp, long[] longs) throws IOException {
        // in.readLongs(tmp, 0, 12);
        System.arraycopy(in, 0, tmp, 0, 12);

        // 256 option
        for (int i = 0; i < 4; i++) {
            int offset = i * 4;
            LongVector input = LongVector.fromArray(LongVector.SPECIES_256, tmp, offset);
            LongVector part1 = input.lanewise(VectorOperators.LSHR, 2).and(MASK_6);
            part1.intoArray(longs, offset);
            LongVector part2 = input.and(MASK_2);
            part2.intoArray(tmp, offset);
        }

        // Assume 256. TODO: need impl for 128, and maybe 512?
//        LongVector input = LongVector.fromArray(LongVector.SPECIES_256, tmp, 0);
//        input.lanewise(VectorOperators.LSHR, 2).lanewise(VectorOperators.AND, MASK8_6).intoArray(longs, 0);
//        input.lanewise(VectorOperators.AND, MASK8_2).intoArray(tmp, 0);
//
//        input = LongVector.fromArray(LongVector.SPECIES_256, tmp, 4);
//        input.lanewise(VectorOperators.LSHR, 2).lanewise(VectorOperators.AND, MASK8_6).intoArray(longs, 4);
//        input.lanewise(VectorOperators.AND, MASK8_2).intoArray(tmp, 4);
//
//        input = LongVector.fromArray(LongVector.SPECIES_256, tmp, 8);
//        input.lanewise(VectorOperators.LSHR, 2).lanewise(VectorOperators.AND, MASK8_6).intoArray(longs, 8);
//        input.lanewise(VectorOperators.AND, MASK8_2).intoArray(tmp, 8);

        //    shiftLongs(tmp, 12, longs, 0, 2, MASK8_6);
        //    shiftLongs(tmp, 12, tmp, 0, 0, MASK8_2);
        for (int iter = 0, tmpIdx = 0, longsIdx = 12; iter < 4; ++iter, tmpIdx += 3, longsIdx += 1) {
            long l0 = tmp[tmpIdx + 0] << 4;
            l0 |= tmp[tmpIdx + 1] << 2;
            l0 |= tmp[tmpIdx + 2] << 0;
            longs[longsIdx + 0] = l0;
        }
    }
}
