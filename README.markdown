These benchmarks were written for the post [Day 3 of Prolog](). Contributions using other libraries or languages are welcome.

Instructions
===

First, clone or download this repository to a location of your choice.

Prolog
---

Install [SWI-Prolog](http://www.swi-prolog.org/) (or your Prolog implementation of choice). The following steps will run the benchmark.

    $ cd constraint-programming-benchmarks/prolog/
    $ swipl
    ?- ['sudoku.pl'].
    ?- bench(100).

You should see something like this when executed:

    ?- bench(100).
        100 iterations taking   7805 msec
    true.

Scala + JaCoP
---

Install [Scala](http://www.scala-lang.org/). The following steps will run the benchmark.

    $ cd constraint-programming-benchmarks/scala/
    $ sbt
    > run

You should see something like this when executed:

    > run
    [info]  0% Scenario{vm=java, trial=0, benchmark=Sudoku, length=100} 97656735.29 ns; ?=42751912.25 ns @ 10 trials
    [info] 
    [info]   ms
    [info] 97.7
    [info] 
    [info] vm: java
    [info] trial: 0
    [info] benchmark: Sudoku
    [info] length: 100
    [success] Total time: 24 s, completed Aug 6, 2012 3:10:35 AM
