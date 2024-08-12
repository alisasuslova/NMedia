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
Первая дочерняя вылетит с исключением, но из-за supervisorScope корректно сработает вторая дочерняя корутина, и родительская тоже сработает.

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
6.	Первая дочерняя корутина delay(1000) /println("ok") успеет выполниться, вторая delay(500) / println("ok") уже нет, потому что Thread.sleep(1000).

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
Выполнится первая дочерняя корутина, вторая корутина выполниться не успеет. В первой произойдет исключение, но из-за SupervisorJob() исключение не передается в родительскую корутину, так что она сработает.
