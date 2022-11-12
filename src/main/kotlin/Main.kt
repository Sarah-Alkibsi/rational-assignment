package rationals

import java.lang.IllegalArgumentException
import java.math.BigInteger


fun main() {
    val half = 1 divBy 2
    val third = 1 divBy 3

    val list : List<List<Int>> = List(3){ i -> List(2){ j -> 1+j} }
    println("flatten:${list.flatten()}")
    println("list:${list}")
    val sum: Rational = half + third

    println(5 divBy 6 == sum)

    val difference: Rational = half - third
    println(1 divBy 6 == difference)

    val product: Rational = half * third
    println(1 divBy 6 == product)

    val quotient: Rational = half / third
    println(3 divBy 2 == quotient)

    val negation: Rational = -half
    println(-1 divBy 2 == negation)

    println((2 divBy 1).toString() == "2")
//    println((-2 divBy 4).toString())
    println((-2 divBy 4).toString() == "-1/2")

    println("117/1098".toRational().toString() == "13/122")

    val twoThirds = 2 divBy 3
    println(half < twoThirds)
    println(half in third..twoThirds)
    println(2000000000L divBy 4000000000L == 1 divBy 2)

    println("912016490186296920119201192141970416029".toBigInteger() divBy "1824032980372593840238402384283940832058".toBigInteger() == 1 divBy 2)
}


operator fun Rational.unaryMinus(): Rational = Rational(-numerator, denominator)

operator fun Rational.plus(that: Rational): Rational {
    return Rational(
        (that.denominator * numerator) + (denominator * that.numerator),
        denominator * that.denominator
    )
}

operator fun Rational.minus(that: Rational): Rational {
    return Rational(
        (that.denominator * numerator) - (denominator * that.numerator),
        denominator * that.denominator
    )
}


operator fun Rational.times(that: Rational): Rational {
    return Rational(numerator * that.numerator, denominator * that.denominator)
}

operator fun Rational.div(that: Rational): Rational {
    return this * that.getReverse()
}

class Rational(var numerator: BigInteger, var denominator: BigInteger = 1.toBigInteger()) : Comparable<Rational> {
    init {
        simplify()
    }

    fun simplify() {
        if (denominator == BigInteger.ZERO) {
            throw (IllegalArgumentException())
        }
        val gcd = numerator.gcd(denominator).takeUnless { it == 0.toBigInteger() } ?: 1 as BigInteger;
        val sign = denominator.signum().toBigInteger()
        this.numerator = numerator / gcd * sign;
        this.denominator = denominator / gcd * sign;
    }

    override fun compareTo(that: Rational): Int {
        return (numerator * that.denominator - that.numerator * denominator).signum()
    }


    override fun equals(that: Any?): Boolean {
        simplify()
        val obj = this;
        return when (that) {
            is Rational -> obj.getResult() == (that as? Rational)?.getResult();
            is String -> that.toRational().getResult() == (that as? Rational)?.getResult();
            else -> false
        }
    }


    fun getResult(): Double {
        return (numerator / denominator).toDouble();
    }

    fun getReverse(): Rational {
        return Rational(denominator, numerator)
    }

    override fun toString(): String {
        simplify()
        return if (denominator ==  BigInteger.ONE) "$numerator" else "$numerator/$denominator";
    }


}

infix fun BigInteger.divBy(divisor: BigInteger): Rational {
    return Rational(this, divisor)
}


infix fun Int.divBy(divisor: Int): Rational {
    return Rational(this.toBigInteger(), divisor.toBigInteger())
}

infix fun Long.divBy(divisor: Long): Rational {
    return Rational(this.toBigInteger(), divisor.toBigInteger())
}

fun String.toRational(): Rational {
    val list = this.takeIf { it.contains('/') }?.split('/') ?: return Rational(
        this.toBigIntegerOrNull() ?: throw (IllegalArgumentException("Illegal!")), BigInteger.ONE
    );
    val num = list[0].toBigIntegerOrNull() ?: throw (IllegalArgumentException("expected numeric values!"))
    var divisor: BigInteger = list[1].takeIf { !it.isNullOrEmpty() }?.toBigIntegerOrNull() ?: BigInteger.ONE;
    return Rational(num, divisor)
}


