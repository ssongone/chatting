import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

fun main() = runBlocking {
    val socket = Socket("localhost", 1234)
    val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
    val writer = PrintWriter(socket.getOutputStream(), true)

    GlobalScope.launch(Dispatchers.IO) {
        while (true) {
            val message = reader.readLine()
            if (message != null) {
                println("서버로부터 받은 메시지: $message")
            }
        }
    }

    val consoleReader = BufferedReader(InputStreamReader(System.`in`))
    while (true) {
        val input = consoleReader.readLine()
        writer.println(input)
    }
}
