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
public class BenchmarkSameFormat {

    private int[] ints;
    private int[] intsOutput = new int[32];
    private long[] longs;
    private long[] longsOutput = new long[128];

    final ForUtil oldForUtil = new ForUtil();
    final DefaultForUtil90 defaultForUtil90 = new DefaultForUtil90();
    final PanamaForUtil90SameFormat panamaForUtil90 = new PanamaForUtil90SameFormat();

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

    // 6
    @org.openjdk.jmh.annotations.Benchmark
    public long[] decode6ForUtil() throws IOException {
        oldForUtil.decode(6, longs, longsOutput);
        return longsOutput;
    }

    @org.openjdk.jmh.annotations.Benchmark
    public long[] decode6ForUtil90Panama() throws IOException {
        panamaForUtil90.decode(6, longs, longsOutput);
        return longsOutput;
    }

    @org.openjdk.jmh.annotations.Benchmark
    public long[] expand8ForUtil() throws IOException {
        oldForUtil.expand8(longs);
        return longs;
    }

    @org.openjdk.jmh.annotations.Benchmark
    public long[] expand8ForUtil90Panama() throws IOException {
        panamaForUtil90.expand8(longs);
        return longs;
    }

}
