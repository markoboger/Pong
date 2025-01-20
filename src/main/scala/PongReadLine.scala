import scala.io.StdIn.readLine
import scala.io.AnsiColor._

object PongReadLine extends App {
  val width = 40
  val height = 20
  val paddleHeight = 4

  var ballX = width / 2
  var ballY = height / 2
  var ballDirX = 1
  var ballDirY = 1

  var leftPaddleY = height / 2 - paddleHeight / 2
  var rightPaddleY = height / 2 - paddleHeight / 2

  var leftScore = 0
  var rightScore = 0

  var running = true

  def draw(): Unit = {
    println("\u001b[H\u001b[2J") // Clear screen
    for (y <- 0 until height) {
      for (x <- 0 until width) {
        if (x == 0 && y >= leftPaddleY && y < leftPaddleY + paddleHeight) {
          print(s"${BLUE}|${RESET}")
        } else if (x == width - 1 && y >= rightPaddleY && y < rightPaddleY + paddleHeight) {
          print(s"${RED}|${RESET}")
        } else if (x == ballX && y == ballY) {
          print(s"${GREEN}O${RESET}")
        } else {
          print(" ")
        }
      }
      println()
    }
    println(s"Score: Left $leftScore - Right $rightScore")
    println("Controls: W/S for Left, O/L for Right. Q to Quit.")
  }

  def update(): Unit = {
    // Move the ball
    ballX += ballDirX
    ballY += ballDirY

    // Ball collision with walls
    if (ballY <= 0 || ballY >= height - 1) ballDirY = -ballDirY

    // Ball collision with paddles
    if (ballX == 1 && ballY >= leftPaddleY && ballY < leftPaddleY + paddleHeight) {
      ballDirX = -ballDirX
    } else if (ballX == width - 2 && ballY >= rightPaddleY && ballY < rightPaddleY + paddleHeight) {
      ballDirX = -ballDirX
    }

    // Ball out of bounds
    if (ballX <= 0) {
      rightScore += 1
      resetBall()
    } else if (ballX >= width - 1) {
      leftScore += 1
      resetBall()
    }
  }

  def resetBall(): Unit = {
    ballX = width / 2
    ballY = height / 2
    ballDirX = if (math.random() < 0.5) 1 else -1
    ballDirY = if (math.random() < 0.5) 1 else -1
  }

  def handleInput(input: String): Unit = input.toLowerCase match {
    case "w" => if (leftPaddleY > 0) leftPaddleY -= 1
    case "s" => if (leftPaddleY < height - paddleHeight) leftPaddleY += 1
    case "o" => if (rightPaddleY > 0) rightPaddleY -= 1
    case "l" => if (rightPaddleY < height - paddleHeight) rightPaddleY += 1
    case "q" => running = false
    case _   => // Do nothing
  }

  def gameLoop(): Unit = {
    while (running) {
      draw()
      val startTime = System.currentTimeMillis()

      // Handle input
      if (System.in.available() > 0) {
        val input = readLine()
        handleInput(input)
      }

      // Update game state
      update()

      // Control frame rate
      val frameTime = 100 // milliseconds per frame
      val elapsedTime = System.currentTimeMillis() - startTime
      val sleepTime = frameTime - elapsedTime
      if (sleepTime > 0) Thread.sleep(sleepTime)
    }
  }

  gameLoop()
}
