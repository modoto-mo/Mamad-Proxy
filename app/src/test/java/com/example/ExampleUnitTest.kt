package com.example

import org.junit.Test
import java.net.URL

class ExampleUnitTest {
  @Test
  fun testFetchProxies() {
    val content = URL("https://c-mamad.ir/mm/mm2/mm3/index.txt").readText()
    println("Content length: ${content.length}")
    println("First 1000 chars:")
    println(content.take(1000))
  }
}
