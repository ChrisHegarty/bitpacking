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

import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 3, time = 3)
@Measurement(iterations = 5, time = 3)
@Fork(value = 1, jvmArgsPrepend = {"--add-modules=jdk.incubator.vector"})
public class Benchmark {

  private int[] ints;
  private int[] intsOutput = new int[32];
  private long[] longs;

  final ForUtil forUtil = new ForUtil();

  @Setup(Level.Trial)
  public void init() {
    ints = new int[128];
    for (int i = 0; i < 128; i++) {
      ints[i] = ThreadLocalRandom.current().nextInt();
    }
    longs = new long[128];
    for (int i = 0; i < 128; i++) {
      longs[i] = ThreadLocalRandom.current().nextLong();
    }
  }

  @org.openjdk.jmh.annotations.Benchmark
  public long[] encode1ForUtil() throws IOException {
     forUtil.encode(longs, 1, longs);
     return longs;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode1SimdPack() throws IOException {
    SimdBitPacking.simdPack(ints, intsOutput, 1);
    return intsOutput;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public long[] encode2ForUtil() throws IOException {
    forUtil.encode(longs, 2, longs);
    return longs;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode2SimdPack() throws IOException {
    SimdBitPacking.simdPack(ints, intsOutput, 2);
    return intsOutput;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public long[] encode3ForUtil() throws IOException {
    forUtil.encode(longs, 3, longs);
    return longs;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode3SimdPack() throws IOException {
    SimdBitPacking.simdPack(ints, intsOutput, 3);
    return intsOutput;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public long[] encode4ForUtil() throws IOException {
    forUtil.encode(longs, 4, longs);
    return longs;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode4SimdPack() throws IOException {
    SimdBitPacking.simdPack(ints, intsOutput, 4);
    return intsOutput;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public long[] encode5ForUtil() throws IOException {
    forUtil.encode(longs, 5, longs);
    return longs;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode5SimdPack() throws IOException {
    SimdBitPacking.simdPack(ints, intsOutput, 5);
    return intsOutput;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public long[] encode6ForUtil() throws IOException {
    forUtil.encode(longs, 6, longs);
    return longs;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode6SimdPack() throws IOException {
    SimdBitPacking.simdPack(ints, intsOutput, 6);
    return intsOutput;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public long[] encode7ForUtil() throws IOException {
    forUtil.encode(longs, 7, longs);
    return longs;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode7SimdPack() throws IOException {
    SimdBitPacking.simdPack(ints, intsOutput, 7);
    return intsOutput;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public long[] encode8ForUtil() throws IOException {
    forUtil.encode(longs, 8, longs);
    return longs;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode8SimdPack() throws IOException {
    SimdBitPacking.simdPack(ints, intsOutput, 8);
    return intsOutput;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public long[] encode9ForUtil() throws IOException {
    forUtil.encode(longs, 9, longs);
    return longs;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode9SimdPack() throws IOException {
    SimdBitPacking.simdPack(ints, intsOutput, 9);
    return intsOutput;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public long[] encode10ForUtil() throws IOException {
    forUtil.encode(longs, 10, longs);
    return longs;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode10SimdPack() throws IOException {
    SimdBitPacking.simdPack(ints, intsOutput, 10);
    return intsOutput;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public long[] encode11ForUtil() throws IOException {
    forUtil.encode(longs, 11, longs);
    return longs;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode11SimdPack() throws IOException {
    SimdBitPacking.simdPack(ints, intsOutput, 11);
    return intsOutput;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public long[] encode12ForUtil() throws IOException {
    forUtil.encode(longs, 12, longs);
    return longs;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode12SimdPack() throws IOException {
    SimdBitPacking.simdPack(ints, intsOutput, 12);
    return intsOutput;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public long[] encode13ForUtil() throws IOException {
    forUtil.encode(longs, 13, longs);
    return longs;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode13SimdPack() throws IOException {
    SimdBitPacking.simdPack(ints, intsOutput, 13);
    return intsOutput;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public long[] encode14ForUtil() throws IOException {
    forUtil.encode(longs, 14, longs);
    return longs;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode14SimdPack() throws IOException {
    SimdBitPacking.simdPack(ints, intsOutput, 14);
    return intsOutput;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public long[] encode15ForUtil() throws IOException {
    forUtil.encode(longs, 15, longs);
    return longs;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode15SimdPack() throws IOException {
    SimdBitPacking.simdPack(ints, intsOutput, 15);
    return intsOutput;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public long[] encode16ForUtil() throws IOException {
    forUtil.encode(longs, 16, longs);
    return longs;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode16SimdPack() throws IOException {
    SimdBitPacking.simdPack(ints, intsOutput, 16);
    return intsOutput;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public long[] encode17ForUtil() throws IOException {
    forUtil.encode(longs, 17, longs);
    return longs;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode17SimdPack() throws IOException {
    SimdBitPacking.simdPack(ints, intsOutput, 17);
    return intsOutput;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public long[] encode18ForUtil() throws IOException {
    forUtil.encode(longs, 18, longs);
    return longs;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode18SimdPack() throws IOException {
    SimdBitPacking.simdPack(ints, intsOutput, 18);
    return intsOutput;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public long[] encode19ForUtil() throws IOException {
    forUtil.encode(longs, 19, longs);
    return longs;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode19SimdPack() throws IOException {
    SimdBitPacking.simdPack(ints, intsOutput, 19);
    return intsOutput;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public long[] encode20ForUtil() throws IOException {
    forUtil.encode(longs, 20, longs);
    return longs;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode20SimdPack() throws IOException {
    SimdBitPacking.simdPack(ints, intsOutput, 20);
    return intsOutput;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public long[] encode21ForUtil() throws IOException {
    forUtil.encode(longs, 21, longs);
    return longs;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode21SimdPack() throws IOException {
    SimdBitPacking.simdPack(ints, intsOutput, 21);
    return intsOutput;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public long[] encode22ForUtil() throws IOException {
    forUtil.encode(longs, 22, longs);
    return longs;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode22SimdPack() throws IOException {
    SimdBitPacking.simdPack(ints, intsOutput, 22);
    return intsOutput;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public long[] encode23ForUtil() throws IOException {
    forUtil.encode(longs, 23, longs);
    return longs;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode23SimdPack() throws IOException {
    SimdBitPacking.simdPack(ints, intsOutput, 23);
    return intsOutput;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public long[] encode24ForUtil() throws IOException {
    forUtil.encode(longs, 24, longs);
    return longs;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode24SimdPack() throws IOException {
    SimdBitPacking.simdPack(ints, intsOutput, 24);
    return intsOutput;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public long[] encode25ForUtil() throws IOException {
    forUtil.encode(longs, 25, longs);
    return longs;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode25SimdPack() throws IOException {
    SimdBitPacking.simdPack(ints, intsOutput, 25);
    return intsOutput;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public long[] encode26ForUtil() throws IOException {
    forUtil.encode(longs, 26, longs);
    return longs;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode26SimdPack() throws IOException {
    SimdBitPacking.simdPack(ints, intsOutput, 26);
    return intsOutput;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public long[] encode27ForUtil() throws IOException {
    forUtil.encode(longs, 27, longs);
    return longs;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode27SimdPack() throws IOException {
    SimdBitPacking.simdPack(ints, intsOutput, 27);
    return intsOutput;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public long[] encode28ForUtil() throws IOException {
    forUtil.encode(longs, 28, longs);
    return longs;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode28SimdPack() throws IOException {
    SimdBitPacking.simdPack(ints, intsOutput, 28);
    return intsOutput;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public long[] encode29ForUtil() throws IOException {
    forUtil.encode(longs, 29, longs);
    return longs;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode29SimdPack() throws IOException {
    SimdBitPacking.simdPack(ints, intsOutput, 29);
    return intsOutput;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public long[] encode30ForUtil() throws IOException {
    forUtil.encode(longs, 30, longs);
    return longs;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode30SimdPack() throws IOException {
    SimdBitPacking.simdPack(ints, intsOutput, 30);
    return intsOutput;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public long[] encode31ForUtil() throws IOException {
    forUtil.encode(longs, 31, longs);
    return longs;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode31SimdPack() throws IOException {
    SimdBitPacking.simdPack(ints, intsOutput, 31);
    return intsOutput;
  }

}
