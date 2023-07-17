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
@Measurement(iterations = 3, time = 3)
@Fork(value = 1, jvmArgsPrepend = {"--add-modules=jdk.incubator.vector"})
public class Benchmark {

  private int[] ints;
  private int[] intsOutput = new int[32];
  private long[] longs;

  final ForUtil oldForUtil = new ForUtil();
  final DefaultForUtil90 defaultForUtil90 = new DefaultForUtil90();
  final PanamaForUtil90 panamaForUtil90 = new PanamaForUtil90();

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

  // 1
  @org.openjdk.jmh.annotations.Benchmark
  public long[] encode1ForUtil() throws IOException {
    oldForUtil.encode(longs, 1, longs);
     return longs;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode1ForUtil90Default() throws IOException {
    defaultForUtil90.encode(ints, 1, intsOutput);
    return intsOutput;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode1ForUtil90Panama() throws IOException {
    panamaForUtil90.encode(ints, 1, intsOutput);
    return intsOutput;
  }

  // 2
  @org.openjdk.jmh.annotations.Benchmark
  public long[] encode2ForUtil() throws IOException {
    oldForUtil.encode(longs, 2, longs);
    return longs;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode2ForUtil90Default() throws IOException {
    defaultForUtil90.encode(ints, 2, intsOutput);
    return intsOutput;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode2ForUtil90Panama() throws IOException {
    panamaForUtil90.encode(ints, 2, intsOutput);
    return intsOutput;
  }

  // 3
  @org.openjdk.jmh.annotations.Benchmark
  public long[] encode3ForUtil() throws IOException {
    oldForUtil.encode(longs, 3, longs);
    return longs;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode3SimdPack() throws IOException {
    panamaForUtil90.encode(ints, 3, intsOutput);
    return intsOutput;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public long[] encode4ForUtil() throws IOException {
    oldForUtil.encode(longs, 4, longs);
    return longs;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode4SimdPack() throws IOException {
    panamaForUtil90.encode(ints, 4, intsOutput);
    return intsOutput;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public long[] encode5ForUtil() throws IOException {
    oldForUtil.encode(longs, 5, longs);
    return longs;
  }

  @org.openjdk.jmh.annotations.Benchmark
  public int[] encode5SimdPack() throws IOException {
    panamaForUtil90.encode(ints, 5, intsOutput);
    return intsOutput;
  }

  // TODO decode/unpack
  // TODO: 6 .. 32

}
