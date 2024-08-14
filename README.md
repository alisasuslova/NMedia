Вопросы: Cancellation
Вопрос №1
Отработает ли в этом коде строка <--? Поясните, почему да или нет.
fun main() = runBlocking {
    val job = CoroutineScope(EmptyCoroutineContext).launch {
        launch {
            delay(500)
            println("ok") // <--
        }
        launch {
            delay(500)
            println("ok")
        }
    }
    delay(100)
    job.cancelAndJoin()
}
Ничего не произойдет, потому что родительская корутина отменится до того delay(100), как запустятся дочерние delay(500).

Вопрос №2
Отработает ли в этом коде строка <--. Поясните, почему да или нет.
fun main() = runBlocking {
    val job = CoroutineScope(EmptyCoroutineContext).launch {
        val child = launch {
            delay(500)
            println("ok") // <--
        }
        launch {
            delay(500)
            println("ok")
        }
        delay(100)
        child.cancel()
    }
    delay(100)
    job.join()
}
Тоже самое дочерняя корутина не успеет стартонуть, а родительская уже отменится.

Вопросы: Exception Handling
Вопрос №1
Отработает ли в этом коде строка <--. Поясните, почему да или нет.
fun main() {
    with(CoroutineScope(EmptyCoroutineContext)) {
        try {
            launch {
                throw Exception("something bad happened")
            }
        } catch (e: Exception) {
            e.printStackTrace() // <--
        }
    }
    Thread.sleep(1000)
}
Нет потому, что неправильно расположен блок try- catch. Он должен быть в пределах блока launch{}.

Вопрос №2
Отработает ли в этом коде строка <--. Поясните, почему да или нет.

fun main() {
    CoroutineScope(EmptyCoroutineContext).launch {
        try {
            coroutineScope {
                throw Exception("something bad happened")
            }
        } catch (e: Exception) {
            e.printStackTrace() // <--
        }
    }
    Thread.sleep(1000)
}
Да, все сработает. Получим StackTrace с записью "something bad happened"

Вопрос №3
Отработает ли в этом коде строка <--. Поясните, почему да или нет.

fun main() {
    CoroutineScope(EmptyCoroutineContext).launch {
        try {
            supervisorScope {
                throw Exception("something bad happened")
            }
        } catch (e: Exception) {
            e.printStackTrace() // <--
        }
    }
    Thread.sleep(1000)
}
Да, сработает. Исключение из дочерней корутины обработается через supervisorScope, и родительская корутина будет работать.

Вопрос №4
Отработает ли в этом коде строка <--. Поясните, почему да или нет.

fun main() {
    CoroutineScope(EmptyCoroutineContext).launch {
        try {
            coroutineScope {
                launch {
                    delay(500)
                    throw Exception("something bad happened") // <--
                }
                launch {
                    throw Exception("something bad happened")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    Thread.sleep(1000)
}
4.	В первой дочерней корутине появится исключение, из-за coroutineScope не сработает вторая дочерняя и родительская тоже не сработает.

Вопрос №5
Отработает ли в этом коде строка <--. Поясните, почему да или нет.

fun main() {
    CoroutineScope(EmptyCoroutineContext).launch {
        try {
            supervisorScope {
                launch {
                    delay(500)
                    throw Exception("something bad happened") // <--
                }
                launch {
                    throw Exception("something bad happened")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace() // <--
        }
    }
    Thread.sleep(1000)
}
2.5.
Сработает вторая дочерняя корутина, выдаст ошибку. Ее обработает supervisorScope. Через delay(500) сработает первая дочерняя корутина и тоже выдаст свою ошибку. Ее также обработает supervisorScope, сработает блок catсh. А через sleep(1000) родительская корутина отменится, и все дочерниие тоже.


Вопрос №6
Отработает ли в этом коде строка <--. Поясните, почему да или нет.

fun main() {
    CoroutineScope(EmptyCoroutineContext).launch {
        CoroutineScope(EmptyCoroutineContext).launch {
            launch {
                delay(1000)
                println("ok") // <--
            }
            launch {
                delay(500)
                println("ok")
            }
            throw Exception("something bad happened")
        }
    }
    Thread.sleep(1000)
}
2.6. 
println("ok") – сработает.
Обе дочерние корутины запустятся, далее произойдет исключение в родительской корутине и отменит выполнение дочерних.


Вопрос №7
Отработает ли в этом коде строка <--. Поясните, почему да или нет.

fun main() {
    CoroutineScope(EmptyCoroutineContext).launch {
        CoroutineScope(EmptyCoroutineContext + SupervisorJob()).launch {
            launch {
                delay(1000)
                println("ok") // <--
            }
            launch {
                delay(500)
                println("ok")
            }
            throw Exception("something bad happened")
        }
    }
    Thread.sleep(1000)
}
2.7.
println("ok") – сработает.
Сработают обе дочерние корутины, далее произойдет исключение в родительской корутине. Это исключение обработается с помощью SupervisorJob(). Родительская продолдит работу и через sleep(1000) будет отменена.
