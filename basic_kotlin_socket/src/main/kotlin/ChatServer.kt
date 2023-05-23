import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.ServerSocket

val clients = mutableListOf<PrintWriter>()

fun main() = runBlocking {
    val server = ServerSocket(1234)
    println("채팅 서버가 시작되었습니다.")

    while (true) {
        val clientSocket = server.accept()
        println("클라이언트가 연결되었습니다.")
        val reader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
        val writer = PrintWriter(clientSocket.getOutputStream(), true)

        clients.add(writer)

        GlobalScope.launch(Dispatchers.IO) {
            while (true) {
                val message = reader.readLine()
                if (message != null) {
                    println("클라이언트로부터 받은 메시지: $message")
                    broadcast(message)
                }
            }
        }
    }
}

fun broadcast(message: String) {
    for (client in clients) {
        client.println(message)
    }
}
