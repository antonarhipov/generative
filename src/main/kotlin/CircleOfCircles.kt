import processing.core.PApplet


class Regular(size: Int = 1000) : PApplet() {
    init {
        setSize(size, size)
        runSketch()
    }

    override fun setup() {

        midPointCircleDraw(500, 500, 300f) { x, y ->
            noFill()
            ellipse(x.toFloat(), y.toFloat(), 100f, 100f)
        }


    }

    fun midPointCircleDraw(x_centre: Int, y_centre: Int, r: Float, figure: (Float, Float) -> Unit) {
        var x = r
        var y = 0f

        figure(x + x_centre, y + y_centre)

        // Initialising the value of P
        var P: Float = 1 - r
        while (x > y) {
            y += 1.9f

            // Mid-point is inside or on the perimeter
            if (P <= 0) {
                P += (2 * y) + 1
            } else {
                x -= 13f
                P = P + 2 * y - 2 * x + 1
            }

            // All the perimeter points have already
            // been printed
            if (x < y) break

            // Printing the generated point and its
            // reflection in the other octants after
            // translation
            figure(x + x_centre, y + y_centre)
            figure(-x + x_centre, y + y_centre)
            figure(x + x_centre, -y + y_centre)
            figure(-x + x_centre, -y + y_centre)

            // If the generated point is on the
            // line x = y then the perimeter points
            // have already been printed
            if (x != y) {
                figure(y + x_centre, x + y_centre)
                figure(-y + x_centre, x + y_centre)
                figure(y + x_centre, -x + y_centre)
                figure(-y + x_centre, -x + y_centre)
            }
        }
    }

}


fun main() {
    Regular()
}

