import processing.core.PApplet
import processing.core.PConstants

data class Circle(val radius: Float, val x: Float, val y: Float, var color: FloatArray = floatArrayOf(0f, 0f, 0f))

const val SIZE = 1000

class CirclePacking : PApplet() {
    companion object Factory {
        fun run() {
            val art = CirclePacking()
            art.setSize(SIZE, SIZE)
            art.runSketch()
            art.updatePixels()
            art.saveFrame("out.jpg")
        }
    }

    val circleSizeCounts = listOf(
        65 to 7, 39 to 27, 22 to 37, 7 to 167, 3 to 301
    )

    fun randomXY(xMin: Float, xMax: Float, yMin: Float, yMax: Float): Pair<Float, Float> {
        return Pair(random(xMin, xMax), random(yMin, yMax))
    }

    override fun setup() {
        strokeCap(PConstants.ROUND)
        colorMode(PConstants.HSB, 360f, 100f, 100f, 1.0f)
        ellipseMode(PConstants.CENTER)

        val allCircles = mutableListOf<Circle>()

        for ((circleSize, circleCount) in circleSizeCounts) {
            for (i in 1..circleCount) {
                // allow up to 100 collisions
                for (c in 0..1000) {
                    // generate random point
                    // do not allow circles to overlap canvas
                    // val (x, y) = randomXY(0f+circleSize, 500f-circleSize, 0f+circleSize, 500f-circleSize);
                    // allow circles overlapping canvas
                    val (x, y) = randomXY(0f, SIZE.toFloat(), 0f, SIZE.toFloat())
                    val testCircle = Circle(circleSize.toFloat(), x, y)
                    if (!checkCircleCollision(allCircles, testCircle)) {
                        // get random color
                        val color = weightedChoice(
                            listOf(
                                floatArrayOf(0f, 0f, random(90f, 100f)) to 0.6f,
                                floatArrayOf(random(180f, 220f), 50f, 50f) to 0.3f,
                                floatArrayOf(random(0f, 20f), 80f, 80f) to 0.1f
                            )
                        )
                        testCircle.color = color
                        allCircles.add(testCircle)
                        break
                    }
                }
            }
        }

        noStroke()
        for (circle in allCircles) {
            val circleColor = circle.color
            fill(circleColor[0], circleColor[1], circleColor[2])
            ellipse(circle.x, circle.y, circle.radius * 2, circle.radius * 2)
        }

    }

    fun checkCircleCollision(circles: List<Circle>, sample: Circle): Boolean =
        circles.any {
            val distance = dist(it.x, it.y, sample.x, sample.y)
            distance <= (it.radius + sample.radius)
        }

    fun weightedChoice(colorsAndWeights: List<Pair<FloatArray, Float>>): FloatArray {
        val weightSum = colorsAndWeights.sumOf { (it.second * 100).toInt() }
        if (weightSum != 100) throw AssertionError("Weights should sum to 1")
        val random = random(0f, 1.0f)
        var weightTotal = 0f
        for (i in colorsAndWeights) {
            if (random >= weightTotal && random <= weightTotal + i.second) {
                return i.first
            }
            weightTotal += i.second
        }
        throw Exception("Should have returned a Weighted Choice...")
    }

    fun rescale(value: Float, oldMin: Float, oldMax: Float, newMin: Float, newMax: Float): Float {
        val oldSpread = oldMax - oldMin
        val newSpread = newMax - newMin
        val a = value - oldMin
        val b = newSpread / oldSpread
        val c = a * b
        val d = c + newMin
        return d
    }
}

fun main() {
    CirclePacking.run()
}

