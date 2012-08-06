package org.example

import annotation.tailrec
import com.google.caliper.Param

import scalaJaCoP._

class Benchmark extends SimpleScalaBenchmark {
  
  @Param(Array("1"))
  val length: Int = 0
  
  val n: Int = 9
  val reg: Int = 3
      
  val problem = List(List(0, 0, 0, 0, 0, 0, 0, 0, 0), 
                     List(0, 0, 0, 0, 0, 3, 0, 8, 5), 
                     List(0, 0, 1, 0, 2, 0, 0, 0, 0), 
                     List(0, 0, 0, 5, 0, 7, 0, 0, 0), 
                     List(0, 0, 4, 0, 0, 0, 1, 0, 0), 
                     List(0, 9, 0, 0, 0, 0, 0, 0, 0), 
                     List(5, 0, 0, 0, 0, 0, 0, 7, 3), 
                     List(0, 0, 2, 0, 1, 0, 0, 0, 0), 
                     List(0, 0, 0, 0, 4, 0, 0, 0, 9))

  def timeSudoku(reps: Int) = repeat(reps) {
                     
    val x = List.tabulate(n)(i=> 
                  List.tabulate(n)(j=>
                    new IntVar("x("+i+","+j+")", 1, n)))

    // constraints

    // fill with the hints
    for(i <- 0 until n) {
      for(j <- 0 until n) {
        if (problem(i)(j) > 0) {
          x(i)(j) #= problem(i)(j)
        }
      }
    }
    
    // rows and columns
    for(i <- 0 until n) {
      alldifferent( Array.tabulate(n)(j=> x(i)(j)) )
      alldifferent( Array.tabulate(n)(j=> x(j)(i)) ) 
    }

    // blocks
    for(i <- 0 until reg; j <- 0 until reg) {
      alldifferent(  (for{ r <- i*reg until i*reg+reg;
                          c <- j*reg until j*reg+reg
                       } yield x(r)(c)).toArray
                 )
    }

    // search
    val result = satisfyAll(search(x.flatten, max_regret, indomain_max), () => { }) 
    
    // satisfy caliper
    x(0)(0).value
  }
  
}

