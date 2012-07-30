These benchmarks were written for the post [Day 3 of Prolog](). Contributions using other libraries or languages are welcome.

Instructions
===

First, clone or download this repository somewhere.

Prolog
---

Install [SWI-Prolog](http://www.swi-prolog.org/) (or your Prolog implementation of choice). The following steps will run the benchmark.

    $ cd constraint-programming-benchmarks/
    $ swipl
    ?- ['sudoku.pl'].
    ?- bench(100).

You should see something like this when executed:

    ?- bench(100).
        100 iterations taking   7805 msec
    true.

Scala + Copris
---
