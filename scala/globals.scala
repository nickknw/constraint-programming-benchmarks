
import JaCoP.core._
import JaCoP.constraints._
import JaCoP.constraints.knapsack._
import JaCoP.constraints.regular._
import JaCoP.constraints.binpacking._
import JaCoP.constraints.netflow._
import JaCoP.search._
import JaCoP.set.core._
import JaCoP.set.constraints._
import JaCoP.set.search._

/**
* Package for defining variables, constraints, global constraints and search methods for [[JaCoP]] constraint solver in Scala.
*/
package object scalaJaCoP {

  val trace = false

  var allSolutions = false

  var printFunctions: Array[() => Unit] = null

  var labels: Array[DepthFirstSearch[_ <: JaCoP.core.Var]] = null

  var limitOnSolutions: Int = -1

  var timeOutValue: Int = -1

  var recordSolutions = false;

  // =============== Global constraints ===============

/**
* Wrapper for [[JaCoP.constraints.Alldiff]].
*
* @param x array of variables to be different. 
*/
  def alldifferent(x: Array[IntVar]) : Unit = {
    val c = new Alldiff( x.asInstanceOf[Array[JaCoP.core.IntVar]] )
    if (trace) println(c)
    Model.impose( c )
  }

/**
* Wrapper for [[JaCoP.constraints.Alldistinct]].
*
* @param x array of variables to be different. 
*/
  def alldistinct(x: Array[IntVar]) : Unit = {
    val c = new Alldistinct( x.asInstanceOf[Array[JaCoP.core.IntVar]] )
    if (trace) println(c)
    Model.impose( c )
  }

/**
* Wrapper for [[JaCoP.constraints.GCC]].
*
* @param x array of variables. 
* @param y array of counters of differnet values from array x. 
*/
  def gcc(x: Array[IntVar], y: Array[IntVar]) : Unit = {
    val c = new GCC( x.asInstanceOf[Array[JaCoP.core.IntVar]], y.asInstanceOf[Array[JaCoP.core.IntVar]] )
    if (trace) println(c)
    Model.impose( c )
  }

/**
* Wrapper for [[JaCoP.constraints.Sum]].
*
* @param res array of variables to be summed up. 
* @param result summation result. 
*/
  def sum[T <: JaCoP.core.IntVar](res: List[T], result: IntVar)(implicit m: ClassManifest[T]) : Unit = {
     val c = new Sum(res.toArray.asInstanceOf[Array[JaCoP.core.IntVar]], result)
     if (trace) println(c)
     Model.impose( c )
   }

/**
* Wrapper for [[JaCoP.constraints.Sum]].
*
* @param res array of variables to be summed up. 
* @return summation result. 
*/
  def sum[T <: JaCoP.core.IntVar](res: List[T])(implicit m: ClassManifest[T]) : IntVar = {
    val result = new IntVar()
    val c = new Sum(res.toArray.asInstanceOf[Array[JaCoP.core.IntVar]], result)
    Model.constr += c
    return result
  }

/**
* Wrapper for [[JaCoP.constraints.SumWeight]].
*
* @param res array of variables to be summed up. 
* @param w array of weights. 
* @param result summation result. 
*/
  def weightedSum[T <: JaCoP.core.IntVar](res: List[T], w: Array[Int], result: IntVar)(implicit m: ClassManifest[T]) : Unit = {
    val c = new SumWeight(res.toArray.asInstanceOf[Array[JaCoP.core.IntVar]], w, result)
    if (trace) println(c)
    Model.impose( c )
  }

/**
* Wrapper for [[JaCoP.constraints.SumWeight]].
*
* @param res array of variables to be summed up. 
* @param w array of weights. 
* @return summation result. 
*/
  def sum[T <: JaCoP.core.IntVar](res: List[T], w: Array[Int])(implicit m: ClassManifest[T]) : IntVar = {
    val result = new IntVar()
    val c = new SumWeight(res.toArray.asInstanceOf[Array[JaCoP.core.IntVar]], w, result)
    if (trace) println(c)
    Model.impose( c )
    return result
  }

/**
* Wrapper for [[JaCoP.constraints.AbsXeqY]].
*
* @param x variable for abs operation. 
* @return absolute value result. 
*/
  def abs(x: JaCoP.core.IntVar) : IntVar = {
    val result = new IntVar()
    val c = new AbsXeqY(x, result)
    if (trace) println(c)
    Model.impose( c )
    return result
  }

/**
* Wrapper for [[JaCoP.constraints.Max]].
*
* @param x array of variables where maximum values is to be found. 
* @param mx maxumum value. 
*/
  def max[T <: JaCoP.core.IntVar](x: List[T], mx: JaCoP.core.IntVar)(implicit m: ClassManifest[T]) : Unit = {
    val c = new Max(x.toArray.asInstanceOf[Array[JaCoP.core.IntVar]], mx)
    if (trace) println(c)
    Model.impose(c)
  }

/**
* Wrapper for [[JaCoP.constraints.Min]].
*
* @param x array of variables where mnimimum values is to be found. 
* @param mx minimum value. 
*/
  def min[T <: JaCoP.core.IntVar](x: List[T], mn: JaCoP.core.IntVar )(implicit m: ClassManifest[T]): Unit = {
    val c = new Min(x.toArray.asInstanceOf[Array[JaCoP.core.IntVar]], mn)
    if (trace) println(c)
    Model.impose(c)
  }

/**
* Wrapper for [[JaCoP.constraints.Max]].
*
* @param res array of variables where maximum values is to be found. 
* @return max value. 
*/
  def max[T <: JaCoP.core.IntVar](x: List[T])(implicit m: ClassManifest[T]) : IntVar = {
    val result = new IntVar()
    val c = new Max(x.toArray.asInstanceOf[Array[JaCoP.core.IntVar]], result)
    Model.constr += c
    return result
  }

/**
* Wrapper for [[JaCoP.constraints.Min]].
*
* @param res array of variables where minimum values is to be found. 
* @return minimum value. 
*/
  def min[T <: JaCoP.core.IntVar](x: List[T])(implicit m: ClassManifest[T]) : IntVar = {
    val result = new IntVar()
    val c = new Min(x.toArray.asInstanceOf[Array[JaCoP.core.IntVar]], result)
    Model.constr += c
    return result
  }

/**
* Wrapper for [[JaCoP.constraints.Count]].
*
* @param list list of variables to count number of values value. 
* @param count of values value. 
*/
  def count[T <: JaCoP.core.IntVar](list: List[T], count: T, value: Int)(implicit m: ClassManifest[T]) : Unit = {
    val c = new Count(list.toArray.asInstanceOf[Array[JaCoP.core.IntVar]], count, value)
    if (trace) println(c)
    Model.impose( c )
  }

/**
* Wrapper for [[JaCoP.constraints.Count]].
*
* @param list list of variables to count number of values value. 
* @return number of values value. 
*/
  def count[T <: JaCoP.core.IntVar](list: List[T], value: Int)(implicit m: ClassManifest[T]) : IntVar = {
    val result = new IntVar()
    val c = new Count(list.toArray.asInstanceOf[Array[JaCoP.core.IntVar]], result, value)
    Model.constr += c
    println(result) 
    return result
  }

/**
* Wrapper for [[JaCoP.constraints.Values]].
*
* @param list list of variables to count number of different values. 
* @param count of different values. 
*/
  def values[T <: JaCoP.core.IntVar](list: List[T], count: IntVar)(implicit m: ClassManifest[T]) : Unit = {
    val c = new Values(list.toArray.asInstanceOf[Array[JaCoP.core.IntVar]], count)
    if (trace) println(c)
    Model.impose( c )
  }

/**
* Wrapper for [[JaCoP.constraints.Values]].
*
* @param list list of variables to count number of different values. 
* @return number of different values. 
*/
  def values[T <: JaCoP.core.IntVar](list: List[T])(implicit m: ClassManifest[T]) : IntVar = {
    val result = new IntVar()
    val c = new Values(list.toArray.asInstanceOf[Array[JaCoP.core.IntVar]], result)
    Model.constr += c
    return result
  }

/**
* Wrapper for [[JaCoP.constraints.Element]].
*
* @param index index to select element from list of elements. 
* @param elements array of integers that can be assigned to values. 
* @param value value selected from list of elements. 
*/
  def element(index: JaCoP.core.IntVar, elements: Array[Int], value: JaCoP.core.IntVar) : Unit = {
    val c = new Element(index, elements, value)
    if (trace) println(c)
    Model.impose( c )
  }


/**
* Wrapper for [[JaCoP.constraints.Element]].
*
* @param index index to select element from list of elements. 
* @param elements array of integers that can be assigned to values. 
* @param value value selected from list of elements. 
* @param offset value of index offset (shift). 
*/
  def element(index: JaCoP.core.IntVar, elements: Array[Int], value: JaCoP.core.IntVar, offset: Int) : Unit = {
    val c = new Element(index, elements, value, offset)
    if (trace) println(c)
    Model.impose( c )
  }
 
/**
* Wrapper for [[JaCoP.constraints.Element]].
*
* @param index index to select element from list of elements. 
* @param elements array of varibales that can be assigned to values. 
* @param value value selected from list of elements. 
*/
  def element[T <: JaCoP.core.IntVar](index: JaCoP.core.IntVar, elements: List[T], value: JaCoP.core.IntVar)(implicit m: ClassManifest[T]) : Unit = {
    val c = new Element(index, elements.toArray.asInstanceOf[Array[JaCoP.core.IntVar]], value)
    if (trace) println(c)
    Model.impose( c )
  }

/**
* Wrapper for [[JaCoP.constraints.Element]].
*
* @param index index to select element from list of elements. 
* @param elements array of varibales that can be assigned to values. 
* @param value value selected from list of elements. 
* @param offset value of index offset (shift). 
*/
  def element[T <: JaCoP.core.IntVar](index: JaCoP.core.IntVar, elements: List[T], value: JaCoP.core.IntVar, offset: Int)(implicit m: ClassManifest[T]) : Unit = {
    val c = new Element(index, elements.toArray.asInstanceOf[Array[JaCoP.core.IntVar]], value, offset)
    if (trace) println(c)
    Model.impose( c )
  }

/**
* Wrapper for [[JaCoP.constraints.Diff2]].
*
* @param x coordinate X of rectangle. 
* @param y coordinate Y of rectangle. 
* @param lx length in derection X of rectangle. 
* @param ly length in derection Y of rectangle. 
*/
  def diff2(x: Array[IntVar], y: Array[IntVar], lx: Array[IntVar], ly: Array[IntVar]) : Unit = {
    val c = new Diff(x.asInstanceOf[Array[JaCoP.core.IntVar]], y.asInstanceOf[Array[JaCoP.core.IntVar]],
		     lx.asInstanceOf[Array[JaCoP.core.IntVar]], ly.asInstanceOf[Array[JaCoP.core.IntVar]])
    if (trace) println(c)
    Model.impose(c)
  }

/**
* Wrapper for [[JaCoP.constraints.Diff2]].
*
* @param rectangles array of four element vectors representing rectnagles [x, y, lx, ly]
*/
  def diff2(rectangles: Array[Array[IntVar]]) : Unit = {
    val c = new Diff(rectangles.asInstanceOf[Array[Array[JaCoP.core.IntVar]]])
    if (trace) println(c)
    Model.impose( new Diff(rectangles.asInstanceOf[Array[Array[JaCoP.core.IntVar]]]) )
  }

/**
* Wrapper for [[JaCoP.constraints.Cumulative]].
*
* @param t array of start times of tasks.
* @param d array of duration of tasks.
* @param r array of number of resources of tasks.
* @param limit limit on number of resources used in a schedule.
*/
  def cumulative(t: Array[IntVar], d: Array[IntVar], r: Array[IntVar], limit: IntVar) : Unit = {
    val c = new Cumulative(t.asInstanceOf[Array[JaCoP.core.IntVar]],
			   d.asInstanceOf[Array[JaCoP.core.IntVar]],
			   r.asInstanceOf[Array[JaCoP.core.IntVar]], limit)
    if (trace) println(c)
    Model.impose( c )
  }

/**
* Wrapper for [[JaCoP.constraints.Circuit]].
*
* @param n array of varibales, which domains define next nodes in the graph.
*/
  def circuit(n: Array[IntVar]) : Unit = {
    val c = new Circuit(n.asInstanceOf[Array[JaCoP.core.IntVar]])
    if (trace) println(c)
    Model.impose( c )
  }

/**
* Wrapper for [[JaCoP.constraints.Assignment]].
*
* @param x array of varibales. 
* @param y array variables that values are permutation of x.
*/
  def assignment(x: Array[IntVar], y: Array[IntVar]) : Unit = {
    val c = new Assignment(x.asInstanceOf[Array[JaCoP.core.IntVar]], y.asInstanceOf[Array[JaCoP.core.IntVar]])
    if (trace) println(c)
    Model.impose( c )
  }

/**
* Wrapper for [[JaCoP.constraints.Among]].
*
* @param list array of varibales. 
* @param kSet values to be checked.
* @param n number of values found.
*/
  def among(list: Array[IntVar], kSet: IntSet, n: IntVar) {
    val c = new Among(list.asInstanceOf[Array[JaCoP.core.IntVar]], kSet, n)
    if (trace) println(c)
    Model.impose( c )
  }

/**
* Wrapper for [[JaCoP.constraints.AmongVar]].
*
* @param listX array of varibales. 
* @param listY array of varibales to be checked if their values .
* @param n number of values found.
*/
  def among(listX: Array[IntVar], listY: Array[IntVar], n: IntVar) {
    val c = new AmongVar(listX.asInstanceOf[Array[JaCoP.core.IntVar]], listY.asInstanceOf[Array[JaCoP.core.IntVar]], n)
    if (trace) println(c)
    Model.impose( c )
  }

/**
* Wrapper for [[JaCoP.constraints.ExtensionalSupportVA]].
*
* @param list array of variables. 
* @param tuples array of tuples allowed to be assigned to variables.
*/
  def table[T <: JaCoP.core.IntVar](list: List[T], tuples: Array[Array[Int]])(implicit m: ClassManifest[T]) {
    val c = new ExtensionalSupportVA(list.toArray.asInstanceOf[Array[JaCoP.core.IntVar]], tuples)
    if (trace) println(c)
    Model.impose( c )
  }

/**
* Wrapper for [[JaCoP.constraints.knapsack.Knapsack]].
*
* @param profits array of profite for items. 
* @param weights array of weights for items.
* @param quantity array of quantities of items.
* @param knapsackCapacity knapsack capacity.
* @param knapsackProfit profite when selling items.
*/
  def knapsack(profits: Array[Int], weights: Array[Int], quantity: List[IntVar], 
	       knapsackCapacity: IntVar, knapsackProfit: IntVar ) {
    val c = new Knapsack(profits, weights, quantity.toArray, knapsackCapacity, knapsackProfit)
    if (trace) println(c)
    Model.impose( c )    
  }

/**
* Wrapper for [[JaCoP.constraints.binpack.Binpack]].
*
* @param bin list containing which bin is assigned to an item. 
* @param load list of loads for bins.
* @param w array of weights for items.
*/
  def binpacking(bin: List[IntVar], load: List[IntVar], w: Array[Int]) {
    val c = new Binpacking(bin.toArray.asInstanceOf[Array[JaCoP.core.IntVar]], load.toArray.asInstanceOf[Array[JaCoP.core.IntVar]], w)
    if (trace) println(c)
    Model.impose( c )    
  }

/**
* Wrapper for [[JaCoP.constraints.regular.Regular]].
*
* @param dfa specification of finite state machine using class fsm. 
* @param vars list of variables assigned to fsm nodes.
*/
  def regular(dfa: fsm, vars: List[IntVar]) {
    val c = new Regular(dfa, vars.toArray)
    if (trace) println(c)
    Model.impose( c )
  }

  // ================== Decompose constraints

/**
* Wrapper for [[JaCoP.constraints.Sequence]].
*
* @param list list of variables to be constrained. 
* @param set set of values to be checked.
* @param q length of the sub-sequence.
* @param min minimal number of occurrences of values in the sub-sequence.
* @param max maximal number of occurrences of values in the sub-sequence.
*/
  def sequence(list: Array[IntVar], set: IntSet, q: Int, min: Int, max: Int) {
    val c = new Sequence(list.asInstanceOf[Array[JaCoP.core.IntVar]], set, q, min, max)
    if (trace) println(c)
    Model.imposeDecomposition( c )
}

/**
* Wrapper for [[JaCoP.constraints.Stretch]].
*
* @param values a list of values to be assigned to sub-sequences. 
* @param min minimal length of the sub-sequence for each value on position i.
* @param max maximal length of the sub-sequence for each value on position i.
* @param x list of variables to be constrained.
*/
  def stretch(values: Array[Int], min: Array[Int], max: Array[Int], x: Array[IntVar]) {
    val c = new Stretch(values, min, max, x.asInstanceOf[Array[JaCoP.core.IntVar]])
    if (trace) println(c)
    Model.imposeDecomposition( c )
}

/**
* Wrapper for [[JaCoP.constraints.Lex]].
*
* @param x array of vectors of varibales to be lexicographically ordered.
*/
  def lex(x: Array[Array[IntVar]]) {
    val c = new JaCoP.constraints.Lex(x.asInstanceOf[Array[Array[JaCoP.core.IntVar]]])
    if (trace) println(c)
    Model.imposeDecomposition(c)
  }

/**
* Wrapper for [[JaCoP.constraints.SoftAlldifferent]].
*
* @param xVars array of variables to be constrained to be different.
* @param costVar measures degree of violation (uses value based violation).
*/
  def softAlldifferent(xVars: Array[IntVar], costVar: IntVar) {
    val violationMeasure = ViolationMeasure.VALUE_BASED
    val c = new SoftAlldifferent(xVars.asInstanceOf[Array[JaCoP.core.IntVar]], costVar, violationMeasure)
    if (trace) println(c)
    Model.imposeDecomposition( c )
  }

/**
* Wrapper for [[JaCoP.constraints.SoftGCC]].
*
* @param xVars array of variables to be constrained to be different.
* @param hardLowerBound  lower bound on limits that can not be violated.
* @param hardUpperBound  upper bound on limits that can not be violated
* @param countedValue values that are counted.
* @param softCounters specifies preferred values for counters and can be violated.
*/
  def softGCC(xVars: Array[IntVar], hardLowerBound: Array[Int], hardUpperBound: Array[Int], countedValue: Array[Int], softCounters: Array[IntVar], 
	      costVar: IntVar) {
    val violationMeasure = ViolationMeasure.VALUE_BASED
    val c = new SoftGCC(xVars.asInstanceOf[Array[JaCoP.core.IntVar]], 
			hardLowerBound, 
			hardUpperBound, 
			countedValue, 
			softCounters.asInstanceOf[Array[JaCoP.core.IntVar]],  
			costVar, violationMeasure)
    if (trace) println(c)
    Model.imposeDecomposition( c )
}

  def network_flow(net: JaCoP.constraints.netflow.NetworkBuilder) {
    val c = new NetworkFlow(net)
    if (trace) println(c)
    Model.impose( c )
  }

  // ================== Logical operations on constraints


/**
* Wrapper for [[JaCoP.constraints.Or]].
*
* @param list constraints to be disjunction.
* @return the constraint that is a a disjunction of constraints.
*/
  def OR(list: PrimitiveConstraint*)  : PrimitiveConstraint = {
    val c = new Or(list.toArray)
    list.foreach( e => Model.constr.remove(Model.constr.indexOf(e)) )
    Model.constr += c
    return c
  }

/**
* Wrapper for [[JaCoP.constraints.Or]].
*
* @param list constraints to be disjunction.
* @return the constraint that is a a disjunction of constraints.
*/
  def OR(list: List[PrimitiveConstraint])  : PrimitiveConstraint = {
    val c = new Or(list.toArray)
    list.foreach( e => Model.constr.remove(Model.constr.indexOf(e)) )
    Model.constr += c
    return c
  }

/**
* Wrapper for [[JaCoP.constraints.And]].
*
* @param list constraints to be conjunction.
* @return the constraint that is a a conjunction of constraints.
*/
  def AND(list: PrimitiveConstraint*) : PrimitiveConstraint = {
    val c = new And(list.toArray)
    list.foreach( e => Model.constr.remove(Model.constr.indexOf(e)) )
    Model.constr += c
    return c
  }

/**
* Wrapper for [[JaCoP.constraints.And]].
*
* @param list constraints to be conjunction.
* @return the constraint that is a a conjunction of constraints.
*/
  def AND(list: List[PrimitiveConstraint]) : PrimitiveConstraint = {
    val c = new And(list.toArray)
    list.foreach( e => Model.constr.remove(Model.constr.indexOf(e)) )
    Model.constr += c
    return c
  }

/**
* Wrapper for [[JaCoP.constraints.Not]].
*
* @param constr constraints to be negated.
* @return the negated constraint.
*/
  def NOT(constr: PrimitiveConstraint) : PrimitiveConstraint = {
    val c = new Not(constr)
    Model.constr.remove(Model.constr.indexOf(constr))
    Model.constr += c
    return c
  }

  // =============== Set constraints ===============


/**
* Wrapper for [[JaCoP.set.constraints.CardAeqX]].
*
* @param s constrained set variable.
* @return variable defining cardinality of s.
*/
  def card(s: SetVar) : IntVar = {
    val result = new IntVar()
    val c = new CardAeqX(s, result)
    Model.constr += c
    return result
  }

/**
* Wrapper for [[JaCoP.set.constraints.CardA]].
*
* @param s constrained set variable.
* @param n cardinality.
*/
  def card(s: SetVar, n: Int) : Unit = {
    val c = new CardA(s, n)
    if (trace) println(c)
    Model.impose( c )
  }

/**
* Wrapper for [[JaCoP.set.constraints.CardAeqX]].
*
* @param s constrained set variable.
* @param n cardinality (IntVar variable).
*/
  def card(s: SetVar, n: JaCoP.core.IntVar) : Unit = {
    val c = new CardAeqX(s, n)
    if (trace) println(c)
    Model.impose( c )
  }

/**
* Wrapper for [[JaCoP.set.constraints.Match]].
*
* @param a  a set variable to be matched against list of IntVar.
* @param list varibales that get values from the set.
*/
  def matching[T <: JaCoP.core.IntVar](a: SetVar, list: List[T])(implicit m: ClassManifest[T]) {
    val c = new Match(a, list.toArray)
    if (trace) println(c)
    Model.impose( c )
  }

  // =============== Search methods ===================

/**
* Minimization search method.
*
* @param select select method defining variable selection and value assignment methods.
* @param cost Cost variable
* @return true if solution found and false otherwise.
*/
   def minimize[T <: JaCoP.core.Var](select: SelectChoicePoint[T], cost: IntVar, printSolutions: () => Unit*)(implicit m: ClassManifest[T]): Boolean = {

     Model.imposeAllConstraints

     val label = dfs
     labels = Array(label)

     printFunctions = new Array(printSolutions.size)
     if (printSolutions.size > 0) {
       var i=0
       for (p <- printSolutions) {
	 printFunctions(i) = p
	 i += 1
       }
    
       label.setSolutionListener(new EmptyListener[T]);
       label.setPrintInfo(false);
       label.setSolutionListener(new ScalaSolutionListener[T]);
     }

     if (limitOnSolutions > 0) {
       label.getSolutionListener().setSolutionLimit(limitOnSolutions)
       label.respectSolutionLimitInOptimization=true
     }

     return label.labeling(Model, select, cost)
   }


/**
* Maximization search method.
*
* @param select select method defining variable selection and value assignment methods.
* @param cost Cost variable
* @return true if solution found and false otherwise.
*/
  def maximize[T <: JaCoP.core.Var](select: SelectChoicePoint[T], cost: IntVar, printSolutions: () => Unit*)(implicit m: ClassManifest[T]): Boolean = {

    val costN = new IntVar("newCost", JaCoP.core.IntDomain.MinInt, JaCoP.core.IntDomain.MaxInt)
    costN #= -cost

    return minimize(select, costN, printSolutions: _*)
  }

/**
* Search method that finds a solution.
*
* @param select select method defining variable selection and value assignment methods.
* @param cost Cost variable
* @return true if solution found and false otherwise.
*/

  def satisfy[T <: JaCoP.core.Var](select: SelectChoicePoint[T], printSolutions: () => Unit*)(implicit m: ClassManifest[T]): Boolean = {

    Model.imposeAllConstraints

    val label = dfs
    labels = Array(label)

    printFunctions = new Array(printSolutions.size)
    if (printSolutions.size > 0) {
      var i=0
      for (p <- printSolutions) {
	printFunctions(i) = p
	i += 1
      }
    
      // label.setSolutionListener(new EmptyListener[T]);
      label.setPrintInfo(false);
      label.setSolutionListener(new ScalaSolutionListener[T]);
    }

    if (timeOutValue > 0)
      label.setTimeOut(timeOutValue)

    if (allSolutions)
      label.getSolutionListener().searchAll(true)

     if (limitOnSolutions > 0) 
       label.getSolutionListener().setSolutionLimit(limitOnSolutions)
    
    label.getSolutionListener().recordSolutions(recordSolutions)

    return label.labeling(Model, select)

  }

/**
* Search method that finds all solutions.
*
* @param select select method defining variable selection and value assignment methods.
* @param cost Cost variable
* @return true if solution found and false otherwise.
*/
  def satisfyAll[T <: JaCoP.core.Var](select: SelectChoicePoint[T], printSolutions: () => Unit*)(implicit m: ClassManifest[T]): Boolean = {

    allSolutions = true

    return satisfy( select, printSolutions: _*)

  }


/**
* Minimization method for sequence of search methods (specified by list of select methods).
*
* @param select list of select methods defining variable selection and value assignment methods for sequence of searchs.
* @param cost Cost variable
* @return true if solution found and false otherwise.
*/
  def minimize_seq[T <: JaCoP.core.Var](select: List[SelectChoicePoint[T]], cost: IntVar, printSolutions: () => Unit*) (implicit m: ClassManifest[T]): Boolean = {

    Model.imposeAllConstraints

    val masterLabel = dfs
    labels = new Array(select.size)
    labels(0) = masterLabel

    if (printSolutions.size > 0) {
	masterLabel.setSolutionListener(new EmptyListener[T]);
       	masterLabel.setPrintInfo(false);
    }

    if (limitOnSolutions > 0) 
      masterLabel.respectSolutionLimitInOptimization=true

    if (timeOutValue > 0)
      masterLabel.setTimeOut(timeOutValue)

    var previousSearch = masterLabel
    var lastLabel = masterLabel
    if (select.length > 1)
      for (i <- 1 until select.length) {
       	val label = dfs
	previousSearch.addChildSearch(label)
	label.setSelectChoicePoint(select(i));
	previousSearch = label
	lastLabel = label
	labels(i) = label

	if (printSolutions.size > 0) {
	  label.setSolutionListener(new EmptyListener[T]);
       	  label.setPrintInfo(false);
	}

	if (limitOnSolutions > 0) 
	  label.respectSolutionLimitInOptimization=true

	if (timeOutValue > 0)
	  label.setTimeOut(timeOutValue)
      }

    printFunctions = new Array(printSolutions.size)
    if (printSolutions.size > 0) {
      var i=0
      for (p <- printSolutions) {
	printFunctions(i) = p
	i += 1
      }

      lastLabel.setPrintInfo(false);
      lastLabel.setSolutionListener(new ScalaSolutionListener[T]);

      if (limitOnSolutions > 0) {
	lastLabel.getSolutionListener().setSolutionLimit(limitOnSolutions)
	lastLabel.respectSolutionLimitInOptimization=true
      }
    }

    return masterLabel.labeling(Model, select(0), cost)
  }
  
/**
* Maximization method for sequence of search methods (specified by list of select methods).
*
* @param select list of select methods defining variable selection and value assignment methods for sequence of searchs.
* @param cost Cost variable
* @return true if solution found and false otherwise.
*/
  def maximize_seq[T <: JaCoP.core.Var](select: List[SelectChoicePoint[T]], cost: IntVar, printSolutions: () => Unit*)(implicit m: ClassManifest[T]): Boolean = {

    val costN = new IntVar("newCost", JaCoP.core.IntDomain.MinInt, JaCoP.core.IntDomain.MaxInt)
    costN #= -cost

    return minimize_seq(select, costN, printSolutions: _*)
  }


/**
* Search method for finding a solution using a sequence of search methods (specified by list of select methods).
*
* @param select list of select methods defining variable selection and value assignment methods for sequence of searchs.
* @param cost Cost variable
* @return true if solution found and false otherwise.
*/
  def satisfy_seq[T <: JaCoP.core.Var](select: List[SelectChoicePoint[T]], printSolutions: () => Unit*)(implicit m: ClassManifest[T]): Boolean = {

    Model.imposeAllConstraints

    val masterLabel = dfs
    labels = new Array(select.size)
    labels(0) = masterLabel

    if (printSolutions.size > 0) {
	masterLabel.setSolutionListener(new EmptyListener[T]);
       	masterLabel.setPrintInfo(false);
    }    

    if (timeOutValue > 0)
      masterLabel.setTimeOut(timeOutValue)

    if (allSolutions)
      masterLabel.getSolutionListener().searchAll(true)

    masterLabel.getSolutionListener().recordSolutions(recordSolutions)

    var previousSearch = masterLabel
    var lastLabel = masterLabel
    if (select.length > 1)
      for (i <- 1 until select.length) {
       	val label = dfs
	previousSearch.addChildSearch(label)
	label.setSelectChoicePoint(select(i));
	previousSearch = label
	lastLabel = label
	labels(i) = label

	if (printSolutions.size > 0) {
	  label.setSolutionListener(new EmptyListener[T]);
       	  label.setPrintInfo(false);
	}

	if (timeOutValue > 0)
	  label.setTimeOut(timeOutValue)

	if (allSolutions)
	  label.getSolutionListener().searchAll(true)

	label.getSolutionListener().recordSolutions(recordSolutions)
      }

    printFunctions = new Array(printSolutions.size)
    if (printSolutions.size > 0) {
      var i=0
      for (p <- printSolutions) {
      	printFunctions(i) = p
      	i += 1
      }
    
      lastLabel.setPrintInfo(false);
      lastLabel.setSolutionListener(new ScalaSolutionListener[T]);

      if (limitOnSolutions > 0) 
	lastLabel.getSolutionListener().setSolutionLimit(limitOnSolutions)
    }

    lastLabel.getSolutionListener().recordSolutions(recordSolutions)

    return masterLabel.labeling(Model, select(0))
  }

/**
* Search method for finding all solutions using a sequence of search methods (specified by list of select methods).
*
* @param select list of select methods defining variable selection and value assignment methods for sequence of searchs.
* @param cost Cost variable
* @return true if solution found and false otherwise.
*/
  def satisfyAll_seq[T <: JaCoP.core.Var](select: List[SelectChoicePoint[T]], printSolutions: () => Unit*)(implicit m: ClassManifest[T]): Boolean = {

    allSolutions = true

    return satisfy_seq( select, printSolutions: _* )

  }

/**
* Depth first search method.
*
* @return standard depth first search.
*/
  def dfs[T <: JaCoP.core.Var](implicit m: ClassManifest[T]) : DepthFirstSearch[T] = {
    val label = new DepthFirstSearch[T]

    label.setAssignSolution(true);
    label.setSolutionListener(new PrintOutListener[T]());
    if (allSolutions)
      label.getSolutionListener().searchAll(true)

    return label

  }

/**
* Defines list of variables, their selection method for search and value selection
*
* @return select method for search.
*/
  def search[T <: JaCoP.core.Var](vars: List[T], heuristic: ComparatorVariable[T], indom: Indomain[T])(implicit m: ClassManifest[T]) : SelectChoicePoint[T] = {
    new SimpleSelect[T](vars.toArray, heuristic, indom)    
  }

/**
* Defines list of variables, their selection method for sequential search and value selection
*
* @return select method for search.
*/
  def search_vector[T <: JaCoP.core.Var](vars: List[List[T]], heuristic: ComparatorVariable[T], indom: Indomain[T])(implicit m: ClassManifest[T]) : SelectChoicePoint[T] = {

    val varsArray = new Array[Array[T]](vars.length)
    for (i <- 0 until vars.length)
      varsArray(i) = vars(i).toArray

    new SimpleMatrixSelect[T](varsArray, heuristic, indom)    
  }

/**
* Defines list of variables, their selection method for split search and value selection
*
* @return select method for search.
*/
  def search_split[T <: JaCoP.core.IntVar](vars: List[T], heuristic: ComparatorVariable[T])(implicit m: ClassManifest[T]) = {
    new SplitSelect[T](vars.toArray, heuristic, new IndomainMiddle[T]())
  }


/**
* Defines functions that prints search statistics
* 
*/
  def statistics = {
    var nodes=0
    var decisions=0
    var wrong=0
    var backtracks=0
    var depth=0
    var solutions=0

    for ( label <- labels) {
      nodes += label.getNodes(); 
      decisions += label.getDecisions();
      wrong += label.getWrongDecisions();
      backtracks += label.getBacktracks();
      depth += label.getMaximumDepth();
      solutions = label.getSolutionListener().solutionsNo();
    }
    println("\nSearch statistics:\n=================="+
	    "\nSearch nodes : "+nodes+
	    "\nSearch decisions : "+decisions+
	    "\nWrong search decisions : "+wrong+
	    "\nSearch backtracks : "+backtracks+
	    "\nMax search depth : "+depth+
	    "\nNumber solutions : "+ solutions 
	  );
  }


/**
* Defines functions that prints search statistics
*
* @param n number of solutions to be explored.
*/
  def numberSolutions(n: Int) = limitOnSolutions = n

/**
* Defines functions that prints search statistics
*
* @param t value of time-out in seconds.
*/
  def timeOut(t: Int) = timeOutValue = t


/**
* Defines null variable selection method that is interpreted by JaCoP as input order.
*
* @return related variable selection method.
*/
  def input_order = null

  // ===============  IntVar & BoolVar specific

/**
* Wrapper for [[JaCoP.search.SmallestDomain]].
*
* @return related variable selection method.
*/
  def first_fail = new SmallestDomain[JaCoP.core.IntVar]

/**
* Wrapper for [[JaCoP.search.MostConstrainedStatic]].
*
* @return related variable selection method.
*/
  def most_constrained = new MostConstrainedStatic[JaCoP.core.IntVar]

/**
* Wrapper for [[JaCoP.search.SmallestMin]].
*
* @return related variable selection method.
*/
  def smallest_min = new SmallestMin[JaCoP.core.IntVar]

/**
* Wrapper for [[JaCoP.search.LargestDomain]].
*
* @return related variable selection method.
*/
  def anti_first_fail = new LargestDomain[JaCoP.core.IntVar]

/**
* Wrapper for [[JaCoP.search.SmallestMin]].
*
* @return related variable selection method.
*/
  def smallest = new SmallestMin[JaCoP.core.IntVar]

/**
* Wrapper for [[JaCoP.search.LargestMax]].
*
* @return related variable selection method.
*/
  def largest = new LargestMax[JaCoP.core.IntVar]

/**
* Wrapper for [[JaCoP.search.MaxRegret]].
*
* @return related variable selection method.
*/
  def max_regret = new MaxRegret[JaCoP.core.IntVar]


/**
* Wrapper for [[JaCoP.search.IndomainMin]].
*
* @return related variable selection method.
*/
  def indomain_min  = new IndomainMin[JaCoP.core.IntVar]

/**
* Wrapper for [[JaCoP.search.IndomainMax]].
*
* @return related variable selection method.
*/
  def indomain_max  = new IndomainMax[JaCoP.core.IntVar]

/**
* Wrapper for [[JaCoP.search.IndomainMiddle]].
*
* @return related variable selection method.
*/
  def indomain_middle = new IndomainMiddle[JaCoP.core.IntVar]

/**
* Wrapper for [[JaCoP.search.IndomainMedian]].
*
* @return related variable selection method.
*/
  def indomain_median = new IndomainMedian[JaCoP.core.IntVar]

/**
* Wrapper for [[JaCoP.search.IndomainRandom]].
*
* @return related variable selection method.
*/
  def indomain_random = new IndomainRandom[JaCoP.core.IntVar]

  // ============= Set specific

/**
* Wrapper for [[JaCoP.set.search.MinCardDiff]].
*
* @return related variable selection method.
*/
  def first_fail_set = new MinCardDiff[JaCoP.set.core.SetVar]

/**
* Wrapper for [[JaCoP.search.MostConstrainedStatic]].
*
* @return related variable selection method.
*/
  def most_constrained_set = new MostConstrainedStatic[JaCoP.set.core.SetVar]

/**
* Currently equivalent to min_glb_card.
*
* @return related variable selection method.
*/
  def smallest_set = min_glb_card

/**
* Wrapper for [[JaCoP.set.search.MinGlbCard]].
*
* @return related variable selection method.
*/
  def min_glb_card = new MinGlbCard[JaCoP.set.core.SetVar]

/**
* Wrapper for [[JaCoP.set.search.MinLubCard]].
*
* @return related variable selection method.
*/
  def min_lub_card = new MinLubCard[JaCoP.set.core.SetVar]

/**
* Wrapper for [[JaCoP.set.search.MaxCardDiff]].
*
* @return related variable selection method.
*/
  def anti_first_fail_set = new MaxCardDiff[JaCoP.set.core.SetVar]


/**
* Wrapper for [[JaCoP.set.search.IndomainSetMin]].
*
* @return related indomain method.
*/
  def indomain_min_set  = new IndomainSetMin[JaCoP.set.core.SetVar]

/**
* Wrapper for [[JaCoP.set.search.IndomainSetMax]].
*
* @return related indomain method.
*/
  def indomain_max_set = new IndomainSetMax[JaCoP.set.core.SetVar]

/**
* Wrapper for [[JaCoP.set.search.IndomainSetRandom]].
*
* @return related indomain method.
*/
  def indomain_random_set = new IndomainSetRandom[JaCoP.set.core.SetVar]
}
