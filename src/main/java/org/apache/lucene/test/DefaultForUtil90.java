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

final class DefaultForUtil90 implements ForUtil90 {
    private final int[] tmp = new int[BLOCK_SIZE];

    private final int[] decoded = new int[BLOCK_SIZE];

    private static final int totalBits = 32;



    /** Encode 128 integers from {@code input} into {@code out}. */
    @Override
    public void encode(int[] input, int bitsPerValue, int[] out) throws IOException {
        int MASK = (1 << bitsPerValue) - 1;
        int cur = 0;
        int bitsRemaining = totalBits;
        tmp[0] = 0;
        tmp[1] = 0;
        tmp[2] = 0;
        tmp[3] = 0;
        for (int i = 0; i < input.length; i += 4) {
            tmp[cur] |= (input[i] & MASK) << (totalBits - bitsRemaining);
            tmp[cur + 1] |= (input[i + 1] & MASK) << (totalBits - bitsRemaining);
            tmp[cur + 2] |= (input[i + 2] & MASK) << (totalBits - bitsRemaining);
            tmp[cur + 3] |= (input[i + 3] & MASK) << (totalBits - bitsRemaining);

            bitsRemaining -= bitsPerValue;

            if (bitsRemaining <= 0) {
                cur += 4;
                tmp[cur] = ((input[i] & MASK) >>> (bitsPerValue + bitsRemaining));
                tmp[cur + 1] = ((input[i + 1] & MASK) >>> (bitsPerValue + bitsRemaining));
                tmp[cur + 2] = ((input[i + 2] & MASK) >>> (bitsPerValue + bitsRemaining));
                tmp[cur + 3] = ((input[i + 3] & MASK) >>> (bitsPerValue + bitsRemaining));
                bitsRemaining += totalBits;
            }
        }

        System.arraycopy(tmp, 0, out, 0, 4 * bitsPerValue);
//        for (int i = 0; i < 4 * bitsPerValue; ++i) {
//            //out.writeInt(tmp[i]);
//        }
    }

    /** Decode 128 integers into {@code output}. */
    @Override
    public void decode(int bitsPerValue, int[] in, int[] output) throws IOException {
        // in.readInts(tmp, 0, 4 * bitsPerValue);
        System.arraycopy(in, 0, tmp, 0, 4 * bitsPerValue);
        int MASK = (1 << bitsPerValue) - 1;
        int cur = 0;
        int bitsRemaining = totalBits;

        for (int i = 0; i < 128; i += 4) {
            output[i] = tmp[cur] & MASK;
            output[i + 1] = tmp[cur + 1] & MASK;
            output[i + 2] = tmp[cur + 2] & MASK;
            output[i + 3] = tmp[cur + 3] & MASK;

            tmp[cur] >>>= bitsPerValue;
            tmp[cur + 1] >>>= bitsPerValue;
            tmp[cur + 2] >>>= bitsPerValue;
            tmp[cur + 3] >>>= bitsPerValue;
            bitsRemaining -= bitsPerValue;

            if (bitsRemaining <= 0) {
                cur += 4;
                int PART_MASK = (1 << (-bitsRemaining)) - 1;
                output[i] |= ((tmp[cur] & PART_MASK) << (bitsRemaining + bitsPerValue));
                output[i + 1] |= ((tmp[cur + 1] & PART_MASK) << (bitsRemaining + bitsPerValue));
                output[i + 2] |= ((tmp[cur + 2] & PART_MASK) << (bitsRemaining + bitsPerValue));
                output[i + 3] |= ((tmp[cur + 3] & PART_MASK) << (bitsRemaining + bitsPerValue));

                tmp[cur] >>>= (-bitsRemaining);
                tmp[cur + 1] >>>= (-bitsRemaining);
                tmp[cur + 2] >>>= (-bitsRemaining);
                tmp[cur + 3] >>>= (-bitsRemaining);
                bitsRemaining += totalBits;
            }
        }
        assert cur == 4 * bitsPerValue;
    }

    /**
     * Decodes 128 integers into 64 {@code longs} such that each long contains two values, each
     * represented with 32 bits. Values [0..63] are encoded in the high-order bits of {@code longs}
     * [0..63], and values [64..127] are encoded in the low-order bits of {@code longs} [0..63]. This
     * representation may allow subsequent operations to be performed on two values at a time.
     */
    @Override
    public void decodeTo32(int bitsPerValue, int[] in, long[] longs) throws IOException {
        decode(bitsPerValue, in, decoded);
        for (int i = 0; i < 64; ++i) {
            longs[i] |= decoded[i];
            longs[i] <<= 32;
            longs[i] |= decoded[i + 64];
        }
    }

    // -- old

    private static final long[] MASKS8 = new long[8];
    private static final long[] MASKS16 = new long[16];
    private static final long[] MASKS32 = new long[32];


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


    private static void shiftLongs(long[] a, int count, long[] b, int bi, int shift, long mask) {
        for (int i = 0; i < count; ++i) {
            b[bi + i] = (a[i] >>> shift) & mask;
        }
    }

    private static void decode6(long[] in, long[] tmp, long[] longs) throws IOException {
        //in.readLongs(tmp, 0, 12);
        System.arraycopy(in, 0, tmp, 0, 12);

        shiftLongs(tmp, 12, longs, 0, 2, MASK8_6);
        shiftLongs(tmp, 12, tmp, 0, 0, MASK8_2);
        for (int iter = 0, tmpIdx = 0, longsIdx = 12; iter < 4; ++iter, tmpIdx += 3, longsIdx += 1) {
            long l0 = tmp[tmpIdx + 0] << 4;
            l0 |= tmp[tmpIdx + 1] << 2;
            l0 |= tmp[tmpIdx + 2] << 0;
            longs[longsIdx + 0] = l0;
        }
    }
}
