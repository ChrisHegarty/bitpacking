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

import jdk.incubator.vector.IntVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;

/**
 * Supports packing and unpacking integers arrays of 128 elements, using 128
 * bit vectors with 4 int lanes.
 * Based on https://github.com/lemire/simdcomp/blob/master/src/simdbitpacking.c
 *
 * So far only:
 * a) 1 - 5 bits per element value are supported. 6 - 32 just need to be implemented.
 * b) The code is a direct port of the C variant. Likely there are better shapes of code, less
 *    unrolling, etc, that might optimize better on the JVM. Doing so should be driven by
 *    benchmarks. For now, a direct port seems like a good start.
 */
public class SimdBitPacking {

  // simdpackwithoutmask(const uint32_t *in, __m128i *out, const uint32_t bit) {
  /* Assumes that integers fit in the prescribed number of bits */
  static void simdPack(int[] input, int[] output, int bit) {
    switch (bit) {
      case 1: SIMD_fastPack1(input, output); return;
      case 2: SIMD_fastPack2(input, output); return;
      case 3: SIMD_fastPack3(input, output); return;
      case 4: SIMD_fastPack4(input, output); return;
      case 5: SIMD_fastPack5(input, output); return;
      case 6: SIMD_fastPack6(input, output); return;
      case 7: SIMD_fastPack7(input, output); return;
      case 8: SIMD_fastPack8(input, output); return;
      case 9: SIMD_fastPack9(input, output); return;
      case 10: SIMD_fastPack10(input, output); return;
      case 11: SIMD_fastPack11(input, output); return;
      case 12: SIMD_fastPack12(input, output); return;
      case 13: SIMD_fastPack13(input, output); return;
      case 14: SIMD_fastPack14(input, output); return;
      case 15: SIMD_fastPack15(input, output); return;
      case 16: SIMD_fastPack16(input, output); return;
      case 17: SIMD_fastPack17(input, output); return;
      case 18: SIMD_fastPack18(input, output); return;
      case 19: SIMD_fastPack19(input, output); return;
      case 20: SIMD_fastPack20(input, output); return;
      case 21: SIMD_fastPack21(input, output); return;
      case 22: SIMD_fastPack22(input, output); return;
      case 23: SIMD_fastPack23(input, output); return;
      case 24: SIMD_fastPack24(input, output); return;
      case 25: SIMD_fastPack25(input, output); return;
      case 26: SIMD_fastPack26(input, output); return;
      case 27: SIMD_fastPack27(input, output); return;
      case 28: SIMD_fastPack28(input, output); return;
      case 29: SIMD_fastPack29(input, output); return;
      case 30: SIMD_fastPack30(input, output); return;
      case 31: SIMD_fastPack31(input, output); return;
      default : throw new UnsupportedOperationException();
    }
  }

  // void simdunpack(const __m128i *in, uint32_t *out, const uint32_t bit) {
  static void simdUnpack(int[] input, int[] output, int bit) {
    switch (bit) {
      case 1: SIMD_fastUnpack1(input, output); return;
      case 2: SIMD_fastUnpack2(input, output); return;
      case 3: SIMD_fastUnpack3(input, output); return;
      case 4: SIMD_fastUnpack4(input, output); return;
      case 5: SIMD_fastUnpack5(input, output); return;
      case 6: SIMD_fastUnpack6(input, output); return;
      case 7: SIMD_fastUnpack7(input, output); return;
      case 8: SIMD_fastUnpack8(input, output); return;
      case 9: SIMD_fastUnpack9(input, output); return;
      case 10: SIMD_fastUnpack10(input, output); return;
      case 11: SIMD_fastUnpack11(input, output); return;
      case 12: SIMD_fastUnpack12(input, output); return;
      case 13: SIMD_fastUnpack13(input, output); return;
      case 14: SIMD_fastUnpack14(input, output); return;
      case 15: SIMD_fastUnpack15(input, output); return;
      case 16: SIMD_fastUnpack16(input, output); return;
      case 17: SIMD_fastUnpack17(input, output); return;
      case 18: SIMD_fastUnpack18(input, output); return;
      case 19: SIMD_fastUnpack19(input, output); return;
      case 20: SIMD_fastUnpack20(input, output); return;
      case 21: SIMD_fastUnpack21(input, output); return;
      case 22: SIMD_fastUnpack22(input, output); return;
      case 23: SIMD_fastUnpack23(input, output); return;
      case 24: SIMD_fastUnpack24(input, output); return;
      case 25: SIMD_fastUnpack25(input, output); return;
      case 26: SIMD_fastUnpack26(input, output); return;
      case 27: SIMD_fastUnpack27(input, output); return;
      case 28: SIMD_fastUnpack28(input, output); return;
      case 29: SIMD_fastUnpack29(input, output); return;
      case 30: SIMD_fastUnpack30(input, output); return;
      case 31: SIMD_fastUnpack31(input, output); return;
      default : throw new UnsupportedOperationException();
    }
  }

  private static final VectorSpecies<Integer> SPECIES_128 = IntVector.SPECIES_128;

  // SIMD_fastpackwithoutmask1_32
  static void SIMD_fastPack1(int[] input, int[] output) {
    int inOff = 0;
    IntVector outVec;
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, 0);

    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 1).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 2).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 3).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 5).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 6).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 7).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 9).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 10).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 11).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 13).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 14).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 15).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 17).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 18).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 19).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 21).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 22).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 23).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 25).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 26).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 27).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 29).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 30).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 31).or(outVec);
    outVec.intoArray(output, 0);
  }

  // SIMD_fastpackwithoutmask2_32
  static void SIMD_fastPack2(int[] input, int[] output) {
    int inOff = 0;
    int outOff = 0;
    IntVector outVec;
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, 0);

    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 2).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 6).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 10).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 14).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 18).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 22).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 26).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 30).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 2).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 6).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 10).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 14).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 18).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 22).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 26).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 30).or(outVec);
    outVec.intoArray(output, outOff);
  }

  // SIMD_fastpackwithoutmask3_32
  static void SIMD_fastPack3(int[] input, int[] output) {
    int inOff = 0;
    int outOff = 0;

    IntVector outVec;
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, inOff);

    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 3).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 6).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 9).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 15).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 18).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 21).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 27).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 30).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 3 - 1);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 1).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 7).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 10).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 13).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 19).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 22).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 25).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 31).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 3 - 2);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 2).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 5).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 11).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 14).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 17).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 23).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 26).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 29).or(outVec);
    outVec.intoArray(output, outOff);
  }

  // SIMD_fastpackwithoutmask4_32
  static void SIMD_fastPack4(int[] input, int[] output) {
    int inOff = 0;
    int outOff = 0;
    IntVector outVec;
    IntVector inVec;

    for (int i = 0; i < 4; i++) {
      inVec = IntVector.fromArray(SPECIES_128, input, inOff);
      outVec = inVec;

      inVec = IntVector.fromArray(SPECIES_128, input, inOff + 4);
      outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);

      inVec = IntVector.fromArray(SPECIES_128, input, inOff + 8);
      outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);

      inVec = IntVector.fromArray(SPECIES_128, input, inOff + 12);
      outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);

      inVec = IntVector.fromArray(SPECIES_128, input, inOff + 16);
      outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);

      inVec = IntVector.fromArray(SPECIES_128, input, inOff + 20);
      outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);

      inVec = IntVector.fromArray(SPECIES_128, input, inOff + 24);
      outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);

      inVec = IntVector.fromArray(SPECIES_128, input, inOff + 28);
      outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);

      outVec.intoArray(output, outOff);
      outOff+=4;
      inOff+=32;
    }
  }

  // SIMD_fastpackwithoutmask5_32
  static void SIMD_fastPack5(int[] input, int[] output) {
    int inOff = 0;
    int outOff = 0;

    IntVector outVec;
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, inOff);

    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 5).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 10).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 15).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 25).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 30).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 5 - 3);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 3).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 13).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 18).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 23).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 5 - 1);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 1).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 6).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 11).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 21).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 26).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 31).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 5 - 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 9).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 14).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 19).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 29).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 5 - 2);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 2).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 7).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 17).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 22).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 27).or(outVec);
    outVec.intoArray(output, outOff);
  }

  // SIMD_fastpackwithoutmask6_32
  static void SIMD_fastPack6(int[] input, int[] output) {
    int inOff = 0;
    int outOff = 0;

    IntVector outVec;
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, inOff);

    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 6).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 18).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 30).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 2);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 10).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 22).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 2).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 14).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 26).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 6).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 18).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 30).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 2);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 10).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 22).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 2).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 14).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 26).or(outVec);
    outVec.intoArray(output, outOff);
  }

  // SIMD_fastpackwithoutmask7_32
  static void SIMD_fastPack7(int[] input, int[] output) {
    int inOff = 0;
    int outOff = 0;

    IntVector outVec;
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, inOff);

    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 7).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 14).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 21).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 3).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 10).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 17).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 31).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 1);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 6).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 13).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 27).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 5);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 2).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 9).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 23).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 30).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 2);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 5).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 19).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 26).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 6);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 1).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 15).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 22).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 29).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 3);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 11).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 18).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 25).or(outVec);
    outVec.intoArray(output, outOff);
  }

  // SIMD_fastpackwithoutmask8_32
  static void SIMD_fastPack8(int[] input, int[] output) {
    int inOff = 0;
    int outOff = 0;

    IntVector outVec;
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, inOff);

    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
  }

  // SIMD_fastpackwithoutmask9_32
  static void SIMD_fastPack9(int[] input, int[] output) {
    int inOff = 0;
    int outOff = 0;

    IntVector outVec;
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, inOff);

    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 9).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 18).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 27).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 5);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 13).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 22).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 31).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 1);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 17).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 26).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 6);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 3).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 21).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 30).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 2);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 7).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 25).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 7);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 2).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 11).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 29).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 3);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 6).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 15).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 1).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 10).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 19).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 5).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 14).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 23).or(outVec);
    outVec.intoArray(output, outOff);
  }

  // SIMD_fastpackwithoutmask10_32
  static void SIMD_fastPack10(int[] input, int[] output) {
    int inOff = 0;
    int outOff = 0;

    IntVector outVec;
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, inOff);

    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 10).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 30).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 2);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 18).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 6).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 26).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 6);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 14).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 2).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 22).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 10).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 30).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 2);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 18).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 6).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 26).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 6);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 14).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 2).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 22).or(outVec);
    outVec.intoArray(output, outOff);
  }

  // SIMD_fastpackwithoutmask11_32
  static void SIMD_fastPack11(int[] input, int[] output) {
    int inOff = 0;
    int outOff = 0;

    IntVector outVec;
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, inOff);

    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 11).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 22).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 10);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 1).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 23).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 9);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 2).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 13).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 3).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 14).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 25).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 7);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 15).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 26).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 6);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 5).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 27).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 5);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 6).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 17).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 7).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 18).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 29).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 3);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 19).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 30).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 2);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 9).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 31).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 1);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 10).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 21).or(outVec);
    outVec.intoArray(output, outOff);
  }

  // SIMD_fastpackwithoutmask12_32
  static void SIMD_fastPack12(int[] input, int[] output) {
    int inOff = 0;
    int outOff = 0;

    IntVector outVec;
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, inOff);

    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    outVec.intoArray(output, outOff);
  }

  // SIMD_fastpackwithoutmask13_32
  static void SIMD_fastPack13(int[] input, int[] output) {
    int inOff = 0;
    int outOff = 0;

    IntVector outVec;
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, inOff);

    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 13).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 26).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 6);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 7).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 1).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 14).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 27).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 5);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 21).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 11);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 2).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 15).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 9).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 22).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 10);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 3).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 29).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 3);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 10).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 23).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 9);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 17).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 30).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 2);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 11).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 5).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 18).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 31).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 1);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 25).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 7);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 6).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 19).or(outVec);
    outVec.intoArray(output, outOff);
  }

  // SIMD_fastpackwithoutmask14_32
  static void SIMD_fastPack14(int[] input, int[] output) {
    int inOff = 0;
    int outOff = 0;

    IntVector outVec;
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, inOff);

    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 14).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 10).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 6).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 2).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 30).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 2);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 26).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 6);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 22).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 10);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 18).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 14).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 10).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 6).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 2).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 30).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 2);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 26).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 6);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 22).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 10);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 18).or(outVec);
    outVec.intoArray(output, outOff);
  }

  // SIMD_fastpackwithoutmask15_32
  static void SIMD_fastPack15(int[] input, int[] output) {
    int inOff = 0;
    int outOff = 0;

    IntVector outVec;
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, inOff);

    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 15).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 30).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 2);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 13).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 11).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 26).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 6);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 9).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 7).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 22).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 10);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 5).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 3).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 18).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 14);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 1).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 31).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 1);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 14).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 29).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 3);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 27).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 5);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 10).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 25).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 7);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 23).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 9);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 6).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 21).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 11);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 19).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 13);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 2).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 17).or(outVec);
    outVec.intoArray(output, outOff);
  }

  // SIMD_fastpackwithoutmask16_32
  static void SIMD_fastPack16(int[] input, int[] output) {
    int inOff = 0;
    int outOff = 0;

    IntVector outVec;
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, inOff);

    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    outVec.intoArray(output, outOff);
  }

  // SIMD_fastpackwithoutmask17_32
  static void SIMD_fastPack17(int[] input, int[] output) {
    int inOff = 0;
    int outOff = 0;

    IntVector outVec;
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, inOff);

    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 17).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 15);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 2).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 19).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 13);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 21).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 11);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 6).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 23).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 9);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 25).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 7);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 10).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 27).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 5);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 29).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 3);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 14).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 31).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 1);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 1).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 18).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 14);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 3).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 5).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 22).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 10);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 7).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 9).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 26).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 6);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 11).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 13).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 30).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 2);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 15).or(outVec);
    outVec.intoArray(output, outOff);
  }

  // SIMD_fastpackwithoutmask18_32
  static void SIMD_fastPack18(int[] input, int[] output) {
    int inOff = 0;
    int outOff = 0;

    IntVector outVec;
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, inOff);

    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 18).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 14);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 22).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 10);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 26).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 6);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 30).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 2);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 2).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 6).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 10).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 14).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 18).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 14);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 22).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 10);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 26).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 6);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 30).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 2);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 2).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 6).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 10).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 14).or(outVec);
    outVec.intoArray(output, outOff);
  }

  // SIMD_fastpackwithoutmask19_32
  static void SIMD_fastPack19(int[] input, int[] output) {
    int inOff = 0;
    int outOff = 0;

    IntVector outVec;
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, inOff);

    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 19).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 13);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 6).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 25).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 7);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 31).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 1);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 18).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 14);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 5).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 11).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 30).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 2);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 17).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 15);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 23).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 9);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 10).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 29).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 3);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 3).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 22).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 10);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 9).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 15).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 17);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 2).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 21).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 11);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 27).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 5);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 14).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 18);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 1).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 7).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 26).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 6);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 13).or(outVec);
    outVec.intoArray(output, outOff);
  }

  // SIMD_fastpackwithoutmask20_32
  static void SIMD_fastPack20(int[] input, int[] output) {
    int inOff = 0;
    int outOff = 0;

    IntVector outVec;
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, inOff);

    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    outVec.intoArray(output, outOff);
  }

  // SIMD_fastpackwithoutmask21_32
  static void SIMD_fastPack21(int[] input, int[] output) {
    int inOff = 0;
    int outOff = 0;

    IntVector outVec;
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, inOff);

    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 21).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 11);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 10).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 31).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 1);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 9).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 30).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 2);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 19).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 13);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 29).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 3);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 18).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 14);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 7).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 17).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 15);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 6).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 27).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 5);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 5).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 26).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 6);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 15).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 17);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 25).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 7);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 14).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 18);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 3).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 13).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 19);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 2).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 23).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 9);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 1).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 22).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 10);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 11).or(outVec);
    outVec.intoArray(output, outOff);
  }

  // SIMD_fastpackwithoutmask22_32
  static void SIMD_fastPack22(int[] input, int[] output) {
    int inOff = 0;
    int outOff = 0;

    IntVector outVec;
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, inOff);

    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 22).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 10);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 2).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 14).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 18);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 26).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 6);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 6).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 18).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 14);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 30).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 2);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 10).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 22).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 10);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 2).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 14).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 18);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 26).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 6);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 6).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 18).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 14);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 30).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 2);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 10).or(outVec);
    outVec.intoArray(output, outOff);
  }

  // SIMD_fastpackwithoutmask23_32
  static void SIMD_fastPack23(int[] input, int[] output) {
    int inOff = 0;
    int outOff = 0;

    IntVector outVec;
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, inOff);

    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 23).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 9);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 14).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 18);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 5).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 19).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 13);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 10).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 22);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 1).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 15).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 17);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 6).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 29).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 3);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 11).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 21);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 2).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 25).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 7);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 7).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 30).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 2);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 21).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 11);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 3).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 26).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 6);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 17).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 15);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 31).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 1);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 22).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 10);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 13).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 19);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 27).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 5);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 18).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 14);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 9).or(outVec);
    outVec.intoArray(output, outOff);
  }

  // SIMD_fastpackwithoutmask24_32
  static void SIMD_fastPack24(int[] input, int[] output) {
    int inOff = 0;
    int outOff = 0;

    IntVector outVec;
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, inOff);

    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    outVec.intoArray(output, outOff);
  }

  // SIMD_fastpackwithoutmask25_32
  static void SIMD_fastPack25(int[] input, int[] output) {
    int inOff = 0;
    int outOff = 0;

    IntVector outVec;
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, inOff);

    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 25).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 7);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 18).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 14);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 11).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 21);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 29).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 3);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 22).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 10);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 15).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 17);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 1).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 26).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 6);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 19).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 13);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 5).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 30).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 2);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 23).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 9);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 9).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 23);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 2).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 27).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 5);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 13).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 19);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 6).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 31).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 1);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 17).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 15);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 10).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 22);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 3).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 21).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 11);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 14).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 18);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 7).or(outVec);
    outVec.intoArray(output, outOff);
  }

  // SIMD_fastpackwithoutmask26_32
  static void SIMD_fastPack26(int[] input, int[] output) {
    int inOff = 0;
    int outOff = 0;

    IntVector outVec;
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, inOff);

    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 26).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 6);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 14).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 18);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 2).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 22).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 10);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 10).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 22);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 30).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 2);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 18).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 14);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 6).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 26).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 6);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 14).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 18);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 2).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 22).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 10);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 10).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 22);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 30).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 2);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 18).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 14);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 6).or(outVec);
    outVec.intoArray(output, outOff);
  }

  // SIMD_fastpackwithoutmask27_32
  static void SIMD_fastPack27(int[] input, int[] output) {
    int inOff = 0;
    int outOff = 0;

    IntVector outVec;
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, inOff);

    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 27).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 5);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 22).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 10);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 17).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 15);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 7).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 25);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 2).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 29).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 3);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 19).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 13);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 14).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 18);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 9).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 23);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 31).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 1);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 26).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 6);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 21).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 11);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 11).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 21);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 6).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 26);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 1).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 23).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 9);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 18).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 14);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 13).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 19);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 3).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 30).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 2);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 25).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 7);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 15).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 17);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 10).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 22);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 5).or(outVec);
    outVec.intoArray(output, outOff);
  }

  // SIMD_fastpackwithoutmask28_32
  static void SIMD_fastPack28(int[] input, int[] output) {
    int inOff = 0;
    int outOff = 0;

    IntVector outVec;
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, inOff);

    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    outVec.intoArray(output, outOff);
  }

  // SIMD_fastpackwithoutmask29_32
  static void SIMD_fastPack29(int[] input, int[] output) {
    int inOff = 0;
    int outOff = 0;

    IntVector outVec;
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, inOff);

    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 29).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 3);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 26).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 6);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 23).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 9);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 17).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 15);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 14).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 18);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 11).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 21);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 5).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 27);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 2).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 31).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 1);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 25).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 7);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 22).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 10);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 19).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 13);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 13).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 19);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 10).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 22);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 7).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 25);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 1).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 30).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 2);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 27).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 5);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 21).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 11);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 18).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 14);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 15).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 17);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 9).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 23);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 6).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 26);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 3).or(outVec);
    outVec.intoArray(output, outOff);
  }

  // SIMD_fastpackwithoutmask30_32
  static void SIMD_fastPack30(int[] input, int[] output) {
    int inOff = 0;
    int outOff = 0;

    IntVector outVec;
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, inOff);

    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 30).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 2);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 26).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 6);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 22).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 10);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 18).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 14);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 14).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 18);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 10).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 22);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 6).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 26);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 2).or(outVec);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);



    outVec = inVec.lanewise(VectorOperators.LSHL, 30).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 2);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 26).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 6);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 22).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 10);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 18).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 14);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 14).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 18);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 10).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 22);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 6).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 26);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 2).or(outVec);
    outVec.intoArray(output, outOff);
  }

  // SIMD_fastpackwithoutmask31_32
  static void SIMD_fastPack31(int[] input, int[] output) {
    int inOff = 0;
    int outOff = 0;

    IntVector outVec;
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, inOff);

    outVec = inVec;
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 31).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 1);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 30).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 2);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 29).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 3);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 28).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 27).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 5);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 26).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 6);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 25).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 7);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 24).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 23).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 9);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 22).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 10);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 21).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 11);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 20).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 19).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 13);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 18).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 14);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 17).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 15);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 16).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 15).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 17);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 14).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 18);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 13).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 19);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 12).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 11).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 21);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 10).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 22);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 9).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 23);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 8).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 7).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 25);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 6).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 26);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 5).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 27);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 4).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 3).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 29);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 2).or(outVec);
    outVec.intoArray(output, outOff);
    outOff+=4;
    outVec = inVec.lanewise(VectorOperators.LSHR, 30);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHL, 1).or(outVec);
    outVec.intoArray(output, outOff);
  }

  // __SIMD_fastunpack1_32
  static void SIMD_fastUnpack1(int[] input, int[] output) {
    IntVector inVec1 = IntVector.fromArray(SPECIES_128, input, 0);
    IntVector inVec2 = inVec1;
    IntVector outVec1, outVec2, outVec3, outVec4;
    final int mask = 1;
    int shift = 0;

    for (int i = 0; i < 8; i++) {
      outVec1 = inVec1.lanewise(VectorOperators.LSHR, shift++).and(mask);
      outVec2 = inVec2.lanewise(VectorOperators.LSHR, shift++).and(mask);
      outVec3 = inVec1.lanewise(VectorOperators.LSHR, shift++).and(mask);
      outVec4 = inVec2.lanewise(VectorOperators.LSHR, shift++).and(mask);
      outVec1.intoArray(output, i * 16 + 0);
      outVec2.intoArray(output, i * 16 + 4);
      outVec3.intoArray(output, i * 16 + 8);
      outVec4.intoArray(output, i * 16 + 12);
    }
  }

  // __SIMD_fastunpack2_32
  static void SIMD_fastUnpack2(int[] input, int[] output) {
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, 0);
    IntVector outVec;
    int inOff = 0;
    int outOff = 0;
    final int mask = (1 << 2) - 1;

    outVec = inVec.and(mask);
    outVec.intoArray(output, outOff);

    outVec = inVec.lanewise(VectorOperators.LSHR, 2).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 6).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 10).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 14).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 18).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 22).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 26).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 30).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 2).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 6).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 10).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 14).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 18).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 22).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 26).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 30).and(mask);
    outVec.intoArray(output, outOff+=4);
  }

  // __SIMD_fastunpack3_32
  static void SIMD_fastUnpack3(int[] input, int[] output) {
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, 0);
    IntVector outVec;
    int inOff = 0;
    int outOff = 0;
    final int mask = (1 << 3) - 1;

    outVec = inVec.and(mask);
    outVec.intoArray(output, outOff);

    outVec = inVec.lanewise(VectorOperators.LSHR, 3).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 6).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 9).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 15).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 18).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 21).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 27).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 30);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 3 - 1).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 1).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 7).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 10).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 13).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 19).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 22).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 25).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 31);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 3 - 2).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 2).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 5).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 11).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 14).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 17).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 23).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 26).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 29).and(mask);
    outVec.intoArray(output, outOff+=4);
  }

  // __SIMD_fastunpack4_32
  static void SIMD_fastUnpack4(int[] input, int[] output) {
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, 0);
    IntVector outVec;
    int inOff = 0;
    int outOff = 0;
    final int mask = (1 << 4) - 1;

    outVec = inVec.and(mask);
    outVec.intoArray(output, outOff);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28).and(mask);
    outVec.intoArray(output, outOff+=4);
  }

  // __SIMD_fastunpack5_32
  static void SIMD_fastUnpack5(int[] input, int[] output) {
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, 0);
    IntVector outVec;
    int inOff = 0;
    int outOff = 0;
    final int mask = (1 << 5) - 1;

    outVec = inVec.and(mask);
    outVec.intoArray(output, outOff);

    outVec = inVec.lanewise(VectorOperators.LSHR, 5).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 10).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 15).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 25).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 30);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 5 - 3).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 3).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 13).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 18).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 23).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 5 - 1).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 1).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 6).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 11).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 21).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 26).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 31);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 5 - 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 9).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 14).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 19).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 29).and(mask);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 5 - 2).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 2).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 7).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 17).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 22).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 27).and(mask);
    outVec.intoArray(output, outOff+=4);
  }

  // __SIMD_fastunpack6_32
  static void SIMD_fastUnpack6(int[] input, int[] output) {
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, 0);
    IntVector outVec;
    int inOff = 0;
    int outOff = 0;
    final int mask = (1 << 6) - 1;

    outVec = inVec.and(mask);
    outVec.intoArray(output, outOff);

    outVec = inVec.lanewise(VectorOperators.LSHR, 6).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 18).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 30);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 2).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 10).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 22).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 2).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 14).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 26).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 6).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 18).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 30);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 2).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 10).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 22).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 2).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 14).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 26);
    outVec.intoArray(output, outOff+=4);
  }

  // __SIMD_fastunpack7_32
  static void SIMD_fastUnpack7(int[] input, int[] output) {
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, 0);
    IntVector outVec;
    int inOff = 0;
    int outOff = 0;
    final int mask = (1 << 7) - 1;

    outVec = inVec.and(mask);
    outVec.intoArray(output, outOff);

    outVec = inVec.lanewise(VectorOperators.LSHR, 7).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 14).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 21).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 3).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 10).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 17).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 31);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 1).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 6).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 13).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 27);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 5).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 2).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 9).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 23).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 30);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 2).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 5).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 19).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 26);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 6).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 1).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 15).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 22).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 29);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 3).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 11).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 18).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 25);
    outVec.intoArray(output, outOff+=4);
  }

  // __SIMD_fastunpack8_32
  static void SIMD_fastUnpack8(int[] input, int[] output) {
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, 0);
    IntVector outVec;
    int inOff = 0;
    int outOff = 0;
    final int mask = (1 << 8) - 1;

    outVec = inVec.and(mask);
    outVec.intoArray(output, outOff);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    outVec.intoArray(output, outOff+=4);
  }

  // __SIMD_fastunpack9_32
  static void SIMD_fastUnpack9(int[] input, int[] output) {
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, 0);
    IntVector outVec;
    int inOff = 0;
    int outOff = 0;
    final int mask = (1 << 9) - 1;

    outVec = inVec.and(mask);
    outVec.intoArray(output, outOff);

    outVec = inVec.lanewise(VectorOperators.LSHR, 9).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 18).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 27);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 5).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 13).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 22).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 31);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 1).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 17).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 26);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 6).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 3).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 21).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 30);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 2).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 7).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 25);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 7).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 2).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 11).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 29);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 3).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 6).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 15).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 1).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 10).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 19).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 5).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 14).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 23);
    outVec.intoArray(output, outOff+=4);
  }

  // __SIMD_fastunpack10_32
  static void SIMD_fastUnpack10(int[] input, int[] output) {
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, 0);
    IntVector outVec;
    int inOff = 0;
    int outOff = 0;
    final int mask = (1 << 10) - 1;

    outVec = inVec.and(mask);
    outVec.intoArray(output, outOff);

    outVec = inVec.lanewise(VectorOperators.LSHR, 10).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 30);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 2).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 18).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 6).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 26);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 6).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 14).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 2).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 22).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 10).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 30);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 2).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 18).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 6).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 26);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 6).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 14).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 2).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 22);
    outVec.intoArray(output, outOff+=4);
  }

  // __SIMD_fastunpack11_32
  static void SIMD_fastUnpack11(int[] input, int[] output) {
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, 0);
    IntVector outVec;
    int inOff = 0;
    int outOff = 0;
    final int mask = (1 << 11) - 1;

    outVec = inVec.and(mask);
    outVec.intoArray(output, outOff);

    outVec = inVec.lanewise(VectorOperators.LSHR, 11).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 22);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 10).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 1).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 23);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 9).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 2).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 13).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 3).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 14).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 25);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 7).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 15).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 26);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 6).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 5).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 27);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 5).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 6).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 17).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 7).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 18).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 29);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 3).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 19).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 30);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 2).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 9).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 31);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 1).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 10).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 21);
    outVec.intoArray(output, outOff+=4);
  }

  // __SIMD_fastunpack12_32
  static void SIMD_fastUnpack12(int[] input, int[] output) {
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, 0);
    IntVector outVec;
    int inOff = 0;
    int outOff = 0;
    final int mask = (1 << 12) - 1;

    outVec = inVec.and(mask);
    outVec.intoArray(output, outOff);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    outVec.intoArray(output, outOff+=4);
  }

  // __SIMD_fastunpack13_32
  static void SIMD_fastUnpack13(int[] input, int[] output) {
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, 0);
    IntVector outVec;
    int inOff = 0;
    int outOff = 0;
    final int mask = (1 << 13) - 1;

    outVec = inVec.and(mask);
    outVec.intoArray(output, outOff);

    outVec = inVec.lanewise(VectorOperators.LSHR, 13).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 26);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 6).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 7).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 12).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 1).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 14).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 27);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 5).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 21);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 11).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 2).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 15).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 9).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 22);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 10).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 3).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 29);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 3).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 10).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 23);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 9).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 17).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 30);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 2).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 11).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 5).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 18).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 31);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 1).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 25);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 7).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 6).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 19);
    outVec.intoArray(output, outOff+=4);
  }

  // __SIMD_fastunpack14_32
  static void SIMD_fastUnpack14(int[] input, int[] output) {
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, 0);
    IntVector outVec;
    int inOff = 0;
    int outOff = 0;
    final int mask = (1 << 14) - 1;

    outVec = inVec.and(mask);
    outVec.intoArray(output, outOff);

    outVec = inVec.lanewise(VectorOperators.LSHR, 14).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 10).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 6).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 12).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 2).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 30);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 2).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 26);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 6).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 22);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 10).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 18).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 14).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 10).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 6).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 12).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 2).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 30);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 2).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 26);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 6).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 22);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 10).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 18);
    outVec.intoArray(output, outOff+=4);
  }

  // __SIMD_fastunpack15_32
  static void SIMD_fastUnpack15(int[] input, int[] output) {
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, 0);
    IntVector outVec;
    int inOff = 0;
    int outOff = 0;
    final int mask = (1 << 15) - 1;

    outVec = inVec.and(mask);
    outVec.intoArray(output, outOff);

    outVec = inVec.lanewise(VectorOperators.LSHR, 15).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 30);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 2).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 13).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 11).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 26);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 6).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 9).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 7).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 22);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 10).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 5).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 12).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 3).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 18);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 14).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 1).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 31);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 1).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 14).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 29);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 3).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 27);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 5).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 10).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 25);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 7).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 23);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 9).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 6).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 21);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 11).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 19);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 13).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 2).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 17);
    outVec.intoArray(output, outOff+=4);
  }

  // __SIMD_fastunpack16_32
  static void SIMD_fastUnpack16(int[] input, int[] output) {
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, 0);
    IntVector outVec;
    int inOff = 0;
    int outOff = 0;
    final int mask = (1 << 16) - 1;

    outVec = inVec.and(mask);
    outVec.intoArray(output, outOff);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    outVec.intoArray(output, outOff+=4);
  }

  // __SIMD_fastunpack17_32
  static void SIMD_fastUnpack17(int[] input, int[] output) {
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, 0);
    IntVector outVec;
    int inOff = 0;
    int outOff = 0;
    final int mask = (1 << 17) - 1;

    outVec = inVec.and(mask);
    outVec.intoArray(output, outOff);

    outVec = inVec.lanewise(VectorOperators.LSHR, 17);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 15).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 2).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 19);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 13).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 21);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 11).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 6).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 23);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 9).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 25);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 7).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 10).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 27);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 5).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 29);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 3).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 14).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 31);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 1).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 16).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 1).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 18);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 14).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 3).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 12).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 5).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 22);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 10).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 7).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 9).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 26);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 6).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 11).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 13).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 30);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 2).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 15);
    outVec.intoArray(output, outOff+=4);
  }

  // __SIMD_fastunpack18_32
  static void SIMD_fastUnpack18(int[] input, int[] output) {
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, 0);
    IntVector outVec;
    int inOff = 0;
    int outOff = 0;
    final int mask = (1 << 18) - 1;

    outVec = inVec.and(mask);
    outVec.intoArray(output, outOff);

    outVec = inVec.lanewise(VectorOperators.LSHR, 18);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 14).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 22);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 10).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 26);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 6).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 30);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 2).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 16).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 2).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 12).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 6).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 10).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 14).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 18);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 14).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 22);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 10).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 26);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 6).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 30);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 2).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 16).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 2).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 12).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 6).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 10).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 14);
    outVec.intoArray(output, outOff+=4);
  }

  // __SIMD_fastunpack19_32
  static void SIMD_fastUnpack19(int[] input, int[] output) {
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, 0);
    IntVector outVec;
    int inOff = 0;
    int outOff = 0;
    final int mask = (1 << 19) - 1;

    outVec = inVec.and(mask);
    outVec.intoArray(output, outOff);

    outVec = inVec.lanewise(VectorOperators.LSHR, 19);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 13).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 6).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 25);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 7).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 31);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 1).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 18);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 14).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 5).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 11).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 30);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 2).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 17);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 15).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 23);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 9).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 10).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 29);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 3).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 16).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 3).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 22);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 10).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 9).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 15);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 17).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 2).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 21);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 11).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 27);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 5).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 14);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 18).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 1).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 12).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 7).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 26);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 6).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 13);
    outVec.intoArray(output, outOff+=4);
  }

  // __SIMD_fastunpack20_32
  static void SIMD_fastUnpack20(int[] input, int[] output) {
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, 0);
    IntVector outVec;
    int inOff = 0;
    int outOff = 0;
    final int mask = (1 << 20) - 1;

    outVec = inVec.and(mask);
    outVec.intoArray(output, outOff);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 12).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 16).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 12).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 16).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 12).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 16).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 12).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 16).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    outVec.intoArray(output, outOff+=4);
  }

  // __SIMD_fastunpack21_32
  static void SIMD_fastUnpack21(int[] input, int[] output) {
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, 0);
    IntVector outVec;
    int inOff = 0;
    int outOff = 0;
    final int mask = (1 << 21) - 1;

    outVec = inVec.and(mask);
    outVec.intoArray(output, outOff);

    outVec = inVec.lanewise(VectorOperators.LSHR, 21);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 11).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 10).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 31);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 1).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 12).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 9).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 30);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 2).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 19);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 13).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 29);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 3).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 18);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 14).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 7).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 17);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 15).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 6).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 27);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 5).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 16).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 5).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 26);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 6).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 15);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 17).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 25);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 7).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 14);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 18).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 3).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 13);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 19).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 2).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 23);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 9).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 20).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 1).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 22);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 10).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 11);
    outVec.intoArray(output, outOff+=4);
  }

  // __SIMD_fastunpack22_32
  static void SIMD_fastUnpack22(int[] input, int[] output) {
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, 0);
    IntVector outVec;
    int inOff = 0;
    int outOff = 0;
    final int mask = (1 << 22) - 1;

    outVec = inVec.and(mask);
    outVec.intoArray(output, outOff);

    outVec = inVec.lanewise(VectorOperators.LSHR, 22);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 10).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 20).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 2).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 14);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 18).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 26);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 6).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 16).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 6).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 18);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 14).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 30);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 2).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 12).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 10).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 22);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 10).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 20).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 2).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 14);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 18).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 26);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 6).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 16).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 6).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 18);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 14).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 30);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 2).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 12).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 10);
    outVec.intoArray(output, outOff+=4);
  }

  // __SIMD_fastunpack23_32
  static void SIMD_fastUnpack23(int[] input, int[] output) {
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, 0);
    IntVector outVec;
    int inOff = 0;
    int outOff = 0;
    final int mask = (1 << 23) - 1;

    outVec = inVec.and(mask);
    outVec.intoArray(output, outOff);

    outVec = inVec.lanewise(VectorOperators.LSHR, 23);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 9).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 14);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 18).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 5).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 19);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 13).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 10);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 22).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 1).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 15);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 17).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 6).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 29);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 3).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 12).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 11);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 21).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 2).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 25);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 7).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 16).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 7).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 30);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 2).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 21);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 11).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 20).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 3).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 26);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 6).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 17);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 15).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 31);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 1).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 22);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 10).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 13);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 19).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 27);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 5).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 18);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 14).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 9);
    outVec.intoArray(output, outOff+=4);
  }

  // __SIMD_fastunpack24_32
  static void SIMD_fastUnpack24(int[] input, int[] output) {
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, 0);
    IntVector outVec;
    int inOff = 0;
    int outOff = 0;
    final int mask = (1 << 24) - 1;

    outVec = inVec.and(mask);
    outVec.intoArray(output, outOff);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 16).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 16).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 16).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 16).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 16).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 16).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 16).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 16).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    outVec.intoArray(output, outOff+=4);
  }

  // __SIMD_fastunpack25_32
  static void SIMD_fastUnpack25(int[] input, int[] output) {
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, 0);
    IntVector outVec;
    int inOff = 0;
    int outOff = 0;
    final int mask = (1 << 25) - 1;

    outVec = inVec.and(mask);
    outVec.intoArray(output, outOff);

    outVec = inVec.lanewise(VectorOperators.LSHR, 25);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 7).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 18);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 14).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 11);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 21).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 29);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 3).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 22);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 10).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 15);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 17).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 24).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 1).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 26);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 6).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 19);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 13).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 20).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 5).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 30);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 2).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 23);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 9).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 16).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 9);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 23).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 2).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 27);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 5).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 12).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 13);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 19).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 6).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 31);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 1).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 17);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 15).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 10);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 22).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 3).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 21);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 11).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 14);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 18).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 7);
    outVec.intoArray(output, outOff+=4);
  }

  // __SIMD_fastunpack26_32
  static void SIMD_fastUnpack26(int[] input, int[] output) {
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, 0);
    IntVector outVec;
    int inOff = 0;
    int outOff = 0;
    final int mask = (1 << 26) - 1;

    outVec = inVec.and(mask);
    outVec.intoArray(output, outOff);

    outVec = inVec.lanewise(VectorOperators.LSHR, 26);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 6).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 12).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 14);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 18).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 24).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 2).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 22);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 10).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 16).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 10);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 22).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 30);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 2).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 18);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 14).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 20).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 6).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 26);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 6).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 12).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 14);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 18).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 24).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 2).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 22);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 10).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 16).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 10);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 22).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 30);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 2).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 18);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 14).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 20).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 6);
    outVec.intoArray(output, outOff+=4);
  }

  // __SIMD_fastunpack27_32
  static void SIMD_fastUnpack27(int[] input, int[] output) {
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, 0);
    IntVector outVec;
    int inOff = 0;
    int outOff = 0;
    final int mask = (1 << 27) - 1;

    outVec = inVec.and(mask);
    outVec.intoArray(output, outOff);

    outVec = inVec.lanewise(VectorOperators.LSHR, 27);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 5).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 22);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 10).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 17);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 15).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 20).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 7);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 25).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 2).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 29);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 3).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 19);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 13).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 14);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 18).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 9);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 23).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 31);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 1).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 26);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 6).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 21);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 11).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 16).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 11);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 21).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 6);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 26).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 1).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 23);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 9).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 18);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 14).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 13);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 19).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 24).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 3).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 30);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 2).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 25);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 7).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 12).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 15);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 17).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 10);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 22).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 5);
    outVec.intoArray(output, outOff+=4);
  }

  // __SIMD_fastunpack28_32
  static void SIMD_fastUnpack28(int[] input, int[] output) {
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, 0);
    IntVector outVec;
    int inOff = 0;
    int outOff = 0;
    final int mask = (1 << 28) - 1;

    outVec = inVec.and(mask);
    outVec.intoArray(output, outOff);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 12).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 16).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 20).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 24).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 12).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 16).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 20).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 24).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 12).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 16).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 20).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 24).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 12).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 16).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 20).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 24).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    outVec.intoArray(output, outOff+=4);
  }

  // __SIMD_fastunpack29_32
  static void SIMD_fastUnpack29(int[] input, int[] output) {
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, 0);
    IntVector outVec;
    int inOff = 0;
    int outOff = 0;
    final int mask = (1 << 29) - 1;

    outVec = inVec.and(mask);
    outVec.intoArray(output, outOff);

    outVec = inVec.lanewise(VectorOperators.LSHR, 29);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 3).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 26);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 6).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 23);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 9).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 12).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 17);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 15).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 14);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 18).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 11);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 21).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 24).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 5);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 27).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 2).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 31);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 1).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 25);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 7).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 22);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 10).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 19);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 13).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 16).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 13);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 19).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 10);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 22).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 7);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 25).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 28).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 1).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 30);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 2).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 27);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 5).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 21);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 11).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 18);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 14).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 15);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 17).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 20).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 9);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 23).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 6);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 26).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 3);
    outVec.intoArray(output, outOff+=4);
  }

  // __SIMD_fastunpack30_32
  static void SIMD_fastUnpack30(int[] input, int[] output) {
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, 0);
    IntVector outVec;
    int inOff = 0;
    int outOff = 0;
    final int mask = (1 << 30) - 1;

    outVec = inVec.and(mask);
    outVec.intoArray(output, outOff);

    outVec = inVec.lanewise(VectorOperators.LSHR, 30);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 2).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 26);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 6).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 22);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 10).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 12).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 18);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 14).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 16).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 14);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 18).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 20).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 10);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 22).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 24).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 6);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 26).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 28).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 2).and(mask);
    outVec.intoArray(output, outOff+=4);

    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 0).and(mask);
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 30);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 2).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 26);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 6).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 22);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 10).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 12).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 18);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 14).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 16).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 14);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 18).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 20).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 10);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 22).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 24).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 6);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 26).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 28).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 2);
    outVec.intoArray(output, outOff+=4);
  }

  // __SIMD_fastunpack31_32
  static void SIMD_fastUnpack31(int[] input, int[] output) {
    IntVector inVec = IntVector.fromArray(SPECIES_128, input, 0);
    IntVector outVec;
    int inOff = 0;
    int outOff = 0;
    final int mask = (1 << 31) - 1;

    outVec = inVec.and(mask);
    outVec.intoArray(output, outOff);

    outVec = inVec.lanewise(VectorOperators.LSHR, 31);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 1).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 30);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 2).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 29);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 3).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 28);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 4).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 27);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 5).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 26);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 6).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 25);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 7).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 24);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 8).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 23);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 9).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 22);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 10).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 21);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 11).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 20);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 12).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 19);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 13).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 18);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 14).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 17);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 15).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 16);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 16).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 15);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 17).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 14);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 18).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 13);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 19).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 12);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 20).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 11);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 21).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 10);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 22).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 9);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 23).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 8);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 24).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 7);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 25).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 6);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 26).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 5);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 27).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 4);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 28).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 3);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 29).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 2);
    inVec = IntVector.fromArray(SPECIES_128, input, inOff+=4);
    outVec = outVec.or(inVec.lanewise(VectorOperators.LSHL, 30).and(mask));
    outVec.intoArray(output, outOff+=4);

    outVec = inVec.lanewise(VectorOperators.LSHR, 1);
    outVec.intoArray(output, outOff+=4);
  }

}