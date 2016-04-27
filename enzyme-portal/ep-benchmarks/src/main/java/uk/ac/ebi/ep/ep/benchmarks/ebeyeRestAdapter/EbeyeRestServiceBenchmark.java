/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.ep.ep.benchmarks.ebeyeRestAdapter;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import uk.ac.ebi.ep.ebeye.EbeyeRestService;
import uk.ac.ebi.ep.ebeye.testing.EbeyeService;

/**
 *
 * @author Joseph <joseph@ebi.ac.uk>
 */
@BenchmarkMode(Mode.All)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@State(Scope.Benchmark)
public class EbeyeRestServiceBenchmark {
    //double x = 1e6;
    private EbeyeRestService service;

    @Bean
    public EbeyeRestService ebeyeRestService() {

        return new EbeyeRestService();
    }

    @Setup
    public void setup() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        context.register(EbeyeService.class);
        context.scan("uk.ac.ebi.ep.ebeye");
        context.refresh();

        service = context.getBean(EbeyeRestService.class);

    }

    @TearDown(Level.Trial)
    public void doTearDown() {

        service = null;
    }

    @Benchmark
    public void query(Blackhole blackhole) throws InterruptedException, ExecutionException {

        String query = "alzheimer disease";
        query = " sildenafil";
        query = "dna ligase";
        //query = "ligase";
        query = "kinase";
        //query="ligase";
        //query = "sildenafil";
        List<String> accessions = service.queryEbeyeForAccessions(query, true);
        blackhole.consume(accessions);

    }

    @Benchmark
    public void queryLazyReact(Blackhole blackhole) throws InterruptedException, ExecutionException {

        String query = "alzheimer disease";
        query = " sildenafil";
        query = "dna ligase";
        //query = "ligase";
        query = "kinase";
        //query="ligase";
        //query = "sildenafil";
        List<String> accessions = service.queryEbeyeForAccessionsLazyReact(query, true, 0);
        blackhole.consume(accessions);

    }

 
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(EbeyeRestServiceBenchmark.class.getSimpleName())
                .threads(20)
                //.addProfiler(StackProfiler.class)
                //.addProfiler(GCProfiler.class)
                .warmupIterations(1)
                .measurementIterations(1)
                .forks(1)
                .build();

        new Runner(opt).run();
    }
}
