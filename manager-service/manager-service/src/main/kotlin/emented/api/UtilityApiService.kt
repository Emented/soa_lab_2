package emented.api

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller

@Controller
class UtilityApiService : UtilityApiDelegate {

    override fun ping(): ResponseEntity<Unit> {
        return ResponseEntity.ok().build()
    }
}