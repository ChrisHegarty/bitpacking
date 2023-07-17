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

/**
 * Encodes and decodes integers to and from a densely packed representation.
 */
public interface ForUtil90 {

    int BLOCK_SIZE = 128;

    int BLOCK_SIZE_LOG2 = 7;

    /** Encodes 128 integers from {@code input} into {@code out}. */
    void encode(int[] input, int bitsPerValue, int[] out) throws IOException;

    /** Number of bytes required to encode 128 integers of {@code bitsPerValue} bits per value. */
    default int numBytes(int bitsPerValue) {
        return bitsPerValue << (BLOCK_SIZE_LOG2 - 3);
    }

    /** Decodes 128 integers into {@code output}. */
    void decode(int bitsPerValue, int[] in, int[] output) throws IOException;

    /**
     * Decodes 128 integers into 64 {@code longs} such that each long contains two values, each
     * represented with 32 bits. Values [0..63] are encoded in the high-order bits of {@code longs}
     * [0..63], and values [64..127] are encoded in the low-order bits of {@code longs} [0..63]. This
     * representation may allow subsequent operations to be performed on two values at a time.
     */
    void decodeTo32(int bitsPerValue, int[] in, long[] longs) throws IOException;
}
