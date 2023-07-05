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

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

import static org.apache.lucene.test.SimdBitPacking.simdPack;
import static org.apache.lucene.test.SimdBitPacking.simdUnpack;
import static org.junit.Assert.assertArrayEquals;

public class TestSimdBitPacking {

  final Random random = new Random();

    @Test
    public void packUnpack() {
        for(int bitsPerValue = 1; bitsPerValue < 31; bitsPerValue++) {
            int[] packed = new int[bitsPerValue * 4];
            for (int i = 0; i < 100; i++) {
                int finalBitsPerValue = bitsPerValue;
                int[] input = IntStream.range(0, 128).map(x -> random.nextInt(1 << finalBitsPerValue)).toArray();
                int[] copy = Arrays.copyOf(input, input.length);
                simdPack(input, packed, bitsPerValue);
                int[] unpacked = new int[128];
                simdUnpack(packed, unpacked, bitsPerValue);
                assertArrayEquals(input, unpacked);
                assertArrayEquals(input, copy);
            }
        }

        int[] packed = new int[31 * 4];
        for (int i = 0; i < 100; i++) {
            int[] input = IntStream.range(0, 128).map(x -> random.nextInt(0x40000000, Integer.MAX_VALUE)).toArray();
            int[] copy = Arrays.copyOf(input, input.length);
            simdPack(input, packed, 31);
            int[] unpacked = new int[128];
            simdUnpack(packed, unpacked, 31);
            assertArrayEquals(input, unpacked);
            assertArrayEquals(input, copy);
        }

    }
}
