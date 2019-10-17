package com.teheidoma.nicknamecolor

import javafx.scene.paint.Color
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import java.util.*
import javax.servlet.http.HttpServletRequest
import kotlin.collections.HashMap

@SpringBootApplication
@Controller
class NicknamecolorApplication{
    val logger = LoggerFactory.getLogger("MainLogger")
    @RequestMapping("/")
    fun index(httpServletRequest: HttpServletRequest): String {
        logger.info("index {}", httpServletRequest.remoteAddr)
        return "index"
    }

    @RequestMapping("/color")
    @ResponseBody
    fun color(@RequestParam nickname:String, httpServletRequest: HttpServletRequest): HashMap<String, String> {
        logger.info("color {} {}", nickname, httpServletRequest.remoteAddr)
        val random = Random(nickname.hashCode().toLong())
        val color = Color.rgb(random.nextInt(255),random.nextInt(255),random.nextInt(255))
        return hashMapOf(
                Pair("hex", color.toString().replace("0x", "#").substring(0, 7)),
                Pair("rgb", "${(color.red*255).toInt()},${(color.green*255).toInt()},${(color.blue*255).toInt()}")
        )
    }
}

fun main(args: Array<String>) {
    runApplication<NicknamecolorApplication>(*args)
}
